package com.example.apatormapbox.models.earthquakes

data class Feature(
    val geometry: Geometry,
    val id: String,
    val properties: Properties,
    val type: String
)