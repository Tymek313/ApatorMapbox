package com.example.apatormapbox.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.apatormapbox.R
import com.example.apatormapbox.database.MainDatabase
import com.example.apatormapbox.helpers.Apifactory
import com.example.apatormapbox.helpers.AppConstants
import com.example.apatormapbox.models.dbentities.StationBasicEntity
import com.example.apatormapbox.models.dbentities.StationDetailsEntity
import com.example.apatormapbox.repositiories.SolarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SolarViewModel(private val app: Application) : AndroidViewModel(app) {

    //COROUTINES
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    //DATABASE
    val database = MainDatabase.getDatabase(app)
    private val repository = SolarRepository(Apifactory.solarApi, database!!.stationDao())
    //PREFERENCES
    private val preferences = PreferenceManager.getDefaultSharedPreferences(app)
    //LIVE DATA
    val stationDetails = MutableLiveData<StationDetailsEntity>()
    val stations = MutableLiveData<List<StationBasicEntity>>()

    fun fetchAllStationsFromApi() {
        scope.launch {
            val usStations = repository.getStationsFromApi(
                40,
                -105,
                preferences.getString(
                    app.resources.getString(R.string.api_key_preference),
                    AppConstants.getDefaultApiKey(app)
                )!!
            )
            val asiaStations = repository.getStationsFromApi(
                40,
                85,
                preferences.getString(
                    app.resources.getString(R.string.api_key_preference),
                    AppConstants.getDefaultApiKey(app)
                )!!
            )
            if (usStations == null || asiaStations == null) {
                this@SolarViewModel.stations.postValue(null)
            } else {
                val joinedStations = ArrayList<StationBasicEntity>().apply {
                    addAll(usStations)
                    addAll(asiaStations)
                }
                this@SolarViewModel.stations.postValue(joinedStations)
            }
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
                stationDetails = repository.getStationDetailsFromApi(
                    stationId,
                    preferences.getString(app.resources.getString(R.string.api_key_preference), "")!!
                )
            }
            this@SolarViewModel.stationDetails.postValue(stationDetails)
        }
    }
}