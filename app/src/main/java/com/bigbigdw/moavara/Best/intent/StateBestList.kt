package com.bigbigdw.moavara.Best.intent

import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Search.BookListDataBest
import com.bigbigdw.moavara.Search.BookListDataBestAnalyze

data class StateBestList(
    val InitBest: DataBaseUser = DataBaseUser(),
    val BestTodayItem: ArrayList<BookListDataBest> = ArrayList<BookListDataBest>(),
    val BestTodayItemBookCode: ArrayList<BookListDataBestAnalyze> = ArrayList<BookListDataBestAnalyze>(),
    val Today: Boolean = false,
    val Week: Boolean = false,
    val Month: Boolean = false,
    val Loading: Boolean = true
)