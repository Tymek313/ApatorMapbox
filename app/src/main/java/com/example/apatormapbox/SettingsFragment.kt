package com.example.apatormapbox


import android.icu.text.DateTimePatternGenerator
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import java.time.LocalDateTime
import java.util.*

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
            Log.d("", "sync")
            val date = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date())
            it.summary = "Last Synchronization: $date"
            true
        }
    }
}
