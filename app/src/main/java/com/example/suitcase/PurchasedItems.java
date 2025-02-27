package com.example.suitcase;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.Adapter.Items_Adapter;

import java.util.ArrayList;

public class PurchasedItems extends AppCompatActivity implements Items_Adapter.RecyclerViewItemClick {

    private RecyclerView recyclerView;
    private Items_Adapter itemsAdapter;
    private ArrayList<ItemsModel> purchasedItemsList;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_items);


        recyclerView = findViewById(R.id.recycler_view_purchased_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        purchasedItemsList = databaseHelper.getPurchasedItems();

        itemsAdapter = new Items_Adapter(this, purchasedItemsList);
        recyclerView.setAdapter(itemsAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        // Handle click on purchased item if needed
    }

    @Override
    public void onShareClick(ItemsModel itemsModel, int position) {

    }

    @Override
    public void onEditClick(ItemsModel itemsModel, int position) {
        // Handle edit on purchased item if needed
    }

    @Override
    public void onDeleteClick(ItemsModel itemsModel, int position) {
        // Handle delete on purchased item if needed
    }
}
