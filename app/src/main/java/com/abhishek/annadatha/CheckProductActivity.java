package com.abhishek.annadatha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abhishek.annadatha.Interfaces.ItemClickListener;
import com.abhishek.annadatha.Model.Products;
import com.abhishek.annadatha.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CheckProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference  unverifiedProductsref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_product);
        unverifiedProductsref= FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView=findViewById(R.id.rvapprove);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().
                setQuery(unverifiedProductsref.orderByChild("productSatate").equalTo("Not Approved"),Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull Products products) {
                holder.txtProductName.setText(products.getPname());
                holder.txtProductPrice.setText("Price: "+products.getPrice());
                holder.txtProductDescription.setText(products.getDescription());
                Picasso.get().load(products.getImage()).into(holder.ivProduct);
                final Products itemclick=products;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(CheckProductActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                        final String pid=itemclick.getPid();
                        CharSequence options[]=new CharSequence[]{

                                "Yes",
                                "No"

                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CheckProductActivity.this);
                        builder.setTitle("Are you sure you want to Approove the Product");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    changeProductState(pid);
                                }

                            }
                        });
                        builder.show();
                    }
                });


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                //Toast.makeText(HomeActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void changeProductState(String pid) {
        unverifiedProductsref.child(pid).child("productSatate").setValue("Approved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CheckProductActivity.this, "Product Approoved", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}