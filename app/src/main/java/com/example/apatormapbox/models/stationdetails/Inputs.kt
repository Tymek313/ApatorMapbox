package com.example.apatormapbox.models.stationdetails


import com.google.gson.annotations.SerializedName

data class Inputs(
    @SerializedName("file_id")
    val fileId: String?,
    @SerializedName("array_type")
    val arrayType: String?,
    @SerializedName("azimuth")
    val azimuth: String?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lon")
    val lon: String?,
    @SerializedName("losses")
    val losses: String?,
    @SerializedName("module_type")
    val moduleType: String?,
    @SerializedName("system_capacity")
    val systemCapacity: String?,
    @SerializedName("tilt")
    val tilt: String?
)