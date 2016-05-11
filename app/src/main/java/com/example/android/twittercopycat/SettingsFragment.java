package com.example.android.twittercopycat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

/**
 * Created by IsabelCosta on 11-05-2016.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_frag);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // For proper lifecycle management in the activity
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // For proper lifecycle management in the activity
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        // Update settings preferences summaries
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    Preference singlePref = preferenceGroup.getPreference(j);
                    updatePreferenceOnSummary(singlePref, singlePref.getKey());
                }
            } else {
                updatePreferenceOnSummary(preference, preference.getKey());
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreferenceOnSummary(findPreference(key), key);
    }

    private void updatePreferenceOnSummary(Preference preference, String key) {
        SharedPreferences sharedPrefs = getPreferenceManager().getSharedPreferences();

        if (preference == null) return;

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setSummary(listPreference.getEntry());

        } else if (preference instanceof NumberPickerPreference) {
            preference.setSummary(String.valueOf(sharedPrefs.getInt(key, 15)));
        }
//        } else if (preference instanceof SwitchPreference) {
//            preference.setSummary(String.valueOf(sharedPrefs.getBoolean(key, false)));
//        }
    }
}
