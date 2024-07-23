package com.example.tripmaster.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.Objects;

@SuppressLint("NotifyDataSetChanged")
public class AddTripActivity extends AppCompatActivity implements IScreenSwitch {

    private static final int PICK_FILE_REQUEST = 1;
    private FileStorageService fileStorageService;

    private RecyclerView recyclerView;
    private ArrayList<EventTrip> eventList;
    private EventTripAdapter eventAdapter;
    private TextInputEditText etDate;
    private DataManager dataManager;
    private Button cancelBtn, saveBtn, finishTripBtn, addEventBtn, uploadBtn;
    private EditText titleTrip, countryTrip;
    private Trip currentTrip;
    private String currentEventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        dataManager = DataManager.getInstance();
        currentTrip = new Trip();  // Initialize currentTrip once
        fileStorageService = new FileStorageService();

        initializeViews();
        setupRecyclerView();
        loadEventTrips();
    }

    private void initializeViews() {
        currentEventDate = "";
        recyclerView = findViewById(R.id.event_trips);
        etDate = findViewById(R.id.etDate);
        titleTrip = findViewById(R.id.title_trip);
        countryTrip = findViewById(R.id.country_trip);
        cancelBtn = findViewById(R.id.cancel_button);
        saveBtn = findViewById(R.id.save_btn);
        finishTripBtn = findViewById(R.id.finish_trip_btn);
        addEventBtn = findViewById(R.id.add_event_btn);
        uploadBtn = findViewById(R.id.upload_btn);

        // buttons
        setupCancelButton();
        setupSaveButton();
        setupFinishButton();
        setupAddEventButton();
        setupUploadButton();

        // date picker
        setupDatePicker();
    }

    private void setupUploadButton() {
        uploadBtn.setOnClickListener(v -> {
            // Launch file picker
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();

            // Use FileStorageService to upload the file
            fileStorageService.uploadFile(fileUri, new FileStorageService.FileUploadCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddTripActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    currentTrip.setFileImgName(fileUri.getLastPathSegment());
                    uploadBtn.setText(fileUri.getLastPathSegment());
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(AddTripActivity.this, "File upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupAddEventButton() {
        addEventBtn.setOnClickListener(btn -> addNewEvent());
    }


    private void addNewEvent() {
        eventList.add(new EventTrip());
        eventAdapter.notifyDataSetChanged();
    }

    private void setupFinishButton() {
        finishTripBtn.setOnClickListener(btn -> {
            dataManager.addTrip(currentTrip);  // Add or update the trip in DataManager
             switchScreen();
        });
    }

    private void setupCancelButton() {
        cancelBtn.setOnClickListener(btn -> switchScreen());
    }

    private void setupSaveButton() {
        saveBtn.setOnClickListener(v -> saveTrip());
    }

    private void setupDatePicker() {
        DatePickerHandler datePickerHandler = new DatePickerHandler(etDate, getSupportFragmentManager());
        datePickerHandler.setDateSelectedListener(date -> {
            if (date != null && !date.isEmpty()) {
                clearAll();
                if (Objects.equals(currentTrip.getStartDate(), ""))
                    currentTrip.setStartDate(date);
                currentEventDate = date;
                loadEventsForSelectedDate(date);
            }
        });
    }

    private void loadEventsForSelectedDate(String date) {
        ArrayList<EventTrip> events = currentTrip.getEventTrips().get(date);
        if (events != null) {
            eventList.clear();
            eventList.addAll(events);
        } else {
            eventList.clear();
            loadEventTrips();  // Optionally load default events
        }
        eventAdapter.notifyDataSetChanged();
    }

    private boolean isTripDataValid() {
        // Check if any of the required fields are empty
        return !titleTrip.getText().toString().trim().isEmpty() &&
                !countryTrip.getText().toString().trim().isEmpty() &&
                !currentTrip.getStartDate().isEmpty();  // Example of another field to check
    }

    private void saveTrip() {
        // Validate trip data
        if (!isTripDataValid()) {
            Toast.makeText(this, "Trip title, location, or start date cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Capture trip and event data
        captureTripData();
        captureEventData();

        // Print saved data to console
        System.out.println("SAVED DATA: " + currentTrip);
    }


    private void clearAll() {
        eventList.clear();
        eventAdapter.notifyDataSetChanged();
    }

    private void captureTripData() {
        currentTrip.setTitle(titleTrip.getText().toString());
        currentTrip.setLocation(countryTrip.getText().toString());
    }

    private void captureEventData() {

        // Create a new list for the current date to avoid reference issues
        ArrayList<EventTrip> eventsForDate = new ArrayList<>(eventList);

        currentTrip.getEventTrips().put(currentEventDate, eventsForDate);
    }

    private void setupRecyclerView() {
        eventList = new ArrayList<>();
        eventAdapter = new EventTripAdapter(eventList, position -> {
            // Remove event from the list
            eventList.remove(position);
            eventAdapter.notifyItemRemoved(position);
            eventAdapter.notifyItemRangeChanged(position, eventList.size());

            // Update currentTrip's event map
            if (currentTrip.getEventTrips().containsKey(currentEventDate)) {
                if (eventList.isEmpty()) {
                    currentTrip.getEventTrips().remove(currentEventDate);
                } else {
                    ArrayList<EventTrip> eventsForDate = new ArrayList<>(eventList);
                    currentTrip.getEventTrips().put(currentEventDate, eventsForDate);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
    }



    private void loadEventTrips() {
        eventList.add(new EventTrip(EventTypeEnum.EMPTY, "", ""));
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void switchScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
