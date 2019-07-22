package com.example.apatormapbox.interfaces

import com.example.apatormapbox.models.stationdetails.StationDetails
import com.example.apatormapbox.models.stations.Station
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SolarApi {
    @GET("pvwatts/v6.json?system_capacity=4&azimuth=180&tilt=40&array_type=1&module_type=1&losses=10")
    fun getStationDetails(@Query("file_id") stationId: String, @Query("api_key") apiKey: String): Deferred<Response<StationDetails>>

    @GET("solar/data_query/v1.json?&radius=2000&all=1")
    fun getStations(@Query("lat") lat: Int, @Query("lon") lon: Int, @Query("api_key") apiKey: String): Deferred<Response<Station>>
}