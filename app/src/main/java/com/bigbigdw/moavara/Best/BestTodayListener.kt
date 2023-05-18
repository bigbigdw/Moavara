package com.bigbigdw.moavara.Best

import com.bigbigdw.moavara.Search.BestItemData


interface BestTodayListener {
    fun getBestTodayList(items : ArrayList<BestItemData>, status : Boolean = false)
}