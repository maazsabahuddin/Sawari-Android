package com.sohaibaijaz.sawaari;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CustomPreviewUserRidesHistory extends BaseAdapter {


    Context context;
    ArrayList<HashMap> rides;
    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;
    private static LayoutInflater inflater=null;

    public CustomPreviewUserRidesHistory(Activity rides_activity, ArrayList<HashMap> array_rides){

        this.rides = array_rides;
        this.context = rides_activity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return rides.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tv_ride_fare;
        TextView tv_booking_id;
        TextView tv_ride_date;
        Button ride_status_btn;
        TextView tv_ride_seats;
        TextView tv_ride_start_time;
        TextView tv_ride_end_time;
        TextView tv_pick_up_location;
        TextView tv_drop_off_location;
        TextView seat_icon;
        TextView pick_up_icon;
        TextView drop_off_icon;
        TextView rebook_ride_icon;
        TextView tv_notrips;
        Button book_ride_btn;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.user_rides_cardview, null);
        sharedPreferences = Objects.requireNonNull(context).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(context);

        final String token = sharedPreferences.getString("token", "");

        holder.ride_status_btn = rowView.findViewById(R.id.ride_status_btn);
        holder.tv_ride_fare = rowView.findViewById(R.id.tv_ride_fare);
        holder.tv_booking_id = rowView.findViewById(R.id.tv_booking_id);
        holder.tv_ride_date = rowView.findViewById(R.id.tv_date);
        holder.tv_ride_seats = rowView.findViewById(R.id.tv_seats);
        holder.tv_ride_start_time = rowView.findViewById(R.id.tv_ride_start_time);
        holder.tv_ride_end_time = rowView.findViewById(R.id.tv_ride_end_time);
        holder.tv_pick_up_location = rowView.findViewById(R.id.tv_pick_up_location);
        holder.tv_drop_off_location = rowView.findViewById(R.id.tv_drop_off_location);
        holder.seat_icon = rowView.findViewById(R.id.tv_seaticon);
        holder.drop_off_icon = rowView.findViewById(R.id.drop_off_icon);
        holder.rebook_ride_icon = rowView.findViewById(R.id.rebook_ride_icon);
        holder.pick_up_icon = rowView.findViewById(R.id.pick_up_icon);

        holder.tv_notrips = rowView.findViewById(R.id.past_trips_textview);
        holder.book_ride_btn = rowView.findViewById(R.id.book_trip_button);

        JSONArray ride = new JSONArray(rides);
        if(ride.length() == 0 || ride.isNull(0)){
            holder.tv_notrips.setVisibility(View.VISIBLE);
            holder.book_ride_btn.setVisibility(View.VISIBLE);
        }

        else{
            try{
                String ride_status = Objects.requireNonNull(rides.get(position).get("ride_status")).toString();
                String ride_fare = Objects.requireNonNull(rides.get(position).get("fare")).toString();
                String ride_date = Objects.requireNonNull(rides.get(position).get("ride_date")).toString();
                String ride_start_time = Objects.requireNonNull(rides.get(position).get("pick_up_time")).toString();
                String ride_end_time = Objects.requireNonNull(rides.get(position).get("drop_off_time")).toString();
                String ride_pick_up_location = Objects.requireNonNull(rides.get(position).get("pick_up_point")).toString();
                String ride_drop_off_location = Objects.requireNonNull(rides.get(position).get("drop_off_point")).toString();
                String ride_booking_id = Objects.requireNonNull(rides.get(position).get("booking_id")).toString();
                String ride_seats = Objects.requireNonNull(rides.get(position).get("seats")).toString();

                if(Objects.requireNonNull(rides.get(position).get("ride_status")).toString().equals("COMPLETED")
                        || Objects.requireNonNull(rides.get(position).get("ride_status")).toString().equals("RIDE CANCELLED")){

                    holder.ride_status_btn.setText(ride_status);
                    holder.tv_ride_fare.setText("Fare " + ride_fare);
                    holder.tv_ride_date.setText(ride_date);
                    holder.tv_booking_id.setText("Booking ID: " + ride_booking_id);
                    holder.tv_ride_seats.setText(ride_seats);
                    holder.tv_ride_start_time.setText(ride_start_time);
                    holder.tv_ride_end_time.setText(ride_end_time);
                    holder.tv_pick_up_location.setText(ride_pick_up_location);
                    holder.tv_drop_off_location.setText(ride_drop_off_location);

                    holder.seat_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.seatinblue, 0, 0, 0);
                    holder.pick_up_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pickupinblue, 0, 0, 0);
                    holder.drop_off_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dropoffinblue, 0, 0, 0);
                    holder.rebook_ride_icon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.businblue, 0, 0, 0);

                }
            }
            catch (java.lang.NullPointerException e){
                Log.d("onActivityResult()", "Something went wrong, some data is null");
            }
        }


        return rowView;
    }
}
