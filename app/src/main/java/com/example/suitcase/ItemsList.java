package com.example.suitcase;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.Adapter.Items_Adapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemsList extends AppCompatActivity implements Items_Adapter.RecyclerViewItemClick {

    private RecyclerView recyclerView;
    private Items_Adapter itemsAdapter;
    private ArrayList<ItemsModel> itemsList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recycler_view_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        itemsList = databaseHelper.getAllItems();

        itemsAdapter = new Items_Adapter(this, itemsList);
        recyclerView.setAdapter(itemsAdapter);

        ImageView backarrow = findViewById(R.id.arrow);
        backarrow.setOnClickListener(v -> {
            Intent intent = new Intent(ItemsList.this, Manage_Items.class);
            startActivity(intent);
            finish();
        });

        FloatingActionButton fab = findViewById(R.id.fab_add_list_item);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ItemsList.this, Add_Items.class);
            startActivity(intent);
        });

        // Attach ItemTouchHelper for swipe gestures
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // No move actions required
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    // Handle delete action
                    ItemsModel itemToDelete = itemsList.get(position);
                    databaseHelper.deleteItem(Long.parseLong(itemToDelete.getId()));
                    itemsList.remove(position);
                    itemsAdapter.notifyItemRemoved(position);
                    Toast.makeText(ItemsList.this, "Item Deleted: " + itemToDelete.getName(), Toast.LENGTH_SHORT).show();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Handle mark as purchased action
                    ItemsModel itemToUpdate = itemsList.get(position);
                    itemToUpdate.setPurchased(true);
                    databaseHelper.update(
                            itemToUpdate.getId(),
                            itemToUpdate.getName(),
                            itemToUpdate.getPrice(),
                            itemToUpdate.getDescription(),
                            itemToUpdate.getImage(),
                            itemToUpdate.isPurchased()
                    );
                    itemsAdapter.notifyItemChanged(position);
                    Toast.makeText(ItemsList.this, "Item Marked as Purchased", Toast.LENGTH_SHORT).show();

                    //Navigate to PurchasedItems activity
                    Intent intent = new Intent(ItemsList.this, PurchasedItems.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20; // Adjust as needed

                if (dX > 0) { // Swiping to the right
                    ColorDrawable background = new ColorDrawable(Color.GREEN);
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
                    background.draw(c);
                } else if (dX < 0) { // Swiping to the left
                    ColorDrawable background = new ColorDrawable(Color.RED);
                    background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    background.draw(c);
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onItemClick(View view, int position) {
        ItemsModel item = itemsList.get(position);
        Intent intent = new Intent(ItemsList.this, ItemDetails.class);
        intent.putExtra("name", item.getName());
        intent.putExtra("price", item.getPrice());
        intent.putExtra("description", item.getDescription());
        Uri imageUri = item.getImage();
        if (imageUri != null) {
            intent.putExtra("image", imageUri.toString());
        }
        startActivity(intent);
    }

    @Override
    public void onShareClick(ItemsModel itemsModel, int position) {
        String shareContent = "Item Name: " + itemsModel.getName() + "\n" +
                "Price: $" + itemsModel.getPrice() + "\n" +
                "Description: " + itemsModel.getDescription();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);

        // Optionally add the item image if available
        Uri imageUri = itemsModel.getImage();
        if (imageUri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/jpeg"); // Update MIME type accordingly
        }

        startActivity(Intent.createChooser(shareIntent, "Share item using"));
    }

    @Override
    public void onEditClick(ItemsModel itemsModel, int position) {
        showEditDialog(itemsModel);
        Toast.makeText(this, "Edit Clicked: " + itemsModel.getName(), Toast.LENGTH_SHORT).show();
    }

    private void showEditDialog(ItemsModel item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.item_dialog, null);
        builder.setView(dialogView);

        CircleImageView editImage = dialogView.findViewById(R.id.editImage);
        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editPrice = dialogView.findViewById(R.id.editPrice);
        EditText editDescription = dialogView.findViewById(R.id.editDescription);
        Button saveButton = dialogView.findViewById(R.id.saveButton);
        Button deleteButton = dialogView.findViewById(R.id.deleteButton);

        editName.setText(item.getName());
        editPrice.setText(String.valueOf(item.getPrice()));
        editDescription.setText(item.getDescription());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        saveButton.setOnClickListener(v -> {
            String newName = editName.getText().toString().trim();
            String newPriceStr = editPrice.getText().toString().trim();
            String newDescription = editDescription.getText().toString().trim();

            if (newName.isEmpty() || newPriceStr.isEmpty() || newDescription.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double newPrice = Double.parseDouble(newPriceStr);

            boolean updated = databaseHelper.update(
                    item.getId(),
                    newName,
                    newPrice,
                    newDescription,
                    item.getImage(),
                    item.isPurchased()
            );

            if (updated) {
                item.setName(newName);
                item.setPrice(newPrice);
                item.setDescription(newDescription);
                itemsAdapter.notifyItemChanged(itemsList.indexOf(item));
                Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show();
            }

            alertDialog.dismiss();
        });

        deleteButton.setOnClickListener(v -> {
            databaseHelper.deleteItem(Long.parseLong(item.getId()));
            itemsList.remove(item);
            itemsAdapter.notifyItemRemoved(itemsList.indexOf(item));
            Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });
    }

    @Override
    public void onDeleteClick(ItemsModel itemsModel, int position) {
        databaseHelper.deleteItem(Long.parseLong(itemsModel.getId()));
        itemsList.remove(position);
        itemsAdapter.notifyItemRemoved(position);
        Toast.makeText(this, "Item Deleted: " + itemsModel.getName(), Toast.LENGTH_SHORT).show();
    }
}
