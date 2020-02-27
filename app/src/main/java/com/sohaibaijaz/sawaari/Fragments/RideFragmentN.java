package com.sohaibaijaz.sawaari.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
        getSupportActionBar().hide();

//        TextView textView = (TextView) findViewById(R.id.tv_seaticon);
//        TextView textView1 = (TextView) findViewById(R.id.pick_up_icon);
//        TextView textView2 = (TextView) findViewById(R.id.drop_off_icon);
//        TextView rebook_ride_icon = findViewById(R.id.rebook_ride_icon);
//        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bus_seat, 0, 0, 0);
//        textView1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pin_location, 0, 0, 0);
//        textView2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pin_location, 0, 0, 0);
//        rebook_ride_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bus, 0, 0, 0);
//        textView.setText("     5");

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        Button back_btn_my_ride = findViewById(R.id.back_btn_my_ride);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);


        back_btn_my_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
