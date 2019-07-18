package com.example.apatormapbox.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apatormapbox.models.dbentities.StationBasicEntity

@Dao
interface StationDao {

    @Query("SELECT * FROM station_basic")
    fun getAllAStations(): List<StationBasicEntity>

    @Insert
    fun insertStation(station: StationBasicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStations(vararg stations: StationBasicEntity)
}