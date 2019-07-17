package com.example.apatormapbox.model.stationdetails


import com.google.gson.annotations.SerializedName

data class SscInfo(
    @SerializedName("build")
    val build: String?,
    @SerializedName("version")
    val version: Int?
)