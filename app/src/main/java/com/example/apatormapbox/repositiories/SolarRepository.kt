package com.example.apatormapbox.repositiories

import com.example.apatormapbox.interfaces.SolarApi
import com.example.apatormapbox.model.stationdetails.Solar
import com.example.apatormapbox.model.stations.Station

class SolarRepository(private val api: SolarApi) : BaseRepository() {
    suspend fun getSolars(): Solar? {
        return safeApiCall(
            call = { api.getSolar().await() },
            errorMessage = "Error fetching solars"
        )
    }

    suspend fun getStations(lat: Int, lon: Int): Station? {
        return safeApiCall(
            call = { api.getStations(lat, lon).await() },
            errorMessage = "Error fetching stations"
        )
    }
}