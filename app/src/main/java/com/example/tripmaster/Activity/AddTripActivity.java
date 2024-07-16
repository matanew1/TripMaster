package com.example.tripmaster.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tripmaster.R;

public class AddTripActivity extends AppCompatActivity implements ScreenSwitchListener {

    private DatePicker datePicker;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        initializeViews();
        initializeSharedPreferences();
        loadSavedDate();
        setupDatePickerListener();
        setupCancelButton();
    }

    /**
     * Initializes the UI components.
     */
    private void initializeViews() {
        datePicker = findViewById(R.id.datePicker);
    }

    /**
     * Initializes SharedPreferences for saving trip data.
     */
    private void initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("TripPreferences", MODE_PRIVATE);
    }

    /**
     * Loads the saved date from SharedPreferences and updates the DatePicker.
     */
    private void loadSavedDate() {
        int year = sharedPreferences.getInt("saved_year", datePicker.getYear());
        int month = sharedPreferences.getInt("saved_month", datePicker.getMonth());
        int day = sharedPreferences.getInt("saved_day", datePicker.getDayOfMonth());
        datePicker.updateDate(year, month, day);
    }

    /**
     * Sets up a listener for the DatePicker to auto-save the selected date.
     */
    private void setupDatePickerListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> saveDate(year, monthOfYear, dayOfMonth));
        }
    }

    /**
     * Saves the selected date to SharedPreferences.
     *
     * @param year      The selected year.
     * @param month     The selected month.
     * @param day       The selected day.
     */
    private void saveDate(int year, int month, int day) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("saved_year", year);
        editor.putInt("saved_month", month);
        editor.putInt("saved_day", day);
        editor.apply();

        String selectedDate = day + "/" + (month + 1) + "/" + year;
        Toast.makeText(this, "Trip Date Saved: " + selectedDate, Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets up the cancel button to switch back to the home screen.
     */
    private void setupCancelButton() {
        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(btn -> switchScreen());
    }

    @Override
    public void switchScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
