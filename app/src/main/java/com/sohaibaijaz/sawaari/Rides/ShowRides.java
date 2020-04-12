package com.sohaibaijaz.sawaari.Rides;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.sohaibaijaz.sawaari.R;

//import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowRides extends AppCompatActivity {

    private HashMap<String, String> pick_up_location = new HashMap<>();
    private HashMap<String, String> drop_off_location = new HashMap<>();
    private String json;

    public static String[] titles = {
            "Today", "Tomorrow",
    };

    public static class MyPagerAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS = 2;
        HashMap<String, String> pick_up_location = new HashMap<>();
        HashMap<String, String> drop_off_location = new HashMap<>();
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private String json;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        public void addFragmentValues(Fragment fragment, HashMap<String, String> pick_up_location,
                                      HashMap<String, String> drop_off_location, String json) {
            mFragmentList.add(fragment);
            this.pick_up_location = pick_up_location;
            this.drop_off_location = drop_off_location;
            this.json = json;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    return TodayRides.newInstanceValues(0, "Today", pick_up_location, drop_off_location, json);

                case 1:
                    return TomorrowRides.newInstanceValues(1, "Tomorrow", pick_up_location, drop_off_location, json);

                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position){
            return titles[position];
        }

    }

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
        final MyPagerAdapter adapterViewPager = new MyPagerAdapter(this.getSupportFragmentManager());

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

        TodayRides today = new TodayRides();
        TomorrowRides tomorrow = new TomorrowRides();

        adapterViewPager.addFragmentValues(today, pick_up_location, drop_off_location, json);
        adapterViewPager.addFragmentValues(tomorrow, pick_up_location, drop_off_location, json);

        vpPager.setAdapter(adapterViewPager);

        back_btn_my_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
