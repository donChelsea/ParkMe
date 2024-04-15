package com.example.parkme.data

import com.example.parkme.domain.ParkingSpot

fun ParkingSpotEntity.toParkingSpot(): ParkingSpot {
    return ParkingSpot(
        lat = lat,
        lng = lng,
        id = id
    )
}

fun ParkingSpot.toParkingSpotEntity(): ParkingSpotEntity {
    return ParkingSpotEntity(
        lat = lat,
        lng = lng,
        id = id
    )
}