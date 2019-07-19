package com.example.apatormapbox.fragments


import android.content.SharedPreferences
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.apatormapbox.R
import com.example.apatormapbox.helpers.DateHelper

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        arrayOf(
            getString(R.string.api_key_preference),
            //getString(R.string.sync_preference),
            getString(R.string.time_window_preference)
        ).forEach { setupPreference(it) }
        findPreference("refresh_map").apply {
            setOnPreferenceClickListener {
                Navigation.findNavController(activity!!, R.id.navHost).navigate(R.id.mapFragment)
                true
            }
        }
        findPreference(getString(R.string.sync_preference)).apply {
            summary = sharedPreferences.getString(getString(R.string.sync_preference), "")
            setOnPreferenceClickListener {
                val lastSyncInfo = "Last Synchronization: ${DateHelper.getToday()}"
                it.summary = lastSyncInfo
                sharedPreferences.edit().putString(it.key, lastSyncInfo).apply()
                true
            }
        }
    }

    private fun listPreferenceValueToEntry(preference: ListPreference, newValue: Int): CharSequence {
        return preference.entries[newValue - 1]
    }

    /*
     *   Ustawianie początkowej wartości oraz
     *   listenera zmiany do wyświetlania obecnej wartości w opcjach
     */
    private fun setupPreference(preferenceKey: String) {
        findPreference(preferenceKey).apply {
            if (this is ListPreference) {
                summary = entry
            } else {
                summary = sharedPreferences.getString(preferenceKey, "")
            }
            setOnPreferenceChangeListener { preference, newValue ->
                if (preference is ListPreference) {
                    summary = listPreferenceValueToEntry(preference, (newValue as String).toInt())
                } else {
                    summary = newValue as String
                }
                true
            }
        }
    }
}
