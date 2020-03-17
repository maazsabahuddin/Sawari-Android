
package com.sohaibaijaz.sawaari.Fragments;

import android.content.Context;
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

            addPreferencesFromResource(R.xml.preferences);

            firstName = firstName + " " + lastName;

            Preference pref = findPreference("name");
            assert pref != null;
            pref.setTitle(firstName);
            pref.setSummary(email + "\n" + phoneNumber);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        /*setPreferencesFromResource(R.xml.preferences, rootKey);*/
    }

}
