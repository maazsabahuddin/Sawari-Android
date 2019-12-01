package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    //Shared preferences code
    public static final String AppPreferences = "AppPreferences";
    public static final String tokenKey = "Token";
    SharedPreferences sharedPreferences;
    //

    private RequestQueue requestQueue;
    private EditText txt_email_phone;
    private EditText txt_password;
    private FrameLayout spinner_frame;
    private ProgressBar spinner;

    public static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyB_jc6BvRZJ-hY0aQS9PDhr3ARFFxIVjcw";

    public static String baseurl= "https://cc-5uof.localhost.run";

    private int backpress = 0;
    @Override
    public void onBackPressed(){
        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

        if (backpress>1) {
            this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        final Button btn_login = findViewById(R.id.btn_login);
        requestQueue = Volley.newRequestQueue(this);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        txt_email_phone = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        spinner_frame = findViewById(R.id.spinner_frame);
        spinner_frame.setVisibility(View.GONE);


        txt_password.setOnEditorActionListener(new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    btn_login.performClick();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(btn_login.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
            });


        //Shared Preferences
        sharedPreferences = getSharedPreferences(AppPreferences, Context.MODE_PRIVATE );


        if(!sharedPreferences.getString("Token",  "").isEmpty()){
            Intent intent = new Intent(MainActivity.this,NavActivity.class );
            finish();
            MainActivity.this.startActivity(intent);
        }

        txt_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == txt_password.getId())
                {
                    txt_password.setCursorVisible(true);
                }
            }
        });



        btn_login.setOnClickListener(btnLoginListener);


        TextView txt_signup = findViewById(R.id.txt_signup);
        txt_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(MainActivity.this, SignupActivity.class);//Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });
    }


    public View.OnClickListener btnLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            final String email_phone = txt_email_phone.getText().toString();
            final String password = txt_password.getText().toString();

            txt_password.setCursorVisible(false);

            if (email_phone.equals("") || password.equals("")){
                Toast.makeText(MainActivity.this, "Email or Password field empty", Toast.LENGTH_LONG).show();
            }
            else{
                try {
                    String URL = baseurl+"/login/";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("email_or_phone", email_phone);
                    jsonBody.put("password", password);
                    final String requestBody = jsonBody.toString();
                    spinner.setVisibility(View.VISIBLE);
                    spinner_frame.setVisibility(View.VISIBLE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            spinner.setVisibility(View.GONE);
                            spinner_frame.setVisibility(View.GONE);

                            Log.i("VOLLEY", response.toString());
                            try {
                                JSONObject json = new JSONObject(response);
                                if (json.getString("status").equals("200")) {

                                    String token = json.getString("token");
                                    //Shared Preferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.remove("Token");
                                    editor.putString("Token", token);
                                    editor.apply();
                                    Intent myIntent = new Intent(MainActivity.this, NavActivity.class);//Optional parameters
                                    finish();
                                    MainActivity.this.startActivity(myIntent);

//

                                }
                                else if (json.getString("status").equals("400")||json.getString("status").equals("404")) {
                                    Toast.makeText(MainActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                    if(json.getString("message").equals("User not authenticated. Please verify first.")){
                                        String token = json.getString("token");
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove("Token");
                                        editor.putString("Token", token);
                                        editor.apply();
                                        Intent myIntent = new Intent(MainActivity.this, VerifyActivity.class);//Optional parameters
                                        MainActivity.this.startActivity(myIntent);
                                    }
                                }
                            } catch (JSONException e) {
                                Log.e("VOLLEY", e.toString());

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            spinner.setVisibility(View.GONE);
                            spinner_frame.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                            Log.e("VOLLEY", error.toString());
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("email_or_phone",email_phone);
                            params.put("password",password);
//                                params.put(KEY_EMAIL, email);
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


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }



    };
}
