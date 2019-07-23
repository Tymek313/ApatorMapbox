package com.example.apatormapbox.helpers

import android.content.Context
import com.example.apatormapbox.R

object AppConstants {
    const val LOCATION_PERMISSION = 1

    fun getDefaultApiKey(context: Context) = context.getString(R.string.solar_api)
}