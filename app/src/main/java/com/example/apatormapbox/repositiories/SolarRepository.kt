package com.example.apatormapbox.repositiories

import com.example.apatormapbox.database.dao.StationDao
import com.example.apatormapbox.interfaces.SolarApi
import com.example.apatormapbox.mappers.JsonToBasicStationEntity
import com.example.apatormapbox.mappers.JsonToStationEntity
import com.example.apatormapbox.models.dbentities.StationBasicEntity
import com.example.apatormapbox.models.dbentities.StationDetailsEntity
import retrofit2.Response

class SolarRepository(private val api: SolarApi, private val stationDao: StationDao) : BaseRepository() {

    suspend fun getStationDetailsFromApi(stationId: String): StationDetailsEntity? {
        return safeApiCall(
            call = {
                val data = api.getStationDetails(stationId).await().body()
                val mappedDetails = JsonToStationEntity.map(data!!)
                Response.success(mappedDetails)
            },
            errorMessage = "Error fetching stationDetails"
        )
    }

    suspend fun getStationDetailsFromDb(stationId: String): StationDetailsEntity? {
        return stationDao.getStationDetails(stationId)
    }

    suspend fun getStationsFromDb(): List<StationBasicEntity>? {
        return stationDao.getAllAStations()
    }

    suspend fun getStationsFromApi(lat: Int, lon: Int): List<StationBasicEntity>? {
        return safeApiCall(
            call = {
                val data = api.getStations(lat, lon).await().body()
                val mappedStations = data!!.outputs!!.allStations!!.map { JsonToBasicStationEntity.map(it!!) }
                stationDao.insertAllStations(*mappedStations.toTypedArray())
                Response.success(mappedStations)
            },
            errorMessage = "Error fetching stations"
        )
    }
}