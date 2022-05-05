package com.mclowicz.mcorganizer.domain.model

import com.mclowicz.mcorganizer.data.database.spot.SpotEntity

data class Spot(
    val id: String,
    val lat: Double,
    val lng: Double,
    val name: String,
    val date: String,
    val time: String,
    val timeStamp: Long
)

fun fromSpot(spot: Spot) = SpotEntity(
    id = spot.id,
    name = spot.name,
    date = spot.date,
    time = spot.time,
    lat = spot.lat,
    lng = spot.lng,
    timeStamp = spot.timeStamp
)

fun fromSpotEntity(spotEntity: SpotEntity) = Spot(
    id = spotEntity.id,
    lat = spotEntity.lat,
    lng = spotEntity.lng,
    name = spotEntity.name,
    date = spotEntity.date,
    time = spotEntity.time,
    timeStamp = spotEntity.timeStamp
)