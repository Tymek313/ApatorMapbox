package com.example.apatormapbox.helpers

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.lang.Exception


object FeaturesHelper {
    fun isConnectedToNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun isLocationEnabled(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var networkEnabled: Boolean
        networkEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return networkEnabled
    }
}