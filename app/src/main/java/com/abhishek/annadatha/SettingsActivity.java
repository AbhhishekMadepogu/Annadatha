package com.abhishek.annadatha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.annadatha.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private CircleImageView profileimage;
    EditText txtphonenumber,txtaddress,txtname;
    TextView tvclose,tvsave,tvchange;
    private Uri imageuri;
    private String url="",checker="";
    private StorageTask storageTask;
    private StorageReference profilepictureref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        profilepictureref= FirebaseStorage.getInstance().getReference().child("ProfilePictures");
        profileimage=findViewById(R.id.ivsettingsprofile_image);
        txtphonenumber=findViewById(R.id.settings_phone);
        txtaddress=findViewById(R.id.settings_Address);
        txtname=findViewById(R.id.settings_full_Name);
        tvchange=findViewById(R.id.txtprofileimagechange);
        tvclose=findViewById(R.id.txtclose);
        tvsave=findViewById(R.id.txtupdate);
        userInfoDisplay(profileimage,txtphonenumber,txtaddress,txtname);
        tvclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(checker.equals("clicked")){
                     userInfoSaved();

                 }
                 else {
                     Updateuser();

                 }
            }
        });
        tvchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";
                CropImage.activity(imageuri).setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data != null){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
             imageuri=result.getUri();
             profileimage.setImageURI(imageuri);
        }
        else {
            Toast.makeText(this, "Error, Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(txtname.getText().toString())){
            Toast.makeText(this, "Name can not be Empty", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(txtaddress.getText().toString())){
            Toast.makeText(this, "Address can not be Empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(txtphonenumber.getText().toString())){
            Toast.makeText(this, "Phone number can not be Empty", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked")){
            uploadProfileImage();
        }

    }
    private void Updateuser(){
        DatabaseReference dbfref=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object>usermap=new HashMap<>();
        usermap.put("name",txtname.getText().toString());
        usermap.put("address",txtaddress.getText().toString());
        usermap.put("phoneOrder",txtphonenumber.getText().toString());
        dbfref.child(Prevalent.CurrentonlineUser.getPhone()).updateChildren(usermap);
        startActivity(new Intent(SettingsActivity.this,MainActivity.class));
        Toast.makeText(SettingsActivity.this, "User Info Updated", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void uploadProfileImage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
         progressDialog.setTitle("Uploading Your Profile Image");
         progressDialog.setMessage("Please Wait while we are Uploading your Profile Image");
         progressDialog.setCanceledOnTouchOutside(false);
         progressDialog.show();
         if(imageuri!=null){
             final StorageReference storageReference=profilepictureref.child(Prevalent.CurrentonlineUser.getPhone() + ".jpg" );
             storageTask=storageReference.putFile(imageuri);
             storageTask.continueWithTask(new Continuation() {
                 @Override
                 public Object then(@NonNull Task task) throws Exception {
                     if(!task.isSuccessful()){
                         throw task.getException();
                     }
                     return storageReference.getDownloadUrl();
                 }
             }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                 @Override
                 public void onComplete(@NonNull Task<Uri> task) {
                     if(task.isSuccessful()){
                         Uri download=task.getResult();
                         url=download.toString();
                         DatabaseReference dbfref=FirebaseDatabase.getInstance().getReference().child("Users");
                         HashMap<String,Object>usermap=new HashMap<>();
                         usermap.put("name",txtname.getText().toString());
                         usermap.put("address",txtaddress.getText().toString());
                         usermap.put("phoneOrder",txtphonenumber.getText().toString());
                         usermap.put("image",url);
                         dbfref.child(Prevalent.CurrentonlineUser.getPhone()).updateChildren(usermap);
                         progressDialog.dismiss();
                         startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                         Toast.makeText(SettingsActivity.this, "User Info Updated", Toast.LENGTH_SHORT).show();
                         finish();
                     }
                     else{
                         progressDialog.dismiss();
                         Toast.makeText(SettingsActivity.this, "An Error Occurred Please Try Again", Toast.LENGTH_SHORT).show();
                     }

                 }
             });
         }
         else {
             Toast.makeText(this, "Image Not Selected", Toast.LENGTH_SHORT).show();
         }
    }


    private void userInfoDisplay(final CircleImageView profileimage, final EditText txtphonenumber, EditText address, EditText name) {
        DatabaseReference Userref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentonlineUser.getPhone());
        Userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists()){
                        String image=dataSnapshot.child("image").getValue().toString();
                        String name=dataSnapshot.child("name").getValue().toString();
                        String address=dataSnapshot.child("address").getValue().toString();
                        String phone=dataSnapshot.child("phone").getValue().toString();
                        Picasso.get().load(image).into(profileimage);
                        txtname.setText(name);
                        txtphonenumber.setText(phone);
                        txtaddress.setText(address);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
