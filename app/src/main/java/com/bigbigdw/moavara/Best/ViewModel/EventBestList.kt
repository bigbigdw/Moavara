package com.bigbigdw.moavara.Best.ViewModel

import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Search.BestListAnalyzeWeek
import com.bigbigdw.moavara.Search.BestItemData
import com.bigbigdw.moavara.Search.BestListAnalyze
import com.bigbigdw.moavara.Search.BottomBestItemData

interface EventBestList {
    object Today : EventBestList
    object TodayDone : EventBestList
    object Week : EventBestList
    object Month : EventBestList
    class InitBest(val initBest: DataBaseUser) : EventBestList
    class BestToday(
        val BestTodayItem: ArrayList<BestItemData>,
        val BestTodayItemBookCode: ArrayList<BestListAnalyze>
    ) : EventBestList

    object Loading : EventBestList
    object Loaded : EventBestList
    class isFirstPick(val isFirstPick : Boolean, val isPicked : Boolean) : EventBestList
    class bestListAnalyzeWeek(val bestListAnalyzeWeek: ArrayList<BestListAnalyzeWeek>) : EventBestList
    class bottomBestItemData(val bottomBestItemData: BottomBestItemData) : EventBestList
}