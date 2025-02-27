package com.example.suitcase.Adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.Category;
import com.example.suitcase.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private RecyclerViewItemClick recyclerViewItemClick;

    public CategoryAdapter(List<Category> categoryList, RecyclerViewItemClick recyclerViewItemClick) {

        this.categoryList = categoryList;
        this.recyclerViewItemClick = recyclerViewItemClick;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);

        Uri imageuri = category.getImageUri();  //Get the Uri from the Category
        holder.categoryImage.setImageURI(imageuri); //Set the image using uri
        holder.categoryName.setText(category.getName());


        // set click listener for cardview

        holder.cardView.setOnClickListener(v -> {
            Log.d("CategoryAdapter", "CardView clicked: " + category.getName());
            if (recyclerViewItemClick !=null){
                recyclerViewItemClick.onItemClick(v, category,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        CardView cardView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_image);
            categoryName = itemView.findViewById(R.id.category_name);
            cardView = itemView.findViewById(R.id.category_cardview);
        }
    }

    public interface RecyclerViewItemClick {
        void onItemClick(View view, Category category, int position);
    }

}
