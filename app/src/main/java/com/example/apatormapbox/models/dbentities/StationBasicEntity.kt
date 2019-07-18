package com.example.apatormapbox.models.dbentities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station_basic")
data class StationBasicEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "lat")
    val lat: Double?,
    @ColumnInfo(name = "lon")
    val lon: Double?
)