package com.example.apatormapbox.model


import com.google.gson.annotations.SerializedName

data class StationInfo(
    @SerializedName("city")
    val city: String?,
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("elev")
    val elev: Double?,
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("lon")
    val lon: Double?,
    @SerializedName("solar_resource_file")
    val solarResourceFile: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("tz")
    val tz: Int?
)