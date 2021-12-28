package com.example.takealook.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Database(entities = [JoaraBest::class], version = 2)
abstract class DataBaseJoara: RoomDatabase() {
    abstract fun bestDao(): BestDao
}