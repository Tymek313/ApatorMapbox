package com.example.apatormapbox.repositiories

import com.example.apatormapbox.interfaces.SolarApi
import com.example.apatormapbox.models.stationdetails.Solar
import com.example.apatormapbox.models.stations.Station

class SolarRepository(private val api: SolarApi) : BaseRepository() {
    suspend fun getSolars(stationId: Int): Solar? {
        return safeApiCall(
            call = { api.getSolar(stationId).await() },
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