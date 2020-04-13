package com.sohaibaijaz.sawaari;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sohaibaijaz.sawaari.Rides.ConfirmRideBooking;
import com.sohaibaijaz.sawaari.Rides.SelectedRideActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class CustomAdapterActivity extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> rides;
    String json;

    private static HashMap<String, String> currentLocation = new HashMap<>();
    private static HashMap<String, String> dropoffLocation = new HashMap<>();

    private static LayoutInflater inflater=null;
    public CustomAdapterActivity(Context activity, ArrayList<HashMap<String, String>> array_rides, String json_response,
                                 HashMap<String, String> pick_up_location, HashMap<String, String> drop_off_location) {
        // TODO Auto-generated constructor stub
        CustomAdapterActivity.currentLocation = pick_up_location;
        CustomAdapterActivity.dropoffLocation = drop_off_location;
        rides = array_rides;
        context = activity;
        json = json_response;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        TextView pick_up_stop_name;
        TextView pick_up_duration_from_home_to_stop;
        TextView pick_up_time;
        TextView drop_off_time;

        TextView drop_off_stop_name;
        TextView drop_off_duration_from_home_stop;

        TextView tv_seats_left;
    }
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.bus_new_card_view, null);

        holder.pick_up_stop_name=rowView.findViewById(R.id.pick_up_stop_name);
        holder.pick_up_duration_from_home_to_stop = rowView.findViewById(R.id.pick_up_duration);
        holder.pick_up_time = rowView.findViewById(R.id.pick_up_time);
        holder.drop_off_time = rowView.findViewById(R.id.drop_off_time);
        holder.drop_off_stop_name = rowView.findViewById(R.id.drop_off_stop_name);
        holder.drop_off_duration_from_home_stop = rowView.findViewById(R.id.drop_off_duration);

        holder.tv_seats_left = rowView.findViewById(R.id.tv_seats_left);
        int seats_left = Integer.parseInt(rides.get(position).get("seats_left"));

        if (seats_left == 0){
            holder.tv_seats_left.setTextColor(Color.parseColor("#96281b"));
            holder.tv_seats_left.setText("Fully Booked!");
        }
        else {

            holder.tv_seats_left.setText("Seats left: " + rides.get(position).get("seats_left"));
            holder.pick_up_stop_name.setText(rides.get(position).get("pick_up_stop_name"));
            holder.pick_up_time.setText(rides.get(position).get("arrival_time"));
            holder.pick_up_duration_from_home_to_stop.setText(rides.get(position).get("pick_up_location_duration") + " from your location");

            holder.drop_off_stop_name.setText(rides.get(position).get("drop_off_stop_name"));
            holder.drop_off_time.setText(rides.get(position).get("departure_time"));
            holder.drop_off_duration_from_home_stop.setText(rides.get(position).get("drop_off_location_duration") + " to "+ rides.get(position).get("drop_off_stop_name"));
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try{
                    //              Toast.makeText(context, rides.get(position).get("vehicle_no_plate").toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, ConfirmRideBooking.class);

                    ArrayList<HashMap<String, String>> selected_ride = new ArrayList<HashMap<String, String>>();
                    HashMap<String, String> selected_ride_hashmap = new HashMap<String, String>();

                    selected_ride_hashmap.put("vehicle_no_plate", rides.get(position).get("vehicle_no_plate"));
                    selected_ride_hashmap.put("ride_date", rides.get(position).get("ride_date"));
                    selected_ride_hashmap.put("route_name", rides.get(position).get("route_name"));
                    selected_ride_hashmap.put("seats_left", rides.get(position).get("seats_left"));
                    selected_ride_hashmap.put("kilometer", rides.get(position).get("kilometer"));
                    selected_ride_hashmap.put("fare_per_km", rides.get(position).get("fare_per_km"));
                    selected_ride_hashmap.put("fare_per_person", rides.get(position).get("fare_per_person"));
                    selected_ride_hashmap.put("ride_start_time", rides.get(position).get("ride_start_time"));

                    selected_ride_hashmap.put("pick_up_stop_name", rides.get(position).get("pick_up_stop_name"));
                    selected_ride_hashmap.put("pick_up_location_distance", rides.get(position).get("pick_up_location_distance"));
                    selected_ride_hashmap.put("pick_up_location_duration", rides.get(position).get("pick_up_location_duration"));
                    selected_ride_hashmap.put("arrival_time", rides.get(position).get("arrival_time"));
                    selected_ride_hashmap.put("pick_up_stop_id", rides.get(position).get("pick_up_stop_id"));

                    selected_ride_hashmap.put("drop_off_stop_name", rides.get(position).get("drop_off_stop_name"));
                    selected_ride_hashmap.put("drop_off_location_distance", rides.get(position).get("drop_off_location_distance"));
                    selected_ride_hashmap.put("drop_off_location_duration", rides.get(position).get("drop_off_location_duration"));
                    selected_ride_hashmap.put("departure_time", rides.get(position).get("departure_time"));
                    selected_ride_hashmap.put("drop_off_stop_id", rides.get(position).get("drop_off_stop_id"));

                    selected_ride.add(selected_ride_hashmap);
                    intent.putExtra("selected_ride", selected_ride);
                    intent.putExtra("pick_up_location", currentLocation);
                    intent.putExtra("drop_off_location", dropoffLocation);
                    intent.putExtra("json", json);

                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
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
    }
    catch (final ParseException e) {
        e.printStackTrace();
    }

    return time_formatted;

}

}



