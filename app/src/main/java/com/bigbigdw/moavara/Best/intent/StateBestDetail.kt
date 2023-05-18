package com.bigbigdw.moavara.Best.intent

import com.bigbigdw.moavara.DataBase.*
import com.bigbigdw.moavara.Retrofit.JoaraBestChapter

data class StateBestDetail(
    val bookCode: String = "",
    val type: String = "",
    val position: Int = 0,
    val hasBookData: Boolean = false,
    val fromPick: Boolean = false,
    val genre : String = "",
    val loading : Boolean = false,
    val dataBaseUser: DataBaseUser = DataBaseUser(),
    val isFirstPick: Boolean = false,
    val isPicked: Boolean = false,
    val bestItemData : BestDetailItemData = BestDetailItemData(),
    val keywords: ArrayList<BestType> = ArrayList(),
    var bestListAnalyze : BestListAnalyze = BestListAnalyze(),
    var joaraChapter: List<JoaraBestChapter> = ArrayList(),
    val typeItems : ArrayList<BestType> = ArrayList(),
    val isTabAnalyze : Boolean = false,
    val isTabComment : Boolean = false,
    val isTabOther : Boolean = false,
    val bookData : ArrayList<BestListAnalyze> = ArrayList()
)