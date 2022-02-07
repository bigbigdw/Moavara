package com.example.moavara.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class DataBest (
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
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}