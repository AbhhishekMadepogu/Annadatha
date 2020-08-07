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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {
    String productName,description,price,quantity,saveCurrentDate,saveCurrentTime,randomKey,downloadImageUrl;
    ImageView ivProduct;
    EditText etProductName,etProductDescription,etProductQuantity,etProductPrice;
    Button btnAddProduct;
    private String category;
    final static int gallerypick=1;
    private Uri imageUri;
    private StorageReference productimageReference;
    private DatabaseReference productsref,sellerref;
    ProgressDialog loadingbar;
    private String sid,sname,saddress,semail,sphone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ivProduct=findViewById(R.id.ivProductImage);
        etProductName=findViewById(R.id.etProductName);
        etProductDescription=findViewById(R.id.etProducDescription);
        etProductPrice=findViewById(R.id.etProductPrice);
        etProductQuantity=findViewById(R.id.etProductQuantity);
        btnAddProduct=findViewById(R.id.btnAddProduct);
        category=getIntent().getExtras().get("category").toString();
        loadingbar=new ProgressDialog(this);
        Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
        productimageReference= FirebaseStorage.getInstance().getReference().child("product images");
        productsref= FirebaseDatabase.getInstance().getReference().child("Products");
        sellerref=FirebaseDatabase.getInstance().getReference().child("Sellers");


        sellerref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    sname=dataSnapshot.child("name").getValue().toString();
                    saddress=dataSnapshot.child("address").getValue().toString();
                    sphone=dataSnapshot.child("phone").getValue().toString();
                    semail=dataSnapshot.child("email").getValue().toString();
                    sid=dataSnapshot.child("sid").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();

            }
        });
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateProductData();

            }
        });
    }

    private void validateProductData() {
        loadingbar.setTitle("Adding New Product");
        loadingbar.setMessage("Please Wait While We Are Adding Your Product.");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        productName=etProductName.getText().toString();
        description=etProductDescription.getText().toString();
        price=etProductPrice.getText().toString();
        quantity=etProductQuantity.getText().toString();
        if(imageUri==null){
            Toast.makeText(this, "Product Image is Mandatory to add a Product", Toast.LENGTH_SHORT).show();
        }
         else if(TextUtils.isEmpty(productName))
            Toast.makeText(this, "Product Name Should not be Empty", Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(description))
            Toast.makeText(this, " Product Description Should not be Empty", Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(price))
            Toast.makeText(this, "Product Price Should not be Empty", Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(quantity))
            Toast.makeText(this, "Product Quantity Should not be Empty", Toast.LENGTH_SHORT).show();
        else{
            storeProductInfo();
        }

    }

    private void storeProductInfo() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MM,dd,yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());
        randomKey=saveCurrentDate+saveCurrentTime;
        final StorageReference filepath=productimageReference.child(imageUri.getLastPathSegment()+randomKey+".jpg");
        final UploadTask uploadTask=filepath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String m=e.toString();
                Toast.makeText(AddProductActivity.this, "Error: " +m+"" , Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddProductActivity.this, "Image Uploaded successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                       if(!task.isSuccessful()){
                           throw task.getException();

                       }
                       downloadImageUrl=filepath.getDownloadUrl().toString();
                       return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        downloadImageUrl=task.getResult().toString();
                        Toast.makeText(AddProductActivity.this, "Product image saved to database successfully", Toast.LENGTH_SHORT).show();
                        saveProductInfotoDB();

                    }
                });
            }
        });





    }

    private void saveProductInfotoDB() {
        HashMap<String,Object> productmap=new HashMap<>();
        productmap.put("pid",randomKey);
        productmap.put("pname",productName);

        productmap.put("date",saveCurrentDate);
        productmap.put("time",saveCurrentTime);
        productmap.put("description",description);
        productmap.put("price",price);
        productmap.put("quantity",quantity);
        productmap.put("image",downloadImageUrl);
        productmap.put("category",category);
        productmap.put("sellerName",sname);
        productmap.put("sid",sid);
        productmap.put("sellerAddress",saddress);
        productmap.put("sellerEmail",semail);
        productmap.put("sellerPhone",sphone);
        productmap.put("productSatate","Not Approved");
        productsref.child(randomKey).updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    loadingbar.dismiss();
                    Toast.makeText(AddProductActivity.this, "Product Added Successfully...", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i=new Intent(AddProductActivity.this,SellerHomeActivity.class);
                    //startActivity(i);
                    loadingbar.dismiss();
                    String m=task.getException().toString();
                    Toast.makeText(AddProductActivity.this,"Error: "+m+"", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void openGallery() {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,gallerypick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==gallerypick && resultCode==RESULT_OK && data != null){
            imageUri=data.getData();
            ivProduct.setImageURI(imageUri);



        }
    }
    private void sellerInfo(){

    }
}
