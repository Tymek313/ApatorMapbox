package com.example.apatormapbox.repositiories

import com.example.apatormapbox.interfaces.SolarApi
import com.example.apatormapbox.model.Solar

class SolarRepository(private val api: SolarApi) : BaseRepository() {
    suspend fun getSolars(): Solar? {
        val response = safeApiCall(
            call = { api.getSolars().await() },
            errorMessage = "Error fetching solars"
        )

        return response
    }
}