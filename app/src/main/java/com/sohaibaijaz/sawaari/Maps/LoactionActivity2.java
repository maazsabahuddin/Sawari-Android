package com.sohaibaijaz.sawaari.Maps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sohaibaijaz.sawaari.R;

public class LoactionActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_loaction2);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container3, new LocationFragment()).commit();
          //  navigationView.setCheckedItem(R.id.nav_home);
        }

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container3,
//                        new LocationFragment()).commit();
    }
}
