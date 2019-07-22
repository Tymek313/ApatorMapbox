package com.example.apatormapbox.models.stations


import com.google.gson.annotations.SerializedName

data class Outputs(
    @SerializedName("all_stations")
    val allStations: List<AllStation?>?,
    @SerializedName("intl")
    val intl: Any?,
    @SerializedName("nsrdb")
    val nsrdb: Nsrdb?,
    @SerializedName("tmy2")
    val tmy2: Tmy2?,
    @SerializedName("tmy3")
    val tmy3: Tmy3?
)