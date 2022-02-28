package com.example.moavara.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moavara.Search.EventData

@Database(entities = [DataBestDay::class], version = 3)
abstract class DataBaseBestDay: RoomDatabase() {
    abstract fun bestDao(): BestDao
}

//abstract class DataBaseBestDay : RoomDatabase() {
//    abstract fun bestDao(): BestDao
//
//    companion object {
//        private var INSTANCE: DataBaseBestDay? = null
//
//        fun getInstance(mContext: Context, str : String): DataBaseBestDay? {
//            if (INSTANCE == null) {
//                synchronized(DataBaseBestDay::class) {
//                    INSTANCE = Room.databaseBuilder(
//                        mContext,
//                        DataBaseBestDay::class.java,
//                        "$str.db"
//                    ).allowMainThreadQueries().build()
//                }
//            }
//            return INSTANCE
//        }
//    }
//}


@Database(entities = [DataEvent::class], version = 4)
abstract class DataPickEvent: RoomDatabase() {
    abstract fun eventDao(): DaoPickEvent
}

@Database(entities = [DataBestMonth::class], version = 3)
abstract class DataBaseBestMonth: RoomDatabase() {
    abstract fun bestDaoMonth(): BestDaoMonth
}