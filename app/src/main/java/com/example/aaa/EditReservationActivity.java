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
 * Handles the editing of an existing reservation. This form is pre-filled with the reservation's
 * current details, allowing staff to make changes and save them.
 */
public class EditReservationActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "com.example.aaa.EDIT_RESERVATION_NAME";
    public static final String EXTRA_DATE = "com.example.aaa.EDIT_RESERVATION_DATE";
    public static final String EXTRA_TIME = "com.example.aaa.EDIT_RESERVATION_TIME";
    public static final String EXTRA_TABLE = "com.example.aaa.EDIT_RESERVATION_TABLE";
    public static final String EXTRA_POSITION = "com.example.aaa.EDIT_RESERVATION_POSITION";

    private EditText nameEditText;
    private TextView dateTextView;
    private TextView timeTextView;
    private EditText tableEditText;
    private int position;

    /**
     * Initializes the activity, populates the form with existing reservation data,
     * and sets up listeners for user interaction.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);

        nameEditText = findViewById(R.id.customer_name_edit_text);
        dateTextView = findViewById(R.id.date_text_view);
        timeTextView = findViewById(R.id.time_text_view);
        tableEditText = findViewById(R.id.table_number_edit_text);

        dateTextView.setOnClickListener(v -> showDatePickerDialog());
        timeTextView.setOnClickListener(v -> showTimePickerDialog());

        Intent intent = getIntent();
        if (intent != null) {
            nameEditText.setText(intent.getStringExtra(EXTRA_NAME));
            dateTextView.setText(intent.getStringExtra(EXTRA_DATE));
            timeTextView.setText(intent.getStringExtra(EXTRA_TIME));
            tableEditText.setText(String.valueOf(intent.getIntExtra(EXTRA_TABLE, 0)));
            position = intent.getIntExtra(EXTRA_POSITION, -1);
        }

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String date = dateTextView.getText().toString();
            String time = timeTextView.getText().toString();
            String tableStr = tableEditText.getText().toString().trim();

            if (name.isEmpty() || date.isEmpty() || time.isEmpty() || tableStr.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_NAME, name);
            resultIntent.putExtra(EXTRA_DATE, date);
            resultIntent.putExtra(EXTRA_TIME, time);
            resultIntent.putExtra(EXTRA_TABLE, Integer.parseInt(tableStr));
            resultIntent.putExtra(EXTRA_POSITION, position);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    /**
     * Displays the system's default date picker dialog.
     */
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.format(Locale.US, "%d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            dateTextView.setText(selectedDate);
        }, year, month, day).show();
    }

    /**
     * Displays the system's default time picker dialog.
     */
    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String selectedTime = String.format(Locale.US, "%02d:%02d", hourOfDay, minute1);
            timeTextView.setText(selectedTime);
        }, hour, minute, true).show();
    }
}
