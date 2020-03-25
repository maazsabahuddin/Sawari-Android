package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
import com.sohaibaijaz.sawaari.Fragments.Updatepassword;
import com.sohaibaijaz.sawaari.Fragments.Verifypassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class UpdatePhoneActivity extends AppCompatActivity {

    EditText editText_PhoneNumber;
    SharedPreferences sharedPreferences;
    Button button_savephonenumber;
    TextView error_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_update_phone);
        sharedPreferences = UpdatePhoneActivity.this.getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);

        editText_PhoneNumber=findViewById(R.id.phonenumber);
        editText_PhoneNumber.setText(sharedPreferences.getString("phone_number", ""));

        error_message=findViewById(R.id.errormessagep);
        button_savephonenumber=findViewById(R.id.savephone);

        button_savephonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_PhoneNumber.setCursorVisible(false);
                update_phonenumber();
            }
        });

        editText_PhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_PhoneNumber.setCursorVisible(true);
                error_message.setVisibility(View.GONE);
            }
        });

    }

    public void update_phonenumber()
    {
        final String new_phonenumber = editText_PhoneNumber.getText().toString();

        if(new_phonenumber.equals(""))
        {
            error_message.setText("Field cannot be empty");
            error_message.setVisibility(View.VISIBLE);
            editText_PhoneNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    editText_PhoneNumber.setCursorVisible(true);
                    error_message.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            //  Toast.makeText(Verifypassword.this, "Field cannot be empty", Toast.LENGTH_LONG).show();
        }
        else {

            try {

                String url = "http://52.15.104.184/phonenumber/change/resend_otp/";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject json = new JSONObject(response);
                                    if (json.getString("status").equals("200")) {

                                         SharedPreferences.Editor editor = sharedPreferences.edit();
                                         editor.putString("phone_number", new_phonenumber);
                                         editor.apply();

                                         Toast.makeText(UpdatePhoneActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();

                                         Intent i = new Intent(UpdatePhoneActivity.this, VerifyPhoneNoActivity.class);
                                         UpdatePhoneActivity.this.startActivity(i);

                                    } else if (json.getString("status").equals("400") || json.getString("status").equals("404")) {

                                        error_message.setText(json.getString("message"));
                                        error_message.setVisibility(View.VISIBLE);
                                        Toast.makeText(UpdatePhoneActivity.this, new_phonenumber, Toast.LENGTH_LONG).show();

                                        editText_PhoneNumber.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                editText_PhoneNumber.setCursorVisible(true);
                                                error_message.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {

                                            }
                                        });
                                        // Toast.makeText(Verifypassword.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        error_message.setText(json.getString("message"));
                                        error_message.setVisibility(View.VISIBLE);
                                        Toast.makeText(UpdatePhoneActivity.this, "ya ye hai", Toast.LENGTH_LONG).show();

                                        editText_PhoneNumber.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                editText_PhoneNumber.setCursorVisible(true);
                                                error_message.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {

                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(UpdatePhoneActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("phone_number", new_phonenumber);

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
}
