package com.sohaibaijaz.sawaari;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.Fragments.CallBack;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;
import com.sohaibaijaz.sawaari.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;

public class UserDetails {

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
                                SettingsFragment.forcedLogout(context);
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


    public static void getUserRides(final Activity context, final CallBack callBack){
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
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user_rides", json.getJSONArray("reservations").toString());
                                editor.apply();
                                callBack.onSuccess(json.getString("status"), json.getString("message"));
                            }
                            else if (json.getString("status").equals("404")) {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                                SettingsFragment.forcedLogout(context);
                            }
                            else if(json.getString("status").equals("401")){
                                SettingsFragment.forcedLogout(context);
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

                                for(int i=0; i < places.length(); i++)
                                {
                                    JSONObject placedetails= places.getJSONObject(i);
                                    String placename= placedetails.getString("place_name");
                                    String placeid= placedetails.getString("place_id");
                                    String longitude= placedetails.getString("longitude");
                                    String latitude= placedetails.getString("latitude");
                                    String placetype= placedetails.getString("place_type");

                                    helper.insertUserPlaces(context, placeid, placename, latitude, longitude,placetype);

                                }
                            }
                            else if (json.getString("status").equals("404")) {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                                SettingsFragment.forcedLogout(context);
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

    public static void verifyUser(final Activity context, final String phone_number, final String otp, final CallBack callBack){

        final SharedPreferences sharedPreferences= Objects.requireNonNull(context).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String token = sharedPreferences.getString("Token", "");

        if(!token.equals("")) {

            try {
                final String URL = MainActivity.baseurl + "/verify/user/";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("VOLLEY", response);
                        try {
                            JSONObject json = new JSONObject(response);

                            if (json.getString("status").equals("200")) {
                                callBack.onSuccess(json.getString("status"), json.getString("message"));
                            }
                            else{
                                callBack.onFailure(json.getString("status"), json.getString("message"));
                            }

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
                        params.put("email_or_phone", phone_number);
                        params.put("otp", otp);
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
