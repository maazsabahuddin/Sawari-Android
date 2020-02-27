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

import androidx.fragment.app.Fragment;

import com.sohaibaijaz.sawaari.CustomAdapterUserRides;
import com.sohaibaijaz.sawaari.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class ride_history extends Fragment {

    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static ride_history newInstance(int page, String title) {
        ride_history fragmentFirst = new ride_history();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
//        getSupportActionBar().hide();
    }

    private ListView lv_rides;
    private SharedPreferences sharedPreferences;
    private TextView tv_no_reservations;
    private Button book_ride_btn;
    private ArrayList<HashMap> rides = new ArrayList<HashMap>();
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_ride, container, false);

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(AppPreferences, Context.MODE_PRIVATE );
        String user_rides = sharedPreferences.getString("user_rides", "");

        lv_rides = view.findViewById(R.id.scheduled_rides_listview);
        tv_no_reservations = view.findViewById(R.id.past_trips_textview);
        book_ride_btn = view.findViewById(R.id.book_trip_button);

        tv_no_reservations.setVisibility(View.GONE);
        book_ride_btn.setVisibility(View.GONE);

        System.out.print(user_rides);
        if (user_rides.equals("") || user_rides.equals("[]"))
        {
            tv_no_reservations.setVisibility(View.VISIBLE);
            book_ride_btn.setVisibility(View.VISIBLE);
        }
        else{

            try {
                JSONArray jsonArray = new JSONArray(user_rides);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (!jsonObject.toString().equals("{}")) {

                        HashMap<String, String> ride = new HashMap<>();
                        ride.put("booking_id", jsonObject.getString("booking_id"));
                        ride.put("pick-up-point", jsonObject.getString("pick_up_point"));
                        ride.put("pick-up-time", jsonObject.getString("pick_up_time"));
                        ride.put("drop-off-point", jsonObject.getString("drop_off_point"));
                        ride.put("drop-off-time", jsonObject.getString("drop_off_time"));
                        ride.put("seats", jsonObject.getString("seats"));
                        ride.put("ride_date", jsonObject.getString("ride_date"));
                        ride.put("ride_status", jsonObject.getString("ride_status"));
                        ride.put("fare", jsonObject.getString("fare"));

                        rides.add(ride);
                    }
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        CustomAdapterUserRides adapterUserRides = new CustomAdapterUserRides(getActivity(), rides);
        lv_rides.setAdapter(adapterUserRides);

        return view;
    }

}
