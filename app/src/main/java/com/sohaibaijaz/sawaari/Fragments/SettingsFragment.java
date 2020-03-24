
package com.sohaibaijaz.sawaari.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.sohaibaijaz.sawaari.PrivacyActivity;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.SecurityActivity;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
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
            Preference preference_privacy = findPreference("privacy");
            Preference preference_profile = findPreference("name");
            Preference preference_security = findPreference("security");
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

                    return false;
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
