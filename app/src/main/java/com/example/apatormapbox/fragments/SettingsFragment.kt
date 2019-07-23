package com.example.apatormapbox.fragments


import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.apatormapbox.R
import com.example.apatormapbox.helpers.ConnectivityHelper
import com.example.apatormapbox.helpers.DateHelper
import com.example.apatormapbox.viewmodels.SolarViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var sharedPreferences: SharedPreferences
    private val solarViewModel: SolarViewModel by viewModel()

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

        findPreference(getString(R.string.sync_preference)).apply {
            summary = sharedPreferences.getString(getString(R.string.sync_preference), "")
            setOnPreferenceClickListener {
                if (ConnectivityHelper.isConnectedToNetwork(context)) {
                    solarViewModel.fetchAllStationsFromApi()
                    val lastSyncInfo = "${getString(R.string.last_sync)}: ${DateHelper.getToday()}"
                    it.summary = lastSyncInfo
                    sharedPreferences.edit().putString(it.key, lastSyncInfo).apply()
                } else {
                    Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                }
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
