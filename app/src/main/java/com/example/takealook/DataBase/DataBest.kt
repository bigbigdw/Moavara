package com.example.takealook.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    var name: String,
    var age: String,
    var phone: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

@Entity
data class JoaraBest (
    var writer: String ? = null,
    var title: String ? = null,
    var bookImg: String ? = null,
    var intro: String ? = null,
    var bookCode: String ? = null,
    var cntChapter: String ? = null,
    var cntPageRead: String ? = null,
    var cntFavorite: String ? = null,
    var cntRecom: String ? = null,
    var number: Int ? = null,
    var day: Int ? = null,
    var week: Int ? = null,
    var point: Int ? = null,
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}