package com.example.suitcase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.Adapter.Items_Adapter;
import com.example.suitcase.Adapter.RecyclerViewItemClick;

import java.util.ArrayList;
import java.util.UUID;

public class Category_Items extends AppCompatActivity implements RecyclerViewItemClick {
    private static final int ADD_ITEM_REQUEST_CODE = 1;

    private ArrayList<ItemsModel> items;
    private Items_Adapter itemsAdapter;
    private String categoryName;
    private Uri categoryImageUri;
    private RecyclerView itemsRecyclerView;
    private Button addItemButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Get data from intent
        Intent intent =getIntent();
        categoryName = intent.getStringExtra("category_name");
        categoryImageUri = Uri.parse(intent.getStringExtra("category_image_uri"));

        // Set the activity title to the category name
        setTitle(categoryName);

        // Initialize the items list
        items = new ArrayList<>();




        // Set up RecyclerView

        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new Items_Adapter((Items_Adapter.RecyclerViewItemClick) this,items);
        itemsRecyclerView.setAdapter(itemsAdapter);

        // Set up Add Item Button
        addItemButton = findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(v -> {
            Intent addItemIntent = new Intent(Category_Items.this, Add_Items.class);
            startActivityForResult(addItemIntent,ADD_ITEM_REQUEST_CODE);
        });


    }

    @Override
    public void onItemClick(View view, int position) {
        // Handle item click (open edit/delete dialog)
        ItemsModel clickedItem = items.get(position);
        showEditDeleteDialog(clickedItem,position);

    }

    @Override
    public void onEditClick(View view, ItemsModel item, int position) {
        // Handle edit click
        // Implement the edit functionality here
    }

    @Override
    public void onDeleteClick(View view, ItemsModel item, int position) {
        // Handle delete click
        // Implement the delete functionality here
        items.remove(position);
        itemsAdapter.notifyItemRemoved(position);
        itemsAdapter.notifyItemRangeChanged(position, items.size());
    }

    @Override
    public void onItemClick(View v, Category category, int position) {
        // Item clicked
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Retrieve data from AddItemActivity
            String name = data.getStringExtra("item_name");
            String description = data.getStringExtra("item_description");
            double price = data.getDoubleExtra("item_price", 0.0);
            String imageUriString = data.getStringExtra("item_image");
            Uri imageUri = imageUriString != null ? Uri.parse(imageUriString) : null;

            // Create a new item and add it to the list
            ItemsModel newItem = new ItemsModel();
            newItem.setId(UUID.randomUUID().toString()); // Generate a unique ID for the item
            newItem.setName(name);
            newItem.setDescription(description);
            newItem.setPrice(price);
            newItem.setImage(imageUri);
            newItem.setPurchased(false);

            items.add(newItem);
            itemsAdapter.notifyDataSetChanged();
        }
    }


    private void showEditDeleteDialog(ItemsModel item, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.item_dialog,null);
        builder.setView(dialogView);

        // Get the dialog elements
        EditText editItemName = dialogView.findViewById(R.id.editName);
        EditText editItemPrice = dialogView.findViewById(R.id.editPrice);
        EditText editItemDescription = dialogView.findViewById(R.id.editDescription);
        EditText editItemImage = dialogView.findViewById(R.id.editImage);
        Button btn_save = dialogView.findViewById(R.id.saveButton);
        Button btn_delete = dialogView.findViewById(R.id.deleteButton);

        //Populate the fields with the existing data

        editItemName.setText(item.getName());
        editItemPrice.setText(String.valueOf(item.getPrice()));
        editItemDescription.setText(item.getDescription());
        editItemImage.setText(item.getImage() != null ? item.getImage().toString() :"");


        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Handle Save button click

        btn_save.setOnClickListener(v -> {
            item.setName(editItemName.getText().toString());
            item.setPrice(Double.parseDouble(editItemPrice.getText().toString()));
            item.setDescription(editItemDescription.getText().toString());
            String imageUriString  = editItemImage.getText().toString();
            item.setImage(!imageUriString.isEmpty() ? Uri.parse(imageUriString) : null);


            //Notify the adapter about the changes
            itemsAdapter.notifyItemChanged(position);
            dialog.dismiss();
        });

        // Handle Delete button click

        btn_delete.setOnClickListener(v -> {
            // Remove the item from the list
            items.remove(position);
            itemsAdapter.notifyItemRemoved(position);
            itemsAdapter.notifyItemRangeChanged(position,items.size());
            dialog.dismiss();
        });
    }
}