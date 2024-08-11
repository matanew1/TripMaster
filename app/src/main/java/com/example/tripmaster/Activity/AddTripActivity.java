package com.example.tripmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaster.Adapter.EventTripAdapter;
import com.example.tripmaster.Data.DataManager;
import com.example.tripmaster.Model.EventTrip;
import com.example.tripmaster.Model.EventTypeEnum;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import com.example.tripmaster.Service.FileStorageService;
import com.example.tripmaster.Utils.DatePickerHandler;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

@SuppressLint("NotifyDataSetChanged")
public class AddTripActivity extends AppCompatActivity implements IScreenSwitch {

    private static final int PICK_FILE_REQUEST = 1;

    private FirebaseUser currentUser;
    private FileStorageService fileStorageService;
    private DataManager dataManager;
    private RecyclerView recyclerView;
    private ArrayList<EventTrip> eventList;
    private EventTripAdapter eventAdapter;
    private TextInputEditText etDate;
    private EditText titleTrip, countryTrip;
    private SwitchCompat sharedSw;
    private Button cancelBtn, saveBtn, finishTripBtn, addEventBtn, uploadBtn;
    private Trip currentTrip;
    private String currentEventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        initializeComponents();
        setupRecyclerView();
        loadDefaultEventTrips();
    }

    private void initializeComponents() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dataManager = DataManager.getInstance();
        dataManager.initialize(currentUser);

        currentTrip = new Trip();
        fileStorageService = new FileStorageService();

        recyclerView = findViewById(R.id.event_trips);
        etDate = findViewById(R.id.etDate);
        titleTrip = findViewById(R.id.title_trip);
        countryTrip = findViewById(R.id.country_trip);
        cancelBtn = findViewById(R.id.cancel_button);
        saveBtn = findViewById(R.id.save_btn);
        finishTripBtn = findViewById(R.id.finish_trip_btn);
        addEventBtn = findViewById(R.id.add_event_btn);
        uploadBtn = findViewById(R.id.upload_btn);
        sharedSw = findViewById(R.id.shared);

        setupButtons();
        setupDatePicker();
    }

    private void setupButtons() {
        cancelBtn.setOnClickListener(v -> switchScreen());
        saveBtn.setOnClickListener(v -> saveTrip());
        finishTripBtn.setOnClickListener(v -> finishTrip());
        addEventBtn.setOnClickListener(v -> addNewEvent());
        uploadBtn.setOnClickListener(v -> openFilePicker());
        sharedSw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentTrip.setShared(isChecked);
        });
    }

    private void setupDatePicker() {
        DatePickerHandler datePickerHandler = new DatePickerHandler(etDate, getSupportFragmentManager());
        datePickerHandler.setDateSelectedListener(date -> {
            if (date != null && !date.isEmpty()) {
                currentEventDate = date;
                if (Objects.equals(currentTrip.getStartDate(), "")) {
                    currentTrip.setStartDate(date);
                }
                loadEventsForSelectedDate(date);
            }
        });
    }

    private void setupRecyclerView() {
        eventList = new ArrayList<>();
        eventAdapter = new EventTripAdapter(eventList, this::removeEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
    }

    private void loadDefaultEventTrips() {
        eventList.add(new EventTrip(EventTypeEnum.EMPTY, "", ""));
        eventAdapter.notifyDataSetChanged();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            uploadFile(fileUri);
        }
    }

    private void uploadFile(Uri fileUri) {
        fileStorageService.uploadFileImage(fileUri, new FileStorageService.FileUploadCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(AddTripActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                currentTrip.setFileImgName(fileUri.getLastPathSegment());
                uploadBtn.setText("File Uploaded !");
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddTripActivity.this, "File upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewEvent() {
        eventList.add(new EventTrip());
        eventAdapter.notifyDataSetChanged();
    }

    private void removeEvent(int position) {
        eventList.remove(position);
        eventAdapter.notifyItemRemoved(position);
        eventAdapter.notifyItemRangeChanged(position, eventList.size());

        if (currentTrip.getEventTrips().containsKey(currentEventDate)) {
            if (eventList.isEmpty()) {
                currentTrip.getEventTrips().remove(currentEventDate);
            } else {
                currentTrip.getEventTrips().put(currentEventDate, new ArrayList<>(eventList));
            }
        }
    }

    private void finishTrip() {
        if (saveTrip()) {
            dataManager.addTrip(currentUser, currentTrip);
            switchScreen();
        }
    }

    private boolean saveTrip() {
        if (!isTripDataValid()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        captureTripData();
        captureEventData();
        return true;
    }

    private boolean isTripDataValid() {
        return !titleTrip.getText().toString().trim().isEmpty() &&
                !countryTrip.getText().toString().trim().isEmpty() &&
                eventList.stream().noneMatch(event -> event.getEventType() == EventTypeEnum.EMPTY) &&
                !currentTrip.getStartDate().isEmpty();
    }

    private void captureTripData() {
        currentTrip.setTitle(titleTrip.getText().toString());
        currentTrip.setLocation(countryTrip.getText().toString());
    }

    private void captureEventData() {
        currentTrip.getEventTrips().put(currentEventDate, new ArrayList<>(eventList));
    }

    private void loadEventsForSelectedDate(String date) {
        ArrayList<EventTrip> events = currentTrip.getEventTrips().get(date);
        if (events != null) {
            eventList.clear();
            eventList.addAll(events);
        } else {
            eventList.clear();
            loadDefaultEventTrips();
        }
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void switchScreen() {
        Intent intent = new Intent(this, NewTripSplashActivity.class);
        startActivity(intent);
        finish();
    }
}
