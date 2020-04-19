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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sohaibaijaz.sawaari.R;

public class LocationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_location);
       // getSupportActionBar().hide();
       // if (savedInstanceState == null) {
       //     getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new LocationFragment()).commit();
//            //  navigationView.setCheckedItem(R.id.nav_home);
     //  }
        // create a FragmentManager
       // FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
       // FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
       // fragmentTransaction.replace(R.id.fragment_container2,  new LocationFragment());
        //fragmentTransaction.commit(); // save the changes
//       getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragment_container2,
//                        new LocationFragment()).commit();
    }
}
