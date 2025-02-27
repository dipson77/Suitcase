package com.example.suitcase.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.ItemsModel;
import com.example.suitcase.R;

import java.util.ArrayList;

public class Items_Adapter extends RecyclerView.Adapter<Items_Adapter.ItemViewHolder> {

    private final RecyclerViewItemClick recyclerViewItemClick;
    private ArrayList<ItemsModel> itemsModels;
    private int selectedPosition = -1; // To track the selected item

    public Items_Adapter(RecyclerViewItemClick recyclerViewItemClick, ArrayList<ItemsModel> itemsModels) {
        this.recyclerViewItemClick = recyclerViewItemClick;
        this.itemsModels = itemsModels;
    }

    @NonNull
    @Override
    public Items_Adapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Items_Adapter.ItemViewHolder holder, int position) {
        ItemsModel itemsModel=itemsModels.get(position);
        holder.txt_name.setText(itemsModel.getName());
        if(itemsModel.isPurchased()){
            holder.txt_name.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

        }
        holder.txt_price.setText(String.valueOf(itemsModel.getPrice()));
        holder.txt_Description.setText(itemsModel.getDescription());
        Uri imageUri=itemsModel.getImage();
        if(imageUri !=null){
            holder.imageView.setImageURI(imageUri);
        }else {
            holder.imageView.setImageResource(R.drawable.upload);
        }



        // Handle click listeners for edit and delete icons
        holder.editIcon.setOnClickListener(v -> recyclerViewItemClick.onEditClick(itemsModel, position));
        holder.deleteIcon.setOnClickListener(v -> recyclerViewItemClick.onDeleteClick(itemsModel, position));

    }

    //Items Count

    @Override
    public int getItemCount() {
        return itemsModels.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, editIcon, deleteIcon, shareIcon;
        TextView txt_name,txt_price,txt_Description;

        //Connection between xml to bind

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_Image);
            txt_name=itemView.findViewById(R.id.item_name);
            txt_price=itemView.findViewById(R.id.item_price);
            txt_Description=itemView.findViewById(R.id.item_description);
            editIcon=itemView.findViewById(R.id.edit_icon);
            deleteIcon=itemView.findViewById(R.id.delete_icon);


            // Handle item view click
            itemView.setOnClickListener(v -> {
                // Update selected position
                int previousSelected = selectedPosition;
                selectedPosition = getAdapterPosition();

                // Notify adapter about position changes for the previously and newly selected item
                notifyItemChanged(previousSelected);
                notifyItemChanged(selectedPosition);

                // Trigger the item click callback
                recyclerViewItemClick.onItemClick(v, getAdapterPosition());
            });
        }

        //Handle item view click
        private void itemViewOnClick(View view) {
            recyclerViewItemClick.onItemClick(view,getAdapterPosition());
        }
    }

    // Interface for item click listeners including edit and delete actions

    public interface RecyclerViewItemClick{


        void onItemClick(View view, int position);

        void onShareClick(ItemsModel itemsModel, int position);

        void onEditClick(ItemsModel itemsModel, int position);

        void onDeleteClick(ItemsModel itemsModel, int position);


    }
}