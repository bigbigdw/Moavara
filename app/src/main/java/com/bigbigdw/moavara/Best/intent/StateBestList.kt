package com.bigbigdw.moavara.Best.intent

import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Search.BestListAnalyzeWeek
import com.bigbigdw.moavara.Search.BestItemData
import com.bigbigdw.moavara.Search.BestListAnalyze
import com.bigbigdw.moavara.Search.BottomBestItemData

data class StateBestList(
    val initBest: DataBaseUser = DataBaseUser(),
    val bestTodayItem: ArrayList<BestItemData> = ArrayList(),
    val bestTodayItemBookCode: ArrayList<BestListAnalyze> = ArrayList(),
    val todayInit: Boolean = true,
    val week: Boolean = false,
    val month: Boolean = false,
    val loading: Boolean = true,
    val isFirstPick: Boolean = false,
    val isPicked: Boolean = false,
    val bestListAnalyzeWeek: ArrayList<BestListAnalyzeWeek> = ArrayList(),
    val bottomBestItemData: BottomBestItemData = BottomBestItemData(),
)