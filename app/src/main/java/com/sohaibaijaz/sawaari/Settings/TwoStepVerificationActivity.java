package com.sohaibaijaz.sawaari.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sohaibaijaz.sawaari.R;

public class TwoStepVerificationActivity extends AppCompatActivity {

    Button button_setupnow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("2-step Verification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_2step_verification);
        button_setupnow=findViewById(R.id.setupnow);

        button_setupnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TwoStepVerificationActivity.this, VerifyPasswordTwoStepVerification_Activity.class);
                TwoStepVerificationActivity.this.startActivity(i);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
