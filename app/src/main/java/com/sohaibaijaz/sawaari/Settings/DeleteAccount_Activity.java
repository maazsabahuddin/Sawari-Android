package com.sohaibaijaz.sawaari.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class DeleteAccount_Activity extends AppCompatActivity {

    TextView sorry_message;
    SharedPreferences sharedPreferences;
    Button button_cancel;
    Button button_continue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Delete Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = DeleteAccount_Activity.this.getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);

        setContentView(R.layout.activity_delete_account);
        sorry_message=findViewById(R.id.sorrymessage);
        button_cancel=findViewById(R.id.cancel);
        button_continue=findViewById(R.id.continue_delete);

        sorry_message.setText(sharedPreferences.getString("first_name", "")+", "+"sorry to see you go");

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DeleteAccount_Activity.this, SettingsActivity.class);
                DeleteAccount_Activity.this.startActivity(i);
            }
        });

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_account();
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


    private void delete_account(){
        try {

            String url = "http://52.15.104.184/delete/user/";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("200")) {

                                    Toast.makeText(DeleteAccount_Activity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(DeleteAccount_Activity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DeleteAccount_Activity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

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
        }catch (Exception e) {
            e.printStackTrace();
            }


    }
}
