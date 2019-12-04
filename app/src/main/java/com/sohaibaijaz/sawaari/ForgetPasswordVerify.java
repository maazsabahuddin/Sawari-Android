package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordVerify extends AppCompatActivity {


    private String email_or_phone;
    private String token_uuid;

    private FrameLayout spinner_frame;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_verify);
        getSupportActionBar().hide();

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner_frame = findViewById(R.id.spinner_frame);
        spinner_frame.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        email_or_phone = extras.getString("email_or_phone");
        token_uuid = extras.getString("token_uuid");

//        Toast.makeText(getApplicationContext(), email_or_phone + token_uuid, Toast.LENGTH_SHORT).show();

        Button next_btn_fp = findViewById(R.id.next_btn_fp);
        final EditText otp_fp_txt = findViewById(R.id.otp_fp_txt);
        TextView resend_otp_txt = findViewById(R.id.resend_otp_txt);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        next_btn_fp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String otp = otp_fp_txt.getText().toString();
                if (otp.equals("")){
                    Toast.makeText(getApplicationContext(), "Enter OTP first", Toast.LENGTH_SHORT).show();
                }
                else if(!otp.equals("")){
                    try{
                        String URL = MainActivity.baseurl+"/confirm/password/reset/?password_uuid="+ URLEncoder.encode(token_uuid, "UTF-8");
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("otp", otp);
                        jsonBody.put("email_or_phone", email_or_phone);
                        spinner.setVisibility(View.VISIBLE);
                        spinner_frame.setVisibility(View.VISIBLE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                spinner.setVisibility(View.GONE);
                                spinner_frame.setVisibility(View.GONE);
                                try{

                                    JSONObject json = new JSONObject(response);
                                    final String status = json.getString("status");
                                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

                                    if(json.getString("status").equals("200")){

                                        Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                        Intent myIntent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                                        myIntent.putExtra("token_uuid", token_uuid);
                                        myIntent.putExtra("email_or_phone", email_or_phone);

                                        startActivity(myIntent);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Exception e){
                                    e.getStackTrace();
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("VOLLEY", error.toString());
                                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        })
                        {
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("otp", otp);
                                params.put("email_or_phone",email_or_phone);
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
                    }
                    catch (UnsupportedEncodingException | JSONException e) {
                        spinner.setVisibility(View.GONE);
                        spinner_frame.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter OTP first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
