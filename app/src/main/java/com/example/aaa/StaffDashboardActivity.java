package com.example.aaa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the main hub for staff members.
 * It's a simple screen with buttons that lead to the different management areas of the app.
 */
public class StaffDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        // Grab the buttons from the layout.
        Button manageMenuButton = findViewById(R.id.manage_menu_button);
        Button manageReservationsButton = findViewById(R.id.manage_reservations_button);

        // When the user clicks "Manage Menu", we'll send them to the ManageMenuActivity.
        manageMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, ManageMenuActivity.class);
            startActivity(intent);
        });

        // Likewise, for "Manage Reservations", we go to the ManageReservationsActivity.
        manageReservationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, ManageReservationsActivity.class);
            startActivity(intent);
        });
    }
}
