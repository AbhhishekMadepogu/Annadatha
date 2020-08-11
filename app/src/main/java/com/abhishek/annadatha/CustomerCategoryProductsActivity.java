package com.abhishek.annadatha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.annadatha.Model.Products;
import com.abhishek.annadatha.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CustomerCategoryProductsActivity extends AppCompatActivity {
    String category;
    RecyclerView rvProducts;
    TextView txtCategoryName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_category_products);
        rvProducts=findViewById(R.id.rvCategoryCustomer);
        txtCategoryName=findViewById(R.id.tvcustomerCAtegory);

        category=getIntent().getExtras().get("category").toString();
        txtCategoryName.setText(category);
        Toast.makeText(this, ""+category, Toast.LENGTH_SHORT).show();
        onStart();


    }

    @Override
    protected void onStart() {
        super.onStart();
        category=getIntent().getExtras().get("category").toString();
        DatabaseReference drefs= FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().
                setQuery(drefs.orderByChild("category").equalTo("fruits"),Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products products) {
                holder.txtProductName.setText(products.getPname());
                holder.txtProductPrice.setText("Price: "+products.getPrice());
                holder.txtProductDescription.setText(products.getDescription());
                Picasso.get().load(products.getImage()).into(holder.ivProduct);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(CustomerCategoryProductsActivity.this,ProductDetailsActivity.class);
                        i.putExtra("pid",products.getPid());
                        startActivity(i);
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                //Toast.makeText(SearchProductsActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                return holder;
            }
        };
        rvProducts.setAdapter(adapter);
        adapter.startListening();
    }
}