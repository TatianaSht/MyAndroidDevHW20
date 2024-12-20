package com.example.myandroiddevhw20.data.model

import com.yandex.mapkit.geometry.Point

data class Attraction(
    val name: String,
    val description: String,
    val point: Point,
)