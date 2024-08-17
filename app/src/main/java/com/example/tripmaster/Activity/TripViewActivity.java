package com.example.tripmaster.Activity;

import static com.example.tripmaster.Utils.Consts.PICK_FILE_REQUEST;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tripmaster.Activity.Fragments.MenuFragment;
import com.example.tripmaster.Activity.Fragments.RatingFragment;
import com.example.tripmaster.Adapter.DaysTripAdapter;
import com.example.tripmaster.Adapter.EventTripViewAdapter;
import com.example.tripmaster.Data.DataManager;
import com.example.tripmaster.Model.EventTrip;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import com.example.tripmaster.Service.FileStorageService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

@SuppressWarnings("all")
public class TripViewActivity extends AppCompatActivity implements IScreenSwitch, DaysTripAdapter.OnDateClickListener {

    private static final String TAG = "TripViewActivity"; // Tag for logging

    private Trip currentTrip;
    private EventTripViewAdapter eventTripViewAdapter;
    private LinearLayout bgTripView;
    private FileStorageService fileStorageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_data);

        FirebaseUser currentUser1 = FirebaseAuth.getInstance().getCurrentUser();
        DataManager dataManager1 = DataManager.getInstance();
        dataManager1.initialize(currentUser1);

        fileStorageService = new FileStorageService();

        // Initialize currentTrip
        if (getIntent() != null && getIntent().hasExtra("clicked_trip")) {
            currentTrip = (Trip) getIntent().getSerializableExtra("clicked_trip");
        } else {
            currentTrip = new Trip(); // Or show an error
        }

        if (savedInstanceState == null) {
            loadMenuFragment();
            loadRatingFragment();
        }


        bgTripView = findViewById(R.id.bg_trip);
        // Load photo URL from current user and set background
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            loadUserProfileBackground();
        }

        bgTripView.setOnClickListener(v -> openFilePicker());


        RatingBar myRatingSlider = findViewById(R.id.rating_bar);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // Update the trip's average rating
            currentTrip.updateCurrentAverageRating(myRatingSlider.getRating());

            // Get the current FirebaseUser
            if (currentUser == null) {
                Log.e(TAG, "User not logged in");
                Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the trip data
            DataManager dataManager = DataManager.getInstance();
            dataManager.getDatabaseService().updateAllTrips(currentTrip);
            Log.d(TAG, "Updated average rating: " + currentTrip.getAverageRating());

            // Switch to the HomeActivity
            switchScreen();
        });

        RecyclerView recyclerViewDays = findViewById(R.id.recyclerViewDays);
        RecyclerView recyclerViewEvents = findViewById(R.id.recyclerViewEvents);

        // Set up RecyclerView for Days
        recyclerViewDays.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<String> eventDates = new ArrayList<>(currentTrip.getEventTrips().keySet());
        DaysTripAdapter daysTripAdapter = new DaysTripAdapter(eventDates, this);
        recyclerViewDays.setAdapter(daysTripAdapter);

        // Set up RecyclerView for Events
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        eventTripViewAdapter = new EventTripViewAdapter(new ArrayList<>());
        recyclerViewEvents.setAdapter(eventTripViewAdapter);

        // Load events for the default selected date
        if (!eventDates.isEmpty()) {
            String defaultDate = eventDates.get(0);
            ArrayList<EventTrip> events = new ArrayList<>(currentTrip.getEventTrips().getOrDefault(defaultDate, new ArrayList<>()));
            eventTripViewAdapter.updateEvents(events);
        }
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
                Toast.makeText(TripViewActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                currentTrip.setFileImgName(fileUri.getLastPathSegment());
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(TripViewActivity.this, "File upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadUserProfileBackground() {
        FileStorageService fileStorageService = new FileStorageService();
        String filePath = "uploads/" + currentTrip.getFileImgName(); // Adjust the path as necessary

        fileStorageService.downloadFileImage(filePath, new FileStorageService.FileDownloadCallback() {
            @Override
            public void onSuccess(Uri fileUri) {
                Glide.with(TripViewActivity.this)
                        .load(fileUri)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                bgTripView.setBackground(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                // Handle if necessary
                            }
                        });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Failed to load profile background image: " + e.getMessage());
                Toast.makeText(TripViewActivity.this, "Failed to load profile background image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateClick(String date) {
        Log.d(TAG, "Selected date: " + date); // Log the selected date

        ArrayList<EventTrip> events = new ArrayList<>(currentTrip.getEventTrips().getOrDefault(date, new ArrayList<>()));
        Log.d(TAG, "Events for date: " + events); // Log events to verify

        eventTripViewAdapter.updateEvents(events);
    }

    @Override
    public void switchScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadMenuFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_container, new MenuFragment())
                .commit();
    }


    private void loadRatingFragment() {
        RatingFragment ratingFragment = RatingFragment.newInstance(currentTrip);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rating_frag, ratingFragment)
                .commit();
    }
}
