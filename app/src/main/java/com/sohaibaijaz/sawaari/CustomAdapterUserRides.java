package com.sohaibaijaz.sawaari;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.Fragments.RidesFragment;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;
import com.sohaibaijaz.sawaari.Settings.Updatepassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CustomAdapterUserRides extends BaseAdapter {


    Activity context;
    ArrayList<HashMap> rides;
    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;
    private static LayoutInflater inflater=null;
    public CustomAdapterUserRides(Activity mainActivity, ArrayList<HashMap> array_rides) {
        // TODO Auto-generated constructor stub

        rides = array_rides;
        context=mainActivity;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return rides.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv_pickup_point;
        TextView tv_dropoff_point;
        TextView tv_res_no;
        TextView tv_date;
        TextView tv_status;
        Button btn_cancel_reservation;
        Button btn_share_ride;

//        TextView tv_ride_fare;
//        TextView tv_booking_id
//        TextView tv_ride_date;
//        Button ride_status_btn;
//        TextView tv_ride_seats;
//        TextView tv_ride_start_time;
//        TextView tv_ride_end_time;
//        TextView tv_pick_up_location;
//        TextView tv_drop_off_location;
    }

    private void openWhatsapp(String ride_share){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        context.startActivity(sendIntent);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.bus_card_user_ride_view, null);
        sharedPreferences = Objects.requireNonNull(context).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(context);
        final String token = sharedPreferences.getString("Token", "");
        holder.tv_date = rowView.findViewById(R.id.tv_date);
        holder.tv_res_no = rowView.findViewById(R.id.tv_res_no);
        holder.tv_dropoff_point = rowView.findViewById(R.id.tv_dropoff_point);
        holder.tv_pickup_point = rowView.findViewById(R.id.tv_pickup_point);
        holder.tv_status = rowView.findViewById(R.id.tv_status);
        holder.btn_cancel_reservation = rowView.findViewById(R.id.btn_cancel_reservation);
        holder.btn_share_ride = rowView.findViewById(R.id.btn_share_ride);

        holder.tv_date.setText(rides.get(position).get("ride_date").toString());
        holder.tv_dropoff_point.setText("Drop off: "+rides.get(position).get("drop-off-point").toString());
        holder.tv_pickup_point.setText("Pick up: "+rides.get(position).get("pick-up-point").toString());
        holder.tv_status.setText(rides.get(position).get("ride_status").toString().toUpperCase());
        holder.tv_res_no.setText("Booking Id: "+rides.get(position).get("booking_id").toString());

        if(rides.get(position).get("ride_status").toString().equals("COMPLETED")
                || rides.get(position).get("ride_status").toString().equals("RIDE CANCELLED")){
            holder.btn_cancel_reservation.setVisibility(View.GONE);
            holder.btn_share_ride.setVisibility(View.GONE);
//            holder.btn_cancel_reservation.setEnabled(false);
//            holder.btn_cancel_reservation.setBackgroundColor(Color.parseColor("#FFA16C6C"));
        }

        holder.btn_share_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent sendIntent = new Intent();
                    String PACKAGE_NAME = "com.sohaibaijaz.sawaari";
//                    Uri imgUri = Uri.parse("android.resource://" + PACKAGE_NAME + "/" + R.mipmap.ic_sawaari);

                    sendIntent.setAction(Intent.ACTION_SEND);
                    String ride_details = "RIDE WITH SAWARI:" +
                            "\nBooking Id: " + rides.get(position).get("booking_id").toString() +
                            "\nRide date: " + rides.get(position).get("ride_date").toString() +
                            "\nPick up: " + rides.get(position).get("pick-up-point").toString() +
                            "\nDrop off: " + rides.get(position).get("drop-off-point").toString();

                    sendIntent.putExtra(Intent.EXTRA_TEXT, ride_details);
                    sendIntent.setType("text/plain");

//                    sendIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
//                    sendIntent.setType("image/png");

                    sendIntent.setPackage("com.whatsapp");
                    context.startActivity(sendIntent);

                }
                catch(Exception e){
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btn_cancel_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String URL = MainActivity.baseurl + "/cancel_ride/";
                    JSONObject jsonBody = new JSONObject();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response){

                            Log.i("VOLLEY", response);
                            try {
                                JSONObject json = new JSONObject(response);

                                if (json.getString("status").equals("200")) {
                                    Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
//                                    UserDetails.getUserRides(context);
                                    RidesFragment ridesFragment = new RidesFragment();
                                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().remove(ridesFragment).commit();
                                } else if (json.getString("status").equals("400") || json.getString("status").equals("404") || json.getString("status").equals("405")) {
//                                    setVisibility(View.GONE);
//                                    btn_share_ride.setVisibility(View.GONE);
                                    Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                Log.e("VOLLEY", e.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                            Log.e("VOLLEY", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("reservation_number", rides.get(position).get("res_no").toString());
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
                    Toast.makeText(context, "Slow Internet Connection.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//

            }
        });
        return rowView;
    }

    public String convertTime(String time_24_hour) {
        String time_formatted = "";
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time_24_hour);
            System.out.println(dateObj);
            time_formatted = new SimpleDateFormat("hh:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();

        }

        return time_formatted;

    }

}



