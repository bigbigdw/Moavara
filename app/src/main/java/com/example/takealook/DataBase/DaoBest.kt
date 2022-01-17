package com.example.takealook.DataBase

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Insert
    fun insert(user: User)

    @Query("DELETE FROM User WHERE name = :name")
    fun deleteUserByName(name: String)
}

@Dao
interface BestDao {

    @Query("DELETE FROM DataBest WHERE day = :day")
    fun deleteWeek(day: Int)

    @Query("SELECT * FROM DataBest WHERE day = :day AND type = :type")
    fun selectWeek(day: Int, type : String): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE day = :day")
    fun selectWeekTotal(day: Int): List<DataBest>

    @Query("SELECT * FROM DataBest")
    fun getAllTotal(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE type = :type")
    fun getAll(type: String): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE day = 1")
    fun getSun(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE day = 2")
    fun getMon(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE day = 3")
    fun getTue(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE day = 4")
    fun getWed(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE day = 5")
    fun getThu(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE day = 6")
    fun getFri(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE day = 7")
    fun getSat(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE week = 1")
    fun getMonthFirst(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE week = 2")
    fun getMonthSecond(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE week = 3")
    fun getMonthThird(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE week = 4")
    fun getMonthFourth(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE week = 5")
    fun getMonthFifth(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE point = 5")
    fun getFirst(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE point = 4")
    fun getSecond(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE point = 3")
    fun getThird(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE point = 2")
    fun getFourth(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE point = 1")
    fun getFifth(): List<DataBest>

    @Query("SELECT * FROM DataBest WHERE point = 10")
    fun getBest(): List<DataBest>

//    @Query("SELECT * FROM JoaraBest WHERE day = :bookCode UPDATE day = 1")
//    fun findBook(bookCode: String)

    @Insert
    fun insert(user: DataBest)

    @Delete
    fun delete(user: DataBest)
}