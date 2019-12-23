package com.sohaibaijaz.sawaari.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.UpdateNameActivity;
import com.sohaibaijaz.sawaari.UpdatePhoneNumberActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.logging.Logger.global;

public class AccountFragment extends Fragment {

    private View fragmentView;
    private SharedPreferences sharedPreferences;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_email;
    private Button btn_update_name;
    private Button btn_change_phone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_account, container, false);
        sharedPreferences= Objects.requireNonNull(this.getActivity()).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        tv_name = fragmentView.findViewById(R.id.tv_name);
        tv_phone = fragmentView.findViewById(R.id.tv_phone);
        tv_email = fragmentView.findViewById(R.id.tv_email);
        btn_update_name = fragmentView.findViewById(R.id.btn_update_name);
        btn_change_phone = fragmentView.findViewById(R.id.btn_change_phone);

        refreshUserDetails();

        fragmentView.setFocusableInTouchMode(true);
        fragmentView.requestFocus();
        fragmentView.setOnKeyListener( new View.OnKeyListener()
        {
            int backpress = 0;
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {

                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    backpress = (backpress + 1);
                    Toast.makeText(getContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

                    if (backpress > 2) {
                        getActivity().finish();
                        System.exit(0);
                    }
                    return true;
                }
                return false;
            }
        } );


        btn_update_name.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity().getApplicationContext(), UpdateNameActivity.class);
                startActivity(i);
                getActivity().finish();

            }
        });

        btn_change_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), UpdatePhoneNumberActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return fragmentView;
    }


    private void refreshUserDetails(){
        String token = sharedPreferences.getString("Token", "");
        String first_name= sharedPreferences.getString("first_name", "");
        String last_name = sharedPreferences.getString("last_name", "");
        String email = sharedPreferences.getString("email","");
        String phone_number = sharedPreferences.getString("phone_number", "");

        if (!token.equals("") ){

            if(first_name.equals("") || last_name.equals("")){
                tv_name.setText("No name registered");
            }
            else {
                String name = first_name + " " + last_name;
                tv_name.setText(name);
            }
            if(email.equals(""))
            {
                tv_email.setText("No email account registered");
            }
            else{
                tv_email.setText(email);
            }

            if(phone_number.equals(""))
            {
                tv_phone.setText("No phone number registered");
            }
            else{
                tv_phone.setText(phone_number);
            }

        }
    }
}
