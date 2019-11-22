package com.sohaibaijaz.sawaari.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.MapActivity;
import com.sohaibaijaz.sawaari.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

        @Override
        public String toString(){
            return "Bus number plate: "+bus_no_plate+"\n"+"Seats left: "+seats_left;
        }



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences= Objects.requireNonNull(this.getActivity()).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("Token", "");
        System.out.println("HomeFragment: "+token);
        if(!token.equals(""))
        {
        final RequestQueue requestQueue = Volley.newRequestQueue(fragmentView.getContext());

        Button dropoff_btn = (Button)fragmentView.findViewById(R.id.btn_dropoff);
        final EditText source_txt = (EditText) fragmentView.findViewById(R.id.txt_source);
        final EditText destination_txt = (EditText) fragmentView.findViewById(R.id.txt_destination);
       // final TextView textView = (TextView)fragmentView.findViewById(R.id.tv_buses);
            final ListView buses_list= (ListView) fragmentView.findViewById(R.id.list_buses);

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

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
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
}
