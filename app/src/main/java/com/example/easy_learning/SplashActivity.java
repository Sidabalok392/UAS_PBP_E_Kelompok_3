package com.example.easy_learning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private Button btnstart_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        btnstart_splash=findViewById(R.id.btnstart_splash);
        getSupportActionBar().setTitle("Welcome");
        btnstart_splash.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(
                SplashActivity.this,RegisterActivity.class));
                finish();
            }
        });
    }
}