package com.example.apatormapbox.model

data class Outputs(
    val ac_annual: Double,
    val ac_monthly: List<Double>,
    val capacity_factor: Double,
    val dc_monthly: List<Double>,
    val poa_monthly: List<Double>,
    val solrad_annual: Double,
    val solrad_monthly: List<Double>
)