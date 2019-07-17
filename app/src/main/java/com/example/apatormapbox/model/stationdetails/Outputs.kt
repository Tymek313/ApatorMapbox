package com.example.apatormapbox.model.stationdetails


import com.google.gson.annotations.SerializedName

data class Outputs(
    @SerializedName("ac_annual")
    val acAnnual: Double?,
    @SerializedName("ac_monthly")
    val acMonthly: List<Double?>?,
    @SerializedName("capacity_factor")
    val capacityFactor: Double?,
    @SerializedName("dc_monthly")
    val dcMonthly: List<Double?>?,
    @SerializedName("poa_monthly")
    val poaMonthly: List<Double?>?,
    @SerializedName("solrad_annual")
    val solradAnnual: Double?,
    @SerializedName("solrad_monthly")
    val solradMonthly: List<Double?>?
)