package com.sohaibaijaz.sawaari;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sohaibaijaz.sawaari.model.User;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home = new Intent(SplashActivity.this, NavActivity.class);
               // Toast.makeText(SplashActivity.this, user.getFirstName(), Toast.LENGTH_SHORT).show();
                startActivity(home);
                finish();
            }
        }, SPLASH_TIMEOUT);

    }
}
