package com.example.tripmaster.Utils;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.tripmaster.Data.DataManager;
import com.example.tripmaster.Model.Trip;

public class TextChangeHandler {

    private DataManager dataManager;

    public TextChangeHandler() {
        dataManager = DataManager.getInstance();
    }

    public void setupTextWatchers(EditText titleTrip, EditText countryTrip) {
        titleTrip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String title = s.toString().trim();
                Trip currentTrip = getCurrentTrip();
                if (currentTrip != null) {
                    currentTrip.setTitle(title);
                    dataManager.updateTrip(currentTrip);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.err.println("RESSSSSSSS: "+dataManager.toString());
            }
        });

        countryTrip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String country = s.toString().trim();
                Trip currentTrip = getCurrentTrip();
                if (currentTrip != null) {
                    currentTrip.setLocation(country);
                    dataManager.updateTrip(currentTrip);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.err.println("RESSSSSSSS: "+dataManager.toString());
            }
        });
    }

    private Trip getCurrentTrip() {
        if (dataManager.getTrips() != null && !dataManager.getTrips().isEmpty()) {
            return dataManager.getTrips().get(dataManager.getTrips().size() - 1);
        }
        return null;
    }
}