package com.example.aaa;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a list of reservations, allowing staff to view, add, or edit bookings.
 * This screen acts as the primary interface for reservation management.
 */
public class ManageReservationsActivity extends AppCompatActivity {

    public static final int ADD_RESERVATION_REQUEST = 1;
    public static final int EDIT_RESERVATION_REQUEST = 2;

    private List<Reservation> reservations;
    private ReservationAdapter adapter;

    /**
     * Initializes the activity, sets up the toolbar and RecyclerView,
     * and populates the initial list of reservations.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reservations);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.reservations_recycler_view);
        FloatingActionButton fab = findViewById(R.id.add_reservation_fab);

        reservations = new ArrayList<>();
        reservations.add(new Reservation("Alice Smith", "2024-06-10", "18:00", 2));
        reservations.add(new Reservation("Bob Johnson", "2024-06-10", "19:30", 4));
        reservations.add(new Reservation("Charlie Brown", "2024-06-11", "20:00", 8));

        adapter = new ReservationAdapter(this, reservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ManageReservationsActivity.this, AddReservationActivity.class);
            startActivityForResult(intent, ADD_RESERVATION_REQUEST);
        });
    }

    /**
     * Handles the result from activities launched for adding or editing a reservation.
     * It updates the reservations list based on the data returned.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult().
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ADD_RESERVATION_REQUEST) {
                String name = data.getStringExtra(AddReservationActivity.EXTRA_NAME);
                String date = data.getStringExtra(AddReservationActivity.EXTRA_DATE);
                String time = data.getStringExtra(AddReservationActivity.EXTRA_TIME);
                int table = data.getIntExtra(AddReservationActivity.EXTRA_TABLE, 0);

                Reservation newReservation = new Reservation(name, date, time, table);
                reservations.add(newReservation);
                adapter.notifyItemInserted(reservations.size() - 1);

            } else if (requestCode == EDIT_RESERVATION_REQUEST) {
                String name = data.getStringExtra(EditReservationActivity.EXTRA_NAME);
                String date = data.getStringExtra(EditReservationActivity.EXTRA_DATE);
                String time = data.getStringExtra(EditReservationActivity.EXTRA_TIME);
                int table = data.getIntExtra(EditReservationActivity.EXTRA_TABLE, 0);
                int position = data.getIntExtra(EditReservationActivity.EXTRA_POSITION, -1);

                if (position != -1) {
                    Reservation reservation = reservations.get(position);
                    reservation.setCustomerName(name);
                    reservation.setDate(date);
                    reservation.setTime(time);
                    reservation.setTableNumber(table);
                    adapter.notifyItemChanged(position);
                }
            }
        }
    }
}
