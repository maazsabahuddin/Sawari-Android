package com.sohaibaijaz.sawaari.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.MapActivity;
import com.sohaibaijaz.sawaari.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private String source = "";
    private String destination = "";

    public class BusInfo {
        String bus_no_plate;
        int seats_left;

        public BusInfo(String bus_no_plate, int seats_left) {
            this.bus_no_plate = bus_no_plate;
            this.seats_left = seats_left;
        }

        public BusInfo() {
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle extras = this.getArguments();

        if(extras!=null)
        {
        final String token = extras.getString("Token");
        final RequestQueue requestQueue = Volley.newRequestQueue(fragmentView.getContext());

        Button dropoff_btn = fragmentView.findViewById(R.id.dropoff_btn);
        final EditText source_txt = fragmentView.findViewById(R.id.source_txt);
        final EditText destination_txt = fragmentView.findViewById(R.id.destination_txt);
        final TextView textView = fragmentView.findViewById(R.id.textView);

        destination_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == destination_txt.getId()) {
                    destination_txt.setCursorVisible(true);
                }
            }
        });

        dropoff_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                destination_txt.setCursorVisible(false);

                source = source_txt.getText().toString();
                destination = destination_txt.getText().toString();
                final ArrayList<HomeFragment.BusInfo> list = new ArrayList<HomeFragment.BusInfo>();

                if (source.equals("") || destination.equals("")) {
                    Toast.makeText(view.getContext(), "Source/Destination fields can't be empty.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String URL = "https://sawaari.serveo.net/display_buses/";
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("from", source);
                        jsonBody.put("to", destination);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("VOLLEY", response.toString());
                                try {
                                    JSONObject json = new JSONObject(response);
                                    JSONArray array = json.getJSONArray("buses");

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object1 = array.getJSONObject(i);
                                        String vehicle_no_plate = object1.getString("vehicle_id__vehicle_no_plate");
                                        String seats_left = object1.getString("seats_left");
                                        list.add(new HomeFragment.BusInfo(vehicle_no_plate, Integer.parseInt(seats_left)));
                                    }
                                    textView.setText("Bus no Plate: " + list.get(0).bus_no_plate + " \nSeats left: " + list.get(0).seats_left);

                                    if (json.getString("status").equals("400") || json.getString("status").equals("404")) {
                                        Toast.makeText(fragmentView.getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                    }

//                                   Intent myIntent = new Intent(MapActivity.this, BusActivity.class);
//                                   myIntent.putExtra("Token", token);
//                                   Bundle bundle = new Bundle();
//                                   bundle.putParcelableArrayList("StudentDetails", list);
//                                   intent.putExtras(bundle);
//                                   startActivity(myIntent);
//                                   finish();

                                } catch (JSONException e) {
                                    Log.e("VOLLEY", e.toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
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
                        Toast.makeText(fragmentView.getContext(), "Slow Internet Connection.", Toast.LENGTH_SHORT).show();
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
}
