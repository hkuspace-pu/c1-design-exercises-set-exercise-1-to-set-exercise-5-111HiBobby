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
 * This screen is for editing an existing menu item. It looks a lot like the "add" screen,
 * but it comes pre-filled with the item's current details.
 */
public class EditMenuItemActivity extends AppCompatActivity {

    // Keys for passing data to and from this activity.
    public static final String EXTRA_NAME = "com.example.aaa.EDIT_NAME";
    public static final String EXTRA_PRICE = "com.example.aaa.EDIT_PRICE";
    public static final String EXTRA_POSITION = "com.example.aaa.EDIT_POSITION";

    // A request code for the image gallery.
    private static final int SELECT_PICTURE = 200;

    // UI bits
    private ImageView imageView;
    private EditText nameEditText;
    private EditText priceEditText;
    private Button selectImageButton;
    private Button saveButton;
    private int position; // We need to remember the item's position in the list.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_item);

        // Wire up the views.
        imageView = findViewById(R.id.menu_item_image);
        nameEditText = findViewById(R.id.menu_item_name);
        priceEditText = findViewById(R.id.menu_item_price);
        selectImageButton = findViewById(R.id.select_image_button);
        saveButton = findViewById(R.id.save_button);

        // Get the data that was passed to us from the list screen.
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_NAME) && intent.hasExtra(EXTRA_PRICE)) {
            // Fill in the form with the item's current data.
            nameEditText.setText(intent.getStringExtra(EXTRA_NAME));
            priceEditText.setText(String.valueOf(intent.getDoubleExtra(EXTRA_PRICE, 0)));
            // We need to hang on to the original position so we can send it back.
            position = intent.getIntExtra(EXTRA_POSITION, -1);
        }

        // The user can select a new image if they want.
        selectImageButton.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), SELECT_PICTURE);
        });

        // When the user saves, we package up the new data and send it back.
        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String priceStr = priceEditText.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the result intent.
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_NAME, name);
            resultIntent.putExtra(EXTRA_PRICE, Double.parseDouble(priceStr));
            resultIntent.putExtra(EXTRA_POSITION, position); // Don't forget the position!

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    /**
     * Called when the user picks an image from the gallery.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
            }
        }
    }
}
