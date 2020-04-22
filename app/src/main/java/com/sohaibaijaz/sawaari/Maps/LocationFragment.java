package com.sohaibaijaz.sawaari.Maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.RealmHelper;
import com.sohaibaijaz.sawaari.Rides.ShowRides;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;
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

public class LocationFragment extends Fragment {


    private View fragmentView;
    Realm realm;
    private GoogleMap mMap;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private HashMap<String, String> dropoffLocation = new HashMap<>();
    private HashMap<String, String> currentLocation = new HashMap<>();

    private FrameLayout spinner_frame;
    private ProgressBar spinner;
    private String placetype;

    private ArrayList<LatLng> markerPoints;
    private AutocompleteSupportFragment autocompleteFragment_pickUp;
    private AutocompleteSupportFragment autocompleteFragmentdropOff;

    private ArrayList<String> placeName;
    ListView placeName_lv;
    private String phone_number;

    private FusedLocationProviderClient fusedLocationClient;

    public static LocationFragment newInstance() {

        LocationFragment LF = new LocationFragment();
        Bundle args = new Bundle();
        LF.setArguments(args);

        return LF;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_location, container, false);

//        spinner_frame = fragmentView.findViewById(R.id.spinner_frame_lf);
//        spinner = fragmentView.findViewById(R.id.progressBar_lf);
//        spinner.setVisibility(View.GONE);
//        spinner_frame.setVisibility(View.GONE);

        User useroject = User.getInstance();

        phone_number=useroject.getPhoneNumber();
        placeName_lv = fragmentView.findViewById(R.id.place_name_listview);
        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);

        autocompleteFragment_pickUp = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_from_location);
        autocompleteFragment_pickUp.setCountry("PK");
        autocompleteFragment_pickUp.setText("Your location");
        autocompleteFragment_pickUp.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment_pickUp.setOnPlaceSelectedListener(placeSelectionListenerFrom);

        autocompleteFragmentdropOff = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_to_location);
        autocompleteFragmentdropOff.setCountry("PK");
        autocompleteFragmentdropOff.setHint("Where To?");
        autocompleteFragmentdropOff.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragmentdropOff.setOnPlaceSelectedListener(placeSelectionListenerTo);

        Bundle b = this.getArguments();
        if (b.getSerializable("currentLocation") != null) {
            currentLocation = (HashMap<String, String>) b.getSerializable("currentLocation");
        }

        LinearLayout add_home = fragmentView.findViewById(R.id.add_home_place);
        LinearLayout add_work = fragmentView.findViewById(R.id.add_work_place);

        add_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placetype="Home";
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
                    BusRouteApi(currentLocation, dropoffLocation, spinner_frame, spinner);
                }

            }
        });

        add_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                placetype="Work";
                dropoffLocation.clear();
                dropoffLocation=helper.getPlace(placetype);
                if(dropoffLocation.get("longitude")== null && dropoffLocation.get("latitude")== null)
                {
                    Intent i = new Intent(getActivity(), LocationActivity.class);
                    Bundle b = new Bundle();
                    b.putString("value" , "Work");
                    b.putString("activity" , "LocationFragment");
                    b.putSerializable("currentLocation" , currentLocation);
                    i.putExtras(b);
                    LocationFragment.this.startActivity(i);
                }
                else {
                    BusRouteApi(currentLocation, dropoffLocation, spinner_frame, spinner);
                }
            }
        });

       // RealmHelper helper = new RealmHelper(realm);
        placeName=helper.getAllRecords(phone_number);

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.activity_listview_savedplaces,R.id.textView,placeName);
        placeName_lv.setAdapter(adapter);

        return fragmentView;
    }


    PlaceSelectionListener placeSelectionListenerFrom = new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(@NonNull Place place) {
            try {
                currentLocation.clear();
                currentLocation.put("name", place.getName());
                currentLocation.put("id", place.getId());
                LatLng latLng = place.getLatLng();
                currentLocation.put("latitude", String.valueOf(latLng.latitude));
                currentLocation.put("longitude", String.valueOf(latLng.longitude));

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

                BusRouteApi(currentLocation, dropoffLocation, spinner_frame, spinner);
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


//            if (dropoffLocation.get("latitude") == null || currentLocation.get("longitude") == null) {
//                Toast.makeText(getContext(), "Select current and drop off location first!", Toast.LENGTH_SHORT).show();
//            }
//            else if(dropoffLocation.get("latitude") != null && currentLocation.get("longitude") != null){
//
//
//                markerPoints.clear();
//                mMap.clear();
//
//                LatLng start = new LatLng(Double.parseDouble(currentLocation.get("latitude")), Double.parseDouble(currentLocation.get("longitude")));
//                LatLng stop = new LatLng(Double.parseDouble(dropoffLocation.get("latitude")), Double.parseDouble(dropoffLocation.get("longitude")));
//
//                markerPoints.add(start);
//                markerPoints.add(stop);
//                MarkerOptions options = new MarkerOptions();
//
//                options.position(start);
//                options.position(stop);
//
//
//                if(markerPoints.size() >=2 ){
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
//
//                    mMap.addMarker(options);
//
//                    LatLng origin = markerPoints.get(0);
//                    LatLng dest = markerPoints.get(1);
//
//                    // Getting URL to the Google Directions API
//                    String url = getDirectionsUrl(origin, dest);
//
//                    DownloadTask downloadTask = new DownloadTask();
//
//                    // Start downloading json data from Google Directions API
//                    downloadTask.execute(url);
//
//                }
//
//            }

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

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        String api_key = "key=" + NavActivity.MAP_VIEW_BUNDLE_KEY;

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + api_key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    public void BusRouteApi(final HashMap<String, String> currentLocation, final HashMap<String, String> dropoffLocation,
                            final FrameLayout spinner_frame, final ProgressBar spinner){
        try {
            SharedPreferences sharedPreferences= Objects.requireNonNull(this.getActivity()).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);

            final String token = sharedPreferences.getString("Token", "");
            RequestQueue requestQueue = Volley.newRequestQueue(fragmentView.getContext());

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

                            Intent i = new Intent(getContext(), ShowRides.class);
                            i.putExtra("json", jsonObj.toString());
                            i.putExtra("pick_up_location", currentLocation);
                            i.putExtra("drop_off_location", dropoffLocation);
                            spinner.setVisibility(View.GONE);
                            spinner_frame.setVisibility(View.GONE);
                            startActivity(i);

                        } else if (jsonObj.getString("status").equals("400")) {
                            Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        else if(jsonObj.getString("status").equals("404")){
                            Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                            SettingsFragment.signout(getActivity());
                            // flag = false;
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

//                    params.put("start_lat", currentLocation.get("latitude"));
//                    params.put("start_lon", currentLocation.get("longitude"));
//                    params.put("stop_lat", dropoffLocation.get("latitude"));
//                    params.put("stop_lon", dropoffLocation.get("longitude"));

                    params.put("stop_lat", "24.913363");
                    params.put("stop_lon", "67.124208");
                    params.put("start_lat", "24.823343");
                    params.put("start_lon", "67.029656");

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