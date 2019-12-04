package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        getSupportActionBar().hide();

        ListView list_buses = (findViewById(R.id.list_buses));
        String[] bus_no = {"ABC-123", "BED-112", "DSA-321"};
        String[] seats_left = {"40", "30", "20"};


        list_buses.setAdapter(new CustomAdapterActivity(this, bus_no, seats_left));


    }
}
