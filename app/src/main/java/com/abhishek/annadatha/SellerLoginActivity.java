package com.abhishek.annadatha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {
    EditText etPhone,etPassword;
    Button btnLogin,btnRegister;
    FirebaseAuth mauth;
    ProgressDialog loadingbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        etPhone=findViewById(R.id.etSellerloginPhone);
        loadingbar=new ProgressDialog(this);
        mauth=FirebaseAuth.getInstance();
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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellerLogin();
            }
        });
    }

    private void sellerLogin() {
        String email=etPhone.getText().toString();
        String password=etPassword.getText().toString();

        if((email!="")&&(password!="")){
            loadingbar.setTitle("Loging into your Account");
            loadingbar.setMessage("Please Wait while we are checking your credentials...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        loadingbar.dismiss();
                        Intent i=new Intent(SellerLoginActivity.this,SellerHomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }

                }
            });
        }
        else{
            Toast.makeText(this, "Please enter Your Details", Toast.LENGTH_SHORT).show();
        }
    }
}