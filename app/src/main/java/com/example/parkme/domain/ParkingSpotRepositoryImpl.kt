package com.example.parkme.domain

import com.example.parkme.data.ParkingSpotDao
import com.example.parkme.data.toParkingSpot
import com.example.parkme.data.toParkingSpotEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ParkingSpotRepositoryImpl(
    private val dao: ParkingSpotDao,
) : ParkingSpotRepository {
    override suspend fun insertParkingSpot(spot: ParkingSpot) = dao.insertParkingSpot(spot.toParkingSpotEntity())

    override suspend fun deleteParkingSpot(spot: ParkingSpot) = dao.deleteParkingSpot(spot.toParkingSpotEntity())

    override suspend fun getParkingSpots(): Flow<List<ParkingSpot>> = dao.getParkingSpots().map { spots ->
        spots.map { it.toParkingSpot() }
    }
}