package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SecurityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_security);
    }
}
