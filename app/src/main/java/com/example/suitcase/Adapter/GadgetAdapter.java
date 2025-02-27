package com.example.suitcase.Adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.Gadget;
import com.example.suitcase.R;

import java.util.ArrayList;

public class GadgetAdapter extends RecyclerView.Adapter<GadgetAdapter.GadgetViewHolder> {

    private ArrayList<Gadget> gadgets;

    public GadgetAdapter(ArrayList<Gadget> gadgets) {
        this.gadgets = gadgets;
    }

    @NonNull
    @Override
    public GadgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gadgets, parent, false);
        return new GadgetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GadgetViewHolder holder, int position) {
        Gadget currentGadget = gadgets.get(position);
        holder.textViewName.setText(currentGadget.getName());
        holder.textViewDescription.setText(currentGadget.getDescription());

        if (currentGadget.isChecked()) {
            holder.textViewName.setPaintFlags(holder.textViewName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textViewName.setPaintFlags(holder.textViewName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return gadgets.size();
    }

    public void removeItem(int position) {
        gadgets.remove(position);
        notifyItemRemoved(position);
    }

    public void markAsChecked(int position) {
        Gadget gadget = gadgets.get(position);
        gadget.setChecked(!gadget.isChecked());
        notifyItemChanged(position);
    }

    public static class GadgetViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewDescription;

        public GadgetViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_gadget_name);
            textViewDescription = itemView.findViewById(R.id.text_gadget_description);
        }
    }
}
