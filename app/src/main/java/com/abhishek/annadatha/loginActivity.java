package com.abhishek.annadatha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.annadatha.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.abhishek.annadatha.Model.User;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {
    Button btnLogin,btnRegister;
     EditText txtPhone,txtPassword;
     ProgressDialog loadingbar;
     private String parentDB="Users";
     SharedPreferences sharedPreferences;
     TextView tvAdmin,tvnotAdmin;
     CheckBox checkRm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin=findViewById(R.id.btnLoginl);
        btnRegister=findViewById(R.id.join_nowbtnl);
        txtPassword=findViewById(R.id.txtPasswordl);
        txtPhone=findViewById(R.id.txtPhonel);
        checkRm=findViewById(R.id.chbrm);
        tvAdmin=findViewById(R.id.tvAdmin);
        tvnotAdmin=findViewById(R.id.tvnotAdmin);
        Paper.init(this);
        loadingbar=new ProgressDialog(this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(loginActivity.this,registerActivity.class);
                startActivity(i);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        tvAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setText("Login as Admin");
                tvAdmin.setVisibility(view.INVISIBLE);
                tvnotAdmin.setVisibility(view.VISIBLE);
                parentDB="Admins";

            }
        });
        tvnotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setText("Login");
                tvAdmin.setVisibility(view.VISIBLE);
                tvnotAdmin.setVisibility(view.INVISIBLE);
                parentDB="Users";

            }
        });
    }


    private void login() {
        String phone=txtPhone.getText().toString();
        String password=txtPassword.getText().toString();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Enter Your Phone Number...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Password...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingbar.setTitle("Loging into your Account");
            loadingbar.setMessage("Please Wait while we are checking your credentials...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
           accessAccount(phone,password);

        }

    }

    private void accessAccount(final String phone1, final String password1) {
        if(checkRm.isChecked()){


            Paper.book().write(Prevalent.phone,phone1);
            Paper.book().write(Prevalent.password,password1);
            //Toast.makeText(this, ""+Paper.book().read(Prevalent.password)+"", Toast.LENGTH_SHORT).show();
        }
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDB).child(phone1).exists()){
                    User userdata=dataSnapshot.child(parentDB).child(phone1).getValue(User.class);
                    if(userdata.getPhone().equals(phone1)){
                        if(userdata.getPassword().equals(password1)){

if(parentDB.equals("Admins")){
    Toast.makeText(loginActivity.this, "Login Successful as Admin...", Toast.LENGTH_SHORT).show();
    loadingbar.dismiss();
    Intent i = new Intent(loginActivity.this,AdminActivity.class);
    startActivity(i);
}
else if(parentDB.equals("Users")){
    Toast.makeText(loginActivity.this, "Login Successful...", Toast.LENGTH_SHORT).show();
    loadingbar.dismiss();
    Intent i = new Intent(loginActivity.this,HomeActivity.class);

    Prevalent.CurrentonlineUser=userdata;
    String l=Prevalent.CurrentonlineUser.getName();
    Paper.book().write(Prevalent.userName,l);
    startActivity(i);
}

                        }
                    }

                }
                else {
                    Toast.makeText(loginActivity.this, "Account with this"+ phone1 +"does not exist", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    //Toast.makeText(loginActivity.this, "You should create a new Account", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
