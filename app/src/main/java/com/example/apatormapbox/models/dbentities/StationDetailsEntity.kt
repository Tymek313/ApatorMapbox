package com.example.apatormapbox.models.dbentities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "station_details")
data class StationDetailsEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "lat")
    var lat: Double? = null,
    @ColumnInfo(name = "lon")
    var lon: Double?,
    @ColumnInfo(name = "elev")
    var elev: Double?,
    @ColumnInfo(name = "tz")
    var tz: Int?,
    @ColumnInfo(name = "location")
    var location: String?,
    @ColumnInfo(name = "city")
    var city: String?,
    @ColumnInfo(name = "state")
    var state: String?,
    @ColumnInfo(name = "solar_resource_file")
    var solarResourceFile: String?,
    @ColumnInfo(name = "distance")
    var distance: Int?,
    @ColumnInfo(name = "poa_monthly")
    var poaMonthly: List<Double?>?,
    @ColumnInfo(name = "dc_monthly")
    var dcMonthly: List<Double?>?,
    @ColumnInfo(name = "ac_monthly")
    var acMonthly: List<Double?>?,
    @ColumnInfo(name = "ac_annual")
    var acAnnual: Double?,
    @ColumnInfo(name = "solrad_monthly")
    var solradMonthly: List<Double?>?,
    @ColumnInfo(name = "solrad_annual")
    var solradAnnual: Double?,
    @ColumnInfo(name = "capacity_factor")
    var capacityFactor: Double?,
    @ColumnInfo(name = "ac")
    var ac: List<Int>?,
    @ColumnInfo(name = "poa")
    var poa: List<Int>?,
    @ColumnInfo(name = "dn")
    var dn: List<Int>?,
    @ColumnInfo(name = "dc")
    var dc: List<Int>?,
    @ColumnInfo(name = "df")
    var df: List<Int>?,
    @ColumnInfo(name = "tamb")
    var tamb: List<Int>?,
    @ColumnInfo(name = "tcell")
    var tcell: List<Int>?,
    @ColumnInfo(name = "wspd")
    var wspd: List<Int>?
)