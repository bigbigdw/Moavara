package com.bigbigdw.moavara.Best.ViewModel

import com.bigbigdw.moavara.DataBase.*
import com.bigbigdw.moavara.Retrofit.JoaraBestChapter

interface EventBestDetail {
    object Loading : EventBestDetail
    object Loaded : EventBestDetail

    class InitBestDetail(
        val bookCode: String,
        val type: String,
        val position: Int,
        val fromPick: Boolean,
        val genre: String,
        val dataBaseUser: DataBaseUser
    ) : EventBestDetail

    class hasBookData(val hasBookData : Boolean) : EventBestDetail

    class IsFirstPick(val isFirstPick : Boolean, val isPicked : Boolean) : EventBestDetail

    class isTab(val isTabAnalyze : Boolean, val isTabComment : Boolean, val isTabOther : Boolean) : EventBestDetail

    class isJoaraChapter(val joaraChapter: List<JoaraBestChapter>) : EventBestDetail

    class bestDetailData(val bestItemData: BestDetailItemData, val bestListAnalyze: BestListAnalyze) : EventBestDetail

    class bestDetailKeywords(val keywords: ArrayList<BestType>) : EventBestDetail

    class bookData(val bookData: ArrayList<BestListAnalyze>) : EventBestDetail
}