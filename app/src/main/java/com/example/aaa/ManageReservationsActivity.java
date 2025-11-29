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
 * This is where staff can manage customer reservations. It shows a list of all the current
 * bookings and lets staff add new ones or edit existing ones.
 */
public class ManageReservationsActivity extends AppCompatActivity {

    // Request codes to help us figure out which screen is sending us a result.
    public static final int ADD_RESERVATION_REQUEST = 1;
    public static final int EDIT_RESERVATION_REQUEST = 2;

    private List<Reservation> reservations; // The data for our list.
    private ReservationAdapter adapter;       // The adapter to link our data to the list view.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reservations);

        // Set up the toolbar.
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a handle on the rest of the UI bits.
        RecyclerView recyclerView = findViewById(R.id.reservations_recycler_view);
        FloatingActionButton fab = findViewById(R.id.add_reservation_fab);

        // Let's create some sample data to work with for now.
        reservations = new ArrayList<>();
        reservations.add(new Reservation("Alice Smith", "2024-06-10", "18:00", 2));
        reservations.add(new Reservation("Bob Johnson", "2024-06-10", "19:30", 4));
        reservations.add(new Reservation("Charlie Brown", "2024-06-11", "20:00", 8));

        // Hook up the RecyclerView to our adapter and a layout manager.
        adapter = new ReservationAdapter(this, reservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // When the plus button is tapped, we'll open the screen to add a new reservation.
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ManageReservationsActivity.this, AddReservationActivity.class);
            startActivityForResult(intent, ADD_RESERVATION_REQUEST);
        });
    }

    /**
     * This gets called when a screen we opened for a result (like adding or editing)
     * is finished.
     *
     * @param requestCode The code we used when we opened the screen.
     * @param resultCode  Tells us if the operation was successful.
     * @param data        The data sent back from the other screen.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // First, let's make sure the user didn't just hit the back button.
        if (resultCode == RESULT_OK && data != null) {
            // Was this a new reservation?
            if (requestCode == ADD_RESERVATION_REQUEST) {
                // Yep. Let's pull the details out of the intent.
                String name = data.getStringExtra(AddReservationActivity.EXTRA_NAME);
                String date = data.getStringExtra(AddReservationActivity.EXTRA_DATE);
                String time = data.getStringExtra(AddReservationActivity.EXTRA_TIME);
                int table = data.getIntExtra(AddReservationActivity.EXTRA_TABLE, 0);

                // Create our new reservation object and add it to the list.
                Reservation newReservation = new Reservation(name, date, time, table);
                reservations.add(newReservation);
                // Tell the adapter that there's a new item to show.
                adapter.notifyItemInserted(reservations.size() - 1);

            // Or was this an edited reservation?
            } else if (requestCode == EDIT_RESERVATION_REQUEST) {
                // Okay, let's get the updated info.
                String name = data.getStringExtra(EditReservationActivity.EXTRA_NAME);
                String date = data.getStringExtra(EditReservationActivity.EXTRA_DATE);
                String time = data.getStringExtra(EditReservationActivity.EXTRA_TIME);
                int table = data.getIntExtra(EditReservationActivity.EXTRA_TABLE, 0);
                int position = data.getIntExtra(EditReservationActivity.EXTRA_POSITION, -1);

                // If we got a valid position, let's update the item in our list.
                if (position != -1) {
                    Reservation reservation = reservations.get(position);
                    reservation.setCustomerName(name);
                    reservation.setDate(date);
                    reservation.setTime(time);
                    reservation.setTableNumber(table);
                    // Tell the adapter this item has changed so it can redraw it.
                    adapter.notifyItemChanged(position);
                }
            }
        }
    }
}
