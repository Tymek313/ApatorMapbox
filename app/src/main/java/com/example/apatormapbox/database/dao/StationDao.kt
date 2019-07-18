package com.example.apatormapbox.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apatormapbox.models.dbentities.StationBasicEntity
import com.example.apatormapbox.models.dbentities.StationDetailsEntity

@Dao
interface StationDao {
    //STATIONS BASIC INFO
    @Query("SELECT * FROM station_basic")
    fun getAllAStations(): List<StationBasicEntity>

    @Insert
    fun insertStation(station: StationBasicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStations(vararg stations: StationBasicEntity)

    //STATION DETAILS
    @Query("SELECT * FROM station_details WHERE id=:id")
    fun getStationDetails(id: String): StationDetailsEntity

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertStationDetails(details: StationDetailsEntity)
}