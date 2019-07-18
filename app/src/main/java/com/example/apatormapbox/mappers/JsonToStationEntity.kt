package com.example.apatormapbox.mappers

import com.example.apatormapbox.models.dbentities.StationDetailsEntity
import com.example.apatormapbox.models.stationdetails.StationDetails

object JsonToStationEntity {
    fun map(sd: StationDetails): StationDetailsEntity {
        return StationDetailsEntity(
            id = sd.inputs?.fileId,
            lat = sd.stationInfo?.lat,
            lon = sd.stationInfo?.lon,
            elev = sd.stationInfo?.elev,
            tz = sd.stationInfo?.tz,
            location = sd.stationInfo?.location,
            city = sd.stationInfo?.city,
            state = sd.stationInfo?.state,
            solarResourceFile = sd.stationInfo?.solarResourceFile,
            distance = sd.stationInfo?.distance,
            poaMonthly = sd.outputs?.poaMonthly,
            dcMonthly = sd.outputs?.dcMonthly,
            acMonthly = sd.outputs?.acMonthly,
            acAnnual = sd.outputs?.acAnnual,
            solradMonthly = sd.outputs?.solradMonthly,
            solradAnnual = sd.outputs?.solradAnnual,
            capacityFactor = sd.outputs?.capacityFactor,
            ac = null,
            poa = null,
            dn = null,
            dc = null,
            df = null,
            tamb = null,
            tcell = null,
            wspd = null
        )
    }
}