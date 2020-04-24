package com.sohaibaijaz.sawaari.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sohaibaijaz.sawaari.R;

public class PrivacyActivity extends AppCompatActivity {

    Button button_location;
    Button button_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        getSupportActionBar().setTitle("Privacy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button_location=findViewById(R.id.location);
        button_delete=findViewById(R.id.delete_account);

//        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//        builder1.setTitle("Turn Off Location Sharing");
//        builder1.setMessage("Go to your device settings for Sawaari, tap Permissions, then turn off location access.");
//        builder1.setCancelable(true);
//
//        builder1.setPositiveButton(
//                "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        final AlertDialog alert11 = builder1.create();

        final Context context = this;
        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                alert11.show();
                try{
                    new AlertDialog.Builder(context)
                            .setTitle("Turn off Location")
                            .setMessage("Sawari can't go on without the device's Location!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
//                                PrivacyActivity.this.finish();
//                                System.exit(0);
                                }
                            })
                            .setIcon(R.mipmap.alert)
                            .show();
                }
                catch(Exception e){}
               // Toast.makeText(PrivacyActivity.this, "Nothing", Toast.LENGTH_LONG).show();

            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PrivacyActivity.this, Verifypassword.class);
                i.putExtra("coming_from", "privacy_activity");
                PrivacyActivity.this.startActivity(i);
               // SettingsFragment.signout(PrivacyActivity.this);
               // finish();
               // Toast.makeText(PrivacyActivity.this, "Nothing", Toast.LENGTH_LONG).show();
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

