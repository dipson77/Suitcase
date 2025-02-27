package com.example.suitcase.Adapter;

import android.view.View;

import com.example.suitcase.Category;
import com.example.suitcase.ItemsModel;

public interface RecyclerViewItemClick {
    public void onItemClick(View view, int position);

    void onEditClick(View view, ItemsModel item, int position);

    void onDeleteClick(View view, ItemsModel item, int position);

    void onItemClick(View v, Category category, int position);
}
