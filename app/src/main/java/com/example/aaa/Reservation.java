package com.example.aaa;

/**
 * A simple data class to hold all the info for a single customer reservation.
 * Just a basic container for our reservation details.
 */
public class Reservation {
    private String customerName;
    private String date; // We're just storing this as a string, e.g., "2024-10-28"
    private String time; // And this as a string, too, e.g., "19:30"
    private int tableNumber;

    /**
     * Constructor for creating a new reservation.
     *
     * @param customerName The name of the person who booked.
     * @param date The date of the booking (YYYY-MM-DD).
     * @param time The time of the booking (HH:MM).
     * @param tableNumber The table they've been assigned.
     */
    public Reservation(String customerName, String date, String time, int tableNumber) {
        this.customerName = customerName;
        this.date = date;
        this.time = time;
        this.tableNumber = tableNumber;
    }

    // --- Just the standard getters and setters below ---

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
}
