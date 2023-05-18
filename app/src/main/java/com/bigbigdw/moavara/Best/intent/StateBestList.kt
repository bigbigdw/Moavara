package com.bigbigdw.moavara.Best.intent

import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.DataBase.BestItemData
import com.bigbigdw.moavara.DataBase.BestListAnalyze
import com.bigbigdw.moavara.DataBase.BestListAnalyzeWeek
import com.bigbigdw.moavara.DataBase.BottomBestItemData

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
    val bestItemData: BestItemData = BestItemData(),
    val type: String = "",
    val position: Int = 0,
)