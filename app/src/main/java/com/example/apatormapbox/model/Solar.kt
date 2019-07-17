package com.example.apatormapbox.model

data class Solar(
    val errors: List<Any>,
    val inputs: Inputs,
    val outputs: Outputs,
    val ssc_info: SscInfo,
    val station_info: StationInfo,
    val version: String,
    val warnings: List<Any>
)