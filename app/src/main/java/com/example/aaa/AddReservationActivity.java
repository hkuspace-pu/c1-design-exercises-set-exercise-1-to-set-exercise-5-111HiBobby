package com.example.aaa;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

/**
 * This screen is a straightforward form for booking a new reservation.
 * It uses the system's own date and time pickers which is much nicer than making the user
 * type it all out. Once done, it sends the new reservation back to the main list.
 */
public class AddReservationActivity extends AppCompatActivity {

    // These are the keys we use for the data we pass back to the previous screen.
    public static final String EXTRA_NAME = "com.example.aaa.RESERVATION_NAME";
    public static final String EXTRA_DATE = "com.example.aaa.RESERVATION_DATE";
    public static final String EXTRA_TIME = "com.example.aaa.RESERVATION_TIME";
    public static final String EXTRA_TABLE = "com.example.aaa.RESERVATION_TABLE";

    // The UI elements in our form.
    private EditText nameEditText;
    private TextView dateTextView;
    private TextView timeTextView;
    private EditText tableEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        // Get a handle on all the views from our layout file.
        nameEditText = findViewById(R.id.customer_name_edit_text);
        dateTextView = findViewById(R.id.date_text_view);
        timeTextView = findViewById(R.id.time_text_view);
        tableEditText = findViewById(R.id.table_number_edit_text);

        // Make the date and time fields pop up the pickers when tapped.
        dateTextView.setOnClickListener(v -> showDatePickerDialog());
        timeTextView.setOnClickListener(v -> showTimePickerDialog());

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            // Grab all the info from the form.
            String name = nameEditText.getText().toString().trim();
            String date = dateTextView.getText().toString();
            String time = timeTextView.getText().toString();
            String tableStr = tableEditText.getText().toString().trim();

            // A quick and simple validation to make sure nothing is left blank.
            if (name.isEmpty() || date.equals(getString(R.string.date_yyyy_mm_dd)) || time.equals(getString(R.string.time_hh_mm)) || tableStr.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return; // Bail out if the form isn't fully filled.
            }

            // Bundle up the new reservation's details to send back.
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_NAME, name);
            resultIntent.putExtra(EXTRA_DATE, date);
            resultIntent.putExtra(EXTRA_TIME, time);
            resultIntent.putExtra(EXTRA_TABLE, Integer.parseInt(tableStr));

            // Set the result and head back to the previous screen.
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    /**
     * Pops up the standard Android date picker, starting at today's date.
     */
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create the dialog and set what happens when the user picks a date.
        new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            // The month is 0-indexed, so we have to add 1 to get it right.
            String selectedDate = String.format(Locale.US, "%d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            dateTextView.setText(selectedDate);
        }, year, month, day).show();
    }

    /**
     * Pops up the standard Android time picker, starting at the current time.
     */
    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create the dialog and set what happens when the user picks a time.
        new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String selectedTime = String.format(Locale.US, "%02d:%02d", hourOfDay, minute1);
            timeTextView.setText(selectedTime);
        }, hour, minute, true).show(); // `true` for 24-hour format.
    }
}
