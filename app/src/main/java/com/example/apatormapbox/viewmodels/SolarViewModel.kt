package com.example.apatormapbox.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apatormapbox.helpers.Apifactory
import com.example.apatormapbox.models.stationdetails.Solar
import com.example.apatormapbox.models.stations.Station
import com.example.apatormapbox.repositiories.SolarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SolarViewModel : ViewModel() {
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    private val repository = SolarRepository(Apifactory.solarApi)
    val solars = MutableLiveData<Solar>()
    val stations = MutableLiveData<Station>()

    fun fetchStations(lat: Int, lon: Int) {
        scope.launch {
            val stations = repository.getStations(lat, lon)
            this@SolarViewModel.stations.postValue(stations)
        }
    }

    fun fetchSolars(stationId: Int) {
        scope.launch {
            val solars = repository.getSolars(stationId)
            this@SolarViewModel.solars.postValue(solars)
        }
    }
}