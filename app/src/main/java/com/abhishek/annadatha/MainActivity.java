package com.abhishek.annadatha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.annadatha.Model.User;
import com.abhishek.annadatha.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Button btnjoin,btnlogin;
    ProgressDialog loadingbar;
    TextView tvSeller;
    //private String parentDB="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnjoin=findViewById(R.id.join_nowbtn);
        btnlogin=findViewById(R.id.join_now);
        tvSeller=findViewById(R.id.tvSeller);
        tvSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,SellerRegistrationActivity.class);
                startActivity(i);
            }
        });
        Paper.init(this);
        //Paper.book().write(Prevalent.phone,"");
        //Paper.book().write(Prevalent.password,"");
        //Paper.book().write(Prevalent.userName,"");
        loadingbar=new ProgressDialog(this);
        btnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this,loginActivity.class);
                startActivity(i);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this,registerActivity.class);
                startActivity(i);
            }
        });

        String phonel = Paper.book().read(Prevalent.phone);

        String passwordl = Paper.book().read(Prevalent.password);
        Toast.makeText(this, ""+Paper.book().read(Prevalent.password), Toast.LENGTH_SHORT).show();

        if(Paper.book().read(Prevalent.phone) != "" && Paper.book().read(Prevalent.password)!= ""){
                Toast.makeText(this, "HI", Toast.LENGTH_SHORT).show();
                String phone=Paper.book().read(Prevalent.phone);
                String password=Paper.book().read(Prevalent.password);
                AllowAccess(phone,password);

                //AllowAccess(Paper.book().read(Prevalent.phone),Paper.book().read(Prevalent.password));
                loadingbar.setTitle("Already Logged in...");
                loadingbar.setMessage("Please Wait while we are logging you in...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent i=new Intent(MainActivity.this,SellerHomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    private void AllowAccess(Object read, Object read1) {
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
    }

    private void AllowAccess(final String phone1, final String password1) {

        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phone1).exists()){
                    User userdata=dataSnapshot.child("Users").child(phone1).getValue(User.class);
                    if(userdata.getPhone().equals(phone1)){
                        if(userdata.getPassword().equals(password1)){
                            //Toast.makeText(MainActivity.this, "Login Successful...", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            Intent i = new Intent(MainActivity.this,HomeActivity.class);
                            Prevalent.CurrentonlineUser=userdata;
                            startActivity(i);

                        }
                    }

                }else {
                    Toast.makeText(MainActivity.this, "Account with this"+ phone1 +"does not exist", Toast.LENGTH_SHORT).show();
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
