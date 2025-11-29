package com.example.aaa;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This screen provides a simple form for staff to add a new item to the menu.
 * After filling out the details and hitting save, it sends the new item's data
 * back to the ManageMenuActivity.
 */
public class AddMenuItemActivity extends AppCompatActivity {

    // Keys for the data we'll pass back in the result Intent.
    public static final String EXTRA_NAME = "com.example.aaa.NAME";
    public static final String EXTRA_PRICE = "com.example.aaa.PRICE";

    // A request code for our image selection intent.
    private static final int SELECT_PICTURE = 200;

    // UI bits
    private ImageView imageView;
    private EditText nameEditText;
    private EditText priceEditText;
    private Button selectImageButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        // Wire up the views.
        imageView = findViewById(R.id.menu_item_image);
        nameEditText = findViewById(R.id.menu_item_name);
        priceEditText = findViewById(R.id.menu_item_price);
        selectImageButton = findViewById(R.id.select_image_button);
        saveButton = findViewById(R.id.save_button);

        // When the user wants to select an image, we'll open up the gallery.
        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        });

        // When the save button is clicked, we validate and package up the data.
        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String priceStr = priceEditText.getText().toString().trim();

            // A quick check to make sure nothing is empty.
            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return; // Bail out if the form isn't complete.
            }

            // Create an intent to ship the data back.
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_NAME, name);
            resultIntent.putExtra(EXTRA_PRICE, Double.parseDouble(priceStr));

            // Set the result and finish this activity.
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    /**
     * This gets called when the user has selected an image from the gallery.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Make sure the result is from our image picker and that it was successful.
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            // The image URI is in the data intent. Check it's not null.
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                // Set the image on our ImageView.
                imageView.setImageURI(selectedImageUri);
            }
        }
    }
}
