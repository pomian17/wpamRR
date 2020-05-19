package com.example.restaurantreservation.di

import android.content.Context
import androidx.room.Room
import com.example.restaurantreservation.data.local.ReservationDAO
import com.example.restaurantreservation.data.local.ReservationDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideReservationDatabase(context: Context): ReservationDatabase =
        Room.databaseBuilder(
            context,
            ReservationDatabase::class.java,
            "reservation_database"
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideReservationDAO(db: ReservationDatabase): ReservationDAO = db.reservationDao()
}