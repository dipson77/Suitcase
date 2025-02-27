package com.example.suitcase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.Adapter.DocumentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Document_List extends AppCompatActivity {

    private ArrayList<String> documentList;
    private DocumentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);

        // Initialize the RecyclerView
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView recyclerView = findViewById(R.id.recycler_view_documents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the document list with some default values
        documentList = new ArrayList<>();
        documentList.add("Passport");
        documentList.add("Visa");
        documentList.add("Flight tickets");
        documentList.add("Hotel reservations");
        documentList.add("Travel insurance");
        documentList.add("Driver's license");
        documentList.add("Emergency contacts");
        documentList.add("Itinerary");
        documentList.add("Vaccination records");
        documentList.add("Credit cards/cash");
        documentList.add("Travel guidebook");
        documentList.add("Copies of important documents");

        // Initialize the adapter and set it to the RecyclerView
        adapter = new DocumentAdapter(documentList);
        recyclerView.setAdapter(adapter);

        //Initialize Backspace arrow
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView backarrow = findViewById(R.id.arrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Document_List.this, Manage_Items.class);
                startActivity(intent);
                finish();
            }
        });

        // Initialize the FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab_add_document);
        fab.setOnClickListener(v -> showAddDocumentDialog());

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
                    documentList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(Document_List.this, "Document removed", Toast.LENGTH_SHORT).show();
                } else if (direction == ItemTouchHelper.LEFT) {
                    // Handle item checked
                    adapter.markAsChecked(position);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    if (dX > 0) {
                        // Swiping to the right (delete action)
                        c.clipRect(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + dX, itemView.getBottom());
                        c.drawColor(ContextCompat.getColor(Document_List.this, android.R.color.holo_red_light));
                    } else {
                        // Swiping to the left (mark as checked action)
                        c.clipRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        c.drawColor(ContextCompat.getColor(Document_List.this, android.R.color.holo_green_light));
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // Method to show the dialog for adding a new document
    private void showAddDocumentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Document");

        // Inflate the custom layout for the dialog
        View customLayout = LayoutInflater.from(this).inflate(R.layout.dialog_add_document, null);
        builder.setView(customLayout);

        // Access the EditText in the custom layout
        final EditText editTextDocument = customLayout.findViewById(R.id.edit_text_document);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String newDocument = editTextDocument.getText().toString().trim();
            if (!newDocument.isEmpty()) {
                addNewDocument(newDocument);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }

    // Method to add a new document to the list and notify the adapter
    private void addNewDocument(String document) {
        documentList.add(document);
        adapter.notifyItemInserted(documentList.size() - 1);
    }
}
