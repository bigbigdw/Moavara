package com.example.moavara.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BestDaoMonth {

    @Query("DELETE FROM DataBestMonth")
    fun initAll()

    @Query("DELETE FROM DataBestMonth WHERE type = :type")
    fun initTypes(type: String)

    @Query("SELECT * FROM DataBestMonth WHERE type = :type AND week = :week ORDER BY title ASC")
    fun getAll(type: String, week: String): List<DataBestMonth>

    @Query("SELECT * FROM DataBestMonth WHERE type = :type")
    fun getAllTypes(type: String): List<DataBestMonth>

    @Insert
    fun insert(user: DataBestMonth)

    @Delete
    fun delete(user: DataBestMonth)
}