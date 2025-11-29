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
 * Provides an interface for staff to manage the restaurant's menu. It lists all available
 * items and allows for adding or editing them.
 */
public class ManageMenuActivity extends AppCompatActivity {

    public static final int ADD_MENU_ITEM_REQUEST = 1;
    public static final int EDIT_MENU_ITEM_REQUEST = 2;

    private List<MenuItem> menuItems;
    private MenuAdapter adapter;

    /**
     * Initializes the activity, sets up the RecyclerView with menu items, and configures the
     * floating action button for adding new items.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.menu_recycler_view);
        FloatingActionButton fab = findViewById(R.id.add_menu_item_fab);

        // In a real application, this data would be fetched from a database or a remote server.
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Pizza Margherita", 12.50, R.drawable.ic_launcher_background));
        menuItems.add(new MenuItem("Caesar Salad", 8.00, R.drawable.ic_launcher_background));
        menuItems.add(new MenuItem("Beef Burger", 14.75, R.drawable.ic_launcher_background));

        adapter = new MenuAdapter(this, menuItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ManageMenuActivity.this, AddMenuItemActivity.class);
            startActivityForResult(intent, ADD_MENU_ITEM_REQUEST);
        });
    }

    /**
     * Processes results from the Add and Edit MenuItem activities. It updates the menu item list
     * based on the data returned.
     *
     * @param requestCode The integer code that you originally supplied to startActivityForResult().
     * @param resultCode  An integer result code returned by the child activity.
     * @param data        An Intent that can return result data to the caller.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ADD_MENU_ITEM_REQUEST) {
                String name = data.getStringExtra(AddMenuItemActivity.EXTRA_NAME);
                double price = data.getDoubleExtra(AddMenuItemActivity.EXTRA_PRICE, 0);

                MenuItem newItem = new MenuItem(name, price, R.drawable.ic_launcher_background);
                menuItems.add(newItem);
                adapter.notifyItemInserted(menuItems.size() - 1);

            } else if (requestCode == EDIT_MENU_ITEM_REQUEST) {
                String name = data.getStringExtra(EditMenuItemActivity.EXTRA_NAME);
                double price = data.getDoubleExtra(EditMenuItemActivity.EXTRA_PRICE, 0);
                int position = data.getIntExtra(EditMenuItemActivity.EXTRA_POSITION, -1);

                if (position != -1) {
                    MenuItem item = menuItems.get(position);
                    item.setName(name);
                    item.setPrice(price);
                    adapter.notifyItemChanged(position);
                }
            }
        }
    }
}
