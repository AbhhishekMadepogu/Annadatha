package com.abhishek.annadatha;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abhishek.annadatha.Model.Products;
import com.abhishek.annadatha.ViewHolder.ItemViewHolder;
import com.abhishek.annadatha.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellerHomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        unverifiedProductsref= FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView=findViewById(R.id.tvSellerHome);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_logout, R.id.navigation_add)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                //Toast.makeText(SellerHomeActivity.this, "Hello mf", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.navigation_add:
                Intent i=new Intent(SellerHomeActivity.this,AdminActivity.class);
                startActivity(i);
                return true;
            case R.id.navigation_logout:
                final FirebaseAuth m =FirebaseAuth.getInstance();
                m.signOut();
                Intent i1=new Intent(SellerHomeActivity.this,MainActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i1);
                finish();

                return  true;
                
        }
        return false;
    }
});
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().
                setQuery(unverifiedProductsref.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class).build();
        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_layout_products,parent,false);
                ItemViewHolder holder=new ItemViewHolder(view);
                //Toast.makeText(HomeActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i, @NonNull Products products) {
                itemViewHolder.txtProductName.setText(products.getPname());
                itemViewHolder.txtProductPrice.setText("Price: "+products.getPrice());
                itemViewHolder.txtProductDescription.setText(products.getDescription());
                Picasso.get().load(products.getImage()).into(itemViewHolder.ivProduct);
                itemViewHolder.tvStatus.setText(products.getProductSatate());
                final Products itemclick=products;
                itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String pid=itemclick.getPid();
                        CharSequence options[]=new CharSequence[]{

                                "Yes",
                                "No"

                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(SellerHomeActivity.this);
                        builder.setTitle("Are you sure you want to Delete the Product");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    DeleteProduct(pid);
                                }

                            }
                        });
                        builder.show();
                    }
                });


            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    private void DeleteProduct(String pid) {
        unverifiedProductsref.child(pid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SellerHomeActivity.this, "Product Deleted Succesfully", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
    }

}