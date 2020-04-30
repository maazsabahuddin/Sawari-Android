package com.sohaibaijaz.sawaari.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class VerifyPhoneNoActivity extends AppCompatActivity {

    EditText editText_otp1;
    EditText editText_otp2;
    EditText editText_otp3;
    EditText editText_otp4;
    EditText editText_otp5;
    EditText editText_otp6;
    TextView phonenumber;
    TextView textView_resend_otp;
    SharedPreferences sharedPreferences;
    TextView otp_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Phone Number");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_verify_phone_no);

        sharedPreferences = VerifyPhoneNoActivity.this.getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);

        editText_otp1=findViewById(R.id.otp1);
        editText_otp2=findViewById(R.id.otp2);
        editText_otp3=findViewById(R.id.otp3);
        editText_otp4=findViewById(R.id.otp4);
        editText_otp5=findViewById(R.id.otp5);
        editText_otp6=findViewById(R.id.otp6);

        editText_otp1.setCursorVisible(false);
        editText_otp2.setCursorVisible(false);
        editText_otp3.setCursorVisible(false);
        editText_otp4.setCursorVisible(false);
        editText_otp5.setCursorVisible(false);
        editText_otp6.setCursorVisible(false);

        otp_message=findViewById(R.id.otp_message);
        phonenumber=findViewById(R.id.number);
        textView_resend_otp=findViewById(R.id.resendotp);


        phonenumber.setText(getIntent().getStringExtra("change_number"));
        otp_message.setVisibility(View.GONE);

        editText_otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(count==1){
                    editText_otp2.requestFocus();
                }
               // editText_otp2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText_otp3.requestFocus();
                if(count<1){
                    editText_otp1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText_otp4.requestFocus();
                if(count<1){
                    editText_otp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText_otp5.requestFocus();
                if(count<1){
                    editText_otp3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText_otp6.requestFocus();
                if(count<1){
                    editText_otp4.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // editText_otp2.requestFocus();
              if(count<1){
                  editText_otp5.requestFocus();
              }
              if(!editText_otp1.getText().toString().equals("")  && !editText_otp2.getText().toString().equals("") && !editText_otp3.getText().toString().equals("") && !editText_otp5.getText().toString().equals("") && !editText_otp6.getText().toString().equals("")){
                  verify_phonenumber();
              }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textView_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend_otp();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void verify_phonenumber(){

        final String verifyphonenumber = getIntent().getStringExtra("change_number");
        final String otp= editText_otp1.getText().toString()+ editText_otp2.getText().toString()+ editText_otp3.getText().toString()+editText_otp4.getText().toString()+editText_otp5.getText().toString()+editText_otp6.getText().toString();


            try {

                String url = MainActivity.baseurl+"/verify/phonenumber/";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject json = new JSONObject(response);
                                    if (json.getString("status").equals("200")) {


                                        User user= User.getInstance();
                                        user.setPhoneNumber(verifyphonenumber);

//                                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                                        editor.putString("phone_number", verifyphonenumber);
//                                        editor.apply();

                                        Toast.makeText(VerifyPhoneNoActivity.this, "main hun", Toast.LENGTH_LONG).show();
                                        otp_message.setText(json.getString("message"));
                                        otp_message.setVisibility(View.VISIBLE);
                                       // Intent i = new Intent(ChangePhoneNumberActivity.this, VerifyPhoneNoActivity.class);
                                      //  ChangePhoneNumberActivity.this.startActivity(i);

                                    } else if (json.getString("status").equals("400")) {

                                        otp_message.setText(json.getString("message"));
                                        otp_message.setVisibility(View.VISIBLE);
                                        Toast.makeText(VerifyPhoneNoActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();


                                        // Toast.makeText(Verifypassword.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                    else if(json.getString("status").equals("404")){
                                        Toast.makeText(VerifyPhoneNoActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                        SettingsFragment.forcedLogout(VerifyPhoneNoActivity.this);
                                        // flag = false;
                                    }
                                    else{
                                        otp_message.setText(json.getString("message"));
                                        otp_message.setVisibility(View.VISIBLE);
                                        Toast.makeText(VerifyPhoneNoActivity.this, "ya ye hai", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(VerifyPhoneNoActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("otp", otp);
                        params.put("phone_number", verifyphonenumber);


                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", sharedPreferences.getString("Token", ""));
                        return headers;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    private void resend_otp(){

        final String newphonenumber = getIntent().getStringExtra("change_number");

        try {
            String url = MainActivity.baseurl+"/phonenumber/change/resend_otp/";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject json = new JSONObject(response);
                                if (json.getString("status").equals("200")) {

                                    otp_message.setTextColor(Color.parseColor("#15a336"));
                                    otp_message.setText(json.getString("message"));
                                    otp_message.setVisibility(View.VISIBLE);
                                    Toast.makeText(VerifyPhoneNoActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();


                                } else if (json.getString("status").equals("400") || json.getString("status").equals("404")) {

                                    otp_message.setText(json.getString("message"));
                                    otp_message.setVisibility(View.VISIBLE);
                                    Toast.makeText(VerifyPhoneNoActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();

                                    // Toast.makeText(Verifypassword.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                }
                                else if(json.getString("status").equals("404")){
                                    Toast.makeText(VerifyPhoneNoActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                    SettingsFragment.forcedLogout(VerifyPhoneNoActivity.this);
                                    // flag = false;
                                }
                                else{
                                    otp_message.setText(json.getString("message"));
                                    otp_message.setVisibility(View.VISIBLE);
                                    Toast.makeText(VerifyPhoneNoActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(VerifyPhoneNoActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("phone_number", newphonenumber);

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", sharedPreferences.getString("Token", ""));
                    return headers;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
