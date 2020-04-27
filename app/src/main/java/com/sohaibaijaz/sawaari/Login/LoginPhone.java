package com.sohaibaijaz.sawaari.Login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sohaibaijaz.sawaari.R;

public class LoginPhone extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);

        EditText phone_number_login_tv = findViewById(R.id.phone_number_login_tv);
        TextView error_message_phone_login = findViewById(R.id.error_message_phone_login);
        ImageView verifyPhoneLoginButton = findViewById(R.id.verifyPhoneLoginButton);
    }
}
