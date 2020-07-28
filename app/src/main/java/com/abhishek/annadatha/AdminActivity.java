package com.abhishek.annadatha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {
    ImageView iveggs,ivfruits,ivgreens,ivmeat,ivmilk,ivrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show();
        iveggs=findViewById(R.id.ivEggs);
        ivfruits=findViewById(R.id.ivFruits);
        ivgreens=findViewById(R.id.ivGreens);
        ivmeat=findViewById(R.id.ivmeat);
        ivmilk=findViewById(R.id.ivmilk);
        ivrice=findViewById(R.id.ivRice);
        iveggs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminActivity.this,AddProductActivity.class);
                i.putExtra("category","eggs");
                startActivity(i);

            }
        });
        ivfruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminActivity.this,AddProductActivity.class);
                i.putExtra("category","fruits");
                startActivity(i);

            }
        });
        ivgreens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminActivity.this,AddProductActivity.class);
                i.putExtra("category","greens");
                startActivity(i);

            }
        });
        ivmeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminActivity.this,AddProductActivity.class);
                i.putExtra("category","meat");
                startActivity(i);

            }
        });
        ivmilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminActivity.this,AddProductActivity.class);
                i.putExtra("category","milk");
                startActivity(i);

            }
        });
        ivrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminActivity.this,AddProductActivity.class);
                i.putExtra("category","rice");
                startActivity(i);

            }
        });

    }
}
