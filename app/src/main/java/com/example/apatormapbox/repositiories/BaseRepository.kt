package com.example.apatormapbox.repositiories

import android.util.Log
import com.example.apatormapbox.helpers.HttpResult
import retrofit2.Response
import java.io.IOException

open class BaseRepository {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {

        val httpResult: HttpResult<T> = safeApiResult(call, errorMessage)
        var data: T? = null

        when (httpResult) {
            is HttpResult.Success ->
                data = httpResult.data
            is HttpResult.Error -> {
                Log.d("1.DataRepository", "$errorMessage & Exception - ${httpResult.exception}")
            }
        }

        return data

    }

    private suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>, errorMessage: String): HttpResult<T> {
        val response = call.invoke()
        if (response.isSuccessful) return HttpResult.Success(response.body()!!)

        return HttpResult.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }
}