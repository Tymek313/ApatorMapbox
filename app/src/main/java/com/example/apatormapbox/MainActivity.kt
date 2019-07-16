package com.example.apatormapbox

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newFragment = SettingsFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_conteiner, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()

        val key = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.userAPI_key), "assd")
        Log.d("key", key)
    }
}
