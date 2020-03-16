
package com.sohaibaijaz.sawaari.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.MainActivity;
import com.sohaibaijaz.sawaari.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class SettingsFragment extends PreferenceFragmentCompat {

    Context context;
    private SharedPreferences sharedPreferences;
    private String publicFirstName;
    private String publicLastName;
    private String publicphone;
    private String publicemail;
    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        token1 = login.token;
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
//        sharedPreferences = getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
        token = sharedPreferences.getString("Token","");

        addPreferencesFromResource(R.xml.preferences);
        account_details();

        final PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("name_cat");
        PreferenceScreen preferenceScreen = this.getPreferenceScreen();

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    public void account_details()
    {
        String url = "http://52.15.104.184/my_details/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {

                                publicFirstName = jsonObject.getString("first_name");
                                publicLastName = jsonObject.getString("last_name");
                                publicphone = jsonObject.getString("phone_number");
                                publicemail = jsonObject.getString("email");

                                publicFirstName= publicFirstName+" "+publicLastName;
                                Preference mPref2 = findPreference("name");
                                mPref2.setTitle(publicFirstName);
                                mPref2.setTitle(publicemail);
                                mPref2.setSummary(publicphone);
                                Log.e("VOLLEY", publicFirstName);

                                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token);
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
}
