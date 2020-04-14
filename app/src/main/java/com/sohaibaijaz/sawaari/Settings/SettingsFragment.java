
package com.sohaibaijaz.sawaari.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.model.User;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    public static String firstName;
    public static String lastName;
    private String phoneNumber;
    private String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
            final String token = sharedPreferences.getString("Token", "");
            User user= User.getInstance();
            phoneNumber = user.getPhoneNumber();
            email = user.getEmail();


            Toast.makeText(getActivity(), user.getLastName(), Toast.LENGTH_SHORT).show();

            firstName= user.getFirstName() ;
            lastName=user.getLastName();

            addPreferencesFromResource(R.xml.account_setting_preference);

            firstName = firstName + " " + lastName;
            Preference preference_privacy = findPreference("privacy");
            Preference preference_profile = findPreference("name");
            Preference preference_security = findPreference("security");
            Preference preference_signout = findPreference("signOutPreference");

            assert preference_profile != null;
            preference_profile.setTitle(firstName);
            
            if(email.equals("")) {
                preference_profile.setSummary(phoneNumber);
            }
            else{
                preference_profile.setSummary(email + "\n" + phoneNumber);

            }

            preference_profile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), UpdateActivity.class);
                    SettingsFragment.this.startActivity(i);

                    return true;
                }
            });
            preference_privacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), PrivacyActivity.class);
                    SettingsFragment.this.startActivity(i);

                    return true;
                }
            });

            preference_security.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), SecurityActivity.class);
                    SettingsFragment.this.startActivity(i);
                    return true;
                }
            });

            preference_signout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
                    try {
                        String URL = MainActivity.baseurl + "/logout/";
//                        spinner.setVisibility(View.VISIBLE);
//                        spinner_frame.setVisibility(View.VISIBLE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                spinner.setVisibility(View.GONE);
//                                spinner_frame.setVisibility(View.GONE);
                                Log.i("VOLLEY", response.toString());
                                try {
                                    JSONObject json = new JSONObject(response);
                                    if (json.getString("status").equals("200")) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove("Token");
                                        editor.remove("first_name");
                                        editor.remove("last_name");
                                        editor.remove("email");
                                        editor.remove("phone_number");
                                        editor.remove("user_rides");
                                        editor.apply();
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                                        Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getActivity().finishAffinity();
                                        startActivity(intent);

                                      // getActivity().finish();

                                    }
                                    else if (json.getString("status").equals("400")||json.getString("status").equals("404")) {
                                        Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Log.e("VOLLEY", e.toString());

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                                spinner.setVisibility(View.GONE);
//                                spinner_frame.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                                Log.e("VOLLEY", error.toString());
                            }
                        }){
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<String, String>();

                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("Authorization", token);
                                return params;
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
                        e.printStackTrace();
                    }
                    return true;
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        /*setPreferencesFromResource(R.xml.account_setting_preference, rootKey);*/
    }


}
