package com.example.moavara.DataBase

import androidx.room.*

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

    @Query("SELECT * FROM DataBestMonth WHERE type = :type LIMIT 5")
    fun getAllFirst(type: String): List<DataBestMonth>

    @Query("SELECT * FROM DataBestMonth WHERE type = :type AND week = :week LIMIT 5")
    fun getAllFirstList(type: String, week: String): List<DataBestMonth>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: DataBestMonth)

    @Delete
    fun delete(user: DataBestMonth)
}