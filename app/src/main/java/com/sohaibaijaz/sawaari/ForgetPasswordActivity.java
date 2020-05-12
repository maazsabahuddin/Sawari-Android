package com.sohaibaijaz.sawaari;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.Fragments.CallBack;
import com.sohaibaijaz.sawaari.Login.LoginAPI;
import com.sohaibaijaz.sawaari.Login.LoginCallBack;
import com.sohaibaijaz.sawaari.Login.LoginView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText email_or_phone;
    private ImageView sendEmailButton;
    private TextView resend_email;
    private TextView error_message_forgot_password, BackForgotPassword;
    private ProgressBar forgetPasswordSpinner;

//    private FrameLayout spinner_frame;
//    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide();

        forgetPasswordSpinner = findViewById(R.id.forgetPasswordSpinner);
        forgetPasswordSpinner.setVisibility(View.GONE);

        error_message_forgot_password = findViewById(R.id.error_message_forgot_password);
        error_message_forgot_password.setVisibility(View.GONE);

        BackForgotPassword = findViewById(R.id.BackForgotPassword);

        resend_email = findViewById(R.id.resend_email);
        resend_email.setAlpha(0.5f);
        resend_email.setEnabled(false);

        email_or_phone = findViewById(R.id.email_or_phone);
        email_or_phone.requestFocus();

        sendEmailButton = findViewById(R.id.sendEmailButton);
        sendEmailButton.setAlpha(0.5f);

        email_or_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == email_or_phone.getId())
                {
                    error_message_forgot_password.setVisibility(View.GONE);
                    email_or_phone.setCursorVisible(true);
                }
            }
        });

        email_or_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                sendEmailButton.setEnabled(false);
                sendEmailButton.setAlpha(0.5f);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                sendEmailButton.setEnabled(true);
                sendEmailButton.setAlpha(1.0f);

                if((email_or_phone.getText().toString()).equals(""))
                {
                    sendEmailButton.setEnabled(false);
                    sendEmailButton.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        BackForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginView.class);
                ForgetPasswordActivity.this.startActivity(intent);
            }
        });

        resend_email.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                error_message_forgot_password.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                error_message_forgot_password.setText("Reset your password from the link resent to your email.");
            }
        });

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email_phone = email_or_phone.getText().toString();

                if (email_phone.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter email or phone number", Toast.LENGTH_SHORT).show();
                }
                else{
                    forgetPasswordSpinner.setVisibility(View.VISIBLE);
                    email_or_phone.setCursorVisible(false);
                    LoginAPI.forgotPassword(ForgetPasswordActivity.this, email_phone, new LoginCallBack() {
                        @Override
                        public void onSuccess(String status_code, String message, String token) {
                            forgetPasswordSpinner.setVisibility(View.GONE);
                            error_message_forgot_password.setVisibility(View.VISIBLE);
                            error_message_forgot_password.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                            error_message_forgot_password.setText(message);
                        }

                        @Override
                        public void onFailure(String status_code, String message) {
                            forgetPasswordSpinner.setVisibility(View.GONE);
                            resend_email.setAlpha(1.0f);
                            resend_email.setEnabled(true);
                            error_message_forgot_password.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                            error_message_forgot_password.setVisibility(View.VISIBLE);
                            error_message_forgot_password.setText(message);
                        }
                    });
                }

            }
        });
    }
}

