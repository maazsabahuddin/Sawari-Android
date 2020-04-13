package com.sohaibaijaz.sawaari.Rides;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sohaibaijaz.sawaari.CustomAdapterActivity;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.model.RidesModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TomorrowRides extends Fragment {
    private String title;
    private int page;
    private static String json;

    private static HashMap<String, String> pick_up_location = new HashMap<>();
    private static HashMap<String, String> drop_off_location = new HashMap<>();

    // newInstance constructor for creating fragment with arguments
    public static TomorrowRides newInstanceValues(int page, String title, HashMap<String, String> pick_up_location,
                                               HashMap<String, String> drop_off_location, String json) {

        TomorrowRides.pick_up_location = pick_up_location;
        TomorrowRides.drop_off_location = drop_off_location;
        TomorrowRides.json = json;

        TomorrowRides tomorrow = new TomorrowRides();
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

    public String getTomorrowDate(){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String local_date = formatter.format(date);

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.add(Calendar.DATE, 1);
        String newDate = formatter.format(c.getTime());

        return newDate;
    }

    ArrayList<HashMap> tomorrow_buses = new ArrayList<HashMap>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tomorrow_rides, container, false);

        ListView list_buses = view.findViewById(R.id.tomorrow_rides_ListView);
        TextView tomorrow_ride_suggestion_tv = view.findViewById(R.id.tomorrow_ride_suggestion_tv);
        TextView tomorrow_no_ride_tv = view.findViewById(R.id.tomorrow_no_ride_tv);

        String tomorrow_date = getTomorrowDate();

        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            RidesModel ride = gson.fromJson(json, RidesModel.class);

            ArrayList<HashMap<String, String>> rideArrayList = new ArrayList<>();

          //  JSONArray rides = new JSONArray(rides_data);
            if(ride.getRides().size()!=0){

             //   tomorrow_no_ride_tv.setVisibility(TextView.GONE);
//                tomorrow_ride_suggestion_tv.setVisibility(TextView.INVISIBLE);

                for(int i=0; i<ride.getRides().size(); i++){
                    HashMap<String, String> ride_hashMap = new HashMap<>();

                  //  JSONObject ride = rides.getJSONObject(i);
                    if(ride.getRides().get(i).getRideDate().equals(tomorrow_date)){

                        tomorrow_no_ride_tv.setVisibility(View.GONE);

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
                tomorrow_no_ride_tv.setVisibility(TextView.VISIBLE);
            }

            list_buses.setAdapter(new CustomAdapterActivity(getActivity(), rideArrayList, json, pick_up_location, drop_off_location));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }
}
