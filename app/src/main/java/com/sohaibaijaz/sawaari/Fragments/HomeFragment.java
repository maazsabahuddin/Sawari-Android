package com.sohaibaijaz.sawaari.Fragments;

import android.Manifest;
import android.app.Activity;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
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
import static com.sohaibaijaz.sawaari.Fragments.HomeFragment.isNetworkAvailable;
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
 private  String checkplace;
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

    }

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

                if(isNetworkAvailable(getActivity())){
//                    if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {
//
//                        showAlertLocationDisabled();
//                    }
//                    else{
                        Intent i = new Intent(getActivity(), LocationActivity.class);
                        Bundle b = new Bundle();
                        b.putString("value" , "Whereto");
                        b.putString("activity" , "HomeFragment");
                        b.putSerializable("currentLocation" , currentLocation);
                        i.putExtras(b);
                        HomeFragment.this.startActivity(i);
//                    }
                }
                else{
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
        final RealmHelper helper = new RealmHelper(realm);

        checkplace=helper.checkPlace("Work");

        ImageView add_home = fragmentView.findViewById(R.id.add_home);
        ImageView add_work = fragmentView.findViewById(R.id.add_work);
        //Toast.makeText(getActivity(), checkplace, Toast.LENGTH_SHORT).show();
        if(checkplace.equals("Work")) {
            add_home.setImageResource(R.drawable.addedhome);
        }
       // else if
       // add_home.setImageResource(R.drawable.addedhome);




        ImageView add_place = fragmentView.findViewById(R.id.add_place);
      //  Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        add_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    showAlertLocationDisabled(getActivity());
                }
                else{
                    phone_number = userObject.getPhoneNumber();
                    placetype="Home";
                    dropoffLocation.clear();
                    dropoffLocation=helper.getPlace(placetype);
                    if(dropoffLocation.get("longitude")== null && dropoffLocation.get("latitude")== null)
                    {
                        Intent i = new Intent(getActivity(), LocationActivity.class);
                        Bundle b = new Bundle();
                        b.putString("value" , "Home");
                        b.putString("activity" , "HomeFragment");
                        b.putSerializable("currentLocation" , currentLocation);
                        i.putExtras(b);
                        HomeFragment.this.startActivity(i);
                    }
                    else {
                        if(!isNetworkAvailable(getActivity())){
                            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            BusRouteApi(currentLocation, dropoffLocation, spinner_frame, spinner, requestQueue, getContext(), getActivity());
                        }
                    }
                }
            }
        });

        add_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    showAlertLocationDisabled(getActivity());
                }
                else{
                    phone_number = userObject.getPhoneNumber();
                    placetype="Work";
                    dropoffLocation.clear();
                    dropoffLocation=helper.getPlace(placetype);
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
                        if(!isNetworkAvailable(getActivity())){
                            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            BusRouteApi(currentLocation, dropoffLocation, spinner_frame, spinner, requestQueue, getContext(), getActivity());
                        }
                    }
                }
            }
        });

        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    showAlertLocationDisabled(getActivity());
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

        if(!isNetworkAvailable(getActivity())){
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else{

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            markerPoints = new ArrayList<LatLng>();
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch(Exception ex) { Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();}

            if(!gps_enabled && !network_enabled) {
//          showAlertLocationDisabled();
            }

            if (!Places.isInitialized()) {
                Places.initialize(getContext(), NavActivity.MAP_VIEW_BUNDLE_KEY);
            }
        }

        SupportMapFragment mapFragment =(SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        try{
            mapFragment.getMapAsync(this);
        }catch (Exception e){}

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


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
//                Log.e(LOG_TAG, "Error checking internet connection", e);
            }
        } else {
//            Log.d(LOG_TAG, "No network available!");
        }
        return false;
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
//            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            showAlertLocationDisabled(getActivity());
        }
        else {

            if(!isNetworkAvailable(getActivity())){
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            else{
                mMap.setMyLocationEnabled(true);
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

                showAlertLocationDisabled(getActivity());
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        LOCATION_PERMISSION_REQUEST_CODE);

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
//            showAlertLocationDisabled();
        }
    }


    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getFragmentManager(), "dialog");
    }

    public void showAlertLocationDisabled(Activity activity) {

        new AlertDialog.Builder(activity)
                .setTitle("Enable Location")
                .setMessage("Sawari can't go on without the device's Location!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(callGPSSettingIntent);
//                        dialog.cancel();
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_REQUEST_CODE);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        getActivity().finish();
//                        System.exit(0);
                        dialog.cancel();
                    }
                })
                .setIcon(R.mipmap.alert)
                .show();
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
