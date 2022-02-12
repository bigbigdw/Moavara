package com.example.moavara.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DataBestDay::class], version = 3)
abstract class DataBaseBestDay: RoomDatabase() {
    abstract fun bestDao(): BestDao
}

@Database(entities = [DataBestMonth::class], version = 3)
abstract class DataBaseBestMonth: RoomDatabase() {
    abstract fun bestDaoMonth(): BestDaoMonth
}