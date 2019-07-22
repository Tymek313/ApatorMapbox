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
    var lat: Double?,
    @ColumnInfo(name = "lon")
    var lon: Double?,
    @ColumnInfo(name = "elev")
    var elev: Double? = null,
    @ColumnInfo(name = "tz")
    var tz: Int? = null,
    @ColumnInfo(name = "location")
    var location: String? = null,
    @ColumnInfo(name = "city")
    var city: String? = null,
    @ColumnInfo(name = "state")
    var state: String? = null,
    @ColumnInfo(name = "solar_resource_file")
    var solarResourceFile: String? = null,
    @ColumnInfo(name = "distance")
    var distance: Int? = null,
    @ColumnInfo(name = "poa_monthly")
    var poaMonthly: List<Double?>? = null,
    @ColumnInfo(name = "dc_monthly")
    var dcMonthly: List<Double?>? = null,
    @ColumnInfo(name = "ac_monthly")
    var acMonthly: List<Double?>? = null,
    @ColumnInfo(name = "ac_annual")
    var acAnnual: Double? = null,
    @ColumnInfo(name = "solrad_monthly")
    var solradMonthly: List<Double?>? = null,
    @ColumnInfo(name = "solrad_annual")
    var solradAnnual: Double? = null,
    @ColumnInfo(name = "capacity_factor")
    var capacityFactor: Double? = null,
    @ColumnInfo(name = "ac")
    var ac: List<Int>? = null,
    @ColumnInfo(name = "poa")
    var poa: List<Int>? = null,
    @ColumnInfo(name = "dn")
    var dn: List<Int>? = null,
    @ColumnInfo(name = "dc")
    var dc: List<Int>? = null,
    @ColumnInfo(name = "df")
    var df: List<Int>? = null,
    @ColumnInfo(name = "tamb")
    var tamb: List<Int>? = null,
    @ColumnInfo(name = "tcell")
    var tcell: List<Int>? = null,
    @ColumnInfo(name = "wspd")
    var wspd: List<Int>? = null
)