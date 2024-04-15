package com.example.parkme.di

import android.app.Application
import androidx.room.Room
import com.example.parkme.data.ParkingSpotDatabase
import com.example.parkme.domain.ParkingSpotRepository
import com.example.parkme.domain.ParkingSpotRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideParkingSpotDatabase(app: Application): ParkingSpotDatabase =
        Room.databaseBuilder(
            app,
            ParkingSpotDatabase::class.java,
            "park_me.db"
        ).build()

    @Singleton
    @Provides
    fun provideParkingSpotRepository(db: ParkingSpotDatabase): ParkingSpotRepository =
        ParkingSpotRepositoryImpl(db.dao)
}