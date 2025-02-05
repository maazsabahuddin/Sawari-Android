package com.sohaibaijaz.sawaari;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserDetails {


    public static void getUserDetails(final Context context){
        final SharedPreferences sharedPreferences= Objects.requireNonNull(context).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String token = sharedPreferences.getString("Token", "");

        if(!token.equals("")) {

           //Getting user details
            try {
                String URL = MainActivity.baseurl + "/my_details/";
                JSONObject jsonBody = new JSONObject();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("VOLLEY", response.toString());
                        try {
                            JSONObject json = new JSONObject(response);

                            if (json.getString("status").equals("200")) {
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.putString("first_name", json.getString("first_name"));
                                edit.putString("last_name", json.getString("last_name"));
                                edit.putString("email", json.getString("email"));
                                edit.putString("phone_number", json.getString("phone_number"));
                                edit.apply();
                            } else if (json.getString("status").equals("400") || json.getString("status").equals("404") || json.getString("status").equals("405")) {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e("VOLLEY", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
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


    public static void getUserRides(final Context context){
        final SharedPreferences sharedPreferences= Objects.requireNonNull(context).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String token = sharedPreferences.getString("Token", "");

        if(!token.equals("")) {

            //Getting user rides
            try {
                String URL = MainActivity.baseurl + "/user_rides/";
                JSONObject jsonBody = new JSONObject();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("VOLLEY", response.toString());
                        try {
                            JSONObject json = new JSONObject(response);

                            if (json.getString("status").equals("200")) {
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.remove("user_rides");
                                edit.putString("user_rides", json.getJSONArray("reservations").toString());
                                edit.apply();
                            } else if (json.getString("status").equals("400") || json.getString("status").equals("404") || json.getString("status").equals("405")) {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e("VOLLEY", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
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
