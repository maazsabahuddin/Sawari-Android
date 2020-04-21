package com.sohaibaijaz.sawaari;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sohaibaijaz.sawaari.model.User;

import java.util.Objects;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    private static int SPLASH_TIMEOUT = 2000;
    SharedPreferences sharedPreferences;
    // Animation
    Animation animFadein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();

        sharedPreferences = getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
        String isToken = sharedPreferences.getString("Token",  "");

        final TextView splashText = findViewById(R.id.splashText);
//        splashText.setVisibility(View.GONE);

        // load the animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        // set animation listener
        animFadein.setAnimationListener(this);

        if(!isToken.equals("")) {
            UserDetails.getUserDetails(SplashActivity.this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    splashText.startAnimation(animFadein);
                    splashText.setVisibility(View.VISIBLE);
                    Intent home = new Intent(SplashActivity.this, NavActivity.class);
                    startActivity(home);
                    finish();
                }
            }, SPLASH_TIMEOUT);
        }
        else{
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
            finish();
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // Animation is repeating
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // Animation started
    }

}
