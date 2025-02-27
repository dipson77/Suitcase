package com.example.suitcase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_Items extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private CircleImageView addImage;
    private EditText itemName, itemPrice, itemDescription;
    private Button btnAddItem;

    private Uri imageUri;

    private DatabaseHelper databaseHelper;

    // Permission request launcher
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openImageChooser();
                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        addImage = findViewById(R.id.add_img);
        itemName = findViewById(R.id.add_item_name);
        itemPrice = findViewById(R.id.add_item_price);
        itemDescription = findViewById(R.id.add_item_description);
        btnAddItem = findViewById(R.id.btn_add_item);
        databaseHelper = new DatabaseHelper(this);
        addImage.setOnClickListener(v -> checkStoragePermissionAndOpenChooser());
        btnAddItem.setOnClickListener(v -> addItem());
        // Check and log the intent data if any
        Intent intent = getIntent();
        if (intent != null) {
            String categoryName = intent.getStringExtra("category_name");
            String categoryImageUriString = intent.getStringExtra("category_image_uri");
            // Log the received data to verify intent extras
            Log.d("Add_Items", "Received category_name: " + categoryName);
            Log.d("Add_Items", "Received category_image_uri: " + categoryImageUriString);
        }
    }
    // Check storage permission and open image chooser
    private void checkStoragePermissionAndOpenChooser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            openImageChooser();
        } else {
            // Request the permission
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    // Open image chooser
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result from image chooser
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if(imageUri != null){
                Log.d("Add_Items", "Selected image URI: " + imageUri.toString());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    addImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Add_Items", "Failed to load image", e);
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }

            }


        }
    }

    // Adding item to database
    private void addItem() {
        String name = itemName.getText().toString().trim();

        String priceString = itemPrice.getText().toString().trim();
        double price;
        String description = itemDescription.getText().toString().trim();

        if (name.isEmpty() ||  priceString.isEmpty() || description.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all the fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert into database
        boolean isInserted = databaseHelper.insertItems(name,price,description, false, imageUri.toString());
        if (isInserted) {
            Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
        }
    }

}
