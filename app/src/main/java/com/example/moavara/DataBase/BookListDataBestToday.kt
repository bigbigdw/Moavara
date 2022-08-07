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
data class BookListDataBestToday (
    var writer: String = "",
    var title: String = "",
    var bookImg: String = "",
    var bookCode: String = "",
    var info1: String = "",
    var info2: String = "",
    var info3: String = "",
    var info4: String = "",
    var info5: String = "",
    var number: Int = 0,
    var numberDiff: Int = 0,
    var date: String = "",
    var type: String = "",
    var status: String = "",
    var trophyCount: Int = 0,
    var memo: String = "",
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

data class FCMAlert (
    var date: String = "",
    var title: String = "",
    var body: String = "",
)

data class BookListDataBest (
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
)

data class BestTodayAverage (
    var info1: String = "",
    var info2: String = "",
    var info3: String = "",
    var info4: String = "",
)

data class BookListDataBestAnalyze (
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
)

data class AnayzeData(
    var date: String = "",
    var info: String = "",
)

data class TrophyInfo (
    var month: Int = 0,
    var week: Int = 0,
    var date: Int = 0,
)

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