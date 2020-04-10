package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sohaibaijaz.sawaari.Fragments.HomeFragment;
import com.sohaibaijaz.sawaari.Fragments.RideFragmentN;
import com.sohaibaijaz.sawaari.Maps.AddPlaceFragment;

public class UserRidesDetailActivity extends AppCompatActivity{

    private Bundle b;
    Context context;

    private String coming_from;
    Button button_back_home;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_rides_detail);
            getSupportActionBar().hide();
//            getSupportActionBar().setTitle("Boarding Pass");
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            b = getIntent().getExtras();
            //ImageView back_button_user_ride_detail = findViewById(R.id.back_button_user_ride_detail);
            TextView user_booked_seats = findViewById(R.id.user_booked_seats);
            TextView ride_date_tv = findViewById(R.id.ride_date);

            button_back_home=findViewById(R.id.back_button_boardingpass);
//            TextView pick_up_icon = findViewById(R.id.pick_up_icon);
            TextView pick_up_time = findViewById(R.id.pick_up_time);
            TextView pick_up_point = findViewById(R.id.pick_up_point);

//            TextView drop_off_icon = findViewById(R.id.drop_off_icon);
            TextView drop_off_time = findViewById(R.id.drop_off_time);
            TextView drop_off_point = findViewById(R.id.drop_off_point);

            TextView booking_number = findViewById(R.id.booking_number);
            TextView vehicle_no_plate = findViewById(R.id.vehicle_no_plate);
            TextView fare_per_person = findViewById(R.id.fare_per_person);

//            TextView return_trip_icon = findViewById(R.id.return_trip_icon);
            TextView return_trip = findViewById(R.id.return_trip);

//            TextView invoice_icon = findViewById(R.id.invoice_icon);
            TextView invoice = findViewById(R.id.invoice);

            ImageView find_stop_icon = findViewById(R.id.find_stop_icon);
            ImageView share_ride_icon = findViewById(R.id.share_ride_icon);
            ImageView cancel_ride_button_icon = findViewById(R.id.cancel_ride_icon);

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

            coming_from=b.getString("coming_from");
//            back_button_user_ride_detail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.blue_back_icon, 0, 0, 0);
//            invoice_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.invoiceicon, 0, 0, 0);
//            return_trip_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pickupinblue, 0, 0, 0);
//            pick_up_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_circle_10px, 0, 0, 0);
//            drop_off_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_circle_10px, 0, 0, 0);
//            find_stop_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.find_stop_icon, 0, 0, 0);
//            share_ride_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.share_icon_48px, 0, 0, 0);
//            cancel_ride_button_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.x_mark_3_40, 0, 0, 0);

            user_booked_seats.setText(ride_seats);
            ride_date_tv.setText(ride_date);
            pick_up_time.setText(ride_start_time);
            drop_off_time.setText(ride_end_time);
            pick_up_point.setText(ride_pick_up_location);
            drop_off_point.setText(ride_drop_off_location);
            booking_number.setText(ride_booking_id);
            vehicle_no_plate.setText(ride_vehicle_no_plate + " SILVER");

            find_stop_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UserRidesDetailActivity.this, "Working", Toast.LENGTH_LONG).show();
                }
            });

            share_ride_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UserRidesDetailActivity.this, "Working", Toast.LENGTH_LONG).show();
                }
            });

            cancel_ride_button_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UserRidesDetailActivity.this, "Working", Toast.LENGTH_LONG).show();
                }
            });

            return_trip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UserRidesDetailActivity.this, "Working", Toast.LENGTH_LONG).show();
                }
            });

            invoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UserRidesDetailActivity.this, "Working", Toast.LENGTH_LONG).show();
                }
            });

//            back_button_user_ride_detail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    onBackPressed();
//                    Intent i = new Intent(UserRidesDetailActivity.this, RideFragmentN.class);
//                    startActivity(i);
//                }
//            });

            button_back_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


//                    HomeFragment newFragment = new HomeFragment();
//                    FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.fragment_container, newFragment);
//                    transaction.addToBackStack(null);
//                    // placeType = "Home";
//                    transaction.commit();
                    if(coming_from== null) {
                        Intent i = new Intent(UserRidesDetailActivity.this, RideFragmentN.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(UserRidesDetailActivity.this, NavActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
//                Intent intent = new Intent(UserRidesDetailActivity.this, NavActivity.class);
//                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
