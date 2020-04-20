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

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sohaibaijaz.sawaari.R;

import java.util.HashMap;

public class LocationActivity extends AppCompatActivity {

    private HashMap<String, String> currentLocation = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getSupportActionBar().hide();

        Bundle b = getIntent().getExtras();
        if (b.getSerializable("pick_up_location") != null) {
           currentLocation = (HashMap<String, String>) b.getSerializable("pick_up_location");
           // Toast.makeText(this,currentLocation.get("longitude"), Toast.LENGTH_SHORT).show();
        }
        if (savedInstanceState == null) {
            Fragment fragment = new LocationFragment();
               Bundle arguments = new Bundle();
                arguments.putSerializable("pick_up_location" , currentLocation);
                fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.place_fragment, fragment).commit();
        }

    }
}
