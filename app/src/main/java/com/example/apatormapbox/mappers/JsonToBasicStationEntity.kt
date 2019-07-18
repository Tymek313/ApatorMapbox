package com.example.apatormapbox.mappers

import com.example.apatormapbox.models.dbentities.StationBasicEntity
import com.example.apatormapbox.models.stations.AllStation

object JsonToBasicStationEntity {
    fun map(allStation: AllStation): StationBasicEntity {
        return StationBasicEntity(
            id = allStation.id!!,
            lat = allStation.lat,
            lon = allStation.lon
        )
    }
}