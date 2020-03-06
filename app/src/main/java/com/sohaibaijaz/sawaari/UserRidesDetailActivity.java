package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.shreyaspatil.MaterialDialog.interfaces.OnCancelListener;
import com.shreyaspatil.MaterialDialog.interfaces.OnDismissListener;
import com.shreyaspatil.MaterialDialog.interfaces.OnShowListener;

public class UserRidesDetailActivity extends AppCompatActivity implements View.OnClickListener, OnCancelListener, OnDismissListener, OnShowListener{

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
    private TextView vertical_line;
    private Button share_btn;
    private Button cancel_btn;

    private TextView tv_ride_date;
    private TextView arrival_time_tv;
    private TextView departure_time_tv;
    private TextView pick_up_location_tv;
    private TextView drop_off_location_tv;
    private TextView booking_id_tv;
    private TextView bus_details_tv;
    private TextView return_trip_tv;
    private TextView invoice_tv;
//    private BottomSheetMaterialDialog mSimpleBottomSheetDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_rides_detail);
            getSupportActionBar().hide();

            b = getIntent().getExtras();
            back_btn_ride_details = findViewById(R.id.back_btn_ride_details);
            my_ride_details_tv = findViewById(R.id.boarding_pass_details_tv);
            tv_ride_seat_icon = findViewById(R.id.tv_ride_seat_icon);;
            green_icon = findViewById(R.id.green_icon);
            red_icon = findViewById(R.id.red_icon);
            return_trip_icon = findViewById(R.id.return_trip_icon);
            invoice_icon = findViewById(R.id.invoice_icon);
            vertical_line = findViewById(R.id.vertical_line);

            tv_ride_seats = findViewById(R.id.tv_ride_seats);
            share_btn = findViewById(R.id.share_ride_btn);
            cancel_btn = findViewById(R.id.cancel_ride_btn);
            tv_ride_date = findViewById(R.id.tv_ride_date);
            arrival_time_tv = findViewById(R.id.arrival_time_tv);
            departure_time_tv = findViewById(R.id.departure_time_tv);
            pick_up_location_tv = findViewById(R.id.pick_up_location_tv);
            drop_off_location_tv = findViewById(R.id.drop_off_location_tv);
            booking_id_tv = findViewById(R.id.booking_id_tv);
            bus_details_tv = findViewById(R.id.bus_details_tv);
            return_trip_tv = findViewById(R.id.return_trip_tv);
            invoice_tv = findViewById(R.id.invoice_tv);
//            Button mButtonBottomSheetDialog = findViewById(R.id.button_simple_bottomsheet_dialog);

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
            vertical_line.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vertical_line, 0, 0, 0);

            tv_ride_seats.setText(ride_seats);
            tv_ride_date.setText(ride_date);
            arrival_time_tv.setText(ride_start_time);
            departure_time_tv.setText(ride_end_time);
            pick_up_location_tv.setText(ride_pick_up_location);
            drop_off_location_tv.setText(ride_drop_off_location);
            booking_id_tv.setText(ride_booking_id + "    ");
            bus_details_tv.setText(ride_vehicle_no_plate + " Silver Color   ");

//            mSimpleBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
//                    .setTitle("Delete?")
//                    .setMessage("Are you sure want to delete this file?")
//                    .setCancelable(false)
//                    .setPositiveButton("Delete", R.drawable.ic_delete, new BottomSheetMaterialDialog.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
//                            dialogInterface.dismiss();
//                        }
//                    })
//                    .setNegativeButton("Cancel", R.drawable.ic_close, new BottomSheetMaterialDialog.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int which) {
//                            Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
//                            dialogInterface.dismiss();
//                        }
//                    })
//                    .build();

//       mButtonSimpleDialog.setOnClickListener(this);
//        mButtonBottomSheetDialog.setOnClickListener(this);
//       mButtonAnimatedDialog.setOnClickListener(this);
//       mButtonAnimatedBottomSheetDialog.setOnClickListener(this);


            back_btn_ride_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Working on this Activity", Toast.LENGTH_SHORT).show();
                }
            });

            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Working on this Activity", Toast.LENGTH_SHORT).show();
                }
            });

            return_trip_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Working on this Activity", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

//            case R.id.button_simple_bottomsheet_dialog :
//                mSimpleBottomSheetDialog.show();
//                break;

        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }

    @Override
    public void onShow(DialogInterface dialogInterface) {

    }
}
