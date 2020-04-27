package com.sohaibaijaz.sawaari.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sohaibaijaz.sawaari.R;

public class LoginView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        getSupportActionBar().hide();

        EditText phone_number_tv = findViewById(R.id.phone_number_tv);
        phone_number_tv.setCursorVisible(false);
        phone_number_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginView.this, LoginPhone.class);
                LoginView.this.startActivity(intent);
            }
        });
    }
}
