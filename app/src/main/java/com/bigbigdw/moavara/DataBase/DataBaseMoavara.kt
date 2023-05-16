package com.bigbigdw.moavara.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomBookListDataBest::class], version = 4)
abstract class DBBest: RoomDatabase() {
    abstract fun bestDao(): DaoBest
}

@Database(entities = [DataEvent::class], version = 4)
abstract class DataPickEvent: RoomDatabase() {
    abstract fun eventDao(): DaoPickEvent
}

@Database(entities = [DataBaseUser::class], version = 1)
abstract class DBUser: RoomDatabase() {
    abstract fun daoUser(): DaoUser
}

@Database(entities = [RoomBookListDataBestAnalyze::class], version = 1)
abstract class DBBestBookCode: RoomDatabase() {
    abstract fun bestDaoBookCode(): DaoBestBookCode
}