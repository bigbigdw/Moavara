package com.bigbigdw.moavara.Best.intent

import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Search.BookBestAnalyzeWeek
import com.bigbigdw.moavara.Search.BookListDataBest
import com.bigbigdw.moavara.Search.BookListDataBestAnalyze

data class StateBestList(
    val InitBest: DataBaseUser = DataBaseUser(),
    val BestTodayItem: ArrayList<BookListDataBest> = ArrayList<BookListDataBest>(),
    val BestTodayItemBookCode: ArrayList<BookListDataBestAnalyze> = ArrayList<BookListDataBestAnalyze>(),
    val TodayInit: Boolean = true,
    val Week: Boolean = false,
    val Month: Boolean = false,
    val Loading: Boolean = true,
    val isFirstPick: Boolean = false,
    val isPicked: Boolean = false,
    val BookBestAnalyzeWeek: ArrayList<BookBestAnalyzeWeek> = ArrayList(),
)