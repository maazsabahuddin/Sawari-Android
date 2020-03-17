package com.sohaibaijaz.sawaari.Rides;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sohaibaijaz.sawaari.CustomAdapterActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.model.RidesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class today_rides extends Fragment {

    private String title;
    private static String json;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static today_rides newInstance(int page, String title, String json) {

        today_rides.json = json;

        today_rides today = new today_rides();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        today.setArguments(args);
        return today;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    public String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String local_date = formatter.format(date);
        return local_date;
    }
    Context context;
    ArrayList<HashMap> buses = new ArrayList<HashMap>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_rides, container, false);

        ListView list_buses = view.findViewById(R.id.today_rides_ListView);
        TextView today_ride_suggestion_tv = view.findViewById(R.id.today_ride_suggestion_tv);
        TextView today_no_ride_tv = view.findViewById(R.id.today_no_ride_tv);

        String local_date = getDate();

        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            RidesModel ride = gson.fromJson(json, RidesModel.class);

            ArrayList<HashMap<String, String>> rideArrayList = new ArrayList<>();

            if(ride.getRides().size()!=0){

//                today_ride_suggestion_tv.setVisibility(View.GONE);

                for(int i=0; i<ride.getRides().size(); i++){

                    HashMap<String, String> ride_hashMap = new HashMap<>();

                    if(ride.getRides().get(i).getRideDate().equals(local_date)) {

                        today_no_ride_tv.setVisibility(View.GONE);

                        ride_hashMap.put("vehicle_no_plate", ride.getRides().get(i).getVehicleNoPlate());
                        ride_hashMap.put("ride_date", ride.getRides().get(i).getRideDate());
                        ride_hashMap.put("route_name", ride.getRides().get(i).getRouteName());
                        ride_hashMap.put("seats_left", ride.getRides().get(i).getSeatsLeft().toString());
                        ride_hashMap.put("kilometer", ride.getRides().get(i).getKilometer());
                        ride_hashMap.put("fare_per_km", ride.getRides().get(i).getFarePerKm().toString());
                        ride_hashMap.put("fare_per_person", ride.getRides().get(i).getFarePerPerson().toString());
                        ride_hashMap.put("ride_start_time", ride.getRides().get(i).getRideStartTime());

                        ride_hashMap.put("pick_up_stop_name", ride.getRides().get(i).getPickUpLocation().getStopName());
                        ride_hashMap.put("pick_up_location_distance", ride.getRides().get(i).getPickUpLocation().getDistance());
                        ride_hashMap.put("pick_up_location_duration", ride.getRides().get(i).getPickUpLocation().getDuration());
                        ride_hashMap.put("arrival_time", ride.getRides().get(i).getPickUpLocation().getArrivalTime());
                        ride_hashMap.put("pick_up_stop_id", ride.getRides().get(i).getPickUpLocation().getStopId().toString());

                        ride_hashMap.put("drop_off_stop_name", ride.getRides().get(i).getDropOffLocation().getStopName());
                        ride_hashMap.put("drop_off_location_distance", ride.getRides().get(i).getDropOffLocation().getDistance());
                        ride_hashMap.put("drop_off_location_duration", ride.getRides().get(i).getDropOffLocation().getDuration());
                        ride_hashMap.put("departure_time", ride.getRides().get(i).getDropOffLocation().getDepartureTime());
                        ride_hashMap.put("drop_off_stop_id", ride.getRides().get(i).getDropOffLocation().getStopId().toString());

                        rideArrayList.add(ride_hashMap);
                    }
                }
            }
            else{
                today_no_ride_tv.setVisibility(View.VISIBLE);
            }

            list_buses.setAdapter(new CustomAdapterActivity(getActivity(), rideArrayList, json));
        }

        catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }
}
