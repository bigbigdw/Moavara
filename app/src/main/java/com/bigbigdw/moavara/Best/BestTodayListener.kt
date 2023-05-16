package com.bigbigdw.moavara.Best

import com.bigbigdw.moavara.Search.BookListDataBest


interface BestTodayListener {
    fun getBestTodayList(items : ArrayList<BookListDataBest>, status : Boolean = false)
}