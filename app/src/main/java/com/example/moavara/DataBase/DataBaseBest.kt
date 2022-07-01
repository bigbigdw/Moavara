package com.example.moavara.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookListDataBestToday::class], version = 4)
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

//@Database(entities = [DataBestDay::class], version = 1)
//abstract class DataBaseBestDayTest: RoomDatabase() {
//    abstract fun bestDao(): BestDao
//
//    companion object {
//        private var Instance: DataBaseBestDayTest? = null
//
//        fun getInstance(context: Context): DataBaseBestDayTest? {
//            if(Instance == null) {
//                synchronized(DataBaseBestDayTest::class) {
//                    Instance = Room.databaseBuilder(
//                        context,
//                        DataBaseBestDayTest::class.java,
//                        "hanja"
//                    ).build()
//                    // migration // .addMigrations(MIGRATION_1_2)
//                }
//            }
//            return Instance
//        }
//
//        fun deleteInstance() {
//            Instance = null
//        }
//
//    }
//}