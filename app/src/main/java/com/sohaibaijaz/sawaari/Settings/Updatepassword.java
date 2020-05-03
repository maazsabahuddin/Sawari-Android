package com.sohaibaijaz.sawaari.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.sohaibaijaz.sawaari.Fragments.CallBack;
import com.sohaibaijaz.sawaari.Login.LoginAPI;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class Updatepassword extends AppCompatActivity {

    ImageView button_update_password;
    EditText editText_new_password;
    SharedPreferences sharedPreferences;
    TextView error_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//        getSupportActionBar().setTitle("Password");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_updatepassword);

        sharedPreferences = Updatepassword.this.getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);

        button_update_password = findViewById(R.id.update_password);
        editText_new_password = findViewById(R.id.pin);
        error_message = findViewById(R.id.error_message_pin);

        editText_new_password.requestFocus();
        error_message.setVisibility(View.GONE);
        editText_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_new_password.setCursorVisible(true);
                error_message.setVisibility(View.GONE);
            }
        });

        button_update_password.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                editText_new_password.setCursorVisible(false);
                final String password_new = editText_new_password.getText().toString();

                if(password_new.equals("")){
                    error_message.setVisibility(View.VISIBLE);
                    error_message.setText("Password fields cannot be empty");
                }
                else{
                    LoginAPI.updatePassword(Updatepassword.this, password_new, new CallBack() {
                        @Override
                        public void onSuccess(String status_code, String message) {
                            Toast.makeText(Updatepassword.this, message, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Updatepassword.this, NavActivity.class);
                            Updatepassword.this.startActivity(i);
                        }

                        @Override
                        public void onFailure(String status_code, String message) {
                            error_message.setVisibility(View.VISIBLE);
                            error_message.setText(message);
                        }
                    });
                }
            }
        });

        editText_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_new_password.setCursorVisible(true);
                error_message.setVisibility(View.GONE);
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

}
