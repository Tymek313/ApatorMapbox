package com.example.apatormapbox.repositiories

import com.example.apatormapbox.database.dao.StationDao
import com.example.apatormapbox.interfaces.SolarApi
import com.example.apatormapbox.mappers.JsonToBasicStationEntity
import com.example.apatormapbox.mappers.JsonToStationEntity
import com.example.apatormapbox.models.dbentities.StationBasicEntity
import com.example.apatormapbox.models.dbentities.StationDetailsEntity
import okhttp3.ResponseBody
import retrofit2.Response

class SolarRepository(private val api: SolarApi, private val stationDao: StationDao) : BaseRepository() {

    suspend fun getStationDetailsFromApi(stationId: String, apiKey: String): StationDetailsEntity? {
        return safeApiCall(
            call = {
                val data = api.getStationDetails(stationId, apiKey).await().body()
                val mappedDetails = JsonToStationEntity.map(data!!)
                stationDao.insertStationDetails(mappedDetails)
                Response.success(mappedDetails)
            },
            errorMessage = "Error fetching stationDetails"
        )
    }

    fun getStationDetailsFromDb(stationId: String): StationDetailsEntity? {
        return stationDao.getStationDetails(stationId)
    }

    suspend fun getStationsFromApi(lat: Int, lon: Int, apiKey: String): List<StationBasicEntity>? {
        return safeApiCall(
            call = {
                val data = api.getStations(lat, lon, apiKey).await().body()
                if (data == null) {
                    //Timber.d("APi zwróciło pustą listę")
                    Response.error(400, ResponseBody.create(null, ""))
                } else {
                    val mappedStations = data.outputs!!.allStations!!.map { JsonToBasicStationEntity.map(it!!) }
                    stationDao.insertAllStations(*mappedStations.toTypedArray())
                    Response.success(mappedStations)
                }
            },
            errorMessage = "Error fetching stations"
        )
    }

    fun getStationsFromDb(): List<StationBasicEntity>? {
        return stationDao.getAllAStations()
    }
}