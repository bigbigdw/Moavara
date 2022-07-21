package com.example.moavara.Util

import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object BestRef {
    private val mRootRef = FirebaseDatabase.getInstance().reference

    fun StrToInt(str: String): Int {
        if(str.contains("만")){
            return str.replace("만","").toInt() * 10000
        } else if (str.contains(",")) {
            return str.replace(",","").toInt()
        } else return str.toInt()
    }

    fun setBestRef(platform: String, genre: String): DatabaseReference {
        return mRootRef.child("Best").child(platform).child(genre)
    }

    fun setBestRefMunpia(type: String): DatabaseReference {
        return mRootRef.child("Best").child(type)
    }

    fun typeListTitle(): List<String> {
        return listOf(
            "조아라",
            "노블레스",
            "프리미엄",
            "네이버시리즈",
            "챌린지리그",
            "베스트리그",
            "카카오페이지",
            "스테이지",
            "리디북스",
            "원스토리",
            "문피아",
            "톡소다",
            "미스터블루"
        )
    }

    fun typeList(): List<String> {
        return listOf(
            "Joara",
            "Joara_Nobless",
            "Joara_Premium",
            "Naver_Today",
            "Naver_Challenge",
            "Naver",
            "Kakao",
            "Kakao_Stage",
            "Ridi",
            "OneStore",
            "Munpia",
            "Toksoda",
            "MrBlue"
        )
    }

    fun typeListTitleSearch(): List<String> {
        return listOf(
            "통합 검색",
            "조아라",
            "네이버시리즈",
            "챌린지리그",
            "베스트리그",
            "카카오페이지",
            "스테이지",
            "리디북스",
            "원스토리",
            "문피아",
            "톡소다",
            "미스터블루"
        )
    }

    fun typeListSearch(): List<String> {
        return listOf(
            "Keyword",
            "Joara",
            "Naver_Today",
            "Naver_Challenge",
            "Naver",
            "Kakao",
            "Kakao_Stage",
            "Ridi",
            "OneStore",
            "Munpia",
            "Toksoda",
            "MrBlue"
        )
    }


    fun setBestRefWeekList(platform: String, genre: String): DatabaseReference {
        if(platform == "Munpia"){
            return setBestRefMunpia(platform).child("week-list")
        } else {
            return setBestRef(platform, genre).child("week-list")
        }
    }

    fun setBookCode(type: String, genre: String, bookCode : String, num : Int): DatabaseReference {
        if(type == "Munpia"){
            return setBestRefMunpia(type).child("BookCode").child(bookCode).child(DBDate.DateMMDD())
        } else {
            return setBestRef(type, genre).child("BookCode").child(bookCode).child(DBDate.DateMMDD())
        }
    }

    fun getBookCode(platform: String, genre: String): DatabaseReference {
        if(platform == "Munpia"){
            return setBestRefMunpia(platform).child("BookCode")
        } else {
            return setBestRef(platform, genre).child("BookCode")
        }
    }

    fun getBestRefToday(platform: String, genre: String): DatabaseReference {
        if(platform == "Munpia"){
            return setBestRefMunpia(platform).child("today").child(DBDate.DayInt().toString())
        } else {
            return setBestRef(platform, genre).child("today").child(DBDate.DayInt().toString())
        }
    }

    fun setBestData(platform: String, num: Int, genre: String): DatabaseReference {
        if(platform == "Munpia"){
            return setBestRefMunpia(platform).child("Data").child(DBDate.Month()).child(DBDate.Week()).child(DBDate.DayInt().toString()).child(num.toString())
        } else {
            return setBestRef(platform, genre).child("Data").child(DBDate.Month()).child(DBDate.Week()).child(DBDate.DayInt().toString()).child(num.toString())
        }
    }

    fun getBestDataToday(platform: String, genre: String): DatabaseReference {
        if(platform == "Munpia"){
            return setBestRefMunpia(platform).child("Data").child(DBDate.Month()).child(DBDate.Week()).child(DBDate.DayInt().toString())
        } else {
            return setBestRef(platform, genre).child("Data").child(DBDate.Month()).child(DBDate.Week()).child(DBDate.DayInt().toString())
        }
    }

    fun getBestDataWeek(platform: String, genre: String): DatabaseReference {
        if(platform == "Munpia"){
            return setBestRefMunpia(platform).child("Data").child(DBDate.Month()).child(DBDate.Week())
        } else {
            return setBestRef(platform, genre).child("Data").child(DBDate.Month()).child(DBDate.Week())
        }
    }

    fun getBestDataMonth(platform: String, genre: String): DatabaseReference {
        if(platform == "Munpia"){
            return setBestRefMunpia(platform).child("Data").child(DBDate.Month())
        } else {
            return setBestRef(platform, genre).child("Data").child(DBDate.Month())
        }
    }

    fun setBookListDataBest(ref: MutableMap<String?, Any>): BookListDataBest {
        return BookListDataBest(
            ref["writerName"] as String,
            ref["subject"] as String,
            ref["bookImg"] as String,
            ref["bookCode"] as String,
            ref["info1"] as String,
            ref["info2"] as String,
            ref["info3"] as String,
            ref["info4"] as String,
            ref["info5"] as String,
            ref["number"] as Int,
            ref["date"] as String,
            ref["type"] as String
        )
    }

    fun setBookListDataBestAnalyze(ref: MutableMap<String?, Any>): BookListDataBestAnalyze {
        return BookListDataBestAnalyze(
            ref["info3"] as String,
            ref["info4"] as String,
            ref["info5"] as String,
            ref["number"] as Int,
            ref["date"] as String,
        )
    }
}