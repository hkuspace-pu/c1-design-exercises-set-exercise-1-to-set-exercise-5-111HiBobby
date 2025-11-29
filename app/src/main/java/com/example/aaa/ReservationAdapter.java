package com.example.aaa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This is the adapter for our reservations RecyclerView. It takes our list of `Reservation`
 * objects and maps them to the list items we see on the screen.
 */
public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private final List<Reservation> reservations; // The data we're working with.
    private final Context context;              // We need this to launch other screens.

    /**
     * A basic constructor to get the data we need.
     *
     * @param context The activity this adapter is being used in.
     * @param reservations The list of reservations to display.
     */
    public ReservationAdapter(Context context, List<Reservation> reservations) {
        this.context = context;
        this.reservations = reservations;
    }

    /**
     * This gets called when the RecyclerView needs to create a new list item view.
     * We just inflate our layout for a single reservation item.
     */
    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    /**
     * This is where we connect the data from a `Reservation` object to the actual views
     * in our list item.
     */
    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        // Get the reservation for the current row.
        Reservation reservation = reservations.get(position);

        // Set the customer's name, the date/time, and the table number.
        holder.customerName.setText(reservation.getCustomerName());
        holder.dateTime.setText(String.format("%s %s", reservation.getDate(), reservation.getTime()));
        holder.tableNumber.setText(String.format("Table: %d", reservation.getTableNumber()));

        // If the user taps on the main part of the item, we'll open the edit screen.
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditReservationActivity.class);
            // We pass all the reservation's info to the edit screen so it can pre-fill the form.
            intent.putExtra(EditReservationActivity.EXTRA_NAME, reservation.getCustomerName());
            intent.putExtra(EditReservationActivity.EXTRA_DATE, reservation.getDate());
            intent.putExtra(EditReservationActivity.EXTRA_TIME, reservation.getTime());
            intent.putExtra(EditReservationActivity.EXTRA_TABLE, reservation.getTableNumber());
            intent.putExtra(EditReservationActivity.EXTRA_POSITION, holder.getAdapterPosition());
            // We start it for a result, expecting to get the updated data back.
            ((Activity) context).startActivityForResult(intent, ManageReservationsActivity.EDIT_RESERVATION_REQUEST);
        });

        // Handle the click on the "Cancel" button.
        holder.cancelButton.setOnClickListener(v -> {
            // Always a good idea to confirm before doing something destructive.
            new AlertDialog.Builder(context)
                    .setTitle("Cancel Reservation")
                    .setMessage("Are you sure you want to cancel this reservation?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // The user is sure. Let's remove the reservation.
                        int currentPosition = holder.getAdapterPosition();
                        if (currentPosition != RecyclerView.NO_POSITION) {
                            reservations.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                            Toast.makeText(context, "Cancelled reservation for " + reservation.getCustomerName(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null) // If they say no, we don't do anything.
                    .show();
        });
    }

    /**
     * Tells the RecyclerView how many items are in our list.
     */
    @Override
    public int getItemCount() {
        return reservations.size();
    }

    /**
     * Our ViewHolder. It's a simple container that holds onto the views for a single list item.
     * This is what makes the RecyclerView so efficient.
     */
    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        // The views in our list item layout.
        TextView customerName;
        TextView dateTime;
        TextView tableNumber;
        Button cancelButton;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find all the views and keep a reference to them.
            customerName = itemView.findViewById(R.id.customer_name);
            dateTime = itemView.findViewById(R.id.reservation_time);
            tableNumber = itemView.findViewById(R.id.table_number);
            cancelButton = itemView.findViewById(R.id.cancel_button);
        }
    }
}
