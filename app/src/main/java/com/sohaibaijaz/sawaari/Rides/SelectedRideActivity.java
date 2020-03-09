package com.sohaibaijaz.sawaari.Rides;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;

public class SelectedRideActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Bundle b;
    private int FARE_PER_SEAT = 50;
    private String FARE_PER_PERSON = (FARE_PER_SEAT + " x 1");
    Context context;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_ride);
        getSupportActionBar().hide();

        final TextView day_and_date = findViewById(R.id.day_and_date);
        final TextView ride_route_name = findViewById(R.id.ride_route_name);
        final TextView ride_seats_left = findViewById(R.id.ride_seats_left);

        final TextView pick_up_time = findViewById(R.id.pick_up_time);
        final TextView pick_up_location_tv = findViewById(R.id.pick_up_location_tv);
        final TextView pick_up_walking_mints = findViewById(R.id.pick_up_walking_mints);

        final TextView drop_off_time = findViewById(R.id.drop_off_time);
        final TextView drop_off_location_tv = findViewById(R.id.drop_off_location_tv);
        final TextView drop_off_walking_mints = findViewById(R.id.drop_off_walking_mints);
        final TextView fare_per_person = findViewById(R.id.fare_per_person);

        final TextView total_fare = findViewById(R.id.total_fare);
        final TextView subtract_seat = findViewById(R.id.subtract_seat);
        final TextView total_seats = findViewById(R.id.total_seats);
        final TextView add_seats = findViewById(R.id.add_seats);
        final TextView payment_method_name = findViewById(R.id.payment_method_name);

        final TextView walking_icon_pick_up = findViewById(R.id.walking_icon_pick_up);
        final TextView walking_icon_drop_off = findViewById(R.id.walking_icon_drop_off);
        final TextView pick_up_circle = findViewById(R.id.pick_up_circle);
        final TextView drop_off_circle = findViewById(R.id.drop_off_circle);
        final TextView dot_line = findViewById(R.id.dot_line);
        final TextView price_method_icon = findViewById(R.id.price_method_icon);
        final Button book_button = findViewById(R.id.book_button);
        final Button back_button = findViewById(R.id.back_btn_ride);

        b = getIntent().getExtras();
        final String vehicle_no_plate = b.getString("vehicle_no_plate");
        final String route_name = b.getString("route_name");
        final String ride_date = b.getString("ride_date");
        final String seats_left = b.getString("seats_left");

        final String pickup_location = b.getString("pickup_location");
        final String pickup_location_id = b.getString("pickup_location_id");
        final String arrival_time = b.getString("arrival_time");
        final String pickup_distance = b.getString("pickup_distance");
        final String pickup_location_time = b.getString("pickup_location_time");

        final String dropoff_location = b.getString("dropoff_location");
        final String dropoff_location_id = b.getString("dropoff_location_id");
        final String dropoff_distance = b.getString("dropoff_distance");
        final String departure_time = b.getString("departure_time");
        final String dropoff_location_time = b.getString("dropoff_location_time");

        sharedPreferences = getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE );
        final String token = sharedPreferences.getString("Token","");

        ride_route_name.setText(route_name);
        ride_seats_left.setText("Remaining Seats " + seats_left);
        day_and_date.setText(ride_date);
        pick_up_location_tv.setText(pickup_location);
        pick_up_time.setText(arrival_time);
        pick_up_walking_mints.setText(pickup_location_time + " walking");
        drop_off_location_tv.setText(dropoff_location);
        drop_off_walking_mints.setText(dropoff_location_time + " walking");
        drop_off_time.setText(departure_time);

        walking_icon_pick_up.setCompoundDrawablesWithIntrinsicBounds(R.drawable.walk, 0, 0, 0);
        walking_icon_drop_off.setCompoundDrawablesWithIntrinsicBounds(R.drawable.walk, 0, 0, 0);
        pick_up_circle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pickupinblue, 0, 0, 0);
        drop_off_circle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dropoffinblue, 0, 0, 0);
//        dot_line.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vertical_line_96, 0, 0, 0);
        price_method_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.money, 0, 0, 0);

        final Spinner spinner = findViewById(R.id.payment_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if(spinner.getSelectedItem().toString().equals("Foree")){
                    payment_method_name.setText(spinner.getSelectedItem().toString()); //+ " 10% off");
                }
                Toast.makeText(SelectedRideActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        subtract_seat.setEnabled(false);
        total_fare.setText(String.valueOf(FARE_PER_SEAT));
        fare_per_person.setText("(" + FARE_PER_PERSON + ")");
        subtract_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seat_value = Integer.parseInt(total_seats.getText().toString());
                String fare_per_person_value;
                if(seat_value==1){
                    fare_per_person_value = FARE_PER_SEAT + " x " + seat_value;
                    fare_per_person. setTextColor(Color.parseColor("#1F3A93"));
                    fare_per_person.setText("(" + fare_per_person_value + ")");
                    total_fare.setText(String.valueOf(FARE_PER_SEAT*seat_value));
                    subtract_seat.setEnabled(false);
                    add_seats.setEnabled(true);
                }
                else if(seat_value==2){
                    seat_value -= 1;
                    fare_per_person_value = FARE_PER_SEAT + " x " + seat_value;
                    fare_per_person. setTextColor(Color.parseColor("#1F3A93"));
                    fare_per_person.setText("(" + fare_per_person_value + ")");
                    total_fare.setText(String.valueOf(FARE_PER_SEAT*seat_value));
                    total_seats.setText(String.valueOf(seat_value));
                    subtract_seat.setEnabled(false);
                    add_seats.setEnabled(true);
                }
                else if(seat_value==3){
                    seat_value -= 1;
                    fare_per_person_value = FARE_PER_SEAT + " x " + seat_value;
                    fare_per_person. setTextColor(Color.parseColor("#1F3A93"));
                    fare_per_person.setText("(" + fare_per_person_value + ")");
                    total_fare.setText(String.valueOf(FARE_PER_SEAT*seat_value));
                    total_seats.setText(String.valueOf(seat_value));
                    add_seats.setEnabled(true);
                }
            }
        });

        add_seats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seat_value = Integer.parseInt(total_seats.getText().toString());
                String fare_per_person_value;
                if(seat_value==3){
                    fare_per_person_value = FARE_PER_SEAT + " x " + seat_value;
                    fare_per_person. setTextColor(Color.parseColor("#1F3A93"));
                    fare_per_person.setText("(" + fare_per_person_value + ")");
                    total_fare.setText(String.valueOf(FARE_PER_SEAT*seat_value));
                    subtract_seat.setEnabled(true);
                    add_seats.setEnabled(false);
                }
                else if(seat_value==1){
                    seat_value += 1;
                    fare_per_person_value = FARE_PER_SEAT + " x " + seat_value;
                    fare_per_person. setTextColor(Color.parseColor("#1F3A93"));
                    fare_per_person.setText("(" + fare_per_person_value + ")");
                    total_fare.setText(String.valueOf(FARE_PER_SEAT*seat_value));
                    total_seats.setText(String.valueOf(seat_value));
                    subtract_seat.setEnabled(true);
                }
                else if(seat_value==2){
                    seat_value += 1;
                    fare_per_person_value = FARE_PER_SEAT + " x " + seat_value;
                    fare_per_person. setTextColor(Color.parseColor("#1F3A93"));
                    fare_per_person.setText("(" + fare_per_person_value + ")");
                    total_fare.setText(String.valueOf(FARE_PER_SEAT*seat_value));
                    total_seats.setText(String.valueOf(seat_value));
                    subtract_seat.setEnabled(true);
                    add_seats.setEnabled(false);
                }
            }
        });

        book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Working Activity", Toast.LENGTH_SHORT).show();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
