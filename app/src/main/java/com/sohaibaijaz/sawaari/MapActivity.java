package com.sohaibaijaz.sawaari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity {

    private String source = "";
    private String destination = "";

    public class BusInfo {
        String bus_no_plate;
        int seats_left;

        public BusInfo(){ }

        public BusInfo(String bus_no_plate, int seats_left) {
            this.bus_no_plate=bus_no_plate;
            this.seats_left=seats_left;
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();
        final String token = extras.getString("Token");
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        Button dropoff_btn = findViewById(R.id.dropoff_btn);
        final EditText source_txt = findViewById(R.id.source_txt);
        final EditText destination_txt = findViewById(R.id.destination_txt);
        final TextView textView = findViewById(R.id.textView);

        destination_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == destination_txt.getId())
                {
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
                final ArrayList<BusInfo> list=new ArrayList<BusInfo>();

                if(source.equals("") || destination.equals("")){
                    Toast.makeText(MapActivity.this, "Source/Destination fields can't be empty.", Toast.LENGTH_SHORT).show();
                }
                else{
                   try{
                       String URL = "https://cc-v6ka.localhost.run/display_buses/";
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

                                   for(int i=0; i<array.length(); i++) {
                                       JSONObject object1=array.getJSONObject(i);
                                       String vehicle_no_plate = object1.getString("vehicle_id__vehicle_no_plate");
                                       String seats_left = object1.getString("seats_left");
                                       list.add(new BusInfo(vehicle_no_plate, Integer.parseInt(seats_left)));
                                   }
                                   textView.setText("Bus no Plate: "+list.get(0).bus_no_plate+ " \nSeats left: "+list.get(0).seats_left);

                                   if (json.getString("status").equals("400")||json.getString("status").equals("404")) {
                                       Toast.makeText(MapActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
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
                       }){
                           @Override
                           protected Map<String,String> getParams(){
                               Map<String,String> params = new HashMap<String, String>();
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
                   }
                   catch (Exception e){
                       Toast.makeText(MapActivity.this, "Slow Internet Connection.", Toast.LENGTH_SHORT).show();
                   }
                }

            }
        });
    }
}
