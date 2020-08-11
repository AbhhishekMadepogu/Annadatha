package com.abhishek.annadatha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CustomerCategoryActivity extends AppCompatActivity {
    ImageView ivEggs,ivMeat,ivFruits,ivGreens,ivRice,ivMilk;
    String categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_category);
        ivEggs=findViewById(R.id.ivEggsc);
        ivFruits=findViewById(R.id.ivFruitsc);
        ivGreens=findViewById(R.id.ivGreensc);
        ivMeat=findViewById(R.id.ivmeatc);
        ivMilk=findViewById(R.id.ivmilkc);
        ivRice=findViewById(R.id.ivRicec);
        ivEggs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categories="eggs";
                intenst(categories);


            }
        });
        ivFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categories="fruits";
                intenst(categories);

            }
        });
        ivGreens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categories="greens";
                intenst(categories);

            }
        });
        ivMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categories="meat";
                intenst(categories);

            }
        });
        ivMilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categories="milk";
                intenst(categories);

            }
        });
        ivRice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categories="rice";
                intenst(categories);

            }
        });

    }

    private void intenst(String categories) {
        String forword=categories;
        Intent i=new Intent(CustomerCategoryActivity.this,CustomerCategoryProductsActivity.class);
        i.putExtra("category",forword);
        startActivity(i);
    }


}