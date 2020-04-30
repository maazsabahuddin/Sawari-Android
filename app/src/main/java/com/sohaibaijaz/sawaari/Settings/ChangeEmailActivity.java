package com.sohaibaijaz.sawaari.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class ChangeEmailActivity extends AppCompatActivity {

    Button button_updatemail;
    EditText text_email;
    SharedPreferences sharedPreferences;
    TextView textViewerror;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        getSupportActionBar().setTitle("Email Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = ChangeEmailActivity.this.getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
        User user = User.getInstance();

        textViewerror=findViewById(R.id.errormessage4);
        button_updatemail=findViewById(R.id.changeEmail);
        text_email= findViewById(R.id.update_email);
        textViewerror.setVisibility(View.GONE);
        text_email.setText(user.getEmail());

        button_updatemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_email.setCursorVisible(false);
                update_email();
            }
        });
        text_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_email.setCursorVisible(true);
                textViewerror.setVisibility(View.GONE);
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

    private  void update_email(){

        final String new_email = text_email.getText().toString();
        if(new_email.equals(""))
        {
            textViewerror.setText("Field cannot be empty");
            textViewerror.setVisibility(View.VISIBLE);
            text_email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    text_email.setCursorVisible(true);
                    textViewerror.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            //  Toast.makeText(Verifypassword.this, "Field cannot be empty", Toast.LENGTH_LONG).show();
        }
        else {

            try {

                String url = MainActivity.baseurl+"/update/email/";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject json = new JSONObject(response);
                                    if (json.getString("status").equals("200")) {

                                        // SharedPreferences.Editor editor = sharedPreferences.edit();
                                        //editor.putString("phone_number", new_phonenumber);
                                        //editor.apply();

                                        Toast.makeText(ChangeEmailActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                        User user= User.getInstance();
                                        user.setEmail(new_email);
//                                        Intent i = new Intent(ChangePhoneNumberActivity.this, VerifyPhoneNoActivity.class);
//                                        i.putExtra("change_number", new_phonenumber );
//                                        startActivity(i);

                                    } else if (json.getString("status").equals("400")) {

                                        textViewerror.setText(json.getString("message"));
                                        textViewerror.setVisibility(View.VISIBLE);
                                        Toast.makeText(ChangeEmailActivity.this, new_email, Toast.LENGTH_LONG).show();

                                        text_email.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                text_email.setCursorVisible(true);
                                                textViewerror.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {

                                            }
                                        });
                                        // Toast.makeText(Verifypassword.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                    else if(json.getString("status").equals("404")){
                                        Toast.makeText(ChangeEmailActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                        SettingsFragment.forcedLogout(ChangeEmailActivity.this);
                                        // flag = false;
                                    }
                                    else{
                                        textViewerror.setText(json.getString("message"));
                                        textViewerror.setVisibility(View.VISIBLE);
                                        Toast.makeText(ChangeEmailActivity.this, "ya ye hai", Toast.LENGTH_LONG).show();

                                        text_email.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                text_email.setCursorVisible(true);
                                                textViewerror.setVisibility(View.GONE);
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
                                Toast.makeText(ChangeEmailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", new_email);

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
