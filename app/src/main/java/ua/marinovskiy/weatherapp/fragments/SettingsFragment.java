package ua.marinovskiy.weatherapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.utils.ListUtil;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        Context mContext = getActivity().getApplicationContext();
        ListPreference mCityPreference = (ListPreference) findPreference("city");
        if (!ListUtil.isConnected(mContext)) {
            mCityPreference.setEnabled(false);
        }

    }
}
