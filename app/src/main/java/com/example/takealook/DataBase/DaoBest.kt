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

//@Dao
//interface UserDao {
//    @Insert
//    fun insert(user: User)
//
//    @Update
//    fun update(user: User)
//
//    @Delete
//    fun delete(user: User)
//}

//@Dao
//interface UserDao {
//    @Query("SELECT * FROM User") // 테이블의 모든 값을 가져와라
//    fun getAll(): List<User>
//
//    @Query("DELETE FROM User WHERE name = :name") // 'name'에 해당하는 유저를 삭제해라
//    fun deleteUserByName(name: String)
//}