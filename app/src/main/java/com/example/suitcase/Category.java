package com.example.suitcase;

import android.net.Uri;

public class Category {
    private Uri imageUri;  // Uri for the image
    private String name;   // Name of the category

    // Constructor to initialize the fields
    public Category (Uri imageUri, String name) {
        this.imageUri = imageUri;
        this.name = name;
    }

    // Getter for imageUri
    public Uri getImageUri() {
        return imageUri;
    }

    // Setter for imageUri
    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }
}
