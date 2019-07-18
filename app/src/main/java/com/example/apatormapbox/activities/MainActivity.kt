package com.example.apatormapbox.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.apatormapbox.R
import com.example.apatormapbox.viewmodels.SolarViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val solarViewModel: SolarViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val viewModel = ViewModelProviders.of(this).get(SolarViewModel::class.java)

        //viewModel.fetchSolars()
        solarViewModel.fetchStations(40, -105)

        solarViewModel.stations.observe(this, Observer {
            Log.d("pobrano stacje", it.toString())
        })

        solarViewModel.solars.observe(this, Observer {
            Log.d("pobrano solary", it.toString())
        })
    }
}
