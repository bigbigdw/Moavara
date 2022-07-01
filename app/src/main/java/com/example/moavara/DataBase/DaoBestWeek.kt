package com.example.moavara.DataBase

import androidx.room.*

@Dao
interface BestDao {

    @Query("SELECT * FROM BookListDataBestToday WHERE title = :title")
    fun getRank(title: String): List<BookListDataBestToday>

    @Query("SELECT number FROM BookListDataBestToday WHERE type = :type AND title = :title GROUP BY type")
    fun findName(type: String, title: String): Int

    @Query("SELECT * FROM BookListDataBestToday WHERE type = :type AND title = :title")
    fun findDay(type: String, title: String): BookListDataBestToday

    @Query("SELECT * FROM BookListDataBestToday WHERE type = :type AND title = :title AND number = :number")
    fun findDay(type: String, title: String , number: Int): BookListDataBestToday

    @Query("SELECT * FROM BookListDataBestToday WHERE type = :type ORDER BY title ASC")
    fun getAll(type: String): List<BookListDataBestToday>

    @Query("SELECT * FROM BookListDataBestToday")
    fun getPickAll(): List<BookListDataBestToday>

    @Query("DELETE FROM BookListDataBestToday WHERE type = :type")
    fun initWeek(type: String)

    @Query("DELETE FROM BookListDataBestToday")
    fun initAll()

    @Query("UPDATE BookListDataBestToday SET memo = :memo WHERE bookCode = :bookCode")
    fun updateItem(memo: String, bookCode: String)

    @Query("DELETE FROM BookListDataBestToday WHERE bookCode = :bookCode")
    fun deleteItem(bookCode: String)

    @Query("SELECT title = :title, COUNT(*) FROM BookListDataBestToday GROUP BY title HAVING COUNT(*) > 1")
    fun countTrophy(title: String): Int

    @Insert
    fun insert(user: BookListDataBestToday)

    @Delete
    fun delete(user: BookListDataBestToday)

    @Update
    fun update(user: BookListDataBestToday)
}