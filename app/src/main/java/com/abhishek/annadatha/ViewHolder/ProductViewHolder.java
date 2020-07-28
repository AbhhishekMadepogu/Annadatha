package com.abhishek.annadatha.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.annadatha.Interfaces.ItemClickListener;
import com.abhishek.annadatha.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductDescription,txtProductPrice;
     public ImageView ivProduct;
     public ItemClickListener listener;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName=itemView.findViewById(R.id.tvproduct_name);
        txtProductDescription=itemView.findViewById(R.id.tvproduct_description);
        txtProductPrice=itemView.findViewById(R.id.tvproduct_price);
        ivProduct=itemView.findViewById(R.id.ivProduct);

    }
    public void setItemclickListener(ItemClickListener listener){
        this.listener=listener;

    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);

    }
}
