package com.example.suitcase.Adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.Booking;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private ArrayList<Booking> bookingList;

    public BookingAdapter(ArrayList<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.textViewType.setText(booking.getType());
        holder.textViewDetails.setText(booking.getDetails());

        if (booking.isCompleted()) {
            holder.textViewType.setPaintFlags(holder.textViewType.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textViewDetails.setPaintFlags(holder.textViewDetails.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textViewType.setPaintFlags(holder.textViewType.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.textViewDetails.setPaintFlags(holder.textViewDetails.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    // Method to mark an item as checked
    public void markAsCompleted(int position) {
        Booking booking = bookingList.get(position);
        booking.setCompleted(!booking.isCompleted());
        notifyItemChanged(position);
    }

    // Method to remove an item
    public void removeItem(int position) {
        bookingList.remove(position);
        notifyItemRemoved(position);
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textViewType;
        TextView textViewDetails;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewType = itemView.findViewById(android.R.id.text1);
            textViewDetails = itemView.findViewById(android.R.id.text2);
        }
    }
}
