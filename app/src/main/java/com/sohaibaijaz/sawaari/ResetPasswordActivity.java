package com.sohaibaijaz.sawaari;

import android.content.Context;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

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

public class ResetPasswordActivity extends AppCompatActivity {


    private EditText pin1;
    private EditText pin2;

    private String email_or_phone;
    private String token_uuid;
    private FrameLayout spinner_frame;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().hide();
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner_frame = findViewById(R.id.spinner_frame);
        spinner_frame.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        email_or_phone = extras.getString("email_or_phone");
        token_uuid = extras.getString("token_uuid");


        pin1 = findViewById(R.id.pin1);
        pin2 = findViewById(R.id.pin2);
        final Button setnewpassword_Btn = findViewById(R.id.setnewpassword_Btn);


        pin2.setOnEditorActionListener(new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    setnewpassword_Btn.performClick();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(setnewpassword_Btn.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        setnewpassword_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    final String pin = pin1.getText().toString();
                    final String confirm_pin = pin2.getText().toString();

                    if(pin1.equals("") || pin2.equals("")){
                        Toast.makeText(getApplicationContext(), "Required fields missing", Toast.LENGTH_SHORT).show();
                    }
                    else if(!pin1.equals("") && !pin2.equals("")){
                        try{
                            spinner.setVisibility(View.VISIBLE);
                            spinner_frame.setVisibility(View.VISIBLE);
                            String URL = MainActivity.baseurl+"/new/password/reset/?password_uuid="+ URLEncoder.encode(token_uuid, "UTF-8");
                            JSONObject jsonBody = new JSONObject();
                            jsonBody.put("email_or_phone", email_or_phone);
                            jsonBody.put("pin1", pin);
                            jsonBody.put("pin2", confirm_pin);

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    spinner.setVisibility(View.GONE);
                                    spinner_frame.setVisibility(View.GONE);
                                    try{

                                        JSONObject json = new JSONObject(response);
                                        if(json.getString("status").equals("200")){

                                            Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                            finish();
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
                                    params.put("email_or_phone",email_or_phone);
                                    params.put("pin1", pin);
                                    params.put("pin2", confirm_pin);
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
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error Encountered.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    spinner.setVisibility(View.GONE);
                    spinner_frame.setVisibility(View.GONE);
                }

            }
        });


    }
}
