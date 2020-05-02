/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sohaibaijaz.sawaari.Maps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.Settings.PrivacyActivity;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;
import com.sohaibaijaz.sawaari.Settings.UpdateHomeAndWorkActivity;

import java.util.HashMap;

public class LocationActivity extends AppCompatActivity {

    private HashMap<String, String> currentLocation = new HashMap<>();
    private String activity;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getSupportActionBar().hide();

        Bundle b = getIntent().getExtras();
        activity = b.getString("activity");
        value = b.getString("value");
        if (b.getSerializable("currentLocation") != null) {
           currentLocation = (HashMap<String, String>) b.getSerializable("currentLocation");
        }

        if (value.equals("Whereto")) {
            Fragment fragment = new LocationFragment();
                Bundle arguments = new Bundle();
                arguments.putSerializable("currentLocation" , currentLocation);
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().replace(R.id.place_fragment, fragment).commit();
        }
        else if (value.equals("Home")) {
            Fragment fragment = new AddPlaceFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable("currentLocation" , currentLocation);
            arguments.putString("value", value);
            arguments.putString("activity", activity);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.place_fragment, fragment).commit();
        }
        else if (value.equals("UpdateHome")) {
            Fragment fragment = new AddPlaceFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable("currentLocation" , currentLocation);
            arguments.putString("value", value);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.place_fragment, fragment).commit();
        }
        else if (value.equals("Work")) {
            Fragment fragment = new AddPlaceFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable("currentLocation" , currentLocation);
            arguments.putString("value", value);
            arguments.putString("activity", activity);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.place_fragment, fragment).commit();
        }
        else if (value.equals("UpdateWork")) {
            Fragment fragment = new AddPlaceFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable("currentLocation" , currentLocation);
            arguments.putString("value", value);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.place_fragment, fragment).commit();
        }
        else if (value.equals("AddPlace")) {
            Fragment fragment = new AddPlaceFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable("currentLocation" , currentLocation);
            arguments.putString("value", value);
            arguments.putString("activity", activity);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.place_fragment, fragment).commit();
        }
        else if (value.equals("SavePlace")) {
            Fragment fragment = new Fragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable("currentLocation" , currentLocation);
            arguments.putString("value", value);
            arguments.putString("activity", activity);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.place_fragment, fragment).commit();
        }


        TextView BackLA = findViewById(R.id.BackLA);
        BackLA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.equals("HomeFragment")){
                    Intent i = new Intent(getApplicationContext(), NavActivity.class);
                    LocationActivity.this.startActivity(i);
                }
                else if(activity.equals("UpdateLocationFragment")){
//                    Intent i = new Intent(getApplicationContext(), UpdateHomeAndWorkActivity.class);
//                    LocationActivity.this.startActivity(i);
                    onBackPressed();
                   // finish();
                }
                else{
                    onBackPressed();
                }
            }
        });
    }
}
