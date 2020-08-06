package com.abhishek.annadatha.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.annadatha.Interfaces.ItemClickListener;
import com.abhishek.annadatha.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtPrice,txtQuanity,txtname;
    public ItemClickListener listener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtname=itemView.findViewById(R.id.itmProductName);
        txtPrice=itemView.findViewById(R.id.itmProductPrice);
        txtQuanity=itemView.findViewById(R.id.itmProductQuantity);

    }
    public void setItemclickListener(ItemClickListener listener){
        this.listener=listener;

    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }
}
