package com.bigbigdw.moavara.Best.ViewModel

import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.DataBase.BestItemData
import com.bigbigdw.moavara.DataBase.BestListAnalyze
import com.bigbigdw.moavara.DataBase.BestListAnalyzeWeek
import com.bigbigdw.moavara.DataBase.BottomBestItemData

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
    class IsFirstPick(val isFirstPick : Boolean, val isPicked : Boolean) : EventBestList
    class bestListAnalyzeWeek(val bestListAnalyzeWeek: ArrayList<BestListAnalyzeWeek>) : EventBestList
    class BottomItem(val bottomBestItemData: BottomBestItemData) : EventBestList
    class ItemData(val bestItemData: BestItemData) : EventBestList
    class Type(val type: String) : EventBestList
    class Position(val position: Int) : EventBestList
}