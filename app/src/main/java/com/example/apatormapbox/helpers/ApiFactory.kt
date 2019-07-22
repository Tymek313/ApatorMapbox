package com.example.apatormapbox.helpers

import com.example.apatormapbox.interfaces.SolarApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Apifactory {

    //Creating Auth Interceptor to add api_key query in front of all the requests.
/*    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url()
            .newBuilder()
            .addQueryParameter("api_key", "0T4V4lMTxPT7b9JJexnz1vLsPzWXlX6XV8exWuud ")
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }*/

    val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    //OkhttpClient for building http request url
    private val solarClient = OkHttpClient().newBuilder()
        /*.addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)*/
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