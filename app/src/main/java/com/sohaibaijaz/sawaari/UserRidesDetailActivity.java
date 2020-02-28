package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sohaibaijaz.sawaari.Fragments.RideFragmentN;

import java.util.Objects;

public class UserRidesDetailActivity extends AppCompatActivity {

    private Bundle b;
    Context context;
    private Button back_btn_ride_details;
    private TextView my_ride_details_tv;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rides_detail);
        getSupportActionBar().hide();

        b = getIntent().getExtras();
        back_btn_ride_details = findViewById(R.id.back_btn_ride_details);
        my_ride_details_tv = findViewById(R.id.my_ride_details_tv);

        final String ride_status = b.getString("ride_status");
        final String ride_fare = b.getString("ride_fare");
        final String ride_date = b.getString("ride_date");
        final String ride_start_time = b.getString("ride_start_time");
        final String ride_end_time = b.getString("ride_end_time");
        final String ride_pick_up_location = b.getString("ride_pick_up_location");
        final String ride_drop_off_location = b.getString("ride_drop_off_location");
        final String ride_booking_id = b.getString("ride_booking_id");
        final String ride_seats = b.getString("ride_seats");
        final String ride_vehicle_no_plate = b.getString("ride_vehicle_no_plate");

        my_ride_details_tv.setText("Boarding Pass");

        back_btn_ride_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                startActivity(new Intent(getApplicationContext(), RideFragmentN.class));
//                Intent i = new Intent(getApplicationContext(), RideFragmentN.class);
//                context.startActivity(i);
            }
        });

    }
}
