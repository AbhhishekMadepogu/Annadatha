package com.abhishek.annadatha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.annadatha.Model.Products;
import com.abhishek.annadatha.Prevalent.Prevalent;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    ImageView imgProduct;
    TextView tvProductName,tvProductPrice,tvDescription;
    Button btnAddcart;
    ElegantNumberButton btnQuantity;
    String pid="",saveCurrentDate,saveCurrentTime,randomKey;
    //String ProductName;
    String phone=Prevalent.CurrentonlineUser.getPhone();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pid=getIntent().getStringExtra("pid");
        setContentView(R.layout.activity_product_details);
        tvProductName=findViewById(R.id.productdetailsName);
        tvDescription=findViewById(R.id.tvProductdetailsDescription);
        tvProductPrice=findViewById(R.id.tvProductdetailsPrice);
        btnAddcart=findViewById(R.id.btnproductdetailsaddtocart);
        btnQuantity=findViewById(R.id.numberbtn);
        imgProduct=findViewById(R.id.productDetailsImage);
        //ProductName=tvProductName.getText().toString();

        getProductDetails(pid);
        btnAddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addtocart();
            }
        });
    }

    private void Addtocart() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MM,dd,yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());
        randomKey=saveCurrentDate+saveCurrentTime;
        final DatabaseReference cartref=FirebaseDatabase.getInstance().getReference().child("Cart");
        DatabaseReference dref=FirebaseDatabase.getInstance().getReference().child("CARTW");
        final HashMap<String,Object> cartmap=new HashMap<>();
        cartmap.put("pid",pid);
        cartmap.put("pname",tvProductName.getText().toString());
        cartmap.put("quantity",btnQuantity.getNumber());
        cartmap.put("price",tvProductPrice.getText().toString());
        cartmap.put("date",saveCurrentDate);
        cartmap.put("time",saveCurrentTime);
        cartmap.put("discount","");
        dref.child(Prevalent.CurrentonlineUser.getPhone()).child(pid).updateChildren(cartmap);
        cartref.child("User View").child(Prevalent.CurrentonlineUser.getPhone()).child("Products").child(pid).updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    cartref.child("Admin View").child(Prevalent.CurrentonlineUser.getPhone()).child("Products").child(pid).updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProductDetailsActivity.this,"Product "+tvProductName.getText().toString()+" Added to cart Successfully ", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });


    }

    private void getProductDetails(String pid) {
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference().child("Products");
         dref.child(pid).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists()){
                     Products products=dataSnapshot.getValue(Products.class);
                     tvProductName.setText(products.getPname());
                     tvProductPrice.setText(products.getPrice());
                     tvDescription.setText(products.getDescription());
                     Picasso.get().load(products.getImage()).into(imgProduct);

                 }
                 else
                     Toast.makeText(ProductDetailsActivity.this, "Not Valibale", Toast.LENGTH_SHORT).show();

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
    }
}
