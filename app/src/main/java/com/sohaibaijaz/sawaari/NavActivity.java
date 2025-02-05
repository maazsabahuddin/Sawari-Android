package com.sohaibaijaz.sawaari;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.sohaibaijaz.sawaari.Fragments.AccountFragment;
import com.sohaibaijaz.sawaari.Fragments.HomeFragment;
import com.sohaibaijaz.sawaari.Fragments.MessageFragment;
import com.sohaibaijaz.sawaari.Fragments.RideFragmentN;
import com.sohaibaijaz.sawaari.Fragments.RidesFragment;
import com.sohaibaijaz.sawaari.Fragments.SettingsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private RequestQueue requestQueue;
    private FrameLayout spinner_frame;
    private ProgressBar spinner;
    private NavigationView navView;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner_frame = findViewById(R.id.spinner_frame);
        spinner_frame.setVisibility(View.GONE);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sharedPreferences = getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }




    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        final String token = sharedPreferences.getString("Token", "");
        switch (menuItem.getItemId()) {

            case R.id.nav_home:
                HomeFragment homeFragment = new HomeFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                break;

            case R.id.nav_account:
//                AccountFragment accountFragment = new AccountFragment();
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, accountFragment).commit();

                break;
            case R.id.nav_ride:
                RideFragmentN ridesFragment = new RideFragmentN();

//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ridesFragment).commit();
                startActivity(new Intent(getApplicationContext(), RideFragmentN.class));
                break;
            case R.id.nav_message:
                MessageFragment messageFragment = new MessageFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, messageFragment).commit();
                break;

            case R.id.nav_logout:

//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.remove("Token");
//                editor.apply();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                finish();
//                startActivity(intent);
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                try {
                    String URL = MainActivity.baseurl + "/logout/";
                    spinner.setVisibility(View.VISIBLE);
                    spinner_frame.setVisibility(View.VISIBLE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            spinner.setVisibility(View.GONE);
                            spinner_frame.setVisibility(View.GONE);
                            Log.i("VOLLEY", response.toString());
                            try {
                                JSONObject json = new JSONObject(response);
                                if (json.getString("status").equals("200")) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.remove("Token");
                                    editor.remove("first_name");
                                    editor.remove("last_name");
                                    editor.remove("email");
                                    editor.remove("phone_number");
                                    editor.remove("user_rides");
                                    editor.apply();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(intent);
                                }
                                else if (json.getString("status").equals("400")||json.getString("status").equals("404")) {
                                    Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Log.e("VOLLEY", e.toString());

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            spinner.setVisibility(View.GONE);
                            spinner_frame.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                            Log.e("VOLLEY", error.toString());
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();

                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("Authorization", token);
                            return params;
                        }
                    };

                    stringRequest.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                            return 50000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 50000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });
                    requestQueue.add(stringRequest);

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        int backpress = 0;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backpress += 1;
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
            if (backpress > 1) {
                this.finish();
            }


            super.onBackPressed();
        }
    }
}
