package com.example.tripmaster.Utils;

import android.view.View;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

public class DatePickerHandler {

    private MaterialDatePicker<Long> materialDatePicker;

    public DatePickerHandler(TextInputEditText etDate, FragmentManager fragmentManager) {
        materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            etDate.setText(materialDatePicker.getHeaderText());
        });

        etDate.setOnClickListener(v -> materialDatePicker.show(fragmentManager, "MATERIAL_DATE_PICKER"));
    }
}
