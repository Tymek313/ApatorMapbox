package com.example.apatormapbox.models.stationdetails


import com.google.gson.annotations.SerializedName

data class StationDetails(
    @SerializedName("errors")
    val errors: List<Any?>?,
    @SerializedName("inputs")
    val inputs: Inputs?,
    @SerializedName("outputs")
    val outputs: Outputs?,
    @SerializedName("ssc_info")
    val sscInfo: SscInfo?,
    @SerializedName("station_info")
    val stationInfo: StationInfo?,
    @SerializedName("version")
    val version: String?,
    @SerializedName("warnings")
    val warnings: List<Any?>?
)