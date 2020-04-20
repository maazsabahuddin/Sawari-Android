package com.sohaibaijaz.sawaari;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.sohaibaijaz.sawaari.Fragments.HomeFragment;
import com.sohaibaijaz.sawaari.Fragments.RideFragmentN;
import com.sohaibaijaz.sawaari.Settings.NotificationsActivity;
import com.sohaibaijaz.sawaari.Settings.SettingsActivity;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;
import com.sohaibaijaz.sawaari.Settings.UpdateActivity;
import com.sohaibaijaz.sawaari.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private RequestQueue requestQueue;
    private FrameLayout spinner_frame;
    private ProgressBar spinner;
    private NavigationView navView;
    private ImageView profileImage;
    private String firstName;
    private String lastName;
    private  ActionBarDrawerToggle toggle;
    //public  String check;
    public static String MAP_VIEW_BUNDLE_KEY;
    TextView textViewN;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);

        MAP_VIEW_BUNDLE_KEY = getText(R.string.GOOGLE_MAP_API_KEY).toString();

        drawer = findViewById(R.id.drawer_layout);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner_frame = findViewById(R.id.spinner_frame);
        spinner_frame.setVisibility(View.GONE);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(false);
       // getSupportActionBar().setDisplayShowHomeEnabled(false);

       final NavigationView navigationView = findViewById(R.id.nav_view);
      //  TextView navTitle = (TextView) findViewById(R.id.titleN);



        navigationView.setNavigationItemSelectedListener(this);

        //TextView textView= findViewById(R.id.);

        //textView.setText("pagal");
 //       User user= User.getInstance();
//        firstName=user.getFirstName();
//        lastName=user.getLastName();
//        firstName = firstName + " " + lastName;

        //check= SettingsFragment.firstName;
       //TextView textViewN= navigationView.getHeaderView(0).findViewById(R.id.titleN);
        textViewN= navigationView.getHeaderView(0).findViewById(R.id.titleN);

        textViewN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavActivity.this, UpdateActivity.class);
                NavActivity.this.startActivity(i);
                drawer.closeDrawer(GravityCompat.START);


            }
        });
       // textViewN.setText(firstName);
        sharedPreferences = getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE );
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                toggle.setDrawerIndicatorEnabled(true);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                User user= User.getInstance();
               firstName=user.getFirstName();
                lastName=user.getLastName();
                firstName = firstName + " " + lastName;
                toggle.setDrawerIndicatorEnabled(false);

                textViewN.setText(firstName);
            }
        };
       // toggle.setDrawerIndicatorEnabled(false);
        drawer.addDrawerListener(toggle);
       // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.syncState();



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }




    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        final String token = sharedPreferences.getString("Token", "");
        switch (menuItem.getItemId()) {

            case R.id.nav_home:
//                startActivity(new Intent(getApplicationContext(), EmptyFragment.class));
//                HomeFragment homeFragment = new HomeFragment();
                    onBackPressed();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                break;

            case R.id.nav_account:
//                AccountFragment accountFragment = new AccountFragment();
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, accountFragment).commit();

                break;
            case R.id.nav_ride:
                RideFragmentN ridesFragment = new RideFragmentN();

//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ridesFragment).commit();
                startActivity(new Intent(getApplicationContext(), RideFragmentN.class));
                break;
            case R.id.nav_message:
              //  MessageFragment messageFragment = new MessageFragment();
               // LocationFragment locationFragment = new LocationFragment();
                startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));

                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, locationFragment).commit();
//                startActivity(new Intent(getApplicationContext(), EmptyFragment.class));
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        int backpress = 0;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backpress += 1;
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
            if (backpress > 1) {
                this.finish();
            }


            super.onBackPressed();
        }
    }
}
