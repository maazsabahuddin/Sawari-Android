package com.sohaibaijaz.sawaari.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class Verifypassword extends AppCompatActivity {

    ImageView button_verify_pass;
    EditText editText_password;
    SharedPreferences sharedPreferences;
    TextView error_message;
    TextView password_message;

    private String comingFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_verify_password);

        sharedPreferences = Verifypassword.this.getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
        editText_password= findViewById(R.id.verify_password);
        button_verify_pass= findViewById(R.id.verifypassword);
        error_message=findViewById(R.id.errormessage);
        password_message=findViewById(R.id.securitytext);

//        Toast.makeText(Verifypassword.this, getIntent().getStringExtra("coming_from"), Toast.LENGTH_LONG).show();

       // editText_password= findViewById(R.id.verify_password);
        //button_verify_pass= findViewById(R.id.verifypassword);
        error_message.setVisibility(View.GONE);

        button_verify_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_password.setCursorVisible(false);
                verify_password();
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

        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                button_verify_pass.setEnabled(false);
                button_verify_pass.setAlpha(0.5f);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                button_verify_pass.setEnabled(true);
                button_verify_pass.setAlpha(1.0f);

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
        }
        else {

            try {

                String url = MainActivity.baseurl+"/password/check/";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject json = new JSONObject(response);
                                    if (json.getString("status").equals("200")) {

                                        Toast.makeText(Verifypassword.this, json.getString("message"), Toast.LENGTH_LONG).show();

                                       if( getIntent().getStringExtra("coming_from").equals("password_preference")){
                                           Intent i = new Intent(Verifypassword.this, Updatepassword.class);
                                           Verifypassword.this.startActivity(i);
                                       }
                                        else if( getIntent().getStringExtra("coming_from").equals("phone_number_preference")){
                                            Intent i = new Intent(Verifypassword.this, ChangePhoneNumberActivity.class);
                                            Verifypassword.this.startActivity(i);
                                        }
                                       else if( getIntent().getStringExtra("coming_from").equals("privacy_activity")){
                                           Intent i = new Intent(Verifypassword.this, DeleteAccount_Activity.class);
                                           Verifypassword.this.startActivity(i);
                                       }
                                       else if( getIntent().getStringExtra("coming_from").equals("two_step_activity")){
                                           Intent i = new Intent(Verifypassword.this, SecurityActivity.class);
                                           i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                           Verifypassword.this.startActivity(i);
                                       }



                                    } else if (json.getString("status").equals("401")) {

                                         password_message.setVisibility(View.GONE);
                                        error_message.setText(json.getString("message"));
                                        error_message.setVisibility(View.VISIBLE);
                                        editText_password.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);

                                        Toast.makeText(Verifypassword.this, getIntent().getStringExtra("coming_from"), Toast.LENGTH_LONG).show();


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
                                    else if(json.getString("status").equals("404")){
                                        Toast.makeText(Verifypassword.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                        SettingsFragment.signout(Verifypassword.this);
                                        // flag = false;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Verifypassword.this, error.toString(), Toast.LENGTH_LONG).show();
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
