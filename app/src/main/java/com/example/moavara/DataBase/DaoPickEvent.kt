package com.example.moavara.DataBase

import androidx.room.*
import com.example.moavara.Search.EventData

@Dao
interface DaoPickEvent {

    @Query("SELECT * FROM DataEvent")
    fun getAll(): List<DataEvent>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: DataEvent?)

    @Delete
    fun delete(user: DataEvent)

    @Update
    fun update(user: DataEvent)
}