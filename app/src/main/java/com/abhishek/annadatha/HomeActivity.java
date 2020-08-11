package com.abhishek.annadatha;

import android.content.Intent;
import android.os.Bundle;

import com.abhishek.annadatha.Model.Products;
import com.abhishek.annadatha.Prevalent.Prevalent;
import com.abhishek.annadatha.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.abhishek.annadatha.ViewHolder.ProductViewHolder;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference productsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String type="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        //type=getIntent().getExtras().get("Admin").toString();
        Intent i=getIntent();
        Bundle bundle=i.getExtras();

        setContentView(R.layout.activity_home);
        Paper.init(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        TextView txtUserName=header.findViewById(R.id.tvUsername);
        ImageView ivUser=header.findViewById(R.id.ivprofile_image);
        String l=Paper.book().read(Prevalent.userName);

        //Toast.makeText(this, Prevalent.CurrentonlineUser.getName(), Toast.LENGTH_SHORT).show();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
       // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);
        recyclerView = findViewById(R.id.recyclemenu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(layoutManager);
        if(bundle!=null){
            type=getIntent().getExtras().get("Admin").toString();
            txtUserName.setText("Admin");

        }
        else {
            txtUserName.setText(Prevalent.CurrentonlineUser.getName());
            Picasso.get().load(Prevalent.CurrentonlineUser.getImage()).into(ivUser);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type==""){
                    navtoCart();
                }
                else{
                    AdminToast();
                }



            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options= new FirebaseRecyclerOptions.Builder<Products>().setQuery(productsRef.orderByChild("productSatate").equalTo("Approved"),Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText("Price: "+model.getPrice());
                holder.txtProductDescription.setText(model.getDescription());
                Picasso.get().load(model.getImage()).into(holder.ivProduct);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(type.equals("Admin")){
                            Intent i=new Intent(HomeActivity.this,AdminChangePriceActivity.class);
                            i.putExtra("pid",model.getPid());
                            startActivity(i);

                        }
                        else{
                            Intent i=new Intent(HomeActivity.this,ProductDetailsActivity.class);
                            i.putExtra("pid",model.getPid());
                            startActivity(i);

                        }

                    }
                });


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout,parent,false);
               ProductViewHolder holder=new ProductViewHolder(view);
                Toast.makeText(HomeActivity.this, "Hello", Toast.LENGTH_SHORT).show();
               return holder;
            }
        };
         recyclerView.setAdapter(adapter);
         adapter.startListening();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.nav_cart){
            if(type==""){
                navtoCart();
            }
            else{
                AdminToast();
            }


        }
        else if(id==R.id.nav_categories){
            if(type==""){
                Intent i=new Intent(HomeActivity.this,CustomerCategoryActivity.class);
                startActivity(i);
            }
            else{
                AdminToast();
            }


        }
        else if(id==R.id.nav_orders){

        }
        else if(id==R.id.nav_logout){
            if (type == "") {
                Paper.book().write(Prevalent.password,"");
                Paper.book().write(Prevalent.phone,"");
                Paper.book().write(Prevalent.userName,"");
                Intent i=new Intent(HomeActivity.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);
                finish();

            }
          else{
                AdminToast();
            }
        }
        else if(id==R.id.nav_Rateus){
          if(type==""){
              Intent i=new Intent(HomeActivity.this,SearchProductsActivity.class);
              startActivity(i);
          }
          else{
              AdminToast();
          }

        }
        else if(id==R.id.nav_settings){
           if(type==""){
               Intent i=new Intent(HomeActivity.this,SettingsActivity.class);
               startActivity(i);
           }
           else{
               AdminToast();
           }

        }
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void AdminToast() {
        Toast.makeText(this, "This Feature is Not Available for Admin", Toast.LENGTH_SHORT).show();
    }

    private void navtoCart() {
        Intent i=new Intent(HomeActivity.this,CartActivity.class);
        startActivity(i);
    }
}
