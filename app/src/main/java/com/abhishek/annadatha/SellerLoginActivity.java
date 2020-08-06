package com.abhishek.annadatha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SellerLoginActivity extends AppCompatActivity {
    EditText etPhone,etPassword;
    Button btnLogin,btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        etPhone=findViewById(R.id.etSellerloginPhone);
        etPassword=findViewById(R.id.etSellerloginPassword);
        btnLogin=findViewById(R.id.btnsellerLoginLogin);
        btnRegister=findViewById(R.id.btnsellerLoginRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SellerLoginActivity.this,SellerRegistrationActivity.class);
                startActivity(i);
            }
        });
    }
}