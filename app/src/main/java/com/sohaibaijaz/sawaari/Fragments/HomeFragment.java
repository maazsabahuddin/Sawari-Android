package com.sohaibaijaz.sawaari.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.NavActivity;
import com.sohaibaijaz.sawaari.PermissionUtils;
import com.sohaibaijaz.sawaari.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback{

    private String source = "";
    private String destination = "";


    private FrameLayout spinner_frame;
    private ProgressBar spinner;


    private GoogleMap mMap;

    private boolean mPermissionDenied = false;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;





    public class BusInfo {
        String bus_no_plate;
        int seats_left;

        public BusInfo(String bus_no_plate, int seats_left) {
            this.bus_no_plate = bus_no_plate;
            this.seats_left = seats_left;
        }

        @Override
        public String toString(){
            return "Bus number plate: "+bus_no_plate+"\n"+"Seats left: "+seats_left;
        }



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment mapFragment =(SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);



        CardView cardView = fragmentView.findViewById(R.id.card_view);
        SharedPreferences sharedPreferences= Objects.requireNonNull(this.getActivity()).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("Token", "");


        System.out.println("HomeFragment: "+token);
        if(!token.equals(""))
        {
        final RequestQueue requestQueue = Volley.newRequestQueue(fragmentView.getContext());

        Button dropoff_btn = (Button)cardView.findViewById(R.id.btn_dropoff);
        final EditText source_txt = (EditText)cardView.findViewById(R.id.txt_source);
        final EditText destination_txt = (EditText) cardView.findViewById(R.id.txt_destination);
        final ListView buses_list= (ListView) cardView.findViewById(R.id.list_buses);

            spinner = (ProgressBar)fragmentView.findViewById(R.id.progressBar1);
            spinner.setVisibility(View.GONE);
            spinner_frame = fragmentView.findViewById(R.id.spinner_frame);
            spinner_frame.setVisibility(View.GONE);

        dropoff_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                destination_txt.setCursorVisible(false);

                source = source_txt.getText().toString();
                destination = destination_txt.getText().toString();

                final ArrayList<BusInfo> list = new ArrayList<BusInfo>();
                final ArrayAdapter<BusInfo> list_adapter = new ArrayAdapter<BusInfo>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);

                if (source.equals("") || destination.equals("")) {
                    Toast.makeText(getContext(), "Source/Destination fields can't be empty.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String URL = MainActivity.baseurl +"/display_buses/";
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("from", source);
                        jsonBody.put("to", destination);
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

                                    if (json.getString("status").equals("400") || json.getString("status").equals("404")) {
                                        Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        JSONArray array = json.getJSONArray("buses");

                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject object1 = array.getJSONObject(i);
                                            String vehicle_no_plate = object1.getString("vehicle_id__vehicle_no_plate");
                                            int seats_left = Integer.parseInt(object1.getString("seats_left"));

                                            list.add(new BusInfo(vehicle_no_plate, seats_left));
                                        }

                                        buses_list.setAdapter(list_adapter);
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
                                params.put("from", source);
                                params.put("to", destination);
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
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {


        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
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
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getFragmentManager(), "dialog");
    }




}
