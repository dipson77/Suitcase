package com.example.suitcase.Adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private ArrayList<String> documentList;

    public DocumentAdapter(ArrayList<String> documentList) {
        this.documentList = documentList;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        String document = documentList.get(position);
        holder.textView.setText(document);
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    // Method to mark an item as checked
    public void markAsChecked(int position) {
        String document = documentList.get(position);
        documentList.set(position, document + " (Checked)");
        notifyItemChanged(position);
    }

    static class DocumentViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
