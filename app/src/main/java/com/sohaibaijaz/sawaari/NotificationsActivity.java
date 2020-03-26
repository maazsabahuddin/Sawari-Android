package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class NotificationsActivity extends AppCompatActivity {

    Button button_account_trip;
    Button button_discount_news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_account_trip=findViewById(R.id.accountandtrip);
        button_discount_news=findViewById(R.id.discountsandnews);

        button_account_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(NotificationsActivity.this, PushNotificationsActivity.class);
                startActivity(i);
            }
        });

        button_discount_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(NotificationsActivity.this, PushNotificationsActivity.class);
                startActivity(i);
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
