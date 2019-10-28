package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        Button btn_login = findViewById(R.id.btn_login);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        final EditText txt_email_phone = findViewById(R.id.txt_email);
        final EditText txt_password = findViewById(R.id.txt_password);

        txt_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == txt_password.getId())
                {
                    txt_password.setCursorVisible(true);
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
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
                        String URL = "https://maaz.serveo.net/login/";
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("email_or_phone", email_phone);
                        jsonBody.put("password", password);
                        final String requestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("VOLLEY", response.toString());
                                try {
                                    JSONObject json = new JSONObject(response);
                                    if (json.getString("status").equals("200")) {

                                        String token = json.getString("token");

                                        Intent myIntent = new Intent(MainActivity.this, MapActivity.class);//Optional parameters
                                        myIntent.putExtra("Token", token);
                                        finish();
                                        MainActivity.this.startActivity(myIntent);

//                                        Toast.makeText(MainActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();

                                    }
                                    else if (json.getString("status").equals("400")||json.getString("status").equals("404")) {
                                        Toast.makeText(MainActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                        if(json.getString("message").equals("Verify your account")){
                                            Intent myIntent = new Intent(MainActivity.this, VerifyActivity.class);//Optional parameters
                                            myIntent.putExtra("Token", json.getString("token"));
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



        });

        TextView txt_signup = findViewById(R.id.txt_signup);
        txt_signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(MainActivity.this, SignupActivity.class);//Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });
    }
}
