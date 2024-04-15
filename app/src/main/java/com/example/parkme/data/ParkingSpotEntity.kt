package com.example.parkme.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.concurrent.Immutable

@Immutable
@Entity
data class ParkingSpotEntity(
    val lat: Double,
    val lng: Double,
    @PrimaryKey val id: Int? = null
)