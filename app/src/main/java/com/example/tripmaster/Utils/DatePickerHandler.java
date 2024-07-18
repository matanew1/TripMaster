package com.example.tripmaster.Utils;

import android.app.DatePickerDialog;
import android.widget.EditText;

import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerHandler {

    private EditText editText;
    private FragmentManager fragmentManager;
    private DateSelectedListener dateSelectedListener;

    public DatePickerHandler(EditText editText, FragmentManager fragmentManager) {
        this.editText = editText;
        this.fragmentManager = fragmentManager;
        setupDatePicker();
    }

    public interface DateSelectedListener {
        void onDateSelected(String date);
    }

    public void setDateSelectedListener(DateSelectedListener listener) {
        this.dateSelectedListener = listener;
    }

    private void setupDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel(calendar);
        };

        editText.setOnClickListener(v -> new DatePickerDialog(editText.getContext(), date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void updateLabel(Calendar calendar) {
        String myFormat = "yyyy-MM-dd"; // Change the format as needed
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String selectedDate = sdf.format(calendar.getTime());
        editText.setText(selectedDate);

        if (dateSelectedListener != null) {
            dateSelectedListener.onDateSelected(selectedDate);
        }
    }
}
