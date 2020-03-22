package com.sohaibaijaz.sawaari.Rides;

import android.content.Intent;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sohaibaijaz.sawaari.Fragments.HomeFragment;
import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.model.Ride;
import com.sohaibaijaz.sawaari.model.RidesModel;

import org.json.JSONObject;
//import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        public void addFragment(Fragment fragment, String json) {
            mFragmentList.add(fragment);
            this.json = json;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return today_rides.newInstance(0, "Today", json);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return tomorrow_rides.newInstance(1, "Tomorrow", json);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
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


//        GsonBuilder builder = new GsonBuilder();
//        Gson gson = builder.create();
//        RidesModel ride = gson.fromJson(json, RidesModel.class);
//
//        ArrayList<HashMap<String, String>> rideArrayList = new ArrayList<HashMap<String, String>>();
//        HashMap<String, String> ride_hashMap = new HashMap<String, String>();
//        for(int i=0; i<ride.getRides().size(); i++){
//
//            ride_hashMap.put("vehicle_no_plate", ride.getRides().get(i).getVehicleNoPlate());
//            ride_hashMap.put("ride_date", ride.getRides().get(i).getRideDate());
//            ride_hashMap.put("route_name", ride.getRides().get(i).getRouteName());
//            ride_hashMap.put("seats_left", ride.getRides().get(i).getSeatsLeft().toString());
//
//            ride_hashMap.put("pick_up_location", ride.getRides().get(i).getPickUpLocation().getDistance());
//            ride_hashMap.put("arrival_time", ride.getRides().get(i).getPickUpLocation().getArrivalTime());
//            ride_hashMap.put("pick_up_stop_name", ride.getRides().get(i).getPickUpLocation().getStopName());
//            ride_hashMap.put("pick_up_stop_id", ride.getRides().get(i).getPickUpLocation().getStopId().toString());
//
//            ride_hashMap.put("drop_off_location", ride.getRides().get(i).getDropOffLocation().getDistance());
//            ride_hashMap.put("departure_time", ride.getRides().get(i).getDropOffLocation().getDepartureTime());
//            ride_hashMap.put("drop_off_stop_name", ride.getRides().get(i).getDropOffLocation().getStopName());
//            ride_hashMap.put("drop_off_stop_id", ride.getRides().get(i).getDropOffLocation().getStopId().toString());
//
//            rideArrayList.add(ride_hashMap);
//        }

        ViewPager vpPager = findViewById(R.id.vpPager_show_rides);
        Button back_btn_my_ride = findViewById(R.id.back_btn_select_ride);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

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
//                onBackPressed();
//                Fragment mFragment = null;
//                mFragment = new HomeFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.nav_home, mFragment).commit();
                Intent intent = new Intent(show_rides.this, NavActivity.class);
                startActivity(intent);
//                HomeFragment homeFragment = new HomeFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id., homeFragment).commit();
            }
        });
    }

}
