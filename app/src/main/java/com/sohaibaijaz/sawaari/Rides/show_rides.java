package com.sohaibaijaz.sawaari.Rides;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.sohaibaijaz.sawaari.R;

import java.util.ArrayList;
import java.util.List;

public class show_rides extends AppCompatActivity {

    String rides_data;

    public static String[] titles = {
            "Today", "Tomorrow",
    };

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        private String rides_data;

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        public void addFragment(Fragment fragment, String rides_data) {
            mFragmentList.add(fragment);
            this.rides_data = rides_data;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return today_rides.newInstance(0, "Today", rides_data);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return tomorrow_rides.newInstance(1, "Tomorrow", rides_data);
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
        setContentView(R.layout.show_rides);
        getSupportActionBar().hide();

        rides_data = getIntent().getStringExtra("rides");
        System.out.println(rides_data);

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

        adapterViewPager.addFragment(today, rides_data);
        adapterViewPager.addFragment(tomorrow, rides_data);

//        adapter = new ViewPagerUnitDetailsAdapter(this.getSupportFragmentManager());
//        tr1 = new today_rides();
//        tr2 = new tomorrow_rides();

//        adapterViewPager.(tr1, rides_data);
//        adapterViewPager.(tr2, rides_data);

        vpPager.setAdapter(adapterViewPager);

        back_btn_my_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                startActivity(new Intent(getApplicationContext(), HomeFragment.class));
            }
        });
    }

}
