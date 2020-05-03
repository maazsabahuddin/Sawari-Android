package com.sohaibaijaz.sawaari.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sohaibaijaz.sawaari.CustomAdapterUserRides;
import com.sohaibaijaz.sawaari.CustomPreviewUserRidesHistory;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.UserDetails;
import com.sohaibaijaz.sawaari.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.sohaibaijaz.sawaari.Fragments.HomeFragment.isNetworkAvailable;
import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class ride_history extends Fragment {

    private String title;
    private int page;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isNetworkAvailable(Objects.requireNonNull(getActivity()))){
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private ListView lv_rides;
    private SharedPreferences sharedPreferences;
    private TextView history_no_trips_tv;
    private Button history_book_ride_btn;
    private FrameLayout spinner_frame_my_rides_history;
    private ProgressBar progressBar_my_rides_history;
    private ArrayList<HashMap> rides = new ArrayList<HashMap>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_ride, container, false);

        lv_rides = view.findViewById(R.id.history_rides_listView);
        history_no_trips_tv = view.findViewById(R.id.past_trips_textview);
        history_book_ride_btn = view.findViewById(R.id.book_trip_button);
        spinner_frame_my_rides_history = view.findViewById(R.id.spinner_frame_my_rides_history);
        progressBar_my_rides_history = view.findViewById(R.id.progressBar_my_rides_history);

        history_no_trips_tv.setVisibility(View.GONE);
        history_book_ride_btn.setVisibility(View.GONE);

        if(!isNetworkAvailable(Objects.requireNonNull(getActivity()))){
            history_no_trips_tv.setVisibility(View.GONE);
            history_book_ride_btn.setVisibility(View.GONE);
            spinner_frame_my_rides_history.setVisibility(View.GONE);
            progressBar_my_rides_history.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else{
            UserDetails.getUserRides(getActivity(), new CallBack() {
                @Override
                public void onSuccess(String status_code, String message) {
                    spinner_frame_my_rides_history.setVisibility(View.GONE);
                    progressBar_my_rides_history.setVisibility(View.GONE);

                    sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(AppPreferences, Context.MODE_PRIVATE );
                    String user_rides = sharedPreferences.getString("user_rides", "");

                    boolean flag = false;
                    System.out.print(user_rides);
                    if (user_rides.equals("") || user_rides.equals("[]"))
                    {
                        history_no_trips_tv.setVisibility(View.VISIBLE);
                        history_book_ride_btn.setVisibility(View.VISIBLE);
                        lv_rides.setVisibility(View.GONE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("user_rides");
                        editor.apply();
                    }
                    else{
                        try {
                            JSONArray jsonArray = new JSONArray(user_rides);
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (!jsonObject.toString().equals("{}")) {

                                    if(!(jsonObject.getString("ride_status").equals("ACTIVE"))){
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
                                        ride.put("vehicle_no_plate", jsonObject.getString("vehicle_no_plate"));

                                        flag = true;
                                        rides.add(ride);
                                    }
                                }
                            }

                            if(flag) {
                                CustomPreviewUserRidesHistory adapterUserRides = new CustomPreviewUserRidesHistory(getActivity(), rides);
                                lv_rides.setAdapter(adapterUserRides);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("user_rides");
                                editor.apply();
                            }
                            else{
                                history_no_trips_tv.setVisibility(View.VISIBLE);
                                history_book_ride_btn.setVisibility(View.VISIBLE);
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(String status_code, String message) {
                    spinner_frame_my_rides_history.setVisibility(View.GONE);
                    progressBar_my_rides_history.setVisibility(View.GONE);
                }
            });

        }
        return view;
    }

}
