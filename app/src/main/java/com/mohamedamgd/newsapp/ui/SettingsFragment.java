package com.mohamedamgd.newsapp.ui;


import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.mohamedamgd.newsapp.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
