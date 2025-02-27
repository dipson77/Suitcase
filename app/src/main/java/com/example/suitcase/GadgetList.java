package com.example.suitcase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.Adapter.GadgetAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class GadgetList extends AppCompatActivity {
    private ArrayList<Gadget> gadgetList;
    private GadgetAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gadget_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) 
        RecyclerView recyclerView = findViewById(R.id.recycler_view_gadgets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the gadget list
        gadgetList = new ArrayList<>();
        gadgetList.add(new Gadget("Smartphone", "For communication and navigation"));
        gadgetList.add(new Gadget("Camera", "For capturing memories"));
        gadgetList.add(new Gadget("Laptop", "For work or entertainment"));
        gadgetList.add(new Gadget("Power Bank", "To charge devices on the go"));

        // Initialize the adapter and set it to the RecyclerView
        adapter = new GadgetAdapter(gadgetList);
        recyclerView.setAdapter(adapter);

        //Initialize Backspace arrow
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView backarrow = findViewById(R.id.arrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GadgetList.this, Manage_Items.class);
                startActivity(intent);
                finish();
            }
        });

        // Initialize the FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab_add_gadgets);
        fab.setOnClickListener(v -> showAddGadgetDialog());

        // Setup ItemTouchHelper for swipe gestures
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // We don't want to handle move actions
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.RIGHT) {
                    // Handle item removal
                    adapter.removeItem(position);
                    Toast.makeText(GadgetList.this, "Gadget removed", Toast.LENGTH_SHORT).show();
                } else if (direction == ItemTouchHelper.LEFT) {
                    // Handle item checked
                    adapter.markAsChecked(position);
                    Toast.makeText(GadgetList.this, "Gadget Packed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    if (dX > 0) {
                        // Swiping to the right (delete action)
                        c.clipRect(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + dX, itemView.getBottom());
                        c.drawColor(ContextCompat.getColor(GadgetList.this, android.R.color.holo_red_light));
                    } else {
                        // Swiping to the left (mark as checked action)
                        c.clipRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        c.drawColor(ContextCompat.getColor(GadgetList.this, android.R.color.holo_green_light));
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // Method to show the dialog for adding a new gadget
    private void showAddGadgetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Gadget");

        // Inflate the custom layout for the dialog
        View customLayout = LayoutInflater.from(this).inflate(R.layout.dialog_add_gadget, null);
        builder.setView(customLayout);

        // Access the EditText in the custom layout
        final EditText editTextGadgetName = customLayout.findViewById(R.id.edit_text_gadget_name);
        final EditText editTextGadgetDescription = customLayout.findViewById(R.id.edit_text_gadget_description);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Access the Save and Cancel buttons
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonSave = customLayout.findViewById(R.id.button_save);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonCancel = customLayout.findViewById(R.id.button_cancel);

        // Handle the Save button click
        buttonSave.setOnClickListener(v -> {
            String name = editTextGadgetName.getText().toString().trim();
            String description = editTextGadgetDescription.getText().toString().trim();
            if (!name.isEmpty() && !description.isEmpty()) {
                addNewGadget(new Gadget(name, description));
                dialog.dismiss(); // Close the dialog after saving
            } else {
                Toast.makeText(GadgetList.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle the Cancel button click
        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }

    // Method to add a new gadget to the list and notify the adapter
    private void addNewGadget(Gadget gadget) {
        gadgetList.add(gadget);
        adapter.notifyItemInserted(gadgetList.size() - 1);
    
    }
}