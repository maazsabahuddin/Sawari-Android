package com.sohaibaijaz.sawaari.Rides;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.BookingActivity;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;
import com.sohaibaijaz.sawaari.Settings.Updatepassword;
import com.sohaibaijaz.sawaari.UserDetails;
import com.sohaibaijaz.sawaari.model.Ride;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectedRideActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private Bundle b;
    ArrayList<HashMap<String, String>> ride;
    private int FARE_PER_SEAT = 50;
    private String FARE_PER_PERSON = (FARE_PER_SEAT + " x 1");
    Context context;
    private String user_json_response;

    @SuppressWarnings("unchecked")
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_ride);
        requestQueue = Volley.newRequestQueue(this);
        getSupportActionBar().hide();

//        final ArrayList<HashMap> rides;

        final TextView day_and_date = findViewById(R.id.day_and_date);
        final TextView ride_route_name = findViewById(R.id.ride_route_name);
        final TextView ride_seats_left = findViewById(R.id.ride_seats_left);

        final TextView arrival_time_tv = findViewById(R.id.pick_up_time);
        final TextView pick_up_location_tv = findViewById(R.id.pick_up_location_tv);
        final TextView pick_up_walking_mints = findViewById(R.id.pick_up_walking_mints);

        final TextView drop_off_time = findViewById(R.id.drop_off_time);
        final TextView drop_off_location_tv = findViewById(R.id.drop_off_location_tv);
        final TextView drop_off_walking_mints = findViewById(R.id.drop_off_walking_mints);
        final TextView seats_icon = findViewById(R.id.seats_icon);

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

        sharedPreferences = getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE );
        final String token = sharedPreferences.getString("Token","");

//        Ride ride = (Ride) getIntent().getSerializableExtra("rides");

        ride = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("selected_ride");
        user_json_response = getIntent().getStringExtra("json");
//        final String vehicle_no_plate = ride.get(0).get("vehicle_no_plate");
//        final String ride_date = ride.get(0).get("ride_date");
//        final String route_name = ride.get(0).get("route_name");
//        final String seats_left = ride.get(0).get("seats_left");
//
//        final String pick_up_stop_name = ride.get(0).get("pick_up_stop_name");
//        final String pick_up_location_distance = ride.get(0).get("pick_up_location_distance");
//        final String pick_up_location_duration = ride.get(0).get("pick_up_location_duration");
//        final String arrival_time = ride.get(0).get("arrival_time");
//        final String pick_up_stop_id = ride.get(0).get("pick_up_stop_id");
//
//        final String drop_off_stop_name = ride.get(0).get("drop_off_stop_name");
//        final String drop_off_location_distance = ride.get(0).get("drop_off_location_distance");
//        final String drop_off_location_duration = ride.get(0).get("drop_off_location_duration");
//        final String departure_time = ride.get(0).get("departure_time");
//        final String drop_off_stop_id = ride.get(0).get("drop_off_stop_id");

        ride_route_name.setText(ride.get(0).get("route_name"));
        ride_seats_left.setText("Remaining Seats " + ride.get(0).get("seats_left"));
        day_and_date.setText(ride.get(0).get("ride_date"));
        pick_up_location_tv.setText(ride.get(0).get("pick_up_stop_name"));
        arrival_time_tv.setText(ride.get(0).get("arrival_time"));
        pick_up_walking_mints.setText(ride.get(0).get("pick_up_location_duration") + " walking");
        drop_off_location_tv.setText(ride.get(0).get("drop_off_stop_name"));
        drop_off_walking_mints.setText(ride.get(0).get("drop_off_location_duration") + " walking");
        drop_off_time.setText(ride.get(0).get("departure_time"));

        walking_icon_pick_up.setCompoundDrawablesWithIntrinsicBounds(R.drawable.walk, 0, 0, 0);
        walking_icon_drop_off.setCompoundDrawablesWithIntrinsicBounds(R.drawable.walk, 0, 0, 0);
        pick_up_circle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pickupinblue, 0, 0, 0);
        drop_off_circle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dropoffinblue, 0, 0, 0);
//        seats_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.blue_seat_icon_40px, 0, 0, 0);
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
//        total_fare.setText(String.valueOf(FARE_PER_SEAT));
//        fare_per_person.setText("(" + FARE_PER_PERSON + ")");
        subtract_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seat_value = Integer.parseInt(total_seats.getText().toString());
                String fare_per_person_value;
                if(seat_value==1){
//                    fare_per_person_value = FARE_PER_SEAT + " x " + seat_value;
//                    fare_per_person. setTextColor(Color.parseColor("#1F3A93"));
//                    fare_per_person.setText("(" + fare_per_person_value + ")");
//                    total_fare.setText(String.valueOf(FARE_PER_SEAT*seat_value));
                    subtract_seat.setEnabled(false);
                    add_seats.setEnabled(true);
                }
                else if(seat_value==2){
                    seat_value -= 1;
//                    fare_per_person_value = FARE_PER_SEAT + " x " + seat_value;
//                    fare_per_person. setTextColor(Color.parseColor("#1F3A93"));
//                    fare_per_person.setText("(" + fare_per_person_value + ")");
//                    total_fare.setText(String.valueOf(FARE_PER_SEAT*seat_value));
                    total_seats.setText(String.valueOf(seat_value));
                    subtract_seat.setEnabled(false);
                    add_seats.setEnabled(true);
                }
                else if(seat_value==3){
                    seat_value -= 1;
//                    fare_per_person_value = FARE_PER_SEAT + " x " + seat_value;
//                    fare_per_person. setTextColor(Color.parseColor("#1F3A93"));
//                    fare_per_person.setText("(" + fare_per_person_value + ")");
//                    total_fare.setText(String.valueOf(FARE_PER_SEAT*seat_value));
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
//                    fare_per_person_value = FARE_PER_SEAT + " x " + seat_value;
//                    fare_per_person. setTextColor(Color.parseColor("#1F3A93"));
//                    fare_per_person.setText("(" + fare_per_person_value + ")");
//                    total_fare.setText(String.valueOf(FARE_PER_SEAT*seat_value));
                    subtract_seat.setEnabled(true);
                    add_seats.setEnabled(false);
                }
                else if(seat_value==1){
                    seat_value += 1;
//                    fare_per_person_value = FARE_PER_SEAT + " x " + seat_value;
//                    fare_per_person. setTextColor(Color.parseColor("#1F3A93"));
//                    fare_per_person.setText("(" + fare_per_person_value + ")");
//                    total_fare.setText(String.valueOf(FARE_PER_SEAT*seat_value));
                    total_seats.setText(String.valueOf(seat_value));
                    subtract_seat.setEnabled(true);
                }
                else if(seat_value==2){
                    seat_value += 1;
//                    fare_per_person_value = FARE_PER_SEAT + " x " + seat_value;
//                    fare_per_person. setTextColor(Color.parseColor("#1F3A93"));
//                    fare_per_person.setText("(" + fare_per_person_value + ")");
//                    total_fare.setText(String.valueOf(FARE_PER_SEAT*seat_value));
                    total_seats.setText(String.valueOf(seat_value));
                    subtract_seat.setEnabled(true);
                    add_seats.setEnabled(false);
                }
            }
        });

        book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String no_of_seats = total_seats.getText().toString();
                final String payment_type = spinner.getSelectedItem().toString();

//                Toast.makeText(getApplicationContext(), "Hello I\'m in", Toast.LENGTH_LONG).show();

                try {
                    String URL = MainActivity.baseurl+"/book_ride/";
//                    spinner.setVisibility(View.VISIBLE);
//                    spinner_frame.setVisibility(View.VISIBLE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
//                            spinner.setVisibility(View.GONE);
//                            spinner_frame.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Getting response", Toast.LENGTH_LONG).show();
                            Log.i("VOLLEY", response.toString());
                            try {
                                final JSONObject json = new JSONObject(response);
                                if (json.getString("status").equals("200")) {

                                    Intent intent = new Intent(SelectedRideActivity.this, ConfirmRideBooking.class);

                                    ArrayList<HashMap<String, String>> ride_booking_details = new ArrayList<HashMap<String, String>>();
                                    HashMap<String, String> ride_booking_details_hashmap = new HashMap<String, String>();

                                    ride_booking_details_hashmap.put("reservation_number", json.get("reservation_number").toString());
                                    ride_booking_details_hashmap.put("vehicle_no_plate", json.get("vehicle").toString());
                                    ride_booking_details_hashmap.put("ride_date", ride.get(0).get("ride_date"));
                                    ride_booking_details_hashmap.put("route_name", ride.get(0).get("route_name"));
                                    ride_booking_details_hashmap.put("seats_left", json.get("seats").toString());

                                    ride_booking_details_hashmap.put("fare_per_person", json.get("fare_per_person").toString());
                                    ride_booking_details_hashmap.put("fare", json.get("fare").toString());
                                    ride_booking_details_hashmap.put("price_per_km", json.get("price_per_km").toString());
                                    ride_booking_details_hashmap.put("kilometer", json.get("kilometer").toString());

                                    ride_booking_details_hashmap.put("pick_up_stop_name", json.get("pick-up-point").toString());
                                    ride_booking_details_hashmap.put("arrival_time", json.get("pick_up_time").toString());

                                    ride_booking_details_hashmap.put("drop_off_stop_name", json.get("drop-off-point").toString());
                                    ride_booking_details_hashmap.put("departure_time", json.get("drop_off_time").toString());

                                    ride_booking_details.add(ride_booking_details_hashmap);

                                    intent.putExtra("json", user_json_response);
                                    intent.putExtra("ride_booking_details", ride_booking_details);

                                    SelectedRideActivity.this.startActivity(intent);
                                }
                                else if (json.getString("status").equals("400")) {
                                    Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                                }
                                else if(json.getString("status").equals("404")){
                                    Toast.makeText(SelectedRideActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                    SettingsFragment.signout(SelectedRideActivity.this);
                                    // flag = false;
                                }
                            } catch (JSONException e) {
                                Log.e("VOLLEY", e.toString());
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            spinner.setVisibility(View.GONE);
//                            spinner_frame.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                            Log.e("VOLLEY", error.toString());
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("vehicle_no_plate", ride.get(0).get("vehicle_no_plate"));
                            params.put("req_seats",no_of_seats);
                            params.put("pick_up_point_stop_id", ride.get(0).get("pick_up_stop_id"));
                            params.put("drop_up_point_stop_id", ride.get(0).get("drop_off_stop_id"));
                            params.put("payment_method", payment_type);
                            params.put("ride_date", ride.get(0).get("ride_date"));
                            params.put("route_id", ride.get(0).get("route_name"));
                            params.put("arrival_time", ride.get(0).get("arrival_time"));
                            params.put("departure_time", ride.get(0).get("departure_time"));
//                                params.put(KEY_EMAIL, email);
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("Authorization", token);
                            return headers;
                        }
                    };

                    stringRequest.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                            return 50000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 50000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });
                    requestQueue.add(stringRequest);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void RideBook(View view)
    {
        Toast.makeText(getApplicationContext(), "Helllo", Toast.LENGTH_LONG).show();
    }

}
