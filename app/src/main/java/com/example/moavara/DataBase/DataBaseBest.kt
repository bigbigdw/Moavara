package com.example.moavara.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DataBestWeek::class], version = 3)
abstract class DataBaseBestWeek: RoomDatabase() {
    abstract fun bestDao(): BestDao
}