package com.abhishek.annadatha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SellerRegistrationActivity extends AppCompatActivity {
    EditText txtPhoneNumber,txtAddress,txtPassword,txtName;
    Button btnLogin,btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);
        txtPhoneNumber=findViewById(R.id.etsellerregPhone);
        txtAddress=findViewById(R.id.etsellerregAddress);
        txtName=findViewById(R.id.etsellerregName);
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

            }
        });
    }
}