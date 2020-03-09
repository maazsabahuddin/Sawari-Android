package com.sohaibaijaz.sawaari.Rides;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;

public class ConfirmRideBooking extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private Bundle b;
    Context context;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_book_activity);
        getSupportActionBar().hide();

        requestQueue = Volley.newRequestQueue(this);

        final TextView ride_date_tv = findViewById(R.id.ride_date);
        final TextView seat_icon = findViewById(R.id.seat_icon);
        final TextView seat_value = findViewById(R.id.seat_value);

        final TextView pick_up_point = findViewById(R.id.pick_up_point);
        final TextView pick_up_icon = findViewById(R.id.pick_up_icon);
        final TextView pick_up_time = findViewById(R.id.pick_up_time);

        final TextView drop_off_point = findViewById(R.id.drop_off_point);
        final TextView drop_off_icon = findViewById(R.id.drop_off_icon);
        final TextView drop_off_time = findViewById(R.id.drop_off_time);

        final TextView vehicle_details = findViewById(R.id.vehicle_details);
        final TextView fare_per_person_tv = findViewById(R.id.fare_per_person);
        final TextView total_fare_value = findViewById(R.id.total_fare_value);
        final TextView add_promo_code = findViewById(R.id.add_promo_code);
        final Button confirm_ride_button = findViewById(R.id.confirm_ride_button);

        final TextView back_button_final_ride_details_activity = findViewById(R.id.back_button_final_ride_details_activity);

        seat_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.blue_seat_icon_40px, 0, 0, 0);
        pick_up_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pickupinblue, 0, 0, 0);
        drop_off_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dropoffinblue, 0, 0, 0);

        b = getIntent().getExtras();
        final String reservation_number = b.getString("reservation_number");
        final String vehicle_no_plate = b.getString("vehicle_no_plate");
        final String route_name = b.getString("route_name");
        final String ride_date = b.getString("ride_date");
        final String seats_left = b.getString("seats_left");

        final String fare_per_person = b.getString("fare_per_person");
        final String fare = b.getString("fare");
        final String kilometer = b.getString("kilometer");
        final String price_per_km = b.getString("price_per_km");

        final String pickup_location = b.getString("pickup_location");
        final String arrival_time = b.getString("arrival_time");

        final String dropoff_location = b.getString("dropoff_location");
        final String departure_time = b.getString("departure_time");

        sharedPreferences = getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE );
        final String token = sharedPreferences.getString("Token","");

        ride_date_tv.setText(ride_date);
        vehicle_details.setText(vehicle_no_plate + " Silver");
        seat_value.setText(" " + seats_left);
        pick_up_point.setText(pickup_location);
        pick_up_time.setText(arrival_time);
        drop_off_point.setText(dropoff_location);
        total_fare_value.setText(fare);
        fare_per_person_tv.setText(fare_per_person);
        drop_off_time.setText(departure_time);

        back_button_final_ride_details_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        confirm_ride_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_LONG).show();
            }
        });
    }
}
