package com.abhishek.annadatha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ApprooveProductsActivity extends AppCompatActivity {
    Button btnApproove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approove_products);
        btnApproove=findViewById(R.id.btnApproove);
        btnApproove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ApprooveProductsActivity.this,CheckProductActivity.class);
                startActivity(i);
            }
        });
    }
}