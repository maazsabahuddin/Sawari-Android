
package com.sohaibaijaz.sawaari.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.Login.LoginView;
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
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String token;
    private FrameLayout spinner_frame;
    private ProgressBar spinner;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        try{
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.account_setting_preference);

            Preference preference_privacy = findPreference("privacy");
            Preference preference_profile = findPreference("name");
            Preference preference_security = findPreference("security");
            Preference preference_signOut = findPreference("signOutPreference");
            Preference preference_updateHomeWork = findPreference("updateHomeWork");

            preference_updateHomeWork.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), UpdateHomeAndWorkActivity.class);
                    SettingsFragment.this.startActivity(i);
                    return true;
                }
            });



            Objects.requireNonNull(preference_privacy).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
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

            preference_signOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SettingsFragment.signOut(getActivity());
                    return true;
                }
            });

            User user= User.getInstance();
            phoneNumber = user.getPhoneNumber();
            email = user.getEmail();
            firstName = user.getFirstName();
            lastName = user.getLastName();

            firstName = firstName + " " + lastName;

            if(!(firstName.equals(""))) {
                Objects.requireNonNull(preference_profile).setTitle(firstName);
            }

            if(!(phoneNumber.equals(""))) {
                if (email.equals("")) {
                    Objects.requireNonNull(preference_profile).setSummary(phoneNumber);
                } else {
                    Objects.requireNonNull(preference_profile).setSummary(email + "\n" + phoneNumber);
                }
            }

            Objects.requireNonNull(preference_profile).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), UpdateActivity.class);
                    SettingsFragment.this.startActivity(i);

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

    public static void forcedLogout(final Activity activity){
        final SharedPreferences sharedPreferences = Objects.requireNonNull(activity).getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("Token");
        editor.apply();
        editor.clear();
        Intent intent = new Intent(activity, LoginView.class);
        activity.finishAffinity();
        activity.startActivity(intent);
    }

    public static void signOut(final Activity context){
        final SharedPreferences sharedPreferences = Objects.requireNonNull(context).getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("Token", "");
        final RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context));
        try {
            String URL = MainActivity.baseurl + "/logout/";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("VOLLEY", response);
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getString("status").equals("200")) {
                           SettingsFragment.forcedLogout(context);
                        }
                        else if (json.getString("status").equals("404")) {
                            Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                            SettingsFragment.forcedLogout(context);
                        }
                        else if (json.getString("status").equals("400")) {
                            Toast.makeText(context, "Server is temporarily down", Toast.LENGTH_SHORT).show();
                            SettingsFragment.forcedLogout(context);
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
    }

}
