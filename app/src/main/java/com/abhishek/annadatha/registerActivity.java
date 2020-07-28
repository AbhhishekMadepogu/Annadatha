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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {
    Button btnRegister,btnLogin;
    EditText txtName,txtPassword,txtPhoneNumber;
   //TextView tvadmin;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister=findViewById(R.id.btnRegister);
        btnLogin=findViewById(R.id.btnLoginR);
        txtPhoneNumber=findViewById(R.id.txtPhoner);
        txtPassword=findViewById(R.id.txtPasswordr);
        txtName=findViewById(R.id.txtNamer);
        loadingbar=new ProgressDialog(this);
       // tvadmin=findViewById(R.id.tvAdmin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(registerActivity.this,loginActivity.class);
                startActivity(i);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();

            }
        });
    }
    private void createAccount(){
        String name=txtName.getText().toString();
        String password=txtPassword.getText().toString();
        String phonenumber=txtPhoneNumber.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter Your Name...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Password...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phonenumber)){
            Toast.makeText(this, "Please Enter Phone Number...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingbar.setTitle("Creating Account");
            loadingbar.setMessage("Please Wait while we are checking your credentials...");
             loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            validatePhoneNumber(name,phonenumber,password);

        }

    }
    private void validatePhoneNumber(final String name, final String phonenumber, final String password) {
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phonenumber).exists())){
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phonenumber);
                    userdataMap.put("name",name);
                    userdataMap.put("password",password);
                    rootRef.child("Users").child(phonenumber).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(registerActivity.this, "Your Account Has been created", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent i =new Intent(registerActivity.this,loginActivity.class);
                                startActivity(i);
                            }
                            else{
                                loadingbar.dismiss();
                                Toast.makeText(registerActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(registerActivity.this, "This"+ phonenumber + "Already Exists ", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(registerActivity.this, "Please try Again with a new Phone Number", Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(registerActivity.this,loginActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
