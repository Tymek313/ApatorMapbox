package com.example.apatormapbox

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Mapbox.getInstance(this, resources.getString(R.string.mapbox_access_token))
    }
}