package com.sohaibaijaz.sawaari.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.sohaibaijaz.sawaari.Maps.AddPlaceFragment;
import com.sohaibaijaz.sawaari.Maps.LocationFragment;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.PermissionUtils;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.Rides.ShowRides;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;


public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback{

    private FrameLayout spinner_frame;
    private ProgressBar spinner;
    Realm realm;
    private String longitudeDB;
    private String latitudeDB;
    private String placeNameDB;
    private String longitudeWDB;
    private String latitudeWDB;
    private String placeNameWDB;
    private String placetype;
    private View fragmentView;
    public static String placeType;
    private GoogleMap mMap;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

  //  private Map<String, String> dropoffLocation = new HashMap<>();
    private HashMap<String, String> currentLocation = new HashMap<>();
    private HashMap<String, String> dropoffLocation = new HashMap<>();

    private ArrayList<LatLng> markerPoints;
    private FusedLocationProviderClient fusedLocationClient;


//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        Fragment childFragment = new LocationFragment();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.replace(R.id.location_fragment, childFragment).commit();
//    }


//    public void buttonClick(View v) {
//        switch(v.getId()) {
//            case R.id.where_to_textview:
//                Intent myIntent = new Intent();
//                myIntent.setClassName("com.sohaibaijaz.sawaari", "com.sohaibaijaz.sawaari.Maps.LF");
//                // for ex: your package name can be "com.example"
//                // your activity name will be "com.example.Contact_Developer"
//                startActivity(myIntent);
//                break;
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        LocationManager lm = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        realm = Realm.getDefaultInstance();
      //  SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);


        TextView where_to_textview = fragmentView.findViewById(R.id.where_to_textview);
//
        where_to_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

////                Intent intent = new Intent(getContext(), LF.class);
////                startActivity(intent);
//

                Fragment fragment = new LocationFragment();
                Bundle arguments = new Bundle();
                arguments.putSerializable("pick_up_location" , currentLocation);
                fragment.setArguments(arguments);
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();

                // Arsalan bhai
//                Fragment newFragment = new LocationFragment();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, newFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();

            }
        });

        ImageView add_home = fragmentView.findViewById(R.id.add_home);
        ImageView add_work = fragmentView.findViewById(R.id.add_work);
        ImageView add_place = fragmentView.findViewById(R.id.add_place);

        add_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placetype="Home";
                getValueHome(placetype);
               if(longitudeDB == null && latitudeDB == null)
                {
                    Fragment newFragment = new AddPlaceFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString("place_type" , "Home");
                    newFragment.setArguments(arguments);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, newFragment);
                    transaction.addToBackStack(null);
                   // placeType = "Home";
                    transaction.commit();

                }
               else {
                   dropoffLocation.clear();
                   dropoffLocation.put("latitude", latitudeDB);
                   dropoffLocation.put("longitude", longitudeDB);
                   dropoffLocation.put("name", placeNameDB);
                   Toast.makeText(getActivity(), longitudeDB+" "+latitudeDB, Toast.LENGTH_SHORT).show();
                   showrides();
               }
            }
        });

        add_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placetype="Work";
                getValueWork(placetype);
               if(longitudeWDB== null && latitudeWDB== null)
                {
                    Fragment newFragment = new AddPlaceFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString("place_type" , "Work");
                    newFragment.setArguments(arguments);
                    FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, newFragment);
                    transaction.addToBackStack(null);
                   // placeType = "Work";
                    transaction.commit();

                }
                else {
                   dropoffLocation.clear();
                   dropoffLocation.put("latitude", latitudeDB);
                   dropoffLocation.put("longitude", longitudeDB);
                   dropoffLocation.put("name", placeNameDB);
                   Toast.makeText(getActivity(), longitudeDB+" "+placeNameDB, Toast.LENGTH_SHORT).show();
                   showrides();
              }



            }
        });

        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment newFragment = new AddPlaceFragment();
                Bundle arguments = new Bundle();
                arguments.putString("place_type" , "Extra");
                newFragment.setArguments(arguments);
                FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
               // placeType = "Work";
                transaction.commit();
//                Fragment newFragment1 = new SavedPlace();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, newFragment1);
//                transaction.addToBackStack(null);
//                transaction.commit();

//                Intent intent = new Intent(getActivity(), SavedPlace.class);
//                startActivity(intent);
            }
        });

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

      //  final RequestQueue requestueue = Volley.newRequestQueue(fragmentView.getContext());
//        final Button dropoff_btn = fragmentView.findViewById(R.id.btn_dropoff);

        spinner = (ProgressBar)fragmentView.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner_frame = fragmentView.findViewById(R.id.spinner_frame);
        spinner_frame.setVisibility(View.GONE);
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//        autocompleteFragment.setCountry("PK");
//        autocompleteFragment.setHint("Enter Drop off Location");
//
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
//        autocompleteFragment.setOnPlaceSelectedListener(placeSelectionListener);

//        user_rides.setVisibility(View.GONE);


//        dropoff_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(getContext(), "Current:"+currentLocation.get("latitude")+","+currentLocation.get("longitude")+"\nDropoff:"+dropoffLocation.get("latitude")+","+dropoffLocation.get("longitude"), Toast.LENGTH_LONG).show();
//                if (dropoffLocation.get("latitude") == null || currentLocation.get("longitude") == null) {
//                    Toast.makeText(getContext(), "Select current and drop off location first!", Toast.LENGTH_SHORT).show();
//                }
//                else if(dropoffLocation.get("latitude") != null && currentLocation.get("longitude") != null) {
//
//                    try {
//
//                        String URL = MainActivity.baseurl + "/bus/route/";
//                        JSONObject jsonBody = new JSONObject();
//                        spinner.setVisibility(View.VISIBLE);
//                        spinner_frame.setVisibility(View.VISIBLE);
//                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                spinner.setVisibility(View.GONE);
//                                spinner_frame.setVisibility(View.GONE);
//                                Log.i("VOLLEY", response.toString());
//                                try {
//                                    JSONObject json = new JSONObject(response);
//
//                                    if (json.getString("status").equals("200")) {
//
//                                        Intent i = new Intent(getContext(), ShowRides.class);
//                                        i.putExtra("json", json.toString());
//                                        i.putExtra("rides", json.getJSONArray("rides").toString());
//                                        startActivity(i);
//
//                                    } else if (json.getString("status").equals("400") || json.getString("status").equals("404")) {
//                                        Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
//                                    }
//
//                                } catch (JSONException e) {
//                                    Log.e("VOLLEY", e.toString());
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                spinner.setVisibility(View.GONE);
//                                spinner_frame.setVisibility(View.GONE);
//                                Toast.makeText(getContext(), "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
//                                Log.e("VOLLEY", error.toString());
//                            }
//                        }) {
//                            @Override
//                            protected Map<String, String> getParams() {
//                                Map<String, String> params = new HashMap<String, String>();
//
////                                params.put("start_lat", currentLocation.get("latitude"));
////                                params.put("start_lon", currentLocation.get("longitude"));
////                                params.put("stop_lat", dropoffLocation.get("latitude"));
////                                params.put("stop_lon", dropoffLocation.get("longitude"));
//
//                                params.put("stop_lat", "24.913363");
//                                params.put("stop_lon", "67.124208");
//                                params.put("start_lat", "24.823343");
//                                params.put("start_lon", "67.029656");
//
//                                return params;
//                            }
//
//                            @Override
//                            public Map<String, String> getHeaders() throws AuthFailureError {
//                                Map<String, String> headers = new HashMap<String, String>();
//                                headers.put("Authorization", token);
//                                return headers;
//                            }
//                        };
//
//                        stringRequest.setRetryPolicy(new RetryPolicy() {
//                            @Override
//                            public int getCurrentTimeout() {
//                                return 500000;
//                            }
//
//                            @Override
//                            public int getCurrentRetryCount() {
//                                return 500000;
//                            }
//
//                            @Override
//                            public void retry(VolleyError error) throws VolleyError {
//
//                            }
//                        });
//
//                        requestQueue.add(stringRequest);
//                    } catch (Exception e) {
//                        Toast.makeText(getContext(), "Slow Internet Connection.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//        });
        return fragmentView;
    }


//    PlaceSelectionListener placeSelectionListener = new PlaceSelectionListener() {
//    @Override
//    public void onPlaceSelected(@NonNull Place place) {
//        dropoffLocation.clear();
//        dropoffLocation.put("name", place.getName());
//        dropoffLocation.put("id", place.getId());
//        LatLng latLng = place.getLatLng();
//        dropoffLocation.put("latitude", String.valueOf(latLng.latitude));
//        dropoffLocation.put("longitude", String.valueOf(latLng.longitude));
//
//
//        if (dropoffLocation.get("latitude") == null || currentLocation.get("longitude") == null) {
//            Toast.makeText(getContext(), "Select current and drop off location first!", Toast.LENGTH_SHORT).show();
//        }
//        else if(dropoffLocation.get("latitude") != null && currentLocation.get("longitude") != null){
//
//
//            markerPoints.clear();
//            mMap.clear();
//
//            LatLng start = new LatLng(Double.parseDouble(currentLocation.get("latitude")), Double.parseDouble(currentLocation.get("longitude")));
//            LatLng stop = new LatLng(Double.parseDouble(dropoffLocation.get("latitude")), Double.parseDouble(dropoffLocation.get("longitude")));
//
//            markerPoints.add(start);
//            markerPoints.add(stop);
//            MarkerOptions options = new MarkerOptions();
//
//            options.position(start);
//            options.position(stop);
//
//
//            if(markerPoints.size() >=2 ){
//                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
//
//                mMap.addMarker(options);
//
//                LatLng origin = markerPoints.get(0);
//                LatLng dest = markerPoints.get(1);
//
//                // Getting URL to the Google Directions API
//                String url = getDirectionsUrl(origin, dest);
//
//                DownloadTask downloadTask = new DownloadTask();
//
//                // Start downloading json data from Google Directions API
//                downloadTask.execute(url);
//
//
//            }
//
//        }
//
//    }
//
//    @Override
//    public void onError(@NonNull Status status) {
//        Toast.makeText(getContext(), "There was an error fetching the place", Toast.LENGTH_SHORT).show();
//    }
//};
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
    @Override
    public void onMapReady(GoogleMap map){
        mMap = map;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.clear();
        enableMyLocation();

    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            currentLocation.put("name", obj.getAddressLine(0));
//            currentLocation.put("id", place.getId());

            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                                getAddress(latitude, longitude);
                                LatLng coordinate = new LatLng(latitude, longitude);
                                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 16);
                                mMap.animateCamera(yourLocation);
                            }
                        }
                    });
        }
    }

//    public void setBoundsLocation(){
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission to access the location is missing.
//            PermissionUtils.requestPermission((AppCompatActivity)this.getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
//                    Manifest.permission.ACCESS_FINE_LOCATION, true);
//        } else if (mMap != null) {
//            // Access to the location has been granted to the app.
//            mMap.setMyLocationEnabled(true);
//
//            LatLng start = new LatLng(Double.parseDouble(currentLocation.get("latitude")), Double.parseDouble(currentLocation.get("longitude")));
//            LatLng stop = new LatLng(Double.parseDouble(dropoffLocation.get("latitude")), Double.parseDouble(dropoffLocation.get("longitude")));
//
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            builder.include(start).include(stop);
//            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 10));
//
//
//        }
//    }


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

//    private String getDirectionsUrl(LatLng origin, LatLng dest) {
//
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//        // Sensor enabled
//        String sensor = "sensor=false";
//
//        String api_key = "key="+MainActivity.MAP_VIEW_BUNDLE_KEY;
//
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + api_key;
//
//        // Output format
//        String output = "json";
//
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//
//        return url;
//    }


//    private String downloadUrl(String strUrl) throws IOException {
//        String data = "";
//        InputStream iStream = null;
//        HttpURLConnection urlConnection = null;
//        try {
//            URL url = new URL(strUrl);
//
//            // Creating an http connection to communicate with url
//            urlConnection = (HttpURLConnection) url.openConnection();
//
//            // Connecting to url
//            urlConnection.connect();
//
//            // Reading data from url
//            iStream = urlConnection.getInputStream();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//
//            StringBuffer sb = new StringBuffer();
//
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//            data = sb.toString();
//
//            br.close();
//
//        } catch (Exception e) {
//            Log.d("Exception", e.toString());
//        } finally {
//            iStream.close();
//            urlConnection.disconnect();
//        }
//        return data;
//    }


//    public class DownloadTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... url) {
//
//            // For storing data from web service
//            String data = "";
//
//            try {
//                // Fetching the data from web service
//                data = downloadUrl(url[0]);
//            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
//            }
//            return data;
//        }
//
//        // Executes in UI thread, after the execution of
//        // doInBackground()
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            ParserTask parserTask = new ParserTask();
//
//            // Invokes the thread for parsing the JSON data
//            parserTask.execute(result);
//        }
//    }

//    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//
//        // Parsing the data in non-ui thread
//        @Override
//        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//
//            JSONObject jObject;
//            List<List<HashMap<String, String>>> routes = null;
//
//            try {
//                jObject = new JSONObject(jsonData[0]);
//                DirectionsJSONParser parser = new DirectionsJSONParser();
//
//                // Starts parsing data
//                routes = parser.parse(jObject);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return routes;
//        }

//        // Executes in UI thread, after the parsing process
//        @Override
//        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//            ArrayList<LatLng> points = null;
//            PolylineOptions lineOptions = null;
//            MarkerOptions markerOptions = new MarkerOptions();
//            String distance = "";
//            String duration = "";
//
//            if (result.size() < 1) {
//                Toast.makeText(getActivity().getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Traversing through all the routes
//            for (int i = 0; i < result.size(); i++) {
//                points = new ArrayList<LatLng>();
//                lineOptions = new PolylineOptions();
//
//                // Fetching i-th route
//                List<HashMap<String, String>> path = result.get(i);
//
//                // Fetching all the points in i-th route
//                for (int j = 0; j < path.size(); j++) {
//                    HashMap<String, String> point = path.get(j);
//
//                    if (j == 0) {    // Get distance from the list
//                        distance = (String) point.get("distance");
//                        continue;
//                    } else if (j == 1) { // Get duration from the list
//                        duration = (String) point.get("duration");
//                        continue;
//                    }
//
//                    double lat = Double.parseDouble(point.get("lat"));
//                    double lng = Double.parseDouble(point.get("lng"));
//                    LatLng position = new LatLng(lat, lng);
//
//                    points.add(position);
//                }
//
//                // Adding all the points in the route to LineOptions
//                lineOptions.addAll(points);
//                lineOptions.width(10);
//                lineOptions.color(Color.BLUE);
//            }
//
//
//            // Drawing polyline in the Google Map for the i-th route
//            mMap.addPolyline(lineOptions);
//        }


//    }

    public void showrides(){
        try {
            SharedPreferences sharedPreferences= Objects.requireNonNull(this.getActivity()).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);

            final String token = sharedPreferences.getString("Token", "");
            RequestQueue requestQueue = Volley.newRequestQueue(fragmentView.getContext());

            String URL = MainActivity.baseurl + "/bus/route/";
            JSONObject jsonBody = new JSONObject();
//                        spinner.setVisibility(View.VISIBLE);
//                        spinner_frame.setVisibility(View.VISIBLE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                                spinner.setVisibility(View.GONE);
//                                spinner_frame.setVisibility(View.GONE);
                    Log.i("VOLLEY", response.toString());
                    try {
                        JSONObject jsonObj = new JSONObject(response);

                        if (jsonObj.getString("status").equals("200")) {

                            Intent i = new Intent(getContext(), ShowRides.class);
                            i.putExtra("json", jsonObj.toString());
                            i.putExtra("pick_up_location", currentLocation);
                            i.putExtra("drop_off_location", dropoffLocation);
                            startActivity(i);

                        } else if (jsonObj.getString("status").equals("400") || jsonObj.getString("status").equals("404")) {
                            Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.e("VOLLEY", e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                                spinner.setVisibility(View.GONE);
//                                spinner_frame.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("start_lat", currentLocation.get("latitude"));
                    params.put("start_lon", currentLocation.get("longitude"));
                    params.put("stop_lat", dropoffLocation.get("latitude"));
                    params.put("stop_lon", dropoffLocation.get("longitude"));

//                                params.put("stop_lat", "24.913363");
//                                params.put("stop_lon", "67.124208");
//                                params.put("start_lat", "24.823343");
//                                params.put("start_lon", "67.029656");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
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
            Toast.makeText(getActivity(), "Slow Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public void getValueHome(final String placeType){


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                RealmResults<com.sohaibaijaz.sawaari.model.Location> results = bgRealm.where(com.sohaibaijaz.sawaari.model.Location.class).equalTo("placeType",placeType).findAll();
                for(com.sohaibaijaz.sawaari.model.Location location : results){
                    longitudeDB=location.getLongitude();
                    latitudeDB=location.getLatitude();
                    placeNameDB=location.getPlaceName();
                }
               // Toast.makeText(getActivity(), longitude+" "+latitude, Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void getValueWork(final String placeType){


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                RealmResults<com.sohaibaijaz.sawaari.model.Location> results = bgRealm.where(com.sohaibaijaz.sawaari.model.Location.class).equalTo("placeType",placeType).findAll();
                for(com.sohaibaijaz.sawaari.model.Location location : results){
                    longitudeWDB=location.getLongitude();
                    latitudeWDB=location.getLatitude();
                    placeNameWDB=location.getPlaceName();
                }
                // Toast.makeText(getActivity(), longitude+" "+latitude, Toast.LENGTH_SHORT).show();

            }
        });
    }

}
