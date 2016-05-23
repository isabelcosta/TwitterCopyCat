package com.example.android.twittercopycat.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.example.android.twittercopycat.R;
import com.example.android.twittercopycat.application.TwitterCopyCatApplication;
import com.example.android.twittercopycat.helpers.Constants;

/**
 * Created by IsabelCosta on 11-05-2016.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = "SettingsFragment";

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
//
//        // Update settings preferences summaries
//        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
//            Preference preference = getPreferenceScreen().getPreference(i);
//            if (preference instanceof PreferenceGroup) {
//                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
//                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
//                    Preference singlePref = preferenceGroup.getPreference(j);
//                    updatePreferenceOnSummary(singlePref, singlePref.getKey());
//                }
//            } else {
//                updatePreferenceOnSummary(preference, preference.getKey());
//            }
//        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreferenceOnApplication(findPreference(key), key);
        updatePreferenceOnSummary(findPreference(key), key);
    }

    private void updatePreferenceOnSummary(Preference preference, String key) {
        SharedPreferences sharedPrefs = TwitterCopyCatApplication.getInstance().getAppSharedPreferences();

        if (preference == null) return;

        if (preference.getKey().equals(Constants.NUMBER_OF_TWEETS_PREF)) {
            // Number of tweets NumberPickerPreference
            preference.setSummary(String.valueOf(sharedPrefs.getInt(key, 5)));
            Log.d(LOG_TAG, Constants.NUMBER_OF_TWEETS_PREF + " - " + String.valueOf(sharedPrefs.getInt(key, 5)));

        } else if (preference.getKey().equals(Constants.SYNC_FREQUENCY_PREF)) {
            // Sync Frequency ListPreference
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setSummary(listPreference.getEntry());
            Log.d(LOG_TAG, Constants.SYNC_FREQUENCY_PREF + " - " + listPreference.getValue());

        }
//        } else if (preference instanceof SwitchPreference) {
//            // Wifi Only SwitchPreference
//            preference.setSummary(String.valueOf(sharedPrefs.getBoolean(key, false)));
//        }
    }

    private void updatePreferenceOnApplication(Preference preference, String key) {
        TwitterCopyCatApplication app = TwitterCopyCatApplication.getInstance();
        SharedPreferences sharedPrefs = app.getAppSharedPreferences();
        SharedPreferences defaultSharedPrefs = getPreferenceScreen().getSharedPreferences();
        SharedPreferences.Editor editor = sharedPrefs.edit();

        if (preference == null) return;

        if (preference.getKey().equals(Constants.NUMBER_OF_TWEETS_PREF)) {
            // Number of tweets NumberPickerPreference
            app.setNumberOfTweetsPref(defaultSharedPrefs.getInt(key, 5));
            editor.putInt(key,defaultSharedPrefs.getInt(key, 5));

        } else if (preference.getKey().equals(Constants.WIFI_ONLY_PREF)) {
            // Wifi Only SwitchPreference
            app.setWifiOnlyPref(defaultSharedPrefs.getBoolean(key, false));
            editor.putBoolean(key,defaultSharedPrefs.getBoolean(key, false));

        } else if (preference.getKey().equals(Constants.SYNC_FREQUENCY_PREF)) {
            // Sync Frequency ListPreference
            ListPreference listPreference = (ListPreference) preference;
            app.setSyncFrequencyPref(Integer.valueOf(listPreference.getValue()));
            editor.putInt(key,Integer.valueOf(listPreference.getValue()));

        }

        editor.apply();
    }
}
