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

    @Query("DELETE FROM JoaraBest WHERE day = :day")
    fun deleteWeek(day: Int)

    @Query("SELECT * FROM JoaraBest")
    fun getAll(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE day = 1")
    fun getSun(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE day = 2")
    fun getMon(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE day = 3")
    fun getTue(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE day = 4")
    fun getWed(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE day = 5")
    fun getThu(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE day = 6")
    fun getFri(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE day = 7")
    fun getSat(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE week = 1")
    fun getMonthFirst(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE week = 2")
    fun getMonthSecond(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE week = 3")
    fun getMonthThird(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE week = 4")
    fun getMonthFourth(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE week = 5")
    fun getMonthFifth(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE point = 5")
    fun getFirst(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE point = 4")
    fun getSecond(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE point = 3")
    fun getThird(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE point = 2")
    fun getFourth(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE point = 1")
    fun getFifth(): List<JoaraBest>

    @Query("SELECT * FROM JoaraBest WHERE point = 10")
    fun getBest(): List<JoaraBest>

    @Insert
    fun insert(user: JoaraBest)

    @Delete
    fun delete(user: JoaraBest)
}