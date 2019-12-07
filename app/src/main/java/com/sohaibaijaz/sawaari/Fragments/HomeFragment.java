package com.sohaibaijaz.sawaari.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.sohaibaijaz.sawaari.BusActivity;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.PermissionUtils;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback{



    private FrameLayout spinner_frame;
    private ProgressBar spinner;
    private  View fragmentView;

    private GoogleMap mMap;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Map<String, String> dropoffLocation = new HashMap<String, String>();
    private Map<String, String> currentLocation = new HashMap<String, String>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        LocationManager lm = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {

          showAlertLocationDisabled();

        }

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), MainActivity.MAP_VIEW_BUNDLE_KEY);
        }

//        placesClient = Places.createClient(getActivity().getApplicationContext());

//        initAutoCompleteTextView();

        SupportMapFragment mapFragment =(SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);





        final SharedPreferences sharedPreferences= Objects.requireNonNull(this.getActivity()).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("Token", "");


        if(!token.equals(""))
        {
        final RequestQueue requestQueue = Volley.newRequestQueue(fragmentView.getContext());

        final Button dropoff_btn = (Button)fragmentView.findViewById(R.id.btn_dropoff);
        spinner = (ProgressBar)fragmentView.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner_frame = fragmentView.findViewById(R.id.spinner_frame);
        spinner_frame.setVisibility(View.GONE);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setCountry("PK");
        autocompleteFragment.setHint("Enter Drop off Location");
                    // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
                    // Set up a PlaceSelectionListener to handle the response.
            autocompleteFragment.setOnPlaceSelectedListener(placeSelectionListener);


            UserDetails.getUserDetails(this.getContext());



        dropoff_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getContext(), "Current:"+currentLocation.get("latitude")+","+currentLocation.get("longitude")+"\nDropoff:"+dropoffLocation.get("latitude")+","+dropoffLocation.get("longitude"), Toast.LENGTH_LONG).show();
                if (dropoffLocation.get("latitude") == null || currentLocation.get("longitude") == null) {
                    Toast.makeText(getContext(), "Select current and drop off location first!", Toast.LENGTH_SHORT).show();
                }
                else if(dropoffLocation.get("latitude") != null && currentLocation.get("longitude") != null){
                    try {


                        String URL = MainActivity.baseurl + "/bus/route/";
                        JSONObject jsonBody = new JSONObject();
                        spinner.setVisibility(View.VISIBLE);
                        spinner_frame.setVisibility(View.VISIBLE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                spinner.setVisibility(View.GONE);
                                spinner_frame.setVisibility(View.GONE);
                                Log.i("VOLLEY", response.toString());
                                try {
                                    JSONObject json = new JSONObject(response);

                                    if(json.getString("status").equals("200")){
//                                        Toast.makeText(getContext(), json.get("rides").toString(), Toast.LENGTH_LONG).show();
//
                                       if(json.get("rides").toString().equals("[]"))
                                       {
                                           Toast.makeText(getContext(), "No rides available", Toast.LENGTH_SHORT).show();
                                       }
                                       else {
                                           Intent i = new Intent(getContext(), BusActivity.class);
                                           i.putExtra("rides", json.get("rides").toString());
                                           startActivity(i);
                                       }
                                    }

                                    else if (json.getString("status").equals("400") || json.getString("status").equals("404")) {
                                        Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    Log.e("VOLLEY", e.toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                spinner.setVisibility(View.GONE);
                                spinner_frame.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                                Log.e("VOLLEY", error.toString());
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("start_lat", currentLocation.get("latitude"));
                                params.put("start_lon", currentLocation.get("longitude"));
                                params.put("stop_lat", dropoffLocation.get("latitude"));
                                params.put("stop_lon", dropoffLocation.get("longitude"));

                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Authorization", token);
                                return headers;
                            }
                        };

                        stringRequest.setRetryPolicy(new RetryPolicy() {
                            @Override
                            public int getCurrentTimeout() {
                                return 50000;
                            }

                            @Override
                            public int getCurrentRetryCount() {
                                return 50000;
                            }

                            @Override
                            public void retry(VolleyError error) throws VolleyError {

                            }
                        });

                        requestQueue.add(stringRequest);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Slow Internet Connection.", Toast.LENGTH_SHORT).show();
                    }


            }
            }

        });




        }

        return fragmentView;
    }




    PlaceSelectionListener placeSelectionListener = new PlaceSelectionListener() {
    @Override
    public void onPlaceSelected(@NonNull Place place) {
        dropoffLocation.put("name", place.getName());
       dropoffLocation.put("id", place.getId());
       LatLng latLng = place.getLatLng();
       dropoffLocation.put("latitude", String.valueOf(latLng.latitude));
       dropoffLocation.put("longitude", String.valueOf(latLng.longitude));

    }

    @Override
    public void onError(@NonNull Status status) {
        Toast.makeText(getContext(), "There was an error fetching the place", Toast.LENGTH_SHORT).show();
    }
};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap map){
        mMap = map;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity)this.getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                currentLocation.put("latitude", String.valueOf(latitude));
                currentLocation.put("longitude", String.valueOf(longitude));
                LatLng coordinate = new LatLng(latitude, longitude);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 16);
                mMap.animateCamera(yourLocation);
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {

        Toast.makeText(getContext(), "Fetching Current Location", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity)this.getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {

            LocationManager locationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                currentLocation.put("latitude", String.valueOf(latitude));
                currentLocation.put("longitude", String.valueOf(longitude));

            }
        }
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            enableMyLocation();
        } else {

            mPermissionDenied = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }

        LocationManager lm = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {

            showAlertLocationDisabled();

        }
    }




    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getFragmentManager(), "dialog");
    }

    private void showAlertLocationDisabled() {

        new AlertDialog.Builder(this.getContext())
                .setTitle("Enable Location")
                .setMessage("Sawaari can't go on without the device's Location!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                        System.exit(0);

                    }
                })
                .setIcon(R.mipmap.alert)
                .show();


    }





}
