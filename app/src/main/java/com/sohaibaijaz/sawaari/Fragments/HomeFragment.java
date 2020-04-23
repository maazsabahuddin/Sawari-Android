package com.sohaibaijaz.sawaari.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
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
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.sohaibaijaz.sawaari.Maps.AddPlaceFragment;
import com.sohaibaijaz.sawaari.Maps.LocationActivity;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.Maps.LocationFragment;
import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.PermissionUtils;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.RealmHelper;
import com.sohaibaijaz.sawaari.Rides.ShowRides;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;
import com.sohaibaijaz.sawaari.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.android.volley.VolleyLog.TAG;
import static com.sohaibaijaz.sawaari.Maps.LocationFragment.BusRouteApi;


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
    private RequestQueue requestQueue;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private HashMap<String, String> currentLocation = new HashMap<>();
    private HashMap<String, String> dropoffLocation = new HashMap<>();

    private ArrayList<LatLng> markerPoints;
    private FusedLocationProviderClient fusedLocationClient;
    private  String phone_number;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        LocationManager lm = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        requestQueue = Volley.newRequestQueue(fragmentView.getContext());
        realm = Realm.getDefaultInstance();
        final User userObject = User.getInstance();

       // phone_number = userObject.getPhoneNumber();
        //Toast.makeText(getActivity(), phone_number, Toast.LENGTH_SHORT).show();


        TextView where_to_textView = fragmentView.findViewById(R.id.where_to_textview);
        where_to_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Enable Location")
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
                                getActivity().finish();
                                System.exit(0);
                            }
                        })
                        .setIcon(R.mipmap.alert)
                        .show();
//                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//
//                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                            LOCATION_PERMISSION_REQUEST_CODE);
//
//                }
//                else{
//                    Intent i = new Intent(getActivity(), LocationActivity.class);
//                    Bundle b = new Bundle();
//                    b.putString("value" , "Whereto");
//                    b.putString("activity" , "HomeFragment");
//                    b.putSerializable("currentLocation" , currentLocation);
//                    i.putExtras(b);
//                    HomeFragment.this.startActivity(i);
//                }
            }
        });
        final RealmHelper helper = new RealmHelper(realm);

        ImageView add_home = fragmentView.findViewById(R.id.add_home);
        ImageView add_work = fragmentView.findViewById(R.id.add_work);
        ImageView add_place = fragmentView.findViewById(R.id.add_place);

        add_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                }
                else{
                    phone_number = userObject.getPhoneNumber();
                    placetype="Home";
                    dropoffLocation.clear();
                    dropoffLocation=helper.getPlace(placetype, phone_number);
                    if(dropoffLocation.get("longitude")== null && dropoffLocation.get("latitude")== null)
                    {
                        Intent i = new Intent(getActivity(), LocationActivity.class);
                        Bundle b = new Bundle();
                        b.putString("value" , "Home");
                        b.putString("activity" , "HomeFragment");
                        b.putSerializable("currentLocation" , currentLocation);
                        i.putExtras(b);
                        HomeFragment.this.startActivity(i);
//                        Fragment newFragment = new AddPlaceFragment();
//                        Bundle arguments = new Bundle();
//                        arguments.putString("value" , "Home");
//                        arguments.putString("activity" , "homeFragment");
//                        arguments.putSerializable("currentLocation" , currentLocation);
//                        newFragment.setArguments(arguments);
//                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.fragment_container, newFragment);
//                        transaction.addToBackStack(null);
//                        transaction.commit();

                    }
                    else {
                        Toast.makeText(getActivity(), dropoffLocation.get("longitude")+" "+dropoffLocation.get("name"), Toast.LENGTH_SHORT).show();
                        showrides();
                    }
                }
            }
        });

        add_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                }
                else{
                    phone_number = userObject.getPhoneNumber();
                    placetype="Work";
                    dropoffLocation.clear();
                    dropoffLocation=helper.getPlace(placetype, phone_number);
                    if(dropoffLocation.get("longitude")== null && dropoffLocation.get("latitude")== null)
                    {
                        Intent i = new Intent(getActivity(), LocationActivity.class);
                        Bundle b = new Bundle();
                        b.putString("value" , "Work");
                        b.putString("activity" , "HomeFragment");
                        b.putSerializable("currentLocation" , currentLocation);
                        i.putExtras(b);
                        HomeFragment.this.startActivity(i);
                    }
                    else {
                        Toast.makeText(getActivity(), dropoffLocation.get("longitude")+" "+dropoffLocation.get("name"), Toast.LENGTH_SHORT).show();
                        BusRouteApi(currentLocation, dropoffLocation, spinner_frame, spinner, requestQueue, getContext(), getActivity());
                    }
                }
            }
        });

        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                }
                else{
                    Intent i = new Intent(getActivity(), LocationActivity.class);
                    Bundle b = new Bundle();
                    b.putString("value" , "AddPlace");
                    b.putString("activity" , "HomeFragment");
                    b.putSerializable("currentLocation" , currentLocation);
                    i.putExtras(b);
                    HomeFragment.this.startActivity(i);
                }
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
            Places.initialize(getContext(), NavActivity.MAP_VIEW_BUNDLE_KEY);
        }


        SupportMapFragment mapFragment =(SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        final SharedPreferences sharedPreferences= Objects.requireNonNull(this.getActivity()).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
//        final String token = sharedPreferences.getString("Token", "");

        spinner = fragmentView.findViewById(R.id.progressBarHF);
        spinner.setVisibility(View.GONE);
        spinner_frame = fragmentView.findViewById(R.id.spinner_frameHF);
        spinner_frame.setVisibility(View.GONE);

        try{
            View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);rlp.setMargins(0,0,30,30);
        }catch (Exception e){}

        return fragmentView;
    }

    @Override
    public void onMapReady(GoogleMap map){
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            mMap = map;
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            Objects.requireNonNull(getActivity()), R.raw.silver));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
//        mMap.clear();
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
            UUID uuid = UUID.randomUUID();
            String placeID = uuid.toString();
            currentLocation.put("id", placeID);

            Log.v("IGA", "Address" + add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);

        }
        else {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);

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
                                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15.0f);
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

        try{
            Toast.makeText(getContext(), "Fetching Current Location", Toast.LENGTH_SHORT).show();
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);

                // Permission to access the location is missing.
//                PermissionUtils.requestPermission((AppCompatActivity)this.getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
//                        Manifest.permission.ACCESS_FINE_LOCATION, true);
            }
            else{

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
        catch (Exception e){
            Toast.makeText(getContext(), "Error " + e.toString(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }



    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
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
                        getActivity().finish();
                        System.exit(0);
                    }
                })
                .setIcon(R.mipmap.alert)
                .show();
    }

    public void showrides(){
        try {
            SharedPreferences sharedPreferences= Objects.requireNonNull(this.getActivity()).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);

            final String token = sharedPreferences.getString("Token", "");

            String URL = MainActivity.baseurl + "/bus/route/";
            JSONObject jsonBody = new JSONObject();

            spinner.setVisibility(View.VISIBLE);
            spinner_frame.setVisibility(View.VISIBLE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    spinner.setVisibility(View.GONE);
                    spinner_frame.setVisibility(View.GONE);

                    Log.i("VOLLEY", response);
                    try {
                        JSONObject jsonObj = new JSONObject(response);

                        if (jsonObj.getString("status").equals("200")) {

                            Intent i = new Intent(getContext(), ShowRides.class);
                            i.putExtra("json", jsonObj.toString());
                            i.putExtra("pick_up_location", currentLocation);
                            i.putExtra("drop_off_location", dropoffLocation);
                            startActivity(i);

                        } else if (jsonObj.getString("status").equals("400")) {
                            Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        else if(jsonObj.getString("status").equals("404")){
                            Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                            SettingsFragment.signout(getActivity());
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
