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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.sohaibaijaz.sawaari.ForgetPasswordActivity;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.RealmHelper;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;
import com.sohaibaijaz.sawaari.SignupActivity;
import com.sohaibaijaz.sawaari.UserDetails;
import com.sohaibaijaz.sawaari.VerifyActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;

import static com.sohaibaijaz.sawaari.Fragments.HomeFragment.isNetworkAvailable;

public class LoginPin extends AppCompatActivity {

    public static final String AppPreferences = "AppPreferences";
    public static final String app = "Customer";
    SharedPreferences sharedPreferences;

    private String token;
    private RequestQueue requestQueue;
    private EditText txt_password;
    private TextView tv_forget_password;
    private TextView error_message_pin_login;
    private ImageView verifyPinLoginButton;
    private TextView BackLoginPin;
    Realm realm;
    RealmHelper helper;
    public static String baseurl= "http://ec2-18-216-187-158.us-east-2.compute.amazonaws.com";

    private int backpress = 0;
    //@Override
//    public void onBackPressed(){
//        backpress = (backpress + 1);
//        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
//
//        if (backpress>1) {
//          //  this.finish();
//            finishAffinity();
//        }
//    }

    private HashMap<String, String> userdetails = new HashMap<>();
    private String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_pin);

        verifyPinLoginButton = findViewById(R.id.verifyPinLoginButton);
        requestQueue = Volley.newRequestQueue(this);
        txt_password = findViewById(R.id.phone_number_login_tv);
        error_message_pin_login = findViewById(R.id.error_message_pin_login);
        tv_forget_password = findViewById(R.id.forgot_password_button);
        BackLoginPin = findViewById(R.id.BackLoginPin);

        txt_password.requestFocus();
        BackLoginPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(!isNetworkAvailable(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        error_message_pin_login.setVisibility(View.GONE);
        verifyPinLoginButton.setAlpha(0.5f);

        realm= Realm.getDefaultInstance();
        helper = new RealmHelper(realm);

        Bundle b = getIntent().getExtras();
        phone_number = b.getString("phone_number");

        txt_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == txt_password.getId())
                {
                    error_message_pin_login.setVisibility(View.GONE);
                    txt_password.setCursorVisible(true);
                }
            }
        });

        txt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                verifyPinLoginButton.setEnabled(false);
                verifyPinLoginButton.setAlpha(0.5f);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                verifyPinLoginButton.setEnabled(true);
                verifyPinLoginButton.setAlpha(1.0f);

                if((txt_password.getText().toString()).equals(""))
                {
                    verifyPinLoginButton.setEnabled(false);
                    verifyPinLoginButton.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_forget_password.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginPin.this, ForgetPasswordActivity.class);
                LoginPin.this.startActivity(i);
            }
        });

        txt_password.setOnEditorActionListener(new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    verifyPinLoginButton.performClick();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(verifyPinLoginButton.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        sharedPreferences = getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
        verifyPinLoginButton.setOnClickListener(btnLoginListener);

    }


    public View.OnClickListener btnLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(!isNetworkAvailable(getApplicationContext())){
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            else{
                final String password = txt_password.getText().toString();
                txt_password.setCursorVisible(false);

                if (password.equals("")){
                    Toast.makeText(LoginPin.this, "Password field cannot be empty", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        String URL = baseurl+"/login/";
                        JSONObject jsonBody = new JSONObject();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                Log.i("VOLLEY", response);
                                try {
                                    JSONObject json = new JSONObject(response);
                                    if (json.getString("status").equals("200")) {
                                        token = json.getString("token");
                                        if(json.getString("message").equals("User not authenticated. Please verify first.")){
                                            Toast.makeText(LoginPin.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                            Intent myIntent = new Intent(LoginPin.this, VerifyActivity.class);//Optional parameters
                                            Bundle b = new Bundle();
                                            b.putString("Token", token);
                                            b.putString("email_phone", phone_number);
                                            myIntent.putExtras(b);
                                            finish();
                                            LoginPin.this.startActivity(myIntent);
                                        }

                                        else {
                                            //Shared Preferences
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("Token", token);
                                            editor.apply();

                                            userdetails=helper.getUserDetailsDB();

                                            if(Objects.equals(userdetails.get("phonenumber"), phone_number))
                                            {
                                                UserDetails.getUserRides(LoginPin.this);
                                                Intent myIntent = new Intent(LoginPin.this, NavActivity.class);//Optional parameters
                                                finish();
                                                LoginPin.this.startActivity(myIntent);
                                            }
                                            else {
                                                helper.DeleteUserDetails(LoginPin.this);
                                                helper.DeleteUserPlaces(LoginPin.this);
                                                UserDetails.getUserDetails(LoginPin.this);
                                                UserDetails.getUserPlaces(LoginPin.this);
                                                UserDetails.getUserRides(LoginPin.this);
                                                Intent myIntent = new Intent(LoginPin.this, NavActivity.class);//Optional parameters
                                                finish();
                                                LoginPin.this.startActivity(myIntent);
                                            }
                                        }
                                    }
                                    else if (json.getString("status").equals("401") || json.getString("status").equals("400")) {
                                        error_message_pin_login.setVisibility(View.VISIBLE);
                                        txt_password.setCursorVisible(false);
                                        error_message_pin_login.setText(json.getString("message"));
                                    }
                                    else if(json.getString("status").equals("404")){
                                        Toast.makeText(LoginPin.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    Log.e("VOLLEY", e.toString());

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VOLLEY", error.toString());
                            }
                        })
                        {
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<>();
                                params.put("email_or_phone", phone_number);
                                params.put("password", password);
                                params.put("app", app);
                                return params;
                            }
                        };

                        stringRequest.setRetryPolicy(new RetryPolicy() {
                            @Override
                            public int getCurrentTimeout() {
                                return 5000;
                            }

                            @Override
                            public int getCurrentRetryCount() {
                                return 5000;
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
        }
    };
}
