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

import com.example.suitcase.Adapter.BookingAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BookingList extends AppCompatActivity {
    private ArrayList<Booking> bookingList;
    private BookingAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view_bookings);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        // Initialize the booking list
        bookingList = new ArrayList<>();
        bookingList.add(new Booking("Flight", "Flight number: ABC123"));
        bookingList.add(new Booking("Hotel", "Hotel name: The Grand Resort"));
        bookingList.add(new Booking("Car Rental", "Rental company: XYZ Rentals"));

        // Initialize the adapter and set it to the RecyclerView
        adapter = new BookingAdapter(bookingList);
        recyclerView.setAdapter(adapter);

        //Initialize Backspace arrow
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView backarrow = findViewById(R.id.arrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingList.this, Manage_Items.class);
                startActivity(intent);
                finish();
            }
        });


        // Initialize the FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab_add_booking);
        fab.setOnClickListener(v -> showAddBookingDialog());

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
                    Toast.makeText(BookingList.this, "Booking removed", Toast.LENGTH_SHORT).show();
                } else if (direction == ItemTouchHelper.LEFT) {
                    // Handle item checked
                    adapter.markAsCompleted(position);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    if (dX > 0) {
                        // Swiping to the right (delete action)
                        c.clipRect(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + dX, itemView.getBottom());
                        c.drawColor(ContextCompat.getColor(BookingList.this, android.R.color.holo_red_light));
                    } else {
                        // Swiping to the left (mark as completed action)
                        c.clipRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        c.drawColor(ContextCompat.getColor(BookingList.this, android.R.color.holo_green_light));
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // Method to show the dialog for adding a new booking
    private void showAddBookingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Booking");

        // Inflate the custom layout for the dialog
        View customLayout = LayoutInflater.from(this).inflate(R.layout.dialog_add_booking, null);
        builder.setView(customLayout);

        // Access the EditText in the custom layout
        final EditText editTextType = customLayout.findViewById(R.id.edit_text_type);
        final EditText editTextDetails = customLayout.findViewById(R.id.edit_text_details);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String type = editTextType.getText().toString().trim();
            String details = editTextDetails.getText().toString().trim();
            if (!type.isEmpty() && !details.isEmpty()) {
                addNewBooking(new Booking(type, details));
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }

    // Method to add a new booking to the list and notify the adapter
    private void addNewBooking(Booking booking) {
        bookingList.add(booking);
        adapter.notifyItemInserted(bookingList.size() - 1);
    
    }
}