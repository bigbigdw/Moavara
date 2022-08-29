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
    var memo: String = ""
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

@Entity
data class RoomBookListDataBest (
    var writer: String = "",
    var title: String = "",
    var bookImg: String = "",
    var bookCode: String = "",
    var info1: String = "",
    var info2: String = "",
    var info3: String = "",
    var info4: String = "",
    var info5: String = "",
    var info6: String = "",
    var number: Int = 0,
    var date: String = "",
    var type: String = "",
    var memo: String = "",
    var week: Int = 0,
    var month: Int = 0,
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

@Entity
data class RoomBookListDataBestAnalyze (
    var info1: String = "",
    var info2: String = "",
    var info3: String = "",
    var info4: String = "",
    var number: Int = 0,
    var numInfo1: Int = 0,
    var numInfo2: Int = 0,
    var numInfo3: Int = 0,
    var numInfo4: Int = 0,
    var date: String = "",
    var numberDiff: Int = 0,
    var trophyCount: Int = 0,
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

@Entity
data class DataBaseUser (
    var Nickname: String = "",
    var Genre: String = "",
    var UID: String = "",
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}