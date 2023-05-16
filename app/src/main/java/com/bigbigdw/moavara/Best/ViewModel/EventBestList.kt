package com.bigbigdw.moavara.Best.ViewModel

import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Search.BookListDataBest
import com.bigbigdw.moavara.Search.BookListDataBestAnalyze

interface EventBestList {
    object Today : EventBestList
    object TodayDone : EventBestList
    object Week : EventBestList
    object Month : EventBestList
    class InitBest(val initBest: DataBaseUser) : EventBestList
    class BestToday(
        val BestTodayItem: ArrayList<BookListDataBest>,
        val BestTodayItemBookCode: ArrayList<BookListDataBestAnalyze>
    ) : EventBestList

    object Loading : EventBestList

    object Loaded : EventBestList
}