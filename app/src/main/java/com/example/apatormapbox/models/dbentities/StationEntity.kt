package com.example.apatormapbox.models.dbentities

data class StationEntity(
    var id: String? = null,
    var lat: Double? = null,
    var lon: Double?,
    var elev: Double?,
    var tz: Int?,
    var location: String?,
    var city: String?,
    var state: String?,
    var solarResourceFile: String?,
    var distance: Int?,
    var poaMonthly: List<Double?>?,
    var dcMonthly: List<Double?>?,
    var acMonthly: List<Double?>?,
    var acAnnual: Double?,
    var solradMonthly: List<Double?>?,
    var solradAnnual: Double?,
    var capacityFactor: Double?,
    var ac: List<Int>?,
    var poa: List<Int>?,
    var dn: List<Int>?,
    var dc: List<Int>?,
    var df: List<Int>?,
    var tamb: List<Int>?,
    var tcell: List<Int>?,
    var wspd: List<Int>?
)