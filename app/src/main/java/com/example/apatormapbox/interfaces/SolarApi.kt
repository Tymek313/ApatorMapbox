package com.example.apatormapbox.interfaces

import com.example.apatormapbox.model.stationdetails.Solar
import com.example.apatormapbox.model.stations.Station
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SolarApi {
    @GET("pvwatts/v6.json?system_capacity=4&azimuth=180&tilt=40&array_type=1&module_type=1&losses=10&radius=10000")
    fun getSolar(): Deferred<Response<Solar>>

    @GET("solar/data_query/v1.json?&radius=2000&all=1")
    fun getStations(@Query("lat") lat: Int, @Query("lon") lon: Int): Deferred<Response<Station>>
}