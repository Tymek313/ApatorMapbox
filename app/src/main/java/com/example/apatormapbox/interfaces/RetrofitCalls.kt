package com.example.apatormapbox.interfaces

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitCalls {
    @GET("v6.json?api_key=DEMO_KEY&lat=40&lon=-105&system_capacity=4&azimuth=180&tilt=40&array_type=1&module_type=1&losses=10")
    fun getSolars(): Call<String>
}