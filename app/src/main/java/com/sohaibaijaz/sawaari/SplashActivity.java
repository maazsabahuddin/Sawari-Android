package com.sohaibaijaz.sawaari;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView i = (ImageView)findViewById(R.id.splash_image);
        i.setImageResource(R.mipmap.ic_sawaari);
        i.isShown();
        Objects.requireNonNull(getSupportActionBar()).hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home = new Intent(SplashActivity.this, NavActivity.class);
                startActivity(home);
                finish();
            }
        }, SPLASH_TIMEOUT);

    }
}
