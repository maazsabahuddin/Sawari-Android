package com.sohaibaijaz.sawaari.Rides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sohaibaijaz.sawaari.R;

public class tomorrow_rides extends Fragment {
    private String title;
    private static String rides_data;
    private int page;

    public tomorrow_rides(){ }

    // newInstance constructor for creating fragment with arguments
    public static tomorrow_rides newInstance(int page, String title, String rides_data) {

        tomorrow_rides.rides_data = rides_data;

        tomorrow_rides tomorrow = new tomorrow_rides();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        tomorrow.setArguments(args);
        return tomorrow;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tomorrow_rides, container, false);

        return view;
    }
}
