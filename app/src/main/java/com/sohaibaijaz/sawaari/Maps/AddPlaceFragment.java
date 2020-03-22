package com.sohaibaijaz.sawaari.Maps;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.sohaibaijaz.sawaari.DirectionsJSONParser;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;

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

public class AddPlaceFragment extends Fragment {

    private View fragmentView;

    private GoogleMap mMap;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Map<String, String> dropoffLocation = new HashMap<String, String>();
    private Map<String, String> currentLocation = new HashMap<String, String>();

    private ArrayList<LatLng> markerPoints;
    private FusedLocationProviderClient fusedLocationClient;


    public static AddPlaceFragment newInstance() {

        AddPlaceFragment LF = new AddPlaceFragment();
        Bundle args = new Bundle();
        LF.setArguments(args);

        return LF;
    }

    public AddPlaceFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.add_place_fragment, container, false);

        AutocompleteSupportFragment autocompleteFragment_pickUp = (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.add_location);

        autocompleteFragment_pickUp.setCountry("PK");
        autocompleteFragment_pickUp.setHint("Add Place");

        autocompleteFragment_pickUp.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment_pickUp.setOnPlaceSelectedListener(placeSelectionListener);

        final Button proceed_button = fragmentView.findViewById(R.id.add_place_button);
        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Working" , Toast.LENGTH_LONG).show();
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
//
//                }
//
//            }

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

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        String api_key = "key="+ MainActivity.MAP_VIEW_BUNDLE_KEY;

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + api_key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

}
