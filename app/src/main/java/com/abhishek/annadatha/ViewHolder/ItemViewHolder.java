package com.abhishek.annadatha.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhishek.annadatha.Interfaces.ItemClickListener;
import com.abhishek.annadatha.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductDescription,txtProductPrice,tvStatus;
    public ImageView ivProduct;
    public ItemClickListener listener;
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName=itemView.findViewById(R.id.tvproduct_name1);
        txtProductDescription=itemView.findViewById(R.id.tvproduct_description1);
        txtProductPrice=itemView.findViewById(R.id.tvproduct_price1);
        ivProduct=itemView.findViewById(R.id.ivProduct1);
        tvStatus=itemView.findViewById(R.id.stats);

    }
    public void setItemclickListener(ItemClickListener listener){
        this.listener=listener;

    }
    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);


    }
}
