package com.example.parkme.domain

import kotlinx.coroutines.flow.Flow

interface ParkingSpotRepository {

    suspend fun insertParkingSpot(spot: ParkingSpot)

    suspend fun deleteParkingSpot(spot: ParkingSpot)

    suspend fun getParkingSpots(): Flow<List<ParkingSpot>>
}