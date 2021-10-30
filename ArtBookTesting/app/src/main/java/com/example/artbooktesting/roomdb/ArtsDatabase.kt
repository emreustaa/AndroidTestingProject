package com.example.artbooktesting.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Art::class], version = 1)
abstract class ArtsDatabase : RoomDatabase() {
    abstract fun artDao(): ArtDao
}