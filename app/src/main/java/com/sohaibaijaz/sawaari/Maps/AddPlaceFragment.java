package com.sohaibaijaz.sawaari.Maps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.sohaibaijaz.sawaari.DirectionsJSONParser;
import com.sohaibaijaz.sawaari.Fragments.HomeFragment;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.PermissionUtils;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.RealmHelper;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;
import com.sohaibaijaz.sawaari.model.Location;
import com.sohaibaijaz.sawaari.model.User;

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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.view.View.GONE;
import static com.android.volley.VolleyLog.TAG;
import static com.sohaibaijaz.sawaari.Fragments.HomeFragment.isNetworkAvailable;

public class AddPlaceFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback{

    private View fragmentView;
    Realm realm;
    private String fromwhere;
    private GoogleMap mMap;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private HashMap<String, String> userLocation = new HashMap<>();
    private ArrayList<LatLng> markerPoints;
    private FusedLocationProviderClient fusedLocationClient;
    private String placeType;
    private String phonenumber;
    private FrameLayout mapViewFrameLayout;
    private SupportMapFragment mapFragment;
    private Button add_place_btn;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.add_place_fragment, container, false);
        realm = Realm.getDefaultInstance();
        AutocompleteSupportFragment autocompleteFragment_pickUp = (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.add_location);

        sharedPreferences = this.getActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);

        autocompleteFragment_pickUp.setCountry("PK");
        autocompleteFragment_pickUp.setHint("Add Place");

        User userobject= User.getInstance();

        phonenumber= userobject.getPhoneNumber();

        Bundle b = this.getArguments();
        placeType = b.getString("value");
        fromwhere = b.getString("activity");
        if(b.getSerializable("currentLocation") != null)
            userLocation = (HashMap<String, String>)b.getSerializable("currentLocation");

        CardView card_viewPF = fragmentView.findViewById(R.id.card_viewPF);
        autocompleteFragment_pickUp.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS, Place.Field.LAT_LNG));
        if(!isNetworkAvailable(getActivity())){
            card_viewPF.setVisibility(GONE);
        }
        else{
            autocompleteFragment_pickUp.setOnPlaceSelectedListener(placeSelectionListener);
        }

        add_place_btn = fragmentView.findViewById(R.id.add_place_button);
        add_place_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if( userLocation.get("longitude")== null){
                        Toast.makeText(getContext(), "Please select a place first" , Toast.LENGTH_LONG).show();
                    }
                    else {

                        //Toast.makeText(getActivity(), userLocation.get("address"), Toast.LENGTH_SHORT).show();

                        addplace(userLocation.get("id"), userLocation.get("name"), userLocation.get("latitude"),
                                userLocation.get("longitude"), placeType, userLocation.get("address"), getActivity(), fromwhere);

//                        if(!result){
//                            Toast.makeText(getActivity(), "Error Updating Place", Toast.LENGTH_SHORT).show();
//                        }

                        if(fromwhere.equals("HomeFragment")){
                            Objects.requireNonNull(getActivity()).onBackPressed();
                        }
                        else if (fromwhere.equals("LocationFragment")){

                           // Toast.makeText(getActivity(), fromwhere, Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getActivity(), LocationActivity.class);
                            Bundle b = new Bundle();
                            b.putString("value" , "Whereto");
                            b.putString("activity" , "HomeFragment");
                            b.putSerializable("currentLocation" , userLocation);
                            i.putExtras(b);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Objects.requireNonNull(getActivity()).finish();
                            AddPlaceFragment.this.startActivity(i);

                        }

                        else if (fromwhere.equals("UpdateLocationFragment")){

                            Objects.requireNonNull(getActivity()).onBackPressed();

                        }

//                        else if (fromwhere.equals("SavedPlace")){
//
//                            // Toast.makeText(getActivity(), fromwhere, Toast.LENGTH_SHORT).show();
//
//                            Objects.requireNonNull(getActivity()).finish();
////                            Intent i = new Intent(getActivity(), SavedPlace.class);
////                            Bundle b = new Bundle();
////                            b.putString("value" , "Whereto");
////                            b.putString("activity" , "HomeFragment");
////                            b.putSerializable("currentLocation" , userLocation);
////                            i.putExtras(b);
////                           // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                            AddPlaceFragment.this.startActivity(i);
//
//                        }


                    }
                }
                catch (Exception e){
                     Toast.makeText(getContext(), "Emulator Error." , Toast.LENGTH_LONG).show();
                }
            }
        });

        boolean gps_enabled = false;
        boolean network_enabled = false;
        LocationManager lm = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);
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

        mapFragment =(SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.mapViewAddPlaceFragment);
        try{
            mapFragment.getMapAsync(this);
        }catch (Exception e){}

        mapViewFrameLayout = fragmentView.findViewById(R.id.mapViewFrameLayout);
        mapViewFrameLayout.setVisibility(GONE);
        add_place_btn.setVisibility(GONE);

        TextView set_location_on_map = fragmentView.findViewById(R.id.set_location_on_map);
        set_location_on_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetworkAvailable(getActivity())){
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else{
                    mapViewFrameLayout.setVisibility(View.VISIBLE);
                    add_place_btn.setVisibility(View.VISIBLE);
                    try{ onMapReady(mMap); } catch (Exception e){}
                }
            }
        });

        return fragmentView;
    }

    PlaceSelectionListener placeSelectionListener = new PlaceSelectionListener() {

        @Override
        public void onPlaceSelected(@NonNull Place place) {
            userLocation.clear();
            userLocation.put("name", place.getName());
            userLocation.put("id", place.getId());
            LatLng latLng = place.getLatLng();
            userLocation.put("latitude", String.valueOf(latLng.latitude));
            userLocation.put("longitude", String.valueOf(latLng.longitude));
            userLocation.put("address", place.getAddress());
          //  Toast.makeText(getActivity(), place.getAddress(), Toast.LENGTH_SHORT).show();

            mapViewFrameLayout.setVisibility(View.VISIBLE);
            add_place_btn.setVisibility(View.VISIBLE);
            try{ onMapReady(mMap); } catch (Exception e){}
        }

        @Override
        public void onError(@NonNull Status status) {
//            Toast.makeText(getContext(), "There was an error fetching the place", Toast.LENGTH_SHORT).show();
        }
    };

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


    public void writeToDB(final String placeID, final String placeName, final String latitude, final String longitude, final String placeAddress, final String placeType) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Location user = bgRealm.createObject(Location.class);
                user.setPlaceID(placeID);
                user.setPlaceName(placeName);
                user.setLatitude(latitude);
                user.setLongitude(longitude);
                user.setPlaceAddress(placeAddress);
                user.setPlaceType(placeType);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
//                Toast.makeText(getActivity(), "Place Saved", Toast.LENGTH_LONG).show();

                // Log.v("Database","Data inserted");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                Log.e("Database", error.getMessage());
            }
        });
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

            String address = obj.getAddressLine(0);
            userLocation.put("address", address);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    LOCATION_PERMISSION_REQUEST_CODE);
            showAlertLocationDisabled();
        }
        else{

            if(!isNetworkAvailable(getActivity())){
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            else{
                // Access to the location has been granted to the app.
                mMap.setMyLocationEnabled(true);
                LocationManager locationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
                android.location.Location location = locationManager.getLastKnownLocation(provider);

                LatLng coordinate = new LatLng(Double.parseDouble(userLocation.get("latitude")), Double.parseDouble(userLocation.get("longitude")));
                getAddress(Double.parseDouble(Objects.requireNonNull(userLocation.get("latitude"))),
                        Double.parseDouble(Objects.requireNonNull(userLocation.get("longitude"))));
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15.0f);
                mMap.animateCamera(yourLocation);
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
                // Permission to access the location is missing.
                PermissionUtils.requestPermission((AppCompatActivity)this.getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                        Manifest.permission.ACCESS_FINE_LOCATION, true);
            } else if (mMap != null) {

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<android.location.Location>() {
                            @Override
                            public void onSuccess(android.location.Location location) {
                                if (location != null) {
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();
                                    userLocation.clear();
                                    userLocation.put("latitude", String.valueOf(latitude));
                                    userLocation.put("longitude", String.valueOf(longitude));
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
    public void onMyLocationClick(@NonNull android.location.Location location) {
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
    }


    public void addplace(final String placeID, final String placeName, final String latitude, final String longitude, final String placeType,
                         final String placeAddress, final Activity activity, final String fromwhere) {

        String url = MainActivity.baseurl+"/update/user/place/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getString("status").equals("200")) {


                              //  Toast.makeText(activity, json.getString("address"), Toast.LENGTH_SHORT).show();
                             //   writeToDB(json.getString("place_id"),json.getString("place_name"), json.getString("latitude"),json.getString("longitude"),json.getString("address"), json.getString("place_type"));

                                if(fromwhere.equals("UpdateLocationFragment")) {
                                    final RealmHelper helper = new RealmHelper(realm);
                                    helper.UpdateUserPlaces(json.getString("place_id"),json.getString("place_name"), json.getString("latitude"),json.getString("longitude"),json.getString("place_address") ,json.getString("place_type"));
                                    Toast.makeText(activity, json.getString("message"), Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    writeToDB(json.getString("place_id"),json.getString("place_name"), json.getString("latitude"),json.getString("longitude"),json.getString("place_address"), json.getString("place_type"));
                                    Toast.makeText(activity, "Place Saved", Toast.LENGTH_SHORT).show();

                                }
                                if (fromwhere.equals("SavedPlace")){

//                            // Toast.makeText(getActivity(), fromwhere, Toast.LENGTH_SHORT).show();
                            Objects.requireNonNull(getActivity()).finish();
                            Intent i = new Intent(getActivity(), SavedPlace.class);
                            Bundle b = new Bundle();
                            b.putString("value" , "Whereto");
                            b.putString("activity" , "HomeFragment");
                            b.putSerializable("currentLocation" , userLocation);
                            i.putExtras(b);
////                           // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            AddPlaceFragment.this.startActivity(i);
//
                        }

                              // Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                //  check1 =1;
                                // flag = true;
                            }
                            else if(json.getString("status").equals("400")){
                                Toast.makeText(activity, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else if(json.getString("status").equals("404")){
                                Toast.makeText(activity, json.getString("message"), Toast.LENGTH_SHORT).show();
                                SettingsFragment.forcedLogout(activity);
                            }
                            else{
                                Toast.makeText(activity, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        //  flag = false;
                    }
                })

        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("place_id", placeID);
                params.put("place_name", placeName);
                params.put("longitude", longitude);
                params.put("latitude", latitude);
                params.put("place_type", placeType);
                params.put("address", placeAddress);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", sharedPreferences.getString("Token", ""));
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }


}
