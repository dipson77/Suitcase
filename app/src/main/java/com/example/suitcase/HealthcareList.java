package com.example.suitcase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.suitcase.Adapter.HealthCareAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class HealthcareList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HealthCareAdapter adapter;
    private ArrayList<HealthSupply> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_healthcare_list);

        // Handle edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_healthcare);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        adapter = new HealthCareAdapter(itemList);
        recyclerView.setAdapter(adapter);

        //Initialize Backspace arrow
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView backarrow = findViewById(R.id.arrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthcareList.this, Manage_Items.class);
                startActivity(intent);
                finish();
            }
        });

        // Initialize FloatingActionButton for adding new items
        FloatingActionButton fab = findViewById(R.id.fab_add_healthcare_item);
        fab.setOnClickListener(v -> showAddItemDialog());

        // Attach ItemTouchHelper for swipe actions
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // Show dialog to add a new healthcare item
    private void showAddItemDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_healthcare_item, null);
        dialogBuilder.setView(dialogView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editTextItemName = dialogView.findViewById(R.id.edit_text_healthcare_name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editTextItemQuantity = dialogView.findViewById(R.id.edit_text_healthcare_quantity);

        dialogBuilder.setPositiveButton("Add", (dialog, which) -> {
            String itemName = editTextItemName.getText().toString().trim();
            String itemQuantityStr = editTextItemQuantity.getText().toString().trim();

            if (!itemName.isEmpty() && !itemQuantityStr.isEmpty()) {
                try {
                    int itemQuantity = Integer.parseInt(itemQuantityStr);

                    // Check if quantity is a positive number
                    if (itemQuantity > 0) {
                        addNewItem(itemName, itemQuantity);
                    } else {
                        editTextItemQuantity.setError("Quantity must be a positive number");
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where quantity input is not a valid number
                    editTextItemQuantity.setError("Please enter a valid number for quantity");
                }
            } else {
                // Provide feedback to the user if fields are empty
                if (itemName.isEmpty()) {
                    editTextItemName.setError("Please enter the item name");
                }
                if (itemQuantityStr.isEmpty()) {
                    editTextItemQuantity.setError("Please enter the item quantity");
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }


    // Add new item to the list
    private void addNewItem(String itemName, int itemQuantity) {
        HealthSupply newItem = new HealthSupply(itemName, itemQuantity);
        itemList.add(newItem);
        adapter.notifyItemInserted(itemList.size() - 1); // Notify adapter about new item insertion
        Log.d("HealthcareList", "Added new item: " + itemName + " with quantity: " + itemQuantity);
    }

    // Inner class for handling swipe-to-delete
    private class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

        public SwipeToDeleteCallback() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            // We are not handling onMove actions here
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                // Swipe left to delete
                itemList.remove(position);
                adapter.notifyItemRemoved(position);
            } else if (direction == ItemTouchHelper.RIGHT) {
                // Swipe right to mark as checked (or any other action)
                HealthSupply item = itemList.get(position);
                item.setPacked(!item.isPacked()); // Toggle the packed status
                adapter.notifyItemChanged(position);
            }
        }
    }
}
