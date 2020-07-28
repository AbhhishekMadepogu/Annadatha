package com.abhishek.annadatha;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceStatee) {
        super.onCreate(savedInstanceStatee);
        startActivity(new Intent(this,MainActivity.class));
    }

}
