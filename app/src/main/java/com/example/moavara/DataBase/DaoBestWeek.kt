package com.example.moavara.DataBase

import androidx.room.*

@Dao
interface BestDao {

    @Query("SELECT * FROM DataBestDay WHERE title = :title")
    fun getRank(title: String): List<DataBestDay>

    @Query("SELECT number FROM DataBestDay WHERE type = :type AND title = :title GROUP BY type")
    fun findName(type: String, title: String): Int

    @Query("SELECT * FROM DataBestDay WHERE type = :type AND title = :title")
    fun findDay(type: String, title: String): DataBestDay

    @Query("SELECT * FROM DataBestDay WHERE type = :type AND title = :title AND number = :number")
    fun findDay(type: String, title: String , number: Int): DataBestDay

    @Query("SELECT * FROM DataBestDay WHERE type = :type ORDER BY title ASC")
    fun getAll(type: String): List<DataBestDay>

    @Query("DELETE FROM DataBestDay WHERE type = :type")
    fun initWeek(type: String)

    @Query("DELETE FROM DataBestDay")
    fun initAll()

    @Query("UPDATE DataBestDay SET memo = :memo WHERE bookCode = :bookCode")
    fun updateItem(memo: String, bookCode: String)

    @Query("DELETE FROM DataBestDay WHERE bookCode = :bookCode")
    fun deleteItem(bookCode: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: DataBestDay)

    @Delete
    fun delete(user: DataBestDay)

    @Update
    fun update(user: DataBestDay)
}