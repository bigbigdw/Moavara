package com.example.moavara.Best

import com.example.moavara.Search.BookListDataBest


interface BestTodayListener {
    fun getBestTodayList(items : ArrayList<BookListDataBest>, status : Boolean = false)
}