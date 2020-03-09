package com.sohaibaijaz.sawaari.Rides;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sohaibaijaz.sawaari.R;

public class ConfirmRideBooking extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_book_activity);
        getSupportActionBar().hide();

        TextView ride_date = findViewById(R.id.ride_date);
        TextView seat_icon = findViewById(R.id.seat_icon);
        TextView seat_value = findViewById(R.id.seat_value);

        TextView pick_up_point = findViewById(R.id.pick_up_point);
        TextView pick_up_icon = findViewById(R.id.pick_up_icon);
        TextView pick_up_time = findViewById(R.id.pick_up_time);

        TextView drop_off_point = findViewById(R.id.drop_off_point);
        TextView drop_off_icon = findViewById(R.id.drop_off_icon);
        TextView drop_off_time = findViewById(R.id.drop_off_time);

        TextView vehicle_details = findViewById(R.id.vehicle_details);
        TextView fare_per_person = findViewById(R.id.fare_per_person);
        TextView total_fare_value = findViewById(R.id.total_fare_value);
        TextView add_promo_code = findViewById(R.id.add_promo_code);

        TextView back_button_final_ride_details_activity = findViewById(R.id.back_button_final_ride_details_activity);

        seat_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.blue_seat_icon_40px, 0, 0, 0);
        pick_up_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pickupinblue, 0, 0, 0);
        drop_off_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dropoffinblue, 0, 0, 0);

        back_button_final_ride_details_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
