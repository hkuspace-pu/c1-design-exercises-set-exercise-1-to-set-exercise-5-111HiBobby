package com.example.aaa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

/**
 * This is the adapter for our menu items RecyclerView. It's the bridge between our list of
 * `MenuItem` objects and the actual list items that get displayed on screen.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final List<MenuItem> menuItems; // The actual data we're displaying.
    private final Context context;          // We need this to launch new activities.

    /**
     * Simple constructor to get the data and context we need.
     *
     * @param context The activity that's using this adapter.
     * @param menuItems The list of menu items to show.
     */
    public MenuAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    /**
     * This gets called when the RecyclerView needs a new list item view.
     * We inflate our `list_item_menu.xml` layout and create a new ViewHolder to hold it.
     */
    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    /**
     * This is where we bind the data from a `MenuItem` object to the views in a ViewHolder.
     * It gets called for each item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        // Grab the item for the current row.
        MenuItem item = menuItems.get(position);

        // Set the text and images for this list item.
        holder.name.setText(item.getName());
        holder.price.setText(String.format(Locale.US, "$%.2f", item.getPrice()));
        holder.image.setImageResource(item.getImageResId());

        // Set up the click listener for the little edit button.
        holder.editButton.setOnClickListener(v -> {
            // We'll launch the EditMenuItemActivity, passing along all the current item's info.
            Intent intent = new Intent(context, EditMenuItemActivity.class);
            intent.putExtra(EditMenuItemActivity.EXTRA_NAME, item.getName());
            intent.putExtra(EditMenuItemActivity.EXTRA_PRICE, item.getPrice());
            intent.putExtra(EditMenuItemActivity.EXTRA_POSITION, holder.getAdapterPosition());
            // We cast the context to an Activity to call this method.
            ((Activity) context).startActivityForResult(intent, ManageMenuActivity.EDIT_MENU_ITEM_REQUEST);
        });

        // Set up the click listener for the delete button.
        holder.deleteButton.setOnClickListener(v -> {
            // It's always a good idea to ask the user if they're sure.
            new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // OK, they're sure. Let's delete it.
                        int currentPosition = holder.getAdapterPosition();
                        if (currentPosition != RecyclerView.NO_POSITION) {
                            menuItems.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                            Toast.makeText(v.getContext(), "Deleted: " + item.getName(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null) // If they say no, just close the dialog.
                    .show();
        });
    }

    /**
     * Just tells the RecyclerView how many items are in our list.
     */
    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    /**
     * The ViewHolder is a little wrapper around the views in a single list item.
     * It helps make scrolling super smooth by caching the view references.
     */
    static class MenuViewHolder extends RecyclerView.ViewHolder {
        // The UI elements for a single menu item.
        ImageView image;
        TextView name;
        TextView price;
        ImageButton editButton;
        ImageButton deleteButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find all the views in our layout and hang on to them.
            image = itemView.findViewById(R.id.menu_item_image);
            name = itemView.findViewById(R.id.menu_item_name);
            price = itemView.findViewById(R.id.menu_item_price);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
