package com.example.apatormapbox.helpers

import com.example.apatormapbox.interfaces.SolarApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Apifactory {
    //OkhttpClient for building http request url
    private val solarClient = OkHttpClient().newBuilder()
        .build()

    fun retrofit(): Retrofit = Retrofit.Builder()
        .client(solarClient)
        .baseUrl("https://developer.nrel.gov/api/")
        .addConverterFactory(GsonConverterFactory.create())
        //.addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val solarApi: SolarApi = retrofit().create(SolarApi::class.java)
}