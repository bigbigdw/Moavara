package com.example.takealook.DataBase

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception
import java.util.concurrent.atomic.AtomicInteger

class DBHelper(context: Context?, name: String?, factory: CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        val sql = ("CREATE TABLE if not exists mytable ("
                + "_id integer primary key autoincrement,"
                + "txt text);")
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql = "DROP TABLE if exists mytable"
        db.execSQL(sql)
        onCreate(db)
    }
}