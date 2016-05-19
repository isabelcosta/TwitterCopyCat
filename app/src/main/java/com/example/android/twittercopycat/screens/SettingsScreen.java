package com.example.android.twittercopycat.screens;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.android.twittercopycat.fragments.SettingsFragment;

public class SettingsScreen extends PreferenceActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
