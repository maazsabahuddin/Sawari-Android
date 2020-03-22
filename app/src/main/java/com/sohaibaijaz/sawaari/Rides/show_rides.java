package com.sohaibaijaz.sawaari.Rides;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sohaibaijaz.sawaari.Fragments.HomeFragment;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.model.Ride;
import com.sohaibaijaz.sawaari.model.RidesModel;

import org.json.JSONException;
import org.json.JSONObject;
//import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class show_rides extends AppCompatActivity {

//    ArrayList<HashMap> rides_data = new ArrayList<HashMap>();
    String rides_data, json;
    HashMap<String, String> pick_up_location = new HashMap<>();
    HashMap<String, String> drop_off_location = new HashMap<>();

    public static String[] titles = {
            "Today", "Tomorrow",
    };

    public static class MyPagerAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS = 2;
        private String json;
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        public void addFragment(Fragment fragment, String json) {
            mFragmentList.add(fragment);
            this.json = json;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    return today_rides.newInstance(0, "Today", json);

                case 1:
                    return tomorrow_rides.newInstance(1, "Tomorrow", json);

                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position){
            return titles[position];
        }

    }

    FragmentPagerAdapter adapterViewPager;
    ArrayList<HashMap> rides;
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_rides);
        getSupportActionBar().hide();

        json = getIntent().getStringExtra("json");

        Intent intent = getIntent();
        pick_up_location = (HashMap<String, String>) intent.getSerializableExtra("pick_up_location");
        drop_off_location = (HashMap<String, String>) intent.getSerializableExtra("drop_off_location");

        final ViewPager vpPager = findViewById(R.id.vpPager_show_rides);
        Button back_btn_my_ride = findViewById(R.id.back_btn_select_ride);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                getRoute();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        MyPagerAdapter adapterViewPager = new MyPagerAdapter(this.getSupportFragmentManager());

        today_rides today = new today_rides();
        tomorrow_rides tomorrow = new tomorrow_rides();

        adapterViewPager.addFragment(today, json);
        adapterViewPager.addFragment(tomorrow, json);
        vpPager.setAdapter(adapterViewPager);


        back_btn_my_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void getRoute(){

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final SharedPreferences sharedPreferences= getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("Token", "");
        try {

            String URL = MainActivity.baseurl + "/bus/route/";
            JSONObject jsonBody = new JSONObject();
//                        spinner.setVisibility(View.VISIBLE);
//                        spinner_frame.setVisibility(View.VISIBLE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                                spinner.setVisibility(View.GONE);
//                                spinner_frame.setVisibility(View.GONE);
                    Log.i("VOLLEY", response.toString());
                    try {
                        JSONObject jsonObj = new JSONObject(response);

                        if (jsonObj.getString("status").equals("200")) {

                            json = jsonObj.toString();

                        } else if (jsonObj.getString("status").equals("400") || jsonObj.getString("status").equals("404")) {
                            Toast.makeText(getApplicationContext(), jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.e("VOLLEY", e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                                spinner.setVisibility(View.GONE);
//                                spinner_frame.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("start_lat", pick_up_location.get("latitude"));
                    params.put("start_lon", pick_up_location.get("longitude"));
                    params.put("stop_lat", drop_off_location.get("latitude"));
                    params.put("stop_lon", drop_off_location.get("longitude"));

//                                params.put("stop_lat", "24.913363");
//                                params.put("stop_lon", "67.124208");
//                                params.put("start_lat", "24.823343");
//                                params.put("start_lon", "67.029656");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", token);
                    return headers;
                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 500000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 500000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Slow Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }

}
