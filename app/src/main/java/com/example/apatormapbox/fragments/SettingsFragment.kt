package com.example.apatormapbox.fragments


import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.apatormapbox.R

import android.util.Log
import android.view.MenuItem
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import java.util.*
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass.
 *
 */
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findPreference(getString(R.string.sync_key)).setOnPreferenceClickListener {
            val date = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date())
            it.summary = "Last Synchronization: $date"
            true
        }
    }
}
