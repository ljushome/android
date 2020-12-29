package com.ljus.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedpreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        if(sharedpreferences.getBoolean("setup",false)){
            Intent intent = new Intent(SplashActivity.this,DeviceControl.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Intent deviceControl = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(deviceControl);
        }
    }
}