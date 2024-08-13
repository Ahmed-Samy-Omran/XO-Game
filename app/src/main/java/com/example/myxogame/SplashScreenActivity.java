package com.example.myxogame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

// Duration of splash screen
        int SPLASH_SCREEN_DURATION = 3000; // 3 seconds



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity after the splash screen duration
                Intent intent = new Intent(SplashScreenActivity.this, AddPlayersActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_DURATION);
    }
}

