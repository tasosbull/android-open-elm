package com.android.openelm;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.android.openelm.R;

public class PreferencesFromXml extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}

