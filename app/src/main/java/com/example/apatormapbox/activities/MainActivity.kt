package com.example.apatormapbox.activities

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.apatormapbox.R
import com.example.apatormapbox.helpers.AppConstants
import com.example.apatormapbox.helpers.Permissions

class MainActivity : AppCompatActivity() {

    //private val solarViewModel: SolarViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*solarViewModel.fetchStationDetails("1-724695")

        solarViewModel.stationDetails.observe(this, Observer {
            Log.d("stationdetails", it.toString())
        })*/

        Permissions.handlePermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            AppConstants.PermissionConstants.LOCATION_PERMISSION.value
        )
    }
}
