package com.sohaibaijaz.sawaari.Rides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
            RidesModel ride = gson.fromJson(rides_data, RidesModel.class);

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
//                    String vehicle_no_plate = ride.getString("vehicle_no_plate");
//                    String seats_left = ride.getString("seats_left");
//                    String route_id = ride.getString("route_name");
//                    String ride_date = ride.getString("ride_date");
//
//                    String pick_up_stop_id = ride.getJSONObject("pick-up-location").getString("stop_id");
//                    String pick_up_stop_name = ride.getJSONObject("pick-up-location").getString("stop_name");
//                    String pick_up_duration = ride.getJSONObject("pick-up-location").getString("duration");
//                    String pick_up_distance = ride.getJSONObject("pick-up-location").getString("distance");
//                    String pick_up_arrival_time = ride.getJSONObject("pick-up-location").getString("arrival_time");
//
//                    String drop_off_stop_id = ride.getJSONObject("drop-off-location").getString("stop_id");
//                    String drop_off_stop_name = ride.getJSONObject("drop-off-location").getString("stop_name");
//                    String drop_off_duration = ride.getJSONObject("drop-off-location").getString("duration");
//                    String drop_off_distance = ride.getJSONObject("drop-off-location").getString("distance");
//                    String drop_off_departure_time = ride.getJSONObject("drop-off-location").getString("departure_time");

//                    if(ride_date.equals(tomorrow_date)){
//
//                        HashMap<String, String> bus = new HashMap<>();
//
//                        bus.put("ride_date", ride_date);
//                        bus.put("vehicle_no_plate", vehicle_no_plate);
//                        bus.put("seats_left", seats_left);
//                        bus.put("route_name", route_id);
//
//                        bus.put("dropoff_location_id", drop_off_stop_id);
//                        bus.put("dropoff_location", drop_off_stop_name);
//                        bus.put("dropoff_location_time", drop_off_duration);
//                        bus.put("departure_time", drop_off_departure_time);
//                        bus.put("dropoff_distance", drop_off_distance);
//
//                        bus.put("pickup_location_id", pick_up_stop_id);
//                        bus.put("pickup_location", pick_up_stop_name);
//                        bus.put("pickup_location_time", pick_up_duration);
//                        bus.put("arrival_time", pick_up_arrival_time);
//                        bus.put("pickup_distance", pick_up_distance);
//
//                        tomorrow_buses.add(bus);
//
//                    }

                }
            }
            else{
                tomorrow_no_ride_tv.setVisibility(TextView.VISIBLE);
//                tomorrow_ride_suggestion_tv.setVisibility(TextView.VISIBLE);
            }
            list_buses.setAdapter(new CustomAdapterActivity(getActivity(), rideArrayList, rides_data));

//            list_buses.setAdapter(new CustomAdapterActivity(getActivity(), tomorrow_buses));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }
}
