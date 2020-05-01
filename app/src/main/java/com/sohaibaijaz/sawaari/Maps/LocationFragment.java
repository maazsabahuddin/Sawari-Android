package com.sohaibaijaz.sawaari.Maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.sohaibaijaz.sawaari.DirectionsJSONParser;
import com.sohaibaijaz.sawaari.Fragments.HomeFragment;
import com.sohaibaijaz.sawaari.Fragments.SavePlacesFragment;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.RealmHelper;
import com.sohaibaijaz.sawaari.Rides.ShowRides;
import com.sohaibaijaz.sawaari.Settings.NotificationsActivity;
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
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;

import static android.view.View.GONE;
import static com.sohaibaijaz.sawaari.Fragments.HomeFragment.isNetworkAvailable;

public class LocationFragment extends Fragment {


    private View fragmentView;
    Realm realm;
    private GoogleMap mMap;

    private HashMap<String, String> dropoffLocation = new HashMap<>();
    private HashMap<String, String> currentLocation = new HashMap<>();

    private FrameLayout spinner_frame;
    private ProgressBar spinner;
    private String placetype;

    private AutocompleteSupportFragment autocompleteFragment_pickUp;
    private AutocompleteSupportFragment autocompleteFragmentdropOff;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private ArrayList<String> placeName;
    ListView placeName_lv;
    private String phone_number;
    RequestQueue requestQueue;
    private String checkhome;
    private  String checkwork;

    TextView textViewhome;
    TextView textViewwork;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_location, container, false);

        spinner_frame = fragmentView.findViewById(R.id.spinner_frame_lf);
        spinner = fragmentView.findViewById(R.id.progressBar_lf);
        spinner.setVisibility(View.GONE);
        spinner_frame.setVisibility(View.GONE);
       // check=fragmentView.findViewById(R.id.textView);


        textViewhome= fragmentView.findViewById(R.id.add_home_place_name);
        textViewwork= fragmentView.findViewById(R.id.add_work_place_name);
        User useroject = User.getInstance();
        requestQueue = Volley.newRequestQueue(fragmentView.getContext());

        phone_number=useroject.getPhoneNumber();
        placeName_lv = fragmentView.findViewById(R.id.place_name_listview);
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        Bundle b = this.getArguments();
        if (b.getSerializable("currentLocation") != null) {
            currentLocation = (HashMap<String, String>) b.getSerializable("currentLocation");
        }

        autocompleteFragment_pickUp = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_from_location);
        autocompleteFragment_pickUp.setCountry("PK");
        autocompleteFragment_pickUp.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment_pickUp.setOnPlaceSelectedListener(placeSelectionListenerFrom);

        autocompleteFragmentdropOff = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_to_location);
        autocompleteFragmentdropOff.setCountry("PK");
        autocompleteFragmentdropOff.setHint("Where To?");
        autocompleteFragmentdropOff.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragmentdropOff.setOnPlaceSelectedListener(placeSelectionListenerTo);

        if(currentLocation.size()==0){
            autocompleteFragment_pickUp.setHint("From?");
        }
        else{
            autocompleteFragment_pickUp.setText("Your location");
        }

        CardView card_viewFrom = fragmentView.findViewById(R.id.card_viewFrom);
        CardView card_viewTo = fragmentView.findViewById(R.id.card_viewTo);

        if(!isNetworkAvailable(getActivity())){
            card_viewFrom.setVisibility(GONE);
            card_viewTo.setVisibility(GONE);
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        LinearLayout add_home = fragmentView.findViewById(R.id.add_home_place);
        LinearLayout add_work = fragmentView.findViewById(R.id.add_work_place);

        LinearLayout saved_place= fragmentView.findViewById(R.id.saved_place);

        add_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                placetype="Home";

                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    showAlertLocationDisabled(getActivity());
                }
                else{
                    dropoffLocation.clear();
                    dropoffLocation=helper.getPlace(placetype);
                    if(dropoffLocation.get("longitude")== null && dropoffLocation.get("latitude")== null)
                    {
                        Intent i = new Intent(getActivity(), LocationActivity.class);
                        Bundle b = new Bundle();
                        b.putString("value" , "Home");
                        b.putString("activity" , "LocationFragment");
                        b.putSerializable("currentLocation" , currentLocation);
                        i.putExtras(b);
                        LocationFragment.this.startActivity(i);
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

                placetype = "Work";
                    if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        showAlertLocationDisabled(getActivity());
                    } else {
                        dropoffLocation.clear();
                        dropoffLocation = helper.getPlace(placetype);
                        if (dropoffLocation.get("longitude") == null && dropoffLocation.get("latitude") == null) {
                            Intent i = new Intent(getActivity(), LocationActivity.class);
                            Bundle b = new Bundle();
                            b.putString("value", "Work");
                            b.putString("activity", "LocationFragment");
                            b.putSerializable("currentLocation", currentLocation);
                            i.putExtras(b);
                            LocationFragment.this.startActivity(i);
                        } else {
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

        saved_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SavedPlace.class);
                Bundle b = new Bundle();
                b.putString("value" , "SavePlace");
                b.putString("activity" , "HomeFragment");
                b.putSerializable("currentLocation" , currentLocation);
                i.putExtras(b);
                LocationFragment.this.startActivity(i);

            }
        });

       // RealmHelper helper = new RealmHelper(realm);
        placeName=helper.getAllRecords();

        checkhome= helper.checkPlace("Home");
        checkwork= helper.checkPlace("Work");
        if(!checkhome.equals(""))
        {
            textViewhome.setText(checkhome);
        }
        if(!checkwork.equals(""))
        {
            textViewwork.setText(checkwork);
        }
        placeName_lv.setEnabled(false);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.activity_listview_savedplaces,R.id.textView_lv,placeName);
        placeName_lv.setAdapter(adapter);

        return fragmentView;
    }


    PlaceSelectionListener  placeSelectionListenerFrom = new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(@NonNull Place place) {
            try {
                currentLocation.clear();
                currentLocation.put("name", place.getName());
                currentLocation.put("id", place.getId());
                LatLng latLng = place.getLatLng();
                currentLocation.put("latitude", String.valueOf(latLng.latitude));
                currentLocation.put("longitude", String.valueOf(latLng.longitude));

                if(dropoffLocation.size()!=0){
                    BusRouteApi(currentLocation, dropoffLocation, spinner_frame, spinner, requestQueue, getContext(), getActivity());
                }
            } catch (Exception e) {

                Toast.makeText(getActivity(), "Please select any place.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(@NonNull Status status) {
//            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
        }
    };


    PlaceSelectionListener placeSelectionListenerTo = new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(@NonNull Place place) {

            try {
                dropoffLocation.clear();
                dropoffLocation.put("name", place.getName());
                dropoffLocation.put("id", place.getId());
                LatLng latLng = place.getLatLng();
                dropoffLocation.put("latitude", String.valueOf(latLng.latitude));
                dropoffLocation.put("longitude", String.valueOf(latLng.longitude));

                Toast.makeText(getActivity(), dropoffLocation.get("latitude"), Toast.LENGTH_LONG).show();

                BusRouteApi(currentLocation, dropoffLocation, spinner_frame, spinner, requestQueue, getContext(), getActivity());
//                Intent i = new Intent(getContext(), ShowRides.class);
//                i.putExtra("pick_up_location", currentLocation);
//                i.putExtra("drop_off_location", dropoffLocation);
//                startActivity(i);

            } catch (Exception e) {
                Toast.makeText(getActivity(), "Please select any place.", Toast.LENGTH_LONG).show();
            }
            finally {
               // realm.close();
            }

        }

        @Override
        public void onError(@NonNull Status status) {
//            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
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

    public void showAlertLocationDisabled(Activity activity) {

        new AlertDialog.Builder(activity)
                .setTitle("Enable Location")
                .setMessage("Sawari can't go on without the device's Location!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_REQUEST_CODE);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    }
                })
                .setIcon(R.mipmap.alert)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Location Accessible", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), NavActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Location not Accessible", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }


    public static void BusRouteApi(final HashMap<String, String> currentLocation, final HashMap<String, String> dropoffLocation,
                                   final FrameLayout spinner_frame, final ProgressBar spinner, RequestQueue requestQueue, final Context context,
                                   final Activity activity){
        try {
            if(currentLocation.size()!=0){
                SharedPreferences sharedPreferences= Objects.requireNonNull(context).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);

                final String token = sharedPreferences.getString("Token", "");

                String URL = MainActivity.baseurl + "/bus/route/";
                JSONObject jsonBody = new JSONObject();

                spinner.setVisibility(View.VISIBLE);
                spinner_frame.setVisibility(View.VISIBLE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("VOLLEY", response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);

                            if (jsonObj.getString("status").equals("200")) {

                                Intent i = new Intent(context, ShowRides.class);
                                i.putExtra("json", jsonObj.toString());
                                i.putExtra("pick_up_location", currentLocation);
                                i.putExtra("drop_off_location", dropoffLocation);
                                spinner.setVisibility(View.GONE);
                                spinner_frame.setVisibility(View.GONE);
                                context.startActivity(i);

                            }
                            else if (jsonObj.getString("status").equals("404")) {
                                spinner.setVisibility(View.GONE);
                                spinner_frame.setVisibility(View.GONE);
                                Toast.makeText(context, jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                                SettingsFragment.forcedLogout(activity);
                            }
                            else if (jsonObj.getString("status").equals("400")) {
                                spinner.setVisibility(View.GONE);
                                spinner_frame.setVisibility(View.GONE);
                                Toast.makeText(context, "Server is temporarily down", Toast.LENGTH_SHORT).show();
                                SettingsFragment.forcedLogout(activity);
                            }

                        } catch (JSONException e) {
                            Log.e("VOLLEY", e.toString());
                            spinner.setVisibility(View.GONE);
                            spinner_frame.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.GONE);
                        spinner_frame.setVisibility(View.GONE);
                        Toast.makeText(context, "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
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

//                    params.put("stop_lat", "24.913363");
//                    params.put("stop_lon", "67.124208");
//                    params.put("start_lat", "24.823343");
//                    params.put("start_lon", "67.029656");

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
                        return 5000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 5000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });

                requestQueue.add(stringRequest);
            }
            else{
                Toast.makeText(context, "Please select current location.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Slow Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }
//    public void writeToDB(final String placeID, final String placeName, final String latitude, final String longitude) {
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm bgRealm) {
//                Location user = bgRealm.createObject(Location.class, placeID.toString());
//               // user.setPlaceID(placeID);
//                user.setPlaceName(placeName);
//                user.setLatitude(latitude);
//                user.setLongitude(longitude);
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                // Transaction was a success.
//                Toast.makeText(getActivity(), "Data inserted", Toast.LENGTH_SHORT).show();
//                Log.v("Database","Data inserted");
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//
//                Log.e("Database", error.getMessage());
//            }
//        });
//    }


}