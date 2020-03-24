package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SecurityActivity extends AppCompatActivity {

    Button button_security;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_security);

        button_security=findViewById(R.id.twostepverification);

        button_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SecurityActivity.this, TwoStepVerificationActivity.class);
                SecurityActivity.this.startActivity(i);
               // Toast.makeText(SecurityActivity.this,"hello",Toast.LENGTH_LONG).show();
            }
        });
    }
}
