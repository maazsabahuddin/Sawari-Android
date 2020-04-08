package com.sohaibaijaz.sawaari.Rides;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.CustomPreviewUserRidesHistory;
import com.sohaibaijaz.sawaari.Fragments.AccountFragment;
import com.sohaibaijaz.sawaari.Fragments.RideFragmentN;
import com.sohaibaijaz.sawaari.Fragments.ride_scheduled;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.UserDetails;
import com.sohaibaijaz.sawaari.UserRidesDetailActivity;
import com.sohaibaijaz.sawaari.VolleyRequestSingletonClass.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmRideBooking extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Bundle b;
    private FrameLayout spinner_frame;
    private ProgressBar spinner;
    private ArrayAdapter<CharSequence> adapter;
    Context context;
    private String user_json_response;
    ArrayList<HashMap<String, String>> ride_booking_details = new ArrayList<HashMap<String, String>>();
    private int seat_value;

    @SuppressWarnings("unchecked")
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_ride_book_activity);
        getSupportActionBar().hide();

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        TextView ride_date = findViewById(R.id.ride_date);
//        final TextView seat_icon = findViewById(R.id.seat_icon);
        TextView bus_remaining_seats = findViewById(R.id.bus_remaining_seats);

        TextView pick_up_stop_duration = findViewById(R.id.pick_up_stop_duration);
        TextView pick_up_point = findViewById(R.id.pick_up_point);
//        TextView pick_up_icon = findViewById(R.id.pick_up_icon);
        TextView pick_up_time = findViewById(R.id.pick_up_time);

        TextView drop_off_stop_duration = findViewById(R.id.drop_off_stop_duration);
        TextView drop_off_point = findViewById(R.id.drop_off_point);
//        TextView drop_off_icon = findViewById(R.id.drop_off_icon);
        TextView drop_off_time = findViewById(R.id.drop_off_time);

//        TextView vehicle_details = findViewById(R.id.vehicle_details);
        TextView route_id = findViewById(R.id.route_id);
        final TextView kilometer = findViewById(R.id.kilometer_tv);
        final TextView fare_per_person = findViewById(R.id.fare_per_person);

        final TextView subtract_seat = findViewById(R.id.subtract_seat);
        final TextView total_seats = findViewById(R.id.seat_value);
        final TextView add_seat = findViewById(R.id.add_seat);

        final TextView total_fare_value = findViewById(R.id.total_fare_value);
        TextView add_promo_code = findViewById(R.id.add_promo_code);

        Button confirm_ride_button = findViewById(R.id.confirm_ride_button);

//        final TextView walking_icon_pick_up = findViewById(R.id.walk_icon_pickup);
//        final TextView walking_icon_drop_off = findViewById(R.id.walk_icon_drop_off);

        TextView back_button_final_ride_details_activity = findViewById(R.id.back_button_final_ride_details_activity);

        spinner = findViewById(R.id.progressBar_ride_booking_activity);
        spinner_frame = findViewById(R.id.spinner_frame_ride_booking_activity);

        spinner.setVisibility(View.GONE);
        spinner_frame.setVisibility(View.GONE);

//        seat_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.seat_icon_32px, 0, 0, 0);
//        pick_up_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pickupinblue, 0, 0, 0);
//        drop_off_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dropoffinblue, 0, 0, 0);
//        walking_icon_pick_up.setCompoundDrawablesWithIntrinsicBounds(R.drawable.walk, 0, 0, 0);
//        walking_icon_drop_off.setCompoundDrawablesWithIntrinsicBounds(R.drawable.walk, 0, 0, 0);

        ride_booking_details = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("selected_ride");
        user_json_response = getIntent().getStringExtra("json");

        sharedPreferences = getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE );
        final String token = sharedPreferences.getString("Token","");

        ride_date.setText(ride_booking_details.get(0).get("ride_date"));
//        vehicle_details.setText(ride_booking_details.get(0).get("vehicle_no_plate") + " Silver");
        bus_remaining_seats.setText(ride_booking_details.get(0).get("seats_left"));
        pick_up_point.setText(ride_booking_details.get(0).get("pick_up_stop_name"));
        pick_up_time.setText(ride_booking_details.get(0).get("arrival_time"));
        drop_off_point.setText(ride_booking_details.get(0).get("drop_off_stop_name"));
        total_fare_value.setText(ride_booking_details.get(0).get("fare_per_person"));
        fare_per_person.setText(ride_booking_details.get(0).get("fare_per_person") + " x 1" );
        drop_off_time.setText(ride_booking_details.get(0).get("departure_time"));
        route_id.setText(ride_booking_details.get(0).get("route_name"));
        kilometer.setText(ride_booking_details.get(0).get("kilometer"));
        subtract_seat.setEnabled(false);

        pick_up_stop_duration.setText(ride_booking_details.get(0).get("pick_up_location_duration") + " from your location.");
        drop_off_stop_duration.setText(ride_booking_details.get(0).get("drop_off_location_duration") + " to your location.");

        final Spinner payment_spinner = findViewById(R.id.payment_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        payment_spinner.setAdapter(adapter);


        back_button_final_ride_details_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seat_value = Integer.parseInt(total_seats.getText().toString());

                if(seat_value==3){
                    subtract_seat.setEnabled(true);
                    add_seat.setEnabled(false);
                    Toast.makeText(ConfirmRideBooking.this, "You cannot book more than 3 seats.", Toast.LENGTH_LONG).show();
                }
                else{
                    seat_value += 1;
                    try {

                        spinner.setVisibility(View.VISIBLE);
                        spinner_frame.setVisibility(View.VISIBLE);

                        String URL = MainActivity.baseurl+"/calculate/fare/";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.i("VOLLEY", response);
                                try {
                                    final JSONObject json = new JSONObject(response);
                                    if (json.getString("status").equals("200")) {

                                        if(seat_value==2){
                                            subtract_seat.setEnabled(true);
                                            add_seat.setEnabled(true);
                                        }
                                        else if(seat_value==3){
                                            subtract_seat.setEnabled(true);
                                            add_seat.setEnabled(false);
                                        }

                                        fare_per_person.setText(ride_booking_details.get(0).get("fare_per_person") + " x " + seat_value);
                                        total_seats.setText(String.valueOf(seat_value));
                                        total_fare_value.setText(json.getString("total_fare"));

                                        spinner.setVisibility(View.GONE);
                                        spinner_frame.setVisibility(View.GONE);

                                    }
                                    else if (json.getString("status").equals("500")) {
                                        Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                                        spinner.setVisibility(View.VISIBLE);
                                        spinner_frame.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    spinner.setVisibility(View.GONE);
                                    spinner_frame.setVisibility(View.GONE);
                                    Log.e("VOLLEY", e.toString());
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                spinner.setVisibility(View.GONE);
                                spinner_frame.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                                Log.e("VOLLEY", error.toString());
                            }
                        }){
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<>();
                                params.put("total_seats", String.valueOf(seat_value));
                                params.put("kilometer", ride_booking_details.get(0).get("kilometer"));
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
//                        MySingleton.getInstance(ConfirmRideBooking.this).addToRequestQueue(stringRequest);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        subtract_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seat_value = Integer.parseInt(total_seats.getText().toString());

                if(seat_value==1){
                    subtract_seat.setEnabled(false);
                    add_seat.setEnabled(true);
//                    Toast.makeText(ConfirmRideBooking.this, "You cannot book more than 3 seats.", Toast.LENGTH_LONG).show();
                }
                else{
                    seat_value -= 1;
                    try {

                        spinner.setVisibility(View.VISIBLE);
                        spinner_frame.setVisibility(View.VISIBLE);

                        String URL = MainActivity.baseurl+"/calculate/fare/";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

//                            Toast.makeText(getApplicationContext(), "Getting response", Toast.LENGTH_LONG).show();
                                Log.i("VOLLEY", response);
                                try {

                                    final JSONObject json = new JSONObject(response);
                                    if (json.getString("status").equals("200")) {

                                        if(seat_value==1){
                                            subtract_seat.setEnabled(false);
                                            add_seat.setEnabled(true);
                                        }
                                        else if(seat_value==2){
                                            subtract_seat.setEnabled(true);
                                            add_seat.setEnabled(true);
                                        }

                                        fare_per_person.setText(ride_booking_details.get(0).get("fare_per_person") + " x " + seat_value);
                                        total_seats.setText(String.valueOf(seat_value));
                                        total_fare_value.setText(json.getString("total_fare"));

                                        spinner.setVisibility(View.GONE);
                                        spinner_frame.setVisibility(View.GONE);
                                    }
                                    else if (json.getString("status").equals("500")) {
                                        spinner.setVisibility(View.GONE);
                                        spinner_frame.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    spinner.setVisibility(View.GONE);
                                    spinner_frame.setVisibility(View.GONE);
                                    Log.e("VOLLEY", e.toString());
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                spinner.setVisibility(View.GONE);
                                spinner_frame.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                                Log.e("VOLLEY", error.toString());
                            }
                        }){
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<>();
                                params.put("total_seats", String.valueOf(seat_value));
                                params.put("kilometer", ride_booking_details.get(0).get("kilometer"));
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
//                        MySingleton.getInstance(ConfirmRideBooking.this).addToRequestQueue(stringRequest);
                        requestQueue.add(stringRequest);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        confirm_ride_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String URL = MainActivity.baseurl + "/confirm/book/ride/";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                        spinner.setVisibility(View.VISIBLE);
                        spinner_frame.setVisibility(View.VISIBLE);

                            Log.i("VOLLEY", response);
                            try {
                                final JSONObject json = new JSONObject(response);
                                if (json.getString("status").equals("200")) {

                                    UserDetails.getUserRides(ConfirmRideBooking.this);
                                    Toast.makeText(ConfirmRideBooking.this,"Your ride is confirmed.", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(ConfirmRideBooking.this, UserRidesDetailActivity.class);

                                    Bundle b = new Bundle();
                                    b.putString("ride_status", json.getString("ride_status"));
                                    b.putString("ride_fare", json.getString("fare"));
                                    b.putString("ride_date", json.getString("ride_date"));
                                    b.putString("ride_start_time", json.getString("pick_up_time"));
                                    b.putString("ride_end_time", json.getString("drop_off_time"));
                                    b.putString("ride_pick_up_location", json.getString("pick-up-point"));
                                    b.putString("ride_drop_off_location", json.getString("drop-off-point"));
                                    b.putString("ride_date", json.getString("ride_date"));
                                    b.putString("ride_booking_id", json.getString("reservation_number"));
                                    b.putString("ride_seats", json.getString("seats"));
                                    b.putString("ride_vehicle_no_plate", json.getString("vehicle_no_plate"));
                                    b.putString("coming_from","Booking");

                                    i.putExtras(b);
                                    spinner.setVisibility(View.GONE);
                                    spinner_frame.setVisibility(View.GONE);
                                    ConfirmRideBooking.this.startActivity(i);
                                    finish();
                                }
                                else if (json.getString("status").equals("501")) {
                                    Toast.makeText(getApplicationContext(), "ForeePay coming soon!", Toast.LENGTH_LONG).show();
                                    spinner.setVisibility(View.GONE);
                                    spinner_frame.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                spinner.setVisibility(View.GONE);
                                spinner_frame.setVisibility(View.GONE);
                                Log.e("VOLLEY", e.toString());
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            spinner.setVisibility(View.GONE);
                            spinner_frame.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                            Log.e("VOLLEY", error.toString());
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();

                            params.put("vehicle_no_plate", ride_booking_details.get(0).get("vehicle_no_plate"));
                            params.put("req_seats", total_seats.getText().toString());
                            params.put("route_id", ride_booking_details.get(0).get("route_name"));
                            params.put("ride_date", ride_booking_details.get(0).get("ride_date"));
                            params.put("ride_start_time", ride_booking_details.get(0).get("ride_start_time"));
                            params.put("pick_up_stop_id", ride_booking_details.get(0).get("pick_up_stop_id"));
                            params.put("drop_off_stop_id", ride_booking_details.get(0).get("drop_off_stop_id"));
                            params.put("arrival_time", ride_booking_details.get(0).get("arrival_time"));
                            params.put("departure_time", ride_booking_details.get(0).get("departure_time"));
                            params.put("payment_method", payment_spinner.getSelectedItem().toString());
                            params.put("fare_per_person", ride_booking_details.get(0).get("fare_per_person"));
                            params.put("kilometer", ride_booking_details.get(0).get("kilometer"));
                            params.put("total_fare", total_fare_value.getText().toString());
                            params.put("fare_per_km", ride_booking_details.get(0).get("fare_per_km"));

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
//                    MySingleton.getInstance(ConfirmRideBooking.this).addToRequestQueue(stringRequest);
                    requestQueue.add(stringRequest);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
