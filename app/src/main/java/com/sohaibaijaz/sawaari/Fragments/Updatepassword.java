package com.sohaibaijaz.sawaari.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.sohaibaijaz.sawaari.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class Updatepassword extends AppCompatActivity {

    Button button_update_password;
    EditText editText_new_password;
    EditText editText_confirm_password;
    SharedPreferences sharedPreferences;
    TextView txt_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_updatepassword);

        sharedPreferences = Updatepassword.this.getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);

        button_update_password=findViewById(R.id.update_password);
        editText_new_password=findViewById(R.id.new_password);
        editText_confirm_password=findViewById(R.id.confirm_password);
        txt_error=findViewById(R.id.errormessage);

        button_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkkrlo.setVisibility(View.VISIBLE);
                editText_confirm_password.setCursorVisible(false);
                editText_new_password.setCursorVisible(false);
                update_password();
            }
        });

        editText_confirm_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_confirm_password.setCursorVisible(true);
                txt_error.setVisibility(View.GONE);
            }
        });

        editText_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_new_password.setCursorVisible(true);
                txt_error.setVisibility(View.GONE);
            }
        });

    }

    public void update_password(){

        final String password_new = editText_new_password.getText().toString();
        final String password_confirm = editText_confirm_password.getText().toString();

        if(password_confirm.equals("") || password_new.equals("")){

            txt_error.setVisibility(View.VISIBLE);
            txt_error.setText("Password fields cannot be empty");
            editText_new_password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    txt_error.setVisibility(View.GONE);
                    editText_new_password.setCursorVisible(true);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            editText_confirm_password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    txt_error.setVisibility(View.GONE);
                    editText_confirm_password.setCursorVisible(true);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        else if(!password_new.equals(password_confirm)) {

            txt_error.setVisibility(View.VISIBLE);
            txt_error.setText("Password fields are not same");

            editText_new_password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    txt_error.setVisibility(View.GONE);
                    editText_new_password.setCursorVisible(true);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            editText_confirm_password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    txt_error.setVisibility(View.GONE);
                    editText_confirm_password.setCursorVisible(true);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        else {

            try {

                String url = "http://52.15.104.184/password/change/";
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

                                        Toast.makeText(Updatepassword.this, json.getString("message"), Toast.LENGTH_LONG).show();


                                    } else if (json.getString("status").equals("401") || json.getString("status").equals("404") || json.getString("status").equals("405") || json.getString("status").equals("406") || json.getString("status").equals("407")) {

                                        Toast.makeText(Updatepassword.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                    else{

                                        txt_error.setVisibility(View.VISIBLE);
                                        txt_error.setText(json.getString("message"));
                                        //Toast.makeText(Updatepassword.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                        editText_new_password.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                txt_error.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {

                                            }
                                        });

                                        editText_confirm_password.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                txt_error.setVisibility(View.GONE);
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
                                Toast.makeText(Updatepassword.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("pin", password_new);
                        params.put("confirm_pin", password_confirm);

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
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
