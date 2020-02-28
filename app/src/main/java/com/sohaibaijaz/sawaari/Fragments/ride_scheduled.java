package com.sohaibaijaz.sawaari.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sohaibaijaz.sawaari.CustomPreviewUserRidesHistory;
import com.sohaibaijaz.sawaari.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class ride_scheduled extends Fragment {

    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static ride_scheduled newInstance(int page, String title) {
        ride_scheduled fragmentSecond = new ride_scheduled();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentSecond.setArguments(args);
        return fragmentSecond;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    private ListView lv_rides;
    private SharedPreferences sharedPreferences;
    private TextView scheduled_no_trips_tv;
    private Button scheduled_book_ride_btn;
    private ArrayList<HashMap> rides = new ArrayList<HashMap>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduled_ride, container, false);

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(AppPreferences, Context.MODE_PRIVATE );
        String user_rides = sharedPreferences.getString("user_rides", "");

        lv_rides = view.findViewById(R.id.scheduled_rides_listview);
        scheduled_no_trips_tv = view.findViewById(R.id.schedule_trips_tv);
        scheduled_book_ride_btn = view.findViewById(R.id.schedule_book_btn);

        scheduled_no_trips_tv.setVisibility(View.GONE);
        scheduled_book_ride_btn.setVisibility(View.GONE);

        boolean flag = false;
        System.out.print(user_rides);
        if (user_rides.equals("") || user_rides.equals("[]"))
        {
            scheduled_no_trips_tv.setVisibility(View.VISIBLE);
            scheduled_book_ride_btn.setVisibility(View.VISIBLE);
        }
        else{

            try {
                JSONArray jsonArray = new JSONArray(user_rides);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (!jsonObject.toString().equals("{}")) {

                        if(jsonObject.getString("ride_status").equals("ACTIVE")){
                            HashMap<String, String> ride = new HashMap<>();
                            ride.put("booking_id", jsonObject.getString("booking_id"));
                            ride.put("pick_up_point", jsonObject.getString("pick_up_point"));
                            ride.put("pick_up_time", jsonObject.getString("pick_up_time"));
                            ride.put("drop_off_point", jsonObject.getString("drop_off_point"));
                            ride.put("drop_off_time", jsonObject.getString("drop_off_time"));
                            ride.put("seats", jsonObject.getString("seats"));
                            ride.put("ride_date", jsonObject.getString("ride_date"));
                            ride.put("ride_status", jsonObject.getString("ride_status"));
                            ride.put("fare", jsonObject.getString("fare"));

                            flag = true;
                            rides.add(ride);
                        }
                    }
                }

                if(flag){
                    CustomPreviewUserRidesHistory adapterUserRides = new CustomPreviewUserRidesHistory(getActivity(), rides);
                    lv_rides.setAdapter(adapterUserRides);
                }
                else{
                    scheduled_no_trips_tv.setVisibility(View.VISIBLE);
                    scheduled_book_ride_btn.setVisibility(View.VISIBLE);
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        return view;
    }

}
