package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class VerifyActivity extends AppCompatActivity {

    private FrameLayout spinner_frame;
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_verify);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.AppPreferences, MODE_PRIVATE);

        final String token = sharedPreferences.getString("Token", "");
        System.out.println("VerifyActivity: "+token);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final Button btn_verify = findViewById(R.id.btn_verify);
        TextView resend_otp = findViewById(R.id.resend_otp);
        final EditText txt_otp = findViewById(R.id.txt_otp);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner_frame = findViewById(R.id.spinner_frame);
        spinner_frame.setVisibility(View.GONE);


        txt_otp.setOnEditorActionListener(new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE || i== KeyEvent.KEYCODE_ENTER){
                    btn_verify.performClick();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(btn_verify.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }

                return false;
            }
        });


        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String URL = MainActivity.baseurl+"/register/resend_otp/";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("authorization", token);
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

                                    Toast.makeText(VerifyActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                                else if (json.getString("status").equals("400")||json.getString("status").equals("404")) {
                                    Toast.makeText(VerifyActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                }
//                                    else if (json.getString("status").equals("400")||json.getString("status").equals("404")) {
//                                        Toast.makeText(VerifyActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
//                                    }
                            } catch (JSONException e) {
                                Log.e("VOLLEY", e.toString());

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            spinner.setVisibility(View.GONE);
                            spinner_frame.setVisibility(View.GONE);
                            Toast.makeText(VerifyActivity.this, "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                            Log.e("VOLLEY", error.toString());
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();

                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("authorization", token);
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
        });



        txt_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == txt_otp.getId())
                {
                    txt_otp.setCursorVisible(true);
                }
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String otp = txt_otp.getText().toString();

                txt_otp.setCursorVisible(false);

                if (!otp.equals("")){
                    try {
                        String URL = MainActivity.baseurl+"/is_verified/";
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("otp", otp);

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

                                        Toast.makeText(VerifyActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                        Intent myIntent = new Intent(VerifyActivity.this, NavActivity.class);//Optional parameters

                                        finish();
                                        VerifyActivity.this.startActivity(myIntent);
                                    }
                                    else if (json.getString("status").equals("400")||json.getString("status").equals("404")) {
                                        Toast.makeText(VerifyActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
//                                    else if (json.getString("status").equals("400")||json.getString("status").equals("404")) {
//                                        Toast.makeText(VerifyActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
//                                    }
                                } catch (JSONException e) {
                                    Log.e("VOLLEY", e.toString());

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                spinner.setVisibility(View.GONE);
                                spinner_frame.setVisibility(View.GONE);
                                Toast.makeText(VerifyActivity.this, "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                                Log.e("VOLLEY", error.toString());
                            }
                        }){
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("otp",otp);

//                                params.put(KEY_EMAIL, email);
                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("authorization", token);

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
    }
}
