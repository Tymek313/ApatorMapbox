package com.example.apatormapbox.mappers

import com.example.apatormapbox.models.dbentities.StationBasicEntity
import com.example.apatormapbox.models.stations.AllStation
import kotlin.random.Random

object JsonToBasicStationEntity {
    fun map(allStation: AllStation): StationBasicEntity {
        return StationBasicEntity(
            id = allStation.id!!,
            lat = allStation.lat!! + Random.nextDouble(0.0001, 0.0009),
            lon = allStation.lon!! + Random.nextDouble(0.0001, 0.0009)
        )
    }
}