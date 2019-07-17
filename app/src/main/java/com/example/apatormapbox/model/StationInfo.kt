package com.example.apatormapbox.model

data class StationInfo(
    val city: String,
    val distance: Int,
    val elev: Double,
    val lat: Double,
    val location: String,
    val lon: Double,
    val solar_resource_file: String,
    val state: String,
    val tz: Int
)