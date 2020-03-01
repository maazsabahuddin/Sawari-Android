package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserRidesDetailActivity extends AppCompatActivity {

    private Bundle b;
    Context context;
    private Button back_btn_ride_details;
    private TextView my_ride_details_tv;
    private TextView tv_ride_seats;
    private TextView tv_ride_seat_icon;
    private TextView green_icon;
    private TextView red_icon;
    private TextView return_trip_icon;
    private TextView invoice_icon;
    private TextView share_btn_icon;
    private TextView cancel_btn_icon;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rides_detail);
        getSupportActionBar().hide();

        b = getIntent().getExtras();
        back_btn_ride_details = findViewById(R.id.back_btn_ride_details);
        my_ride_details_tv = findViewById(R.id.boarding_pass_details_tv);
        tv_ride_seat_icon = findViewById(R.id.tv_ride_seat_icon);
        tv_ride_seats = findViewById(R.id.tv_ride_seats);
        green_icon = findViewById(R.id.green_icon);
        red_icon = findViewById(R.id.red_icon);
        return_trip_icon = findViewById(R.id.return_trip_icon);
        invoice_icon = findViewById(R.id.invoice_icon);
        share_btn_icon = findViewById(R.id.share_btn_icon);
        cancel_btn_icon = findViewById(R.id.cancel_btn_icon);

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
        tv_ride_seat_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.seatinblue, 0, 0, 0);
        green_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greencircle, 0, 0, 0);
        red_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_circle, 0, 0, 0);
        return_trip_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shareicon, 0, 0, 0);
        invoice_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.invoiceicon, 0, 0, 0);
        share_btn_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shareicon, 0, 0, 0);
        cancel_btn_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cancelbtnicon, 0, 0, 0);
        tv_ride_seats.setText(ride_seats);

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
