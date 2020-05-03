package com.sohaibaijaz.sawaari.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sohaibaijaz.sawaari.Fragments.CallBack;
import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.RealmHelper;
import com.sohaibaijaz.sawaari.Settings.Updatepassword;
import com.sohaibaijaz.sawaari.Settings.VerifyPhoneNoActivity;
import com.sohaibaijaz.sawaari.UserDetails;

import io.realm.Realm;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class LoginVerify extends AppCompatActivity {

    EditText editText_otp1;
    EditText editText_otp2;
    EditText editText_otp3;
    EditText editText_otp4;
    EditText editText_otp5;
    EditText editText_otp6;
    TextView phonenumber_message;
    TextView textView_resend_otp;
    TextView BackLoginVerify;
    SharedPreferences sharedPreferences;
    TextView otp_message;

    Realm realm;
    RealmHelper helper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_verify);

        sharedPreferences = LoginVerify.this.getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);

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

        otp_message = findViewById(R.id.otp_message);
        phonenumber_message = findViewById(R.id.phone_number_message);
        textView_resend_otp = findViewById(R.id.resendotp);
        BackLoginVerify = findViewById(R.id.BackLoginVerify);

        realm= Realm.getDefaultInstance();
        helper = new RealmHelper(realm);

        editText_otp1.requestFocus();
        final String phone_number = getIntent().getStringExtra("phone_number");
        phonenumber_message.setText("Enter the 6 digit code sent to " + phone_number);
        otp_message.setVisibility(View.GONE);

        editText_otp2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_DEL){
                    editText_otp1.setText("");
                    editText_otp1.requestFocus();
                }
                return true;
            }
        });
        editText_otp3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_DEL){
                    editText_otp2.setText("");
                    editText_otp2.requestFocus();

                }
                return true;
            }
        });
        editText_otp4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_DEL){
                    editText_otp3.setText("");
                    editText_otp3.requestFocus();
                }
                return true;
            }
        });
        editText_otp5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_DEL){
                    editText_otp4.setText("");
                    editText_otp4.requestFocus();
                }
                return true;
            }
        });
        editText_otp6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_DEL){
                    if(!editText_otp6.getText().toString().equals("")){
                        editText_otp6.setText("");
                        otp_message.setVisibility(View.GONE);
                    }
                    else{
                        editText_otp5.setText("");
                        editText_otp5.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });

        editText_otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==1){
                    editText_otp2.requestFocus();
                }
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText_otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText_otp6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        editText_otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!editText_otp1.getText().toString().equals("") && !editText_otp2.getText().toString().equals("") && !editText_otp3.getText().toString().equals("") && !editText_otp5.getText().toString().equals("") && !editText_otp6.getText().toString().equals("")){
                    final String otp = editText_otp1.getText().toString() +
                            editText_otp2.getText().toString()+
                            editText_otp3.getText().toString()+
                            editText_otp4.getText().toString()+
                            editText_otp5.getText().toString()+
                            editText_otp6.getText().toString();

                    UserDetails.verifyUser(LoginVerify.this, phone_number, otp, new CallBack() {
                        @Override
                        public void onSuccess(String message, String status_code) {
                            helper.DeleteUserDetails(LoginVerify.this);
                            helper.DeleteUserPlaces(LoginVerify.this);
                            UserDetails.getUserDetails(LoginVerify.this);
                            UserDetails.getUserPlaces(LoginVerify.this);

                            Intent myIntent = new Intent(LoginVerify.this, Updatepassword.class);//Optional parameters
                            myIntent.putExtra("coming_from", "set_password");
                            LoginVerify.this.startActivity(myIntent);
                        }

                        @Override
                        public void onFailure(String status_code, String message) {
                            otp_message.setVisibility(View.VISIBLE);
                            otp_message.setText(message);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        BackLoginVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
