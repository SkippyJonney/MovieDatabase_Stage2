package com.example.jonathan.moviedatabase_stage1.Utils;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.jonathan.moviedatabase_stage1.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_view);
    }
}
