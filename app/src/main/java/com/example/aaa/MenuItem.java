package com.example.aaa;

/**
 * Just a plain old Java object (POJO) to represent a single item on our menu.
 * Nothing fancy here, just a simple data container.
 */
public class MenuItem {
    private String name;
    private double price;
    private int imageResId; // For now, we're just using a local drawable resource ID.

    /**
     * Constructor for creating a new menu item.
     *
     * @param name The name of the dish, like "Margherita Pizza".
     * @param price The price of the dish.
     * @param imageResId The local drawable ID for the item's picture.
     */
    public MenuItem(String name, double price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
    }

    // --- Just the standard getters and setters below ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
