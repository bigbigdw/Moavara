package com.example.moavara.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataEvent (
    var link: String = "",
    var imgfile: String = "",
    var title: String = "",
    var genre: String = "",
    var type: String = "",
    var memo: String = "",
){
    @PrimaryKey(autoGenerate = true) var id: Int = 2
}

@Entity
data class DataBestDay (
    var writer: String ? = null,
    var title: String ? = null,
    var bookImg: String ? = null,
    var bookCode: String ? = null,
    var info1: String ? = null,
    var info2: String ? = null,
    var info3: String ? = null,
    var info4: String ? = null,
    var info5: String ? = null,
    var number: Int ? = null,
    var numberDiff: Int ? = null,
    var date: String ? = null,
    var type: String ? = null,
    var status: String ? = null,
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