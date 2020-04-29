package com.sohaibaijaz.sawaari.Fragments;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.sohaibaijaz.sawaari.R;

import static com.sohaibaijaz.sawaari.Fragments.HomeFragment.isNetworkAvailable;


public class RideFragmentN extends AppCompatActivity {

    public static String[] titles = {
            "Scheduled", "History",
    };

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    return ride_scheduled.newInstance(0, "Scheduled");

                case 1:
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

        ViewPager vpPager = findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isNetworkAvailable(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
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
