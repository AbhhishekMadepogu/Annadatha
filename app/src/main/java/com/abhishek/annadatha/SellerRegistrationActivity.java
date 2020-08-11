package com.abhishek.annadatha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {
    EditText txtPhoneNumber,txtAddress,txtPassword,txtName,txtEmail;
    Button btnLogin,btnRegister;
    private FirebaseAuth mauth;
    ProgressDialog loadingbar;
     String name= "",password="",Address="",phone="",email="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);
        loadingbar=new ProgressDialog(this);
        mauth=FirebaseAuth.getInstance();
        txtPhoneNumber=findViewById(R.id.etsellerregPhone);
        txtAddress=findViewById(R.id.etsellerregAddress);
        txtName=findViewById(R.id.etsellerregName);
        txtEmail=findViewById(R.id.etsellerEmail);
        txtPassword=findViewById(R.id.etsellerregPassword);
        btnRegister=findViewById(R.id.btnSellerregregister);
        btnLogin=findViewById(R.id.btnSellerreglogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SellerRegistrationActivity.this,SellerLoginActivity.class);
                startActivity(i);

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellerRegister();

            }
        });
    }

    private void sellerRegister() {
         name= txtName.getText().toString();
      password=txtPassword.getText().toString();
        Address=txtAddress.getText().toString();
      phone=txtPhoneNumber.getText().toString();
       email=txtEmail.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "name should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Address)){
            Toast.makeText(this, "Address should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Phone  should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Email should not be empty", Toast.LENGTH_SHORT).show();
        }

       else {
            loadingbar.setTitle("Creating Seller Account");
            loadingbar.setMessage("Please Wait while we are checking your credentials...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isComplete()){
                        final DatabaseReference rootref;
                        rootref= FirebaseDatabase.getInstance().getReference();
                        String sid=mauth.getCurrentUser().getUid();
                        HashMap<String,Object>sellermap=new HashMap<>();
                        sellermap.put("sid",sid);
                        sellermap.put("phone",phone);
                        sellermap.put("email",email);
                        sellermap.put("password",password);
                        sellermap.put("address",Address);
                        sellermap.put("name",name);
                        rootref.child("Sellers").child(sid).updateChildren(sellermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingbar.dismiss();
                                Toast.makeText(SellerRegistrationActivity.this, "Seller Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(SellerRegistrationActivity.this,SellerHomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                                
                            }
                        });

                    }

                }
            });




        }

    }
}