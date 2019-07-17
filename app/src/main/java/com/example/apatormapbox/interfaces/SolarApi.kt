package com.example.apatormapbox.interfaces

import com.example.apatormapbox.model.Solar
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface SolarApi {
    @GET("v6.json?lat=40&lon=-105&system_capacity=4&azimuth=180&tilt=40&array_type=1&module_type=1&losses=10")
    fun getSolars(): Deferred<Response<Solar>>
}