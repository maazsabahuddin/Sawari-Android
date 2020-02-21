package com.sohaibaijaz.sawaari.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sohaibaijaz.sawaari.CustomAdapterUserRides;
import com.sohaibaijaz.sawaari.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class RidesFragment extends Fragment {

    private View fragmentView;
    private ListView lv_rides;
    private SharedPreferences sharedPreferences;
    private TextView tv_no_reservations;
    private ArrayList<HashMap> rides = new ArrayList<HashMap>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_rides, container, false);

        sharedPreferences = getActivity().getSharedPreferences(AppPreferences, Context.MODE_PRIVATE );
        String user_rides = sharedPreferences.getString("user_rides", "");

        lv_rides = fragmentView.findViewById(R.id.list_rides);
        tv_no_reservations = fragmentView.findViewById(R.id.tv_no_reservations);
        tv_no_reservations.setVisibility(View.GONE);
        System.out.print(user_rides);
        if (user_rides.equals("") || user_rides.equals("[]"))
        {
            tv_no_reservations.setVisibility(View.VISIBLE);
        }
        else{

            try {
                JSONArray jsonArray = new JSONArray(user_rides);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (!jsonObject.toString().equals("{}")) {
                        HashMap<String, String> ride = new HashMap<>();
                        ride.put("res_no", jsonObject.getString("reservation_no"));
                        ride.put("pickup_point", jsonObject.getString("pick_up_point"));
                        ride.put("dropoff_point", jsonObject.getString("drop_off_point"));
                        ride.put("ride_date", jsonObject.getString("ride_date"));
                        ride.put("status", jsonObject.getString("ride_status"));

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
        //On Back pressed
        fragmentView.setFocusableInTouchMode(true);
        fragmentView.requestFocus();
        fragmentView.setOnKeyListener( new View.OnKeyListener()
        {
            int backpress = 0;
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {

                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    backpress = (backpress + 1);
                    Toast.makeText(getContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

                    if (backpress > 2) {
                        getActivity().finish();
                        System.exit(0);
                    }
                    return true;
                }
                return false;
            }
        });

        return fragmentView;
    }


}
