package com.sohaibaijaz.sawaari.Rides;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;
import com.sohaibaijaz.sawaari.Settings.Updatepassword;
import com.sohaibaijaz.sawaari.model.RidesModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class TodayRides extends Fragment {

    private String title;
    private static String json;
    private int page;
    private static HashMap<String, String> pick_up_location = new HashMap<>();
    private static HashMap<String, String> drop_off_location = new HashMap<>();

    // newInstance constructor for creating fragment with arguments
    public static TodayRides newInstanceValues(int page, String title, HashMap<String, String> pick_up_location,
                                               HashMap<String, String> drop_off_location, String json) {

        TodayRides.pick_up_location = pick_up_location;
        TodayRides.drop_off_location = drop_off_location;
        TodayRides.json = json;

        TodayRides today = new TodayRides();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        today.setArguments(args);
        return today;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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

//        getRoute();
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

            list_buses.setAdapter(new CustomAdapterActivity(getActivity(), rideArrayList, json, pick_up_location, drop_off_location));
        }

        catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    public void getRoute(){

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("Token", "");
        try {

            String URL = MainActivity.baseurl + "/bus/route/";
            JSONObject jsonBody = new JSONObject();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("VOLLEY", response);
                    try {
                        JSONObject jsonObj = new JSONObject(response);

                        if (jsonObj.getString("status").equals("200")) {
                            json = jsonObj.toString();
                        }
                        else if (jsonObj.getString("status").equals("400")) {
                            Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        else if(jsonObj.getString("status").equals("404")){
                            Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                            SettingsFragment.signout(getActivity());
                            // flag = false;
                        }


                    } catch (JSONException e) {
                        Log.e("VOLLEY", e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("start_lat", pick_up_location.get("latitude"));
                    params.put("start_lon", pick_up_location.get("longitude"));
                    params.put("stop_lat", drop_off_location.get("latitude"));
                    params.put("stop_lon", drop_off_location.get("longitude"));

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", token);
                    return headers;
                }
            };

//            stringRequest.setRetryPolicy(new RetryPolicy() {
//                @Override
//                public int getCurrentTimeout() {
//                    return 500;
//                }
//
//                @Override
//                public int getCurrentRetryCount() {
//                    return 500;
//                }
//
//                @Override
//                public void retry(VolleyError error) throws VolleyError {
//
//                }
//            });

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Slow Internet Connection.", Toast.LENGTH_SHORT).show();
        }



    }

}
