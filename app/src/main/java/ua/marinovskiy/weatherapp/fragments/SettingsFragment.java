package ua.marinovskiy.weatherapp.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import ua.marinovskiy.weatherapp.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
