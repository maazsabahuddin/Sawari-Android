package com.sohaibaijaz.sawaari;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;
import com.sohaibaijaz.sawaari.Settings.Updatepassword;
import com.sohaibaijaz.sawaari.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;

public class UserDetails {

    //String phoneNumber;

    public static void getUserDetails(final Activity context){
        final SharedPreferences sharedPreferences= Objects.requireNonNull(context).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String token = sharedPreferences.getString("Token", "");
        final Realm  realm = Realm.getDefaultInstance();
        if(!token.equals("")) {

           //Getting user details
            try {
                final String URL = MainActivity.baseurl + "/user/details/";
                JSONObject jsonBody = new JSONObject();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("VOLLEY", response);
                        try {
                            JSONObject json = new JSONObject(response);

                            if (json.getString("status").equals("200")) {

                                Log.i("Abcs","aajaoooouser");


                                final RealmHelper helper = new RealmHelper(realm);

                                User user = User.getInstance();
                                user.setFirstName(json.getString("first_name"));
                                user.setLastName(json.getString("last_name"));
                                user.setPhoneNumber(json.getString("phone_number"));
                                user.setEmail(json.getString("email"));

                                String phoneNumber = json.getString("phone_number");
                                String firstName = json.getString("first_name");
                                String lastName = json.getString("last_name");
                                String email= json.getString("email");

                                helper.InsertUserDetails(context, phoneNumber, firstName, lastName, email);

                            }
                            else if (json.getString("status").equals("404")) {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                                SettingsFragment.signout(context);
                            }
                            else if(json.getString("status").equals("401")){
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e("VOLLEY", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        Log.e("VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();


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
                Toast.makeText(context, "Slow Internet Connection.", Toast.LENGTH_SHORT).show();
            }




        }
        else{
            Toast.makeText(context, "There was problem connecting to the server", Toast.LENGTH_SHORT).show();
        }
    }


    public static void getUserRides(final Activity context){
        final SharedPreferences sharedPreferences= Objects.requireNonNull(context).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String token = sharedPreferences.getString("Token", "");

        if(!token.equals("")) {

            //Getting user rides
            try {
                String URL = MainActivity.baseurl + "/user/rides/";
                JSONObject jsonBody = new JSONObject();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("VOLLEY", response);
                        try {
                            JSONObject json = new JSONObject(response);

                            if (json.getString("status").equals("200")) {
                             //   Log.i("Abcs","aajaorides");

                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.remove("user_rides");
                                edit.putString("user_rides", json.getJSONArray("reservations").toString());
                                edit.apply();
                            }
                            else if (json.getString("status").equals("404")) {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                                SettingsFragment.signout(context);
                            }
                            else if(json.getString("status").equals("401")){
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e("VOLLEY", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        Log.e("VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
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
                Toast.makeText(context, "Slow Internet Connection.", Toast.LENGTH_SHORT).show();
            }


        }
        else{
            Toast.makeText(context, "There was problem connecting to the server", Toast.LENGTH_SHORT).show();
        }
    }


    public static void getUserPlaces(final Activity context){

        final SharedPreferences sharedPreferences= Objects.requireNonNull(context).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String token = sharedPreferences.getString("Token", "");
        final Realm  realm = Realm.getDefaultInstance();

        if(!token.equals("")) {

            //Getting user details
            try {
                final String URL = MainActivity.baseurl + "/user/places/";
                JSONObject jsonBody = new JSONObject();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("VOLLEY", response);
                        try {
                            JSONObject json = new JSONObject(response);

                            if (json.getString("status").equals("200")) {


                                final RealmHelper helper = new RealmHelper(realm);

                                JSONArray places = json.getJSONArray("places");

                               // String finalname= name.getString("place_name");
                               // String abc= places.substring("place_name")
                              //  JSONObject jsonO = new JSONObject(places);
                             //   Log.i("Abcs",);
                                //String placeid=places.getString(0);
                               // Log.i("Abcs",placeid);

                               // HashMap<String, String> ride_hashMap = new HashMap<>();

                                for(int i=0; i < places.length(); i++)
                                {
                                    JSONObject placedetails= places.getJSONObject(i);
                                    String placename= placedetails.getString("place_name");
                                    String placeid= placedetails.getString("place_id");
                                    String longitude= placedetails.getString("longitude");
                                    String latitude= placedetails.getString("latitude");
                                    String placetype= placedetails.getString("place_type");
                                   // Log.i("Abcs",finalname);

                                    //ride_hashMap.put("abc",places.getString(i));
                                   // ride.getRides().get(i).getVehicleNoPlate());
                                  // String placename = ("place_name");
//                                    String placetype= jsonO.getString("place_type");
//                                    String longitude= jsonO.getString("longitude");
//                                    String latitude= jsonO.getString("latitude");
                                    helper.insertUserPlaces(context, placeid, placename, latitude, longitude,placetype);

                                }

                               // User user1= User.getInstance();
                              //  ArrayList placeid = json.getJSONArray("places");


                              //  String phone= user1.getPhoneNumber();

                               // Log.i("Abcs","aajaoplace");
                              // Toast.makeText(context, "kiun nhi?", Toast.LENGTH_SHORT).show();

                               // helper.insertUserPlaces(context, placeid, placename, latitude, longitude,placetype,"+923363343632");

                            }
                            else if (json.getString("status").equals("404")) {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                                SettingsFragment.signout(context);
                            }
                            else if(json.getString("status").equals("401")){
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e("VOLLEY", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        Log.e("VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();


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
                Toast.makeText(context, "Slow Internet Connection.", Toast.LENGTH_SHORT).show();
            }




        }
        else{
            Toast.makeText(context, "There was problem connecting to the server", Toast.LENGTH_SHORT).show();
        }
    }

}
