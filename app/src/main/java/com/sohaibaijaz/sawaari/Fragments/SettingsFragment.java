
package com.sohaibaijaz.sawaari.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.sohaibaijaz.sawaari.R;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPreferences;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
            firstName = sharedPreferences.getString("first_name", "");
            lastName = sharedPreferences.getString("last_name", "");
            phoneNumber = sharedPreferences.getString("phone_number", "");
            email = sharedPreferences.getString("email", "");

            addPreferencesFromResource(R.xml.account_setting_preference);

            firstName = firstName + " " + lastName;

            Preference preference_profile = findPreference("name");
            assert preference_profile != null;
            preference_profile.setTitle(firstName);
            preference_profile.setSummary(email + "\n" + phoneNumber);

            preference_profile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
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

}
