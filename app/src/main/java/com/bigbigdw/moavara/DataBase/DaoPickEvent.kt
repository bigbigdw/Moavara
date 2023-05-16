package com.bigbigdw.moavara.DataBase

import androidx.room.*

@Dao
interface DaoPickEvent {

    @Query("SELECT * FROM DataEvent")
    fun getAll(): List<DataEvent>

    @Query("UPDATE DataEvent SET memo = :memo WHERE link = :link")
    fun updateItem(memo: String, link: String)

    @Query("DELETE FROM DataEvent WHERE link = :link")
    fun deleteItem(link: String)

    @Insert
    fun insert(user: DataEvent)

    @Delete
    fun delete(user: DataEvent)

    @Update
    fun update(user: DataEvent)
}