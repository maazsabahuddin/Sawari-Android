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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CustomAdapterActivity extends BaseAdapter {


    Context context;
    ArrayList<HashMap> rides;

    private static LayoutInflater inflater=null;
    public CustomAdapterActivity(Activity mainActivity, ArrayList<HashMap> array_rides) {
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
        TextView tv_pickup_location;
        TextView tv_dropoff_location;
        TextView tv_seats_left;
        TextView tv_pickup_location_time;
        TextView tv_dropoff_location_time;
        TextView tv_pickup_time;

    }
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.bus_card_view, null);
        holder.tv_pickup_location=(TextView) rowView.findViewById(R.id.tv_pickup_location);
        holder.tv_dropoff_location_time = (TextView) rowView.findViewById(R.id.tv_dropoff_location_time);
        holder.tv_pickup_location_time = (TextView) rowView.findViewById(R.id.tv_pickup_location_time);
        holder.tv_dropoff_location=(TextView) rowView.findViewById(R.id.tv_dropoff_location);
        holder.tv_seats_left = (TextView) rowView.findViewById(R.id.tv_seats_left);
        holder.tv_pickup_time = (TextView) rowView.findViewById(R.id.tv_pickup_time);
        int seats_left = Integer.parseInt(rides.get(position).get("seats_left").toString());
//        int seats_left = 0;
        if ( seats_left == 0){
            holder.tv_seats_left.setTextColor(Color.parseColor("#96281b"));
            holder.tv_seats_left.setText("Fully Booked!");
        }
        else {
            holder.tv_seats_left.setText("Seats left: " + rides.get(position).get("seats_left").toString());
        }
        holder.tv_pickup_time.setText(convertTime(rides.get(position).get("arrival_time").toString()));
        holder.tv_pickup_location.setText("Pickup: "+rides.get(position).get("pickup_location").toString());
        holder.tv_dropoff_location.setText("Drop off: "+rides.get(position).get("dropoff_location").toString());
        holder.tv_pickup_location_time.setText(rides.get(position).get("pickup_location_time").toString()+" from your location");
        holder.tv_dropoff_location_time.setText(rides.get(position).get("dropoff_location_time").toString()+" to "+rides.get(position).get("dropoff_location").toString());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//              Toast.makeText(context, rides.get(position).get("vehicle_no_plate").toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, BookingActivity.class);

                Bundle b = new Bundle();
                b.putString("vehicle_no_plate", rides.get(position).get("vehicle_no_plate").toString());
                b.putString("pickup_location_id", rides.get(position).get("pickup_location_id").toString());
                b.putString("pickup_location", rides.get(position).get("pickup_location").toString());
                b.putString("dropoff_location_id", rides.get(position).get("dropoff_location_id").toString());
                b.putString("dropoff_location", rides.get(position).get("dropoff_location").toString());
                b.putString("dropoff_distance", rides.get(position).get("dropoff_distance").toString());
                b.putString("pickup_distance", rides.get(position).get("pickup_distance").toString());
                i.putExtras(b);
                context.startActivity(i);
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



