package com.example.suitcase.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.HealthSupply;
import com.example.suitcase.R;

import java.util.ArrayList;

public class HealthCareAdapter extends RecyclerView.Adapter<HealthCareAdapter.ViewHolder> {

    private ArrayList<HealthSupply> itemList;

    public HealthCareAdapter(ArrayList<HealthSupply> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_healthcare, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HealthSupply item = itemList.get(position);
        holder.textName.setText(item.getName());
        holder.textQuantity.setText(String.valueOf(item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textQuantity;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_healthcare_name);
            textQuantity = itemView.findViewById(R.id.text_healthcare_quantity);

        }
    }

    // Method to delete item
    public void deleteItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }
}
