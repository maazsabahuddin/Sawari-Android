package com.sohaibaijaz.sawaari.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.ListFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sohaibaijaz.sawaari.Rides.BusActivity;
import com.sohaibaijaz.sawaari.DirectionsJSONParser;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.PermissionUtils;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.Rides.show_rides;
import com.sohaibaijaz.sawaari.model.RidesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback{

    private FrameLayout spinner_frame;
    private ProgressBar spinner;
    private View fragmentView;

    private GoogleMap mMap;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Map<String, String> dropoffLocation = new HashMap<String, String>();
    private Map<String, String> currentLocation = new HashMap<String, String>();

    private ArrayList<LatLng> markerPoints;
    private FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        LocationManager lm = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;


        //on Back Pressed
        fragmentView.setFocusableInTouchMode(true);
        fragmentView.requestFocus();
        fragmentView.setOnKeyListener( new View.OnKeyListener()
        {
            int backpress = 0;
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {

                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    backpress = (backpress + 1);
                    Toast.makeText(getContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

                    if (backpress > 2) {
                        getActivity().finish();
                        System.exit(0);
                    }
                    return true;
                }
                return false;
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        markerPoints = new ArrayList<LatLng>();
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


        SupportMapFragment mapFragment =(SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);


        final SharedPreferences sharedPreferences= Objects.requireNonNull(this.getActivity()).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("Token", "");
        System.out.print("Token: "+ token);

        if(token.equals(""))
        {
            Intent i = new Intent(getActivity(), MainActivity.class);
            HomeFragment.this.startActivity(i);
//            Toast.makeText(getContext(), "Invalid token", Toast.LENGTH_LONG).show();
        }

        final RequestQueue requestQueue = Volley.newRequestQueue(fragmentView.getContext());
        final Button dropoff_btn = fragmentView.findViewById(R.id.btn_dropoff);

        spinner = (ProgressBar)fragmentView.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner_frame = fragmentView.findViewById(R.id.spinner_frame);
        spinner_frame.setVisibility(View.GONE);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setCountry("PK");
        autocompleteFragment.setHint("Enter Drop off Location");

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(placeSelectionListener);

//        user_rides.setVisibility(View.GONE);
        String rides_data = sharedPreferences.getString("user_rides", "");
        boolean active_rides_flag = false;

        try{
            JSONArray rides = new JSONArray(rides_data);
            if(rides.length() == 0){
                active_rides_flag = false;
            }
            for(int i=0; i< rides.length(); i++){
                JSONObject ride = rides.getJSONObject(i);
                String ride_status = ride.getString("ride_status");

                if(ride_status.equals("ACTIVE") || ride_status.equals("active")){
                    active_rides_flag = true;
                    break;
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

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

//                                    GsonBuilder builder = new GsonBuilder();
//                                    Gson gson = builder.create();
//                                    RidesModel model = gson.fromJson(json.toString(), RidesModel.class);

                                    if(json.getString("status").equals("200")){

                                        Intent i = new Intent(getContext(), show_rides.class);
                                        i.putExtra("rides", json.toString());
                                        startActivity(i);

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

                                System.out.println("Starting latitude" + currentLocation.get("latitude"));
                                System.out.println("Starting longitude" + currentLocation.get("longitude"));
                                System.out.println("Drop off latitude" + dropoffLocation.get("latitude"));
                                System.out.println("Drop off longitude" + dropoffLocation.get("longitude"));

                                params.put("start_lat", currentLocation.get("latitude"));
                                params.put("start_lon", currentLocation.get("longitude"));
                                params.put("stop_lat", dropoffLocation.get("latitude"));
                                params.put("stop_lon", dropoffLocation.get("longitude"));
                                System.out.println(currentLocation.get("latitude")+"\n"+currentLocation.get("longitude")+"\n"+dropoffLocation.get("latitude")+"\n"+dropoffLocation.get("longitude"));

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
                                return 500000;
                            }

                            @Override
                            public int getCurrentRetryCount() {
                                return 500000;
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
        return fragmentView;
    }


    PlaceSelectionListener placeSelectionListener = new PlaceSelectionListener() {
    @Override
    public void onPlaceSelected(@NonNull Place place) {
        dropoffLocation.clear();
        dropoffLocation.put("name", place.getName());
        dropoffLocation.put("id", place.getId());
        LatLng latLng = place.getLatLng();
        dropoffLocation.put("latitude", String.valueOf(latLng.latitude));
        dropoffLocation.put("longitude", String.valueOf(latLng.longitude));


        if (dropoffLocation.get("latitude") == null || currentLocation.get("longitude") == null) {
            Toast.makeText(getContext(), "Select current and drop off location first!", Toast.LENGTH_SHORT).show();
        }
        else if(dropoffLocation.get("latitude") != null && currentLocation.get("longitude") != null){


            markerPoints.clear();
            mMap.clear();

            LatLng start = new LatLng(Double.parseDouble(currentLocation.get("latitude")), Double.parseDouble(currentLocation.get("longitude")));
            LatLng stop = new LatLng(Double.parseDouble(dropoffLocation.get("latitude")), Double.parseDouble(dropoffLocation.get("longitude")));

            markerPoints.add(start);
            markerPoints.add(stop);
            MarkerOptions options = new MarkerOptions();

            options.position(start);
            options.position(stop);


            if(markerPoints.size() >=2 ){
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

                mMap.addMarker(options);

                LatLng origin = markerPoints.get(0);
                LatLng dest = markerPoints.get(1);

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(origin, dest);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);


            }

        }

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
        mMap.clear();
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

//            LocationManager locationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//            Criteria criteria = new Criteria();
//            String provider = locationManager.getBestProvider(criteria, true);
//            Location location = locationManager.getLastKnownLocation(provider);
//

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                currentLocation.clear();
                                currentLocation.put("latitude", String.valueOf(latitude));
                                currentLocation.put("longitude", String.valueOf(longitude));
                                LatLng coordinate = new LatLng(latitude, longitude);
                                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 16);
                                mMap.animateCamera(yourLocation);
                            }
                        }
                    });
        }
    }

    public void setBoundsLocation(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity)this.getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

            LatLng start = new LatLng(Double.parseDouble(currentLocation.get("latitude")), Double.parseDouble(currentLocation.get("longitude")));
            LatLng stop = new LatLng(Double.parseDouble(dropoffLocation.get("latitude")), Double.parseDouble(dropoffLocation.get("longitude")));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(start).include(stop);
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 10));


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

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                currentLocation.clear();
                                currentLocation.put("latitude", String.valueOf(latitude));
                                currentLocation.put("longitude", String.valueOf(longitude));
                                LatLng coordinate = new LatLng(latitude, longitude);
                                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 16);
                                mMap.animateCamera(yourLocation);
                            }
                        }
                    });
        }


        return false;
    }



    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location", Toast.LENGTH_LONG).show();
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
        else{
//            enableMyLocation();
                    if (dropoffLocation.get("latitude") == null || currentLocation.get("longitude") == null) {
            enableMyLocation();
        }
        else if(dropoffLocation.get("latitude") != null && currentLocation.get("longitude") != null) {

            setBoundsLocation();
        }
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

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        String api_key = "key="+MainActivity.MAP_VIEW_BUNDLE_KEY;

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + api_key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getActivity().getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
            }


            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }


    }



}
