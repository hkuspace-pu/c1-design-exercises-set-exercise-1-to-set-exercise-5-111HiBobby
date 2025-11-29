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
 * This is where staff can manage the restaurant's menu. It displays all the menu items in a list
 * and provides ways to add new items or edit existing ones.
 */
public class ManageMenuActivity extends AppCompatActivity {

    // These are request codes we use to figure out which activity is sending us a result.
    public static final int ADD_MENU_ITEM_REQUEST = 1;
    public static final int EDIT_MENU_ITEM_REQUEST = 2;

    private List<MenuItem> menuItems; // Our list of menu items.
    private MenuAdapter adapter;      // The adapter that feeds our data to the RecyclerView.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        // Set up the toolbar at the top of the screen.
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a handle on our main UI elements.
        RecyclerView recyclerView = findViewById(R.id.menu_recycler_view);
        FloatingActionButton fab = findViewById(R.id.add_menu_item_fab);

        // Here, we're just creating some sample data. In a real app, we'd probably load this
        // from a database or a server.
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Pizza Margherita", 12.50, R.drawable.ic_launcher_background));
        menuItems.add(new MenuItem("Caesar Salad", 8.00, R.drawable.ic_launcher_background));
        menuItems.add(new MenuItem("Beef Burger", 14.75, R.drawable.ic_launcher_background));

        // Now we set up the RecyclerView. We need to give it an adapter and a layout manager.
        adapter = new MenuAdapter(this, menuItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // If the user clicks the floating action button, we'll open the "add item" screen.
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ManageMenuActivity.this, AddMenuItemActivity.class);
            // We use startActivityForResult because we expect to get data back.
            startActivityForResult(intent, ADD_MENU_ITEM_REQUEST);
        });
    }

    /**
     * This method gets called when an activity we started for a result (like adding or editing
     * an item) finishes.
     *
     * @param requestCode The code we used to start the activity.
     * @param resultCode  The result code from the activity (e.g., RESULT_OK).
     * @param data        The intent carrying the result data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // First, make sure everything went well and we actually have some data.
        if (resultCode == RESULT_OK && data != null) {
            // Check if we're getting a result from the "add item" screen.
            if (requestCode == ADD_MENU_ITEM_REQUEST) {
                // Grab the new item's details from the result intent.
                String name = data.getStringExtra(AddMenuItemActivity.EXTRA_NAME);
                double price = data.getDoubleExtra(AddMenuItemActivity.EXTRA_PRICE, 0);

                // Create a new MenuItem and add it to our list.
                MenuItem newItem = new MenuItem(name, price, R.drawable.ic_launcher_background);
                menuItems.add(newItem);
                // Tell the adapter that a new item was added so it can update the list.
                adapter.notifyItemInserted(menuItems.size() - 1);

            // Check if we're getting a result from the "edit item" screen.
            } else if (requestCode == EDIT_MENU_ITEM_REQUEST) {
                // Grab the updated details and the item's original position.
                String name = data.getStringExtra(EditMenuItemActivity.EXTRA_NAME);
                double price = data.getDoubleExtra(EditMenuItemActivity.EXTRA_PRICE, 0);
                int position = data.getIntExtra(EditMenuItemActivity.EXTRA_POSITION, -1);

                // If we have a valid position, update the item at that spot in the list.
                if (position != -1) {
                    MenuItem item = menuItems.get(position);
                    item.setName(name);
                    item.setPrice(price);
                    // Tell the adapter that the item has changed so it can redraw it.
                    adapter.notifyItemChanged(position);
                }
            }
        }
    }
}
