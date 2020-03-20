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

import com.sohaibaijaz.sawaari.Fragments.SettingsFragment;
import com.sohaibaijaz.sawaari.R;

public class LocationActivity extends AppCompatActivity {
    public static final String
            KEY_PREF_EXAMPLE_SWITCH = "example_switch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content,
                        new LocationFragment()).commit();
    }
}
