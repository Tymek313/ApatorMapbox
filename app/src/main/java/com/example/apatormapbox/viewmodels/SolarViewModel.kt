package com.example.apatormapbox.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.apatormapbox.database.MainDatabase
import com.example.apatormapbox.helpers.Apifactory
import com.example.apatormapbox.models.dbentities.StationBasicEntity
import com.example.apatormapbox.models.dbentities.StationDetailsEntity
import com.example.apatormapbox.repositiories.SolarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SolarViewModel(application: Application) : AndroidViewModel(application) {

    //COROUTINES
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    //DATABASE
    val database = MainDatabase.getDatabase(application)
    private val repository = SolarRepository(Apifactory.solarApi, database!!.stationDao())
    //LIVE DATA
    val stationDetails = MutableLiveData<StationDetailsEntity>()
    val stations = MutableLiveData<List<StationBasicEntity>>()

    fun fetchStationsFromApi(lat: Int, lon: Int) {
        scope.launch {
            val stations = repository.getStationsFromApi(lat, lon)
            this@SolarViewModel.stations.postValue(stations)
        }
    }

    fun fetchStationsFromDb() {
        scope.launch {
            val stations = repository.getStationsFromDb()
            this@SolarViewModel.stations.postValue(stations)
        }
    }

    fun fetchStationDetails(stationId: String) {
        scope.launch {
            var stationDetails = repository.getStationDetailsFromDb(stationId)
            if (stationDetails == null) {
                stationDetails = repository.getStationDetailsFromApi(stationId)
            }
            this@SolarViewModel.stationDetails.postValue(stationDetails)
        }
    }
}