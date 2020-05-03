package com.sohaibaijaz.sawaari.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.Fragments.CallBack;
import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.RealmHelper;
import com.sohaibaijaz.sawaari.Settings.ChangePhoneNumberActivity;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;
import com.sohaibaijaz.sawaari.UserDetails;
import com.sohaibaijaz.sawaari.VerifyActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;

import static com.sohaibaijaz.sawaari.Fragments.HomeFragment.isNetworkAvailable;
import static com.sohaibaijaz.sawaari.Login.LoginPin.app;
import static com.sohaibaijaz.sawaari.Login.LoginPin.baseurl;
import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class LoginPhone extends AppCompatActivity {

    private RequestQueue requestQueue;
    private EditText phone_number_login_tv;
    private ImageView verifyPhoneLoginButton;
    private TextView error_message_phone_login;
    private TextView countryCode;
    SharedPreferences sharedPreferences;
    Realm realm;
    RealmHelper helper;
    private HashMap<String, String> userdetails = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestQueue = Volley.newRequestQueue(this);

        countryCode = findViewById(R.id.countryCode);
        phone_number_login_tv = findViewById(R.id.phone_number_login_tv);
        error_message_phone_login = findViewById(R.id.error_message_phone_login);
        verifyPhoneLoginButton = findViewById(R.id.verifyPhoneLoginButton);
        final TextView BackLoginPhone = findViewById(R.id.BackLoginPhone);
        verifyPhoneLoginButton.setAlpha(0.5f);
        error_message_phone_login.setVisibility(View.GONE);

        realm= Realm.getDefaultInstance();
        helper = new RealmHelper(realm);

        phone_number_login_tv.requestFocus();
        if(!isNetworkAvailable(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        BackLoginPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        phone_number_login_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                verifyPhoneLoginButton.setEnabled(false);
                verifyPhoneLoginButton.setAlpha(0.5f);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                verifyPhoneLoginButton.setEnabled(true);
                verifyPhoneLoginButton.setAlpha(1.0f);

                if((phone_number_login_tv.getText().toString()).equals(""))
                {
                    verifyPhoneLoginButton.setEnabled(false);
                    verifyPhoneLoginButton.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone_number_login_tv.setOnEditorActionListener(new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    verifyPhoneLoginButton.performClick();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(verifyPhoneLoginButton.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        phone_number_login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == phone_number_login_tv.getId())
                {
                    error_message_phone_login.setVisibility(View.GONE);
                    phone_number_login_tv.setCursorVisible(true);
                }
            }
        });

        verifyPhoneLoginButton.setOnClickListener(btnLoginListener);

    }

    public View.OnClickListener btnLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(!isNetworkAvailable(getApplicationContext())){
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            else{
                final String phone_number = countryCode.getText().toString() + phone_number_login_tv.getText().toString();
                phone_number_login_tv.setCursorVisible(false);

                if (phone_number.equals("")){
                    error_message_phone_login.setVisibility(View.VISIBLE);
                    error_message_phone_login.setText("Please enter phonenumber");
                }
                else{
                    LoginAPI.loginUser(LoginPhone.this, phone_number, new LoginCallBack() {
                        @Override
                        public void onSuccess(String status_code, String message, String token) {
                            if(message.equals("User successfully registered.") || message.equals("User verified and login successfully.")){

                                sharedPreferences = LoginPhone.this.getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Token", token);
                                editor.apply();

                                userdetails=helper.getUserDetailsDB();
                                if(Objects.equals(userdetails.get("phonenumber"), phone_number))
                                {
                                    Intent myIntent = new Intent(LoginPhone.this, NavActivity.class);
                                    finish();
                                    LoginPhone.this.startActivity(myIntent);
                                }
                                else {
                                    helper.DeleteUserDetails(LoginPhone.this);
                                    helper.DeleteUserPlaces(LoginPhone.this);
                                    UserDetails.getUserDetails(LoginPhone.this);
                                    UserDetails.getUserPlaces(LoginPhone.this);

                                    Intent myIntent = new Intent(LoginPhone.this, NavActivity.class);
                                    finish();
                                    LoginPhone.this.startActivity(myIntent);
                                }
                            }
                            else if(message.equals("OTP has been successfully sent.")){
                                sharedPreferences = LoginPhone.this.getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Token", token);
                                editor.apply();

                                Intent intent = new Intent(LoginPhone.this, LoginVerify.class);
                                intent.putExtra("phone_number", phone_number);
                                LoginPhone.this.startActivity(intent);
                            }

                            else if(message.equals("User Exist.")){
                                Intent intent = new Intent(LoginPhone.this, LoginPin.class);
                                intent.putExtra("phone_number", phone_number);
                                LoginPhone.this.startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(String status_code, String message) {
                            if (status_code.equals("401") || status_code.equals("400")) {
                                error_message_phone_login.setVisibility(View.VISIBLE);
                                error_message_phone_login.setText(message);

                                phone_number_login_tv.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        phone_number_login_tv.setCursorVisible(true);
                                        error_message_phone_login.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                            }
                            else if(status_code.equals("404")){
                                error_message_phone_login.setVisibility(View.VISIBLE);
                                error_message_phone_login.setText(message);
                                Toast.makeText(LoginPhone.this, message, Toast.LENGTH_LONG).show();
                                SettingsFragment.forcedLogout(LoginPhone.this);
                            }
                        }
                    });
                }
            }
        }
    };

}
