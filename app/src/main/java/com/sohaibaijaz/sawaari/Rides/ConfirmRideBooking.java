package com.sohaibaijaz.sawaari.Rides;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;

public class ConfirmRideBooking extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private Bundle b;
    Context context;
    private String user_json_response;
    ArrayList<HashMap<String, String>> ride_booking_details = new ArrayList<HashMap<String, String>>();

    @SuppressWarnings("unchecked")
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

        ride_booking_details = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("ride_booking_details");
        user_json_response = getIntent().getStringExtra("json");

        final String reservation_number = ride_booking_details.get(0).get("reservation_number");

        sharedPreferences = getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE );
        final String token = sharedPreferences.getString("Token","");

        ride_date_tv.setText(ride_booking_details.get(0).get("ride_date"));
        vehicle_details.setText(ride_booking_details.get(0).get("vehicle_no_plate") + " Silver");
        seat_value.setText(" " + ride_booking_details.get(0).get("seats_left"));
        pick_up_point.setText(ride_booking_details.get(0).get("pick_up_stop_name"));
        pick_up_time.setText(ride_booking_details.get(0).get("arrival_time"));
        drop_off_point.setText(ride_booking_details.get(0).get("drop_off_stop_name"));
        total_fare_value.setText(ride_booking_details.get(0).get("fare"));
        fare_per_person_tv.setText(ride_booking_details.get(0).get("fare_per_person"));
        drop_off_time.setText(ride_booking_details.get(0).get("departure_time"));

        back_button_final_ride_details_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ConfirmRideBooking.this, show_rides.class);
                i.putExtra("json", user_json_response);
                ConfirmRideBooking.this.startActivity(i);

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
