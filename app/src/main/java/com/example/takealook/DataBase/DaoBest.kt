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
    @Query("SELECT * FROM JoaraBest")
    fun getAll(): List<JoaraBest>

    @Insert
    fun insert(user: JoaraBest)

}