package com.bigbigdw.moavara.DataBase

import androidx.room.*

@Dao
interface DaoBestBookCode {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: RoomBookListDataBestAnalyze)

    @Delete
    fun delete(user: RoomBookListDataBestAnalyze)

    @Update
    fun update(user: RoomBookListDataBestAnalyze)

    @Query("SELECT * FROM RoomBookListDataBestAnalyze")
    fun get(): List<RoomBookListDataBestAnalyze>

    @Query("DELETE FROM RoomBookListDataBestAnalyze")
    fun initAll()
}