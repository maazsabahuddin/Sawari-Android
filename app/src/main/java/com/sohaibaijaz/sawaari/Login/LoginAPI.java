package com.sohaibaijaz.sawaari.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sohaibaijaz.sawaari.Login.LoginPin.app;
import static com.sohaibaijaz.sawaari.Login.LoginPin.baseurl;

public class LoginAPI {

    public static void loginUser(final Activity context, final String phone_number, final LoginCallBack callBack){
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String URL = baseurl+"/check/user/";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Log.i("VOLLEY", response);
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getString("status").equals("200")) {
                            if(json.getString("message").equals("User Exist.")){
                                callBack.onSuccess(json.getString("status"), json.getString("message"),
                                        "");
                            }
                            else{
                                callBack.onSuccess(json.getString("status"), json.getString("message"),
                                        json.getString("token"));
                            }
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
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    params.put("phone_number", phone_number);
                    params.put("app", app);
                    return params;
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePassword(final Activity context, final String pin, final CallBack callBack){
        final SharedPreferences sharedPreferences= Objects.requireNonNull(context).getSharedPreferences(MainActivity.AppPreferences, Context.MODE_PRIVATE);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String token = sharedPreferences.getString("Token", "");

        if(!token.equals("")) {
            try {
                String URL = baseurl+"/password/change/";
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
                }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<>();
                        params.put("pin", pin);
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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(context, "There was problem connecting to the server", Toast.LENGTH_SHORT).show();
        }

    }
}
