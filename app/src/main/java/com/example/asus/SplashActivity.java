package com.example.asus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

//создаем класс для вызова Splash screen
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
