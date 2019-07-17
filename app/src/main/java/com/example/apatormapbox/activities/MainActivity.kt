package com.example.apatormapbox.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.apatormapbox.R
import com.example.apatormapbox.viewmodels.SolarViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(SolarViewModel::class.java)

        //viewModel.fetchSolars()
        viewModel.fetchStations(40, -105)

        viewModel.stations.observe(this, Observer {
            Log.d("pobrano stacje", it.toString())
        })

        viewModel.solars.observe(this, Observer {
            Log.d("pobrano solary", it.toString())
        })
    }
}
