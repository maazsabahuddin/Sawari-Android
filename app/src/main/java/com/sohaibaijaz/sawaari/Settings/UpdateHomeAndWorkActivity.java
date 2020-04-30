package com.sohaibaijaz.sawaari.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sohaibaijaz.sawaari.Maps.LocationFragment;
import com.sohaibaijaz.sawaari.R;

public class UpdateHomeAndWorkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_home_work);
        getSupportActionBar().hide();
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Fragment fragment = new UpdateHomeWorkFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.HomeWorkFragment, fragment).commit();
        }

        TextView BackLA = findViewById(R.id.BackHomeWorkButton);
        BackLA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                //finish();
            }
        });
    }
}
