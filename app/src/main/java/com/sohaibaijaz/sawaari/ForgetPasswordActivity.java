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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText email_or_phone;
    private ImageView sendEmailButton;
    private TextView resend_email;
    private TextView error_message_forgot_password, BackForgotPassword;

//    private FrameLayout spinner_frame;
//    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide();

        error_message_forgot_password = findViewById(R.id.error_message_forgot_password);
        error_message_forgot_password.setVisibility(View.GONE);

        BackForgotPassword = findViewById(R.id.BackForgotPassword);

        resend_email = findViewById(R.id.resend_email);
        resend_email.setAlpha(0.5f);
        resend_email.setEnabled(false);

        email_or_phone = findViewById(R.id.email_or_phone);
        email_or_phone.requestFocus();

        sendEmailButton = findViewById(R.id.sendEmailButton);

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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
                onBackPressed();
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
                    email_or_phone.setCursorVisible(false);
                    try {
                        String URL = MainActivity.baseurl + "/forgot/password/";
                        JSONObject jsonBody = new JSONObject();
//                        jsonBody.put("email_or_phone", email_phone);
//                        final String requestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.i("VOLLEY", response);
                                try {
                                    JSONObject json = new JSONObject(response);
                                    if (json.getString("status").equals("200")) {

                                        error_message_forgot_password.setVisibility(View.VISIBLE);
                                        error_message_forgot_password.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                                        error_message_forgot_password.setText(json.getString("message"));

                                    } else if (json.getString("status").equals("400")) {
                                        resend_email.setAlpha(1.0f);
                                        resend_email.setEnabled(true);
                                        error_message_forgot_password.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                                        error_message_forgot_password.setVisibility(View.VISIBLE);
                                        error_message_forgot_password.setText(json.getString("message"));
//                                        Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Log.e("VOLLEY", e.toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VOLLEY", error.toString());
                                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("email_or_phone", email_phone);
                                return params;
                            }
                        };

                        stringRequest.setRetryPolicy(new RetryPolicy() {
                            @Override
                            public int getCurrentTimeout() {
                                return 50000;
                            }

                            @Override
                            public int getCurrentRetryCount() {
                                return 50000;
                            }

                            @Override
                            public void retry(VolleyError error) throws VolleyError {

                            }
                        });

                        requestQueue.add(stringRequest);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    }

