package com.example.moavara.DataBase

import androidx.room.*

@Dao
interface DaoUser {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: DataBaseUser)

    @Delete
    fun delete(user: DataBaseUser)

    @Update
    fun update(user: DataBaseUser)

    @Query("SELECT * FROM DataBaseUser")
    fun get(): DataBaseUser

    @Query("DELETE FROM DataBaseUser")
    fun init()
}