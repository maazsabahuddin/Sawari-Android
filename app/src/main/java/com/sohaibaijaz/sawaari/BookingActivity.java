package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    private FrameLayout spinner_frame;
    private ProgressBar spinner;
    private FrameLayout confirm_frame;
    private FrameLayout confirmation_frame;
    private String[] list = {"Cash", "Card"};
    private EditText txt_no_of_seats;
    private Spinner spinner_payment_tpye;
    private Button btn_book_seats;
    private  SharedPreferences sharedPreferences;
    private  Bundle b;
    private RequestQueue requestQueue;
    private CardView cardConfirm;
    private CardView confirmationCard;
    private Button btn_confirm_booking;
    private TextView tv_reservation_number;
    private TextView tv_pickup_point;
    private TextView tv_dropoff_point;
    private TextView tv_price_per_km;
    private TextView tv_total_fare;
    private Button btn_confirmation_ok;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getSupportActionBar().hide();

        requestQueue = Volley.newRequestQueue(this);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner_frame = findViewById(R.id.spinner_frame);
        spinner_frame.setVisibility(View.GONE);
        confirm_frame = findViewById(R.id.confirm_frame);
        confirm_frame.setVisibility(View.GONE);
        cardConfirm = confirm_frame.findViewById(R.id.confirmCard);
        btn_confirm_booking = cardConfirm.findViewById(R.id.btn_confirm_booking);

        confirmation_frame = (FrameLayout) findViewById(R.id.confirmation_frame);
        confirmationCard = (CardView) findViewById(R.id.confirmationCard);
        confirmation_frame.setVisibility(View.GONE);
        tv_dropoff_point = (TextView) confirmationCard.findViewById(R.id.tv_dropoff_point);
        tv_pickup_point = (TextView) confirmationCard.findViewById(R.id.tv_pickup_point);
        tv_reservation_number = (TextView) confirmationCard.findViewById(R.id.tv_reservation_number);
        tv_price_per_km = (TextView) confirmationCard.findViewById(R.id.tv_price_per_km);
        tv_total_fare = (TextView ) confirmationCard.findViewById(R.id.tv_fare);
        btn_confirmation_ok = (Button) confirmationCard.findViewById(R.id.btn_ok_confirmation);

        sharedPreferences = getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE );
        txt_no_of_seats = (EditText) findViewById(R.id.txt_no_of_seats);
        spinner_payment_tpye = (Spinner) findViewById(R.id.spinner_payment_type);
        btn_book_seats = (Button) findViewById(R.id.btn_book_seats);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,list);
        spinner_payment_tpye.setAdapter(adapter);
         b = getIntent().getExtras();
        final String vehicle_no_plate = b.getString("vehicle_no_plate");
        final String pickup_location = b.getString("pickup_location");
        String picup_location_id = b.getString("pickup_location_id");

        final String dropoff_location = b.getString("dropoff_location");
        String dropoff_location_id = b.getString("dropoff_location_id");

        final String token = sharedPreferences.getString("Token","");

       // Toast.makeText(getApplicationContext(), vehicle_no_plate+"\n"+pickup_location+"\n"+picup_location_id+"\n"+pickup_distance+"\n"+dropoff_location+"\n"+dropoff_location_id+"\n"+dropoff_distance, Toast.LENGTH_LONG).show();

        btn_book_seats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String no_of_seats = txt_no_of_seats.getText().toString();
                final String payment_type = spinner_payment_tpye.getSelectedItem().toString();

                String pickup_distance = b.getString("pickup_distance");
                pickup_distance  = pickup_distance.substring(0, pickup_distance.indexOf(" "));

                final String kilometer = pickup_distance;
                String dropoff_distance = b.getString("dropoff_distance");
                dropoff_distance = dropoff_distance.substring(0, dropoff_distance.indexOf(" "));

                if (no_of_seats.equals("") || (payment_type.equals(""))){
                    Toast.makeText(getApplicationContext(), "Enter number of seats and select a payment method!", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        String URL = MainActivity.baseurl+"/book_ride/";
                        spinner.setVisibility(View.VISIBLE);
                        spinner_frame.setVisibility(View.VISIBLE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                spinner.setVisibility(View.GONE);
                                spinner_frame.setVisibility(View.GONE);

                                Log.i("VOLLEY", response.toString());
                                try {
                                    final JSONObject json = new JSONObject(response);
                                    if (json.getString("status").equals("200")) {

                                        //Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                                        confirm_frame.setVisibility(View.VISIBLE);
                                        btn_confirm_booking.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                try {
                                                    String URL = MainActivity.baseurl+"/confirm_ride/";
                                                    final String reservation_number = json.getString("reservation_number");
                                                    JSONObject jsonBody = new JSONObject();
                                                    jsonBody.put("authorization", token);
                                                    final String requestBody = jsonBody.toString();
                                                    spinner.setVisibility(View.VISIBLE);
                                                    spinner_frame.setVisibility(View.VISIBLE);
                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            spinner.setVisibility(View.GONE);
                                                            spinner_frame.setVisibility(View.GONE);
                                                            confirm_frame.setVisibility(View.GONE);
                                                            Log.i("VOLLEY", response.toString());
                                                            try {
                                                                JSONObject json = new JSONObject(response);
                                                                if (json.getString("status").equals("200")) {
                                                                    System.out.println(json.getString("status"));
                                                                    //Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                                                    confirmation_frame.setVisibility(View.VISIBLE);
                                                                    tv_reservation_number.setText("Reservation no: "+json.getString("Reservation Number"));
                                                                    tv_pickup_point.setText("Pickup point: "+json.getString("Pick-up point"));
                                                                    tv_dropoff_point.setText("Drop off point: "+json.getString("Drop-up point"));
                                                                    tv_price_per_km.setText("Price per km: "+ json.getString("price_per_km")+" rupees");
                                                                    tv_total_fare.setText("Total fare: "+json.getString("Fare")+" rupees");
                                                                    btn_confirmation_ok.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            confirmation_frame.setVisibility(View.GONE);
                                                                            finish();
                                                                            finish();
                                                                        }
                                                                    });
                                                                }
                                                                else if (json.getString("status").equals("400")||json.getString("status").equals("404")) {
                                                                    System.out.println(json.getString("status"));
                                                                    Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                                                }

                                                            } catch (JSONException e) {
                                                                Log.e("VOLLEY", e.toString());

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
                                                            params.put("reservation_number", reservation_number);
                                                            return params;
                                                        }

                                                        @Override
                                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                                            Map<String, String>  params = new HashMap<String, String>();
                                                            params.put("authorization", token);
                                                            return params;
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

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });

                                    }
                                    else if (json.getString("status").equals("400")||json.getString("status").equals("404")) {
                                        Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_LONG).show();

                                    }
                                } catch (JSONException e) {
                                    Log.e("VOLLEY", e.toString());

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
                                params.put("vehicle_no_plate",vehicle_no_plate);
                                params.put("req_seats",no_of_seats);
                                params.put("pick_up_point", pickup_location);
                                params.put("drop_up_point", dropoff_location);
                                params.put("kilometer", kilometer);
                                params.put("payment_method", payment_type);

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
            }
        });



    }

}
