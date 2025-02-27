package com.example.suitcase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemDetails extends AppCompatActivity   {

    private CircleImageView itemImage;
    private TextView itemName, itemPrice, itemDescription;
    private Button shareButton, cancelButton;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Initialize views
        itemImage = findViewById(R.id.item_details_image);
        itemName = findViewById(R.id.item_details_name);
        itemPrice = findViewById(R.id.item_details_price);
        itemDescription = findViewById(R.id.item_details_description);
        shareButton = findViewById(R.id.share_button);
        cancelButton = findViewById(R.id.button_cancel);

        // Get data from intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price", 0);
        String description = intent.getStringExtra("description");
        String imageUriString = intent.getStringExtra("image");
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
            itemImage.setImageURI(imageUri);
        } else {
            itemImage.setImageResource(R.drawable.upload);
        }

        // Set data to views
        itemName.setText(name);
        itemPrice.setText(String.format("₹%.2f", price));
        itemDescription.setText(description);

        // Share button click listener
        shareButton.setOnClickListener(v -> {
            String shareText = "Check out this item:\n\n" +
                    "Name: " + name + "\n" +
                    "Price: ₹" + String.format("%.2f", price) + "\n" +
                    "Description: " + description;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            startActivity(Intent.createChooser(shareIntent, "Share item via"));
        });
        cancelButton.setOnClickListener(v -> {
            Intent cancelintent = new Intent(ItemDetails.this, Manage_Items.class);
            startActivity(cancelintent);
            finish();
        });


    }
}
