package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TwoStepVerificationActivity extends AppCompatActivity {

    Button button_setupnow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_2step_verification);
        button_setupnow=findViewById(R.id.setupnow);

        button_setupnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TwoStepVerificationActivity.this, VerifyPassword3_Activity.class);
                TwoStepVerificationActivity.this.startActivity(i);
            }
        });
    }
}
