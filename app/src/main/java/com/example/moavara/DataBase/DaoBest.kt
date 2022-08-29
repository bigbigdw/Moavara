package com.example.moavara.DataBase

import androidx.room.*

@Dao
interface DaoBest {

    @Query("SELECT * FROM RoomBookListDataBest WHERE bookCode = :bookCode")
    fun getRank(bookCode: String): List<RoomBookListDataBest>

    @Query("SELECT number FROM RoomBookListDataBest WHERE type = :type AND title = :title GROUP BY type")
    fun findName(type: String, title: String): Int

    @Query("SELECT * FROM RoomBookListDataBest WHERE type = :type AND title = :title")
    fun findDay(type: String, title: String): RoomBookListDataBest

    @Query("SELECT * FROM RoomBookListDataBest WHERE type = :type AND title = :title AND number = :number")
    fun findDay(type: String, title: String , number: Int): RoomBookListDataBest

    @Query("SELECT * FROM RoomBookListDataBest")
    fun getAll(): List<RoomBookListDataBest>

    @Query("SELECT * FROM RoomBookListDataBest")
    fun getPickAll(): List<RoomBookListDataBest>

    @Query("DELETE FROM RoomBookListDataBest WHERE type = :type")
    fun initWeek(type: String)

    @Query("DELETE FROM RoomBookListDataBest")
    fun initAll()

    @Query("UPDATE RoomBookListDataBest SET memo = :memo WHERE bookCode = :bookCode")
    fun updateItem(memo: String, bookCode: String)

    @Query("DELETE FROM RoomBookListDataBest WHERE bookCode = :bookCode")
    fun deleteItem(bookCode: String)

    @Query("SELECT COUNT(title) FROM RoomBookListDataBest WHERE title =:title")
    fun countTrophy(title: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: RoomBookListDataBest)

    @Delete
    fun delete(user: RoomBookListDataBest)

    @Update
    fun update(user: RoomBookListDataBest)
}