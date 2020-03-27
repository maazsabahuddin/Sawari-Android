package com.sohaibaijaz.sawaari.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.R;


public class RideFragmentN extends AppCompatActivity {

    public static String[] titles = {
            "Scheduled", "History",
    };

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return ride_scheduled.newInstance(0, "Scheduled");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ride_history.newInstance(1, "History");
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ride_history);
        getSupportActionBar().setTitle("My Rides");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
       // Button back_btn_my_ride = findViewById(R.id.back_btn_my_ride);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);


//        back_btn_my_ride.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                onBackPressed();
//                startActivity(new Intent(getApplicationContext(), NavActivity.class));
//            }
//        });
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
