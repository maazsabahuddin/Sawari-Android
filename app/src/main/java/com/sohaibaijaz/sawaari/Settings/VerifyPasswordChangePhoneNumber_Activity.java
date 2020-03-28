package com.sohaibaijaz.sawaari.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
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
import com.sohaibaijaz.sawaari.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class VerifyPasswordChangePhoneNumber_Activity extends AppCompatActivity {

    Button button_verify_pass;
    EditText editText_password;
    SharedPreferences sharedPreferences;
    TextView error_message;
    TextView password_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_password_change_phone_number);
        getSupportActionBar().setTitle("Phone Number");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = VerifyPasswordChangePhoneNumber_Activity.this.getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
        editText_password= findViewById(R.id.verify_password4);
        button_verify_pass= findViewById(R.id.verifypassword4);
        error_message=findViewById(R.id.errormessage4);
        password_message=findViewById(R.id.securitytext4);

        error_message.setVisibility(View.GONE);

        button_verify_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_password.setCursorVisible(false);
                verify_password();
            }
        });
        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                button_verify_pass.setEnabled(false);
                button_verify_pass.setAlpha(0.5f);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                button_verify_pass.setEnabled(true);
                button_verify_pass.setAlpha(1);

                if((editText_password.getText().toString()).equals(""))
                {
                    button_verify_pass.setEnabled(false);
                    button_verify_pass.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editText_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_password.setCursorVisible(true);
                error_message.setVisibility(View.GONE);
                password_message.setVisibility(View.VISIBLE);

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

    public void verify_password()
    {
        final String password_verify = editText_password.getText().toString();

        if(password_verify.equals(""))
        {
            password_message.setVisibility(View.GONE);
            error_message.setText("Field cannot be empty");
            error_message.setVisibility(View.VISIBLE);
            editText_password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    editText_password.setCursorVisible(true);
                    error_message.setVisibility(View.GONE);
                    password_message.setVisibility(View.VISIBLE);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            //  Toast.makeText(Verifypassword.this, "Field cannot be empty", Toast.LENGTH_LONG).show();
        }
        else {

            try {

                String url = "http://52.15.104.184/password/check/";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject json = new JSONObject(response);
                                    if (json.getString("status").equals("200")) {


                                        // SharedPreferences.Editor editor = sharedPreferences.edit();
                                        //editor.putString("Token", token);
                                        //editor.apply();

                                        // Toast.makeText(VerifyPasswordChangePhoneNumber_Activity.this, json.getString("message"), Toast.LENGTH_LONG).show();

                                        Intent i = new Intent(VerifyPasswordChangePhoneNumber_Activity.this, ChangePhoneNumberActivity.class);
                                        VerifyPasswordChangePhoneNumber_Activity.this.startActivity(i);

                                    } else if (json.getString("status").equals("401")) {

                                        error_message.setText(json.getString("message"));
                                        error_message.setVisibility(View.VISIBLE);
                                        editText_password.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);

                                        editText_password.addTextChangedListener(new TextWatcher() {

                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                editText_password.setCursorVisible(true);
                                                error_message.setVisibility(View.GONE);
                                                editText_password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorJacksonPurple), PorterDuff.Mode.SRC_ATOP);

                                                password_message.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {

                                            }
                                        });
                                        // Toast.makeText(Verifypassword.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(VerifyPasswordChangePhoneNumber_Activity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("password", password_verify);

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
