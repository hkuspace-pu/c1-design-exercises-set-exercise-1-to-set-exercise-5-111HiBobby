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
 * Handles the creation of a new menu item. It provides a form for staff to input item details,
 * which are then returned to the calling activity (ManageMenuActivity).
 */
public class AddMenuItemActivity extends AppCompatActivity {

    // Keys for returning data in the result Intent.
    public static final String EXTRA_NAME = "com.example.aaa.NAME";
    public static final String EXTRA_PRICE = "com.example.aaa.PRICE";

    // Request code for the image selection Intent.
    private static final int SELECT_PICTURE = 200;

    private ImageView imageView;
    private EditText nameEditText;
    private EditText priceEditText;
    private Button selectImageButton;
    private Button saveButton;

    /**
     * Initializes the activity, binds the UI components, and sets up event listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        imageView = findViewById(R.id.menu_item_image);
        nameEditText = findViewById(R.id.menu_item_name);
        priceEditText = findViewById(R.id.menu_item_price);
        selectImageButton = findViewById(R.id.select_image_button);
        saveButton = findViewById(R.id.save_button);

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        });

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String priceStr = priceEditText.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return; // Exit if form is incomplete.
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_NAME, name);
            resultIntent.putExtra(EXTRA_PRICE, Double.parseDouble(priceStr));

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    /**
     * Catches the result from the image gallery. If an image is successfully selected,
     * it's displayed in the ImageView.
     * @param requestCode The integer request code originally supplied to startActivityForResult().
     * @param resultCode The integer result code returned by the child activity.
     * @param data An Intent that can return result data to the caller.
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
