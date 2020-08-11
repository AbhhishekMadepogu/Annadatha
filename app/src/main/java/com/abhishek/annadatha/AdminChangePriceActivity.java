package com.abhishek.annadatha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.abhishek.annadatha.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminChangePriceActivity extends AppCompatActivity {
    EditText txtPrice,txtDescription,txtName;
    Button btnApplyCahnges;
    ImageView ivProductImage;
    String pid;
    DatabaseReference dref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_change_price);
        pid=getIntent().getStringExtra("pid");
        dref= FirebaseDatabase.getInstance().getReference().child("Products").child(pid);
        txtPrice=findViewById(R.id.txtPriceChangePrice);
        txtDescription=findViewById(R.id.txtPriceChangeDescription);
        txtName=findViewById(R.id.txtNameChangePrice);
        ivProductImage=findViewById(R.id.ivchangeprice);
        btnApplyCahnges=findViewById(R.id.btnAPllychanges);
        onDisplay();
        btnApplyCahnges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();


            }
        });

    }

    private void applyChanges() {
        String name=txtName.getText().toString();
        String description=txtDescription.getText().toString();
        String price=txtPrice.getText().toString();
        if (name.equals("")){
            Toast.makeText(this, "Product Name should not be Empty", Toast.LENGTH_SHORT).show();

        }
        else if (description.equals("")){
            Toast.makeText(this, "Product Description should not be Empty", Toast.LENGTH_SHORT).show();

        }
        else if (price.equals("")){
            Toast.makeText(this, "Product Price should not be Empty", Toast.LENGTH_SHORT).show();

        }
        HashMap<String,Object> productmap=new HashMap<>();

        productmap.put("pname",name);
        productmap.put("description",description);
        productmap.put("price",price);
        dref.updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AdminChangePriceActivity.this, "Product Details Edited Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void onDisplay() {
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                   // Products products=dataSnapshot.getValue(Products.class);
                    String name=dataSnapshot.child("pname").getValue().toString();
                    String Description=dataSnapshot.child("description").getValue().toString();
                    String Price=dataSnapshot.child("price").getValue().toString();
                    String Image=dataSnapshot.child("image").getValue().toString();
                    txtName.setText(name);
                    txtDescription.setText(Description);
                    txtPrice.setText(Price);
                    Picasso.get().load(Image).into(ivProductImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}