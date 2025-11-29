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
 * Allows staff to edit the details of an existing menu item. The form is pre-populated
 * with the item's current data, and the updated information is sent back to the menu management screen.
 */
public class EditMenuItemActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "com.example.aaa.EDIT_NAME";
    public static final String EXTRA_PRICE = "com.example.aaa.EDIT_PRICE";
    public static final String EXTRA_POSITION = "com.example.aaa.EDIT_POSITION";

    private static final int SELECT_PICTURE = 200;

    private ImageView imageView;
    private EditText nameEditText;
    private EditText priceEditText;
    private Button selectImageButton;
    private Button saveButton;
    private int position;

    /**
     * Initializes the activity, populates the form with the existing menu item's data,
     * and sets up the save and image selection listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_item);

        imageView = findViewById(R.id.menu_item_image);
        nameEditText = findViewById(R.id.menu_item_name);
        priceEditText = findViewById(R.id.menu_item_price);
        selectImageButton = findViewById(R.id.select_image_button);
        saveButton = findViewById(R.id.save_button);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_NAME) && intent.hasExtra(EXTRA_PRICE)) {
            nameEditText.setText(intent.getStringExtra(EXTRA_NAME));
            priceEditText.setText(String.valueOf(intent.getDoubleExtra(EXTRA_PRICE, 0)));
            position = intent.getIntExtra(EXTRA_POSITION, -1);
        }

        selectImageButton.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), SELECT_PICTURE);
        });

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String priceStr = priceEditText.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_NAME, name);
            resultIntent.putExtra(EXTRA_PRICE, Double.parseDouble(priceStr));
            resultIntent.putExtra(EXTRA_POSITION, position);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    /**
     * Handles the result from the image gallery, updating the ImageView with the selected image.
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
