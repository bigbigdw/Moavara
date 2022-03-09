package com.example.moavara.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moavara.Search.EventData

@Database(entities = [DataBestDay::class], version = 4)
abstract class DataBaseBestDay: RoomDatabase() {
    abstract fun bestDao(): BestDao
}


@Database(entities = [DataEvent::class], version = 4)
abstract class DataPickEvent: RoomDatabase() {
    abstract fun eventDao(): DaoPickEvent
}

@Database(entities = [DataBestMonth::class], version = 3)
abstract class DataBaseBestMonth: RoomDatabase() {
    abstract fun bestDaoMonth(): BestDaoMonth
}