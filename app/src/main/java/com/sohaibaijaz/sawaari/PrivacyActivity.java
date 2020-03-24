package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sohaibaijaz.sawaari.Fragments.Updatepassword;
import com.sohaibaijaz.sawaari.Fragments.Verifypassword;

public class PrivacyActivity extends AppCompatActivity {

    Button button_location;
    Button button_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        getSupportActionBar().hide();
        button_location=findViewById(R.id.location);
        button_delete=findViewById(R.id.delete_account);

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Turn Off Location Sharing");
        builder1.setMessage("Go to your device settings for Sawaari, tap Permissions, then turn off location access.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert11 = builder1.create();

        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alert11.show();

               // Toast.makeText(PrivacyActivity.this, "Nothing", Toast.LENGTH_LONG).show();

            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PrivacyActivity.this, VerifyPassword2_Activity.class);
                PrivacyActivity.this.startActivity(i);
               // Toast.makeText(PrivacyActivity.this, "Nothing", Toast.LENGTH_LONG).show();
            }
        });


    }



}
