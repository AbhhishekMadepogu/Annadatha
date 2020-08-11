package com.abhishek.annadatha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abhishek.annadatha.Model.Products;
import com.abhishek.annadatha.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProductsActivity extends AppCompatActivity {
    EditText edtSearch;
    Button btnSearch;
    RecyclerView rvSearch;
    String StringSearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        edtSearch=findViewById(R.id.edtSearch);
        btnSearch=findViewById(R.id.btnSearch);
        rvSearch=findViewById(R.id.rvSearch);
        rvSearch.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringSearch=edtSearch.getText().toString();
                onStart();


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().
                setQuery(dref.orderByChild("pname").startAt(StringSearch).endAt(StringSearch),Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products products) {
                holder.txtProductName.setText(products.getPname());
                holder.txtProductPrice.setText("Price: "+products.getPrice());
                holder.txtProductDescription.setText(products.getDescription());
                Picasso.get().load(products.getImage()).into(holder.ivProduct);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(SearchProductsActivity.this,ProductDetailsActivity.class);
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
        rvSearch.setAdapter(adapter);
        adapter.startListening();


    }
}