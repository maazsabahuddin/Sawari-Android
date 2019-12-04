package com.sohaibaijaz.sawaari;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class CustomAdapterActivity extends BaseAdapter {

    String [] bus_no;
    Context context;
    String [] seats_left;
    private static LayoutInflater inflater=null;
    public CustomAdapterActivity(Activity mainActivity, String[] array_bus_no, String[] array_seats_left) {
        // TODO Auto-generated constructor stub
        bus_no =array_bus_no;
        context=mainActivity;
        seats_left=array_seats_left;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bus_no.length;
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
        TextView tv_bus_no;
        TextView tv_seats_left;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.bus_card_view, null);
        holder.tv_bus_no=(TextView) rowView.findViewById(R.id.tv_bus_no);
        holder.tv_seats_left = (TextView) rowView.findViewById(R.id.tv_seats_left);
        holder.tv_bus_no.setText("Bus no: "+bus_no[position]);
        holder.tv_seats_left.setText("Seats left: "+seats_left[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+bus_no[position], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }


}
