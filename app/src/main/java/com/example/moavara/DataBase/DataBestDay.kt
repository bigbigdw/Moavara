package com.example.moavara.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class DataBestDay (
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
    var date: String ? = null,
    var type: String ? = null,
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

@Entity
data class DataBestMonth (
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
    var date: String ? = null,
    var type: String ? = null,
    var week: String ? = null,
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}