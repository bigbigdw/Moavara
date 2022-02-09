package com.example.moavara.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BestDao {

    @Query("SELECT number FROM DataBestWeek WHERE type = :type AND title = :title GROUP BY type")
    fun findName(type: String, title: String): Int

    @Query("SELECT * FROM DataBestWeek WHERE type = :type ORDER BY title ASC")
    fun getAll(type: String): List<DataBestWeek>

    @Query("DELETE FROM DataBestWeek")
    fun initAll()

    @Query("SELECT writer, title, bookImg, intro, bookCode, cntChapter, cntPageRead, cntFavorite, cntRecom, SUM(number), date, id FROM DataBestWeek GROUP BY writer, title, bookImg, intro, bookCode, cntChapter, cntPageRead, cntFavorite, cntRecom, date, id")
    fun test(): List<DataBestWeek>

    @Insert
    fun insert(user: DataBestWeek)

    @Delete
    fun delete(user: DataBestWeek)
}