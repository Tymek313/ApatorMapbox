package com.example.apatormapbox.models.earthquakes

data class Earthquakes(
    val bbox: List<Double>,
    val features: List<Feature>,
    val metadata: Metadata,
    val type: String
)