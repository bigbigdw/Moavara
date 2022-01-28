package com.example.moavara.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Database(entities = [DataBest::class], version = 3)
abstract class DataBaseBest: RoomDatabase() {
    abstract fun bestDao(): BestDao
}