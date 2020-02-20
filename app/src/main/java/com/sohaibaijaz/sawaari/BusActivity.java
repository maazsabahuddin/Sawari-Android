package com.sohaibaijaz.sawaari;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sohaibaijaz.sawaari.Fragments.HomeFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BusActivity extends AppCompatActivity {


    ArrayList<HashMap> buses = new ArrayList<HashMap>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        getSupportActionBar().hide();

        Button btn_back = findViewById(R.id.btn_back);
        ListView list_buses = (findViewById(R.id.list_buses));
        TextView ride_txt = findViewById(R.id.ride_txt);
        TextView suggestionTextView = findViewById(R.id.suggestionTextView);
        Bundle b = getIntent().getExtras();
        String rides_data = b.getString("rides");

        try {

            JSONArray rides = new JSONArray(rides_data);
            if(rides.length() == 0 || rides.isNull(0)){
                ride_txt.setVisibility(TextView.VISIBLE);
                suggestionTextView.setVisibility(TextView.VISIBLE);
//                Toast.makeText(getApplicationContext(), "No Rides Available.", Toast.LENGTH_LONG).show();
            }

            else{
                for(int i=0 ; i< rides.length(); i++){
                    JSONObject ride = rides.getJSONObject(i);

                    String vehicle_no_plate = ride.getString("vehicle_no_plate");
                    String seats_left = ride.getString("seats_left");
                    String ride_date = ride.getString("ride_date");

                    String pick_up_stop_id = ride.getJSONObject("pick-up-location").getString("stop_id");
                    String pick_up_stop_name = ride.getJSONObject("pick-up-location").getString("stop_name");
                    String pick_up_duration = ride.getJSONObject("pick-up-location").getString("duration");
                    String pick_up_distance = ride.getJSONObject("pick-up-location").getString("distance");
                    String pick_up_arrival_time = ride.getJSONObject("pick-up-location").getString("arrival_time");

                    String drop_off_stop_id = ride.getJSONObject("drop-off-location").getString("stop_id");
                    String drop_off_stop_name = ride.getJSONObject("drop-off-location").getString("stop_name");
                    String drop_off_duration = ride.getJSONObject("drop-off-location").getString("duration");
                    String drop_off_distance = ride.getJSONObject("drop-off-location").getString("distance");
                    String drop_off_departure_time = ride.getJSONObject("drop-off-location").getString("departure_time");

                    HashMap<String, String> bus = new HashMap<>();
                    bus.put("ride_date", ride_date);
                    bus.put("vehicle_no_plate", vehicle_no_plate);
                    bus.put("seats_left", seats_left);

                    bus.put("dropoff_location_id", drop_off_stop_id);
                    bus.put("dropoff_location", drop_off_stop_name);
                    bus.put("dropoff_location_time", drop_off_duration);
                    bus.put("dropoff_departure_time", drop_off_departure_time);
                    bus.put("dropoff_distance", drop_off_distance);

                    bus.put("pickup_location_id", pick_up_stop_id);
                    bus.put("pickup_location", pick_up_stop_name);
                    bus.put("pickup_location_time", pick_up_duration);
                    bus.put("arrival_time", pick_up_arrival_time);
                    bus.put("pickup_distance", pick_up_distance);

                    buses.add(bus);

                }
            }

            list_buses.setAdapter(new CustomAdapterActivity(this, buses));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                Intent i = new Intent(BusActivity.this, HomeFragment.class);
//                finish();
//                BusActivity.this.startActivity(i);
            }
        });

    }

}

