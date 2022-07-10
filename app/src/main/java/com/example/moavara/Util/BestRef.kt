package com.example.moavara.Util

import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.DataBase.BookListDataBestToday
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

object BestRef {
    private val mRootRef = FirebaseDatabase.getInstance().reference

    fun StrToInt(str: String): Int {
        if(str.contains("만")){
            return str.replace("만","").toInt() * 10000
        } else if (str.contains(",")) {
            return str.replace(",","").toInt()
        } else return str.toInt()
    }

    fun setBestRef(type: String, genre: String): DatabaseReference {
        return mRootRef.child("best").child(type).child(genre)
    }

    fun setBestRefMunpia(type: String): DatabaseReference {
        return mRootRef.child("best").child(type)
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
//            "미스터블루"
        )
    }

    fun typeList(): List<String> {
        return listOf(
            "Joara",
            "Joara Nobless",
            "Joara Premium",
            "Naver Today",
            "Naver Challenge",
            "Naver",
            "Kakao",
            "Kakao Stage",
            "Ridi",
            "OneStore",
            "Munpia",
            "Toksoda",
//            "MrBlue"
        )
    }


    fun setBestRefWeekList(type: String, genre: String): DatabaseReference {
        if(type == "Munpia"){
            return setBestRefMunpia(type).child("week-list")
        } else {
            return setBestRef(type, genre).child("week-list")
        }
    }

    fun setBestRefWeekCompared(type: String, num: Int, genre: String): DatabaseReference {
        if(type == "Munpia"){
            return setBestRefMunpia(type).child("week-list").child(DBDate.Week() + ((DBDate.DayInt() * 1000) + num).toString())
        } else {
            return setBestRef(type, genre).child("week-list").child(DBDate.Week() + ((DBDate.DayInt() * 1000) + num).toString())
        }
    }

    fun setBookData(type: String, num: Int, genre: String): DatabaseReference {
        if(type == "Munpia"){
            return setBestRefMunpia(type).child("today BookData").child(num.toString())
        } else {
            return setBestRef(type, genre).child("today BookData").child(num.toString())
        }
    }

    fun setBestRefToday(type: String, num: Int, genre: String): DatabaseReference {
        if(type == "Munpia"){
            return setBestRefMunpia(type).child("today").child(DBDate.Day()).child(num.toString())
        } else {
            return setBestRef(type, genre).child("today").child(DBDate.Day()).child(num.toString())
        }
    }

    fun getBestRefToday(type: String, genre: String): DatabaseReference {
        if(type == "Munpia"){
            return setBestRefMunpia(type).child("today").child(DBDate.Day())
        } else {
            return setBestRef(type, genre).child("today").child(DBDate.Day())
        }
    }

    fun setBestRefWeek(type: String, num: Int, genre: String): DatabaseReference {
        if(type == "Munpia"){
            return setBestRefMunpia(type).child("week").child(DBDate.Week()).child(num.toString()).child(DBDate.DayString())
        } else {
            return setBestRef(type, genre).child("week").child(DBDate.Week()).child(num.toString()).child(DBDate.DayString())
        }
    }

    fun setBestRefMonthWeek(type: String, genre: String): DatabaseReference {
        if(type == "Munpia"){
            return setBestRefMunpia(type).child("month").child(DBDate.Month()).child(DBDate.Week())
                .child(DBDate.DayString())
        } else {
            return setBestRef(type, genre).child("month").child(DBDate.Month()).child(DBDate.Week())
                .child(DBDate.DayString())
        }
    }

    fun setBestRefMonthDay(type: String, num: Int, genre: String): DatabaseReference {
        if(type == "Munpia"){
            return setBestRefMunpia(type).child("month").child(DBDate.Month()).child(DBDate.Week())
                .child(DBDate.DayString()).child("day").child(num.toString())
        } else {
            return setBestRef(type, genre).child("month").child(DBDate.Month()).child(DBDate.Week())
                .child(DBDate.DayString()).child("day").child(num.toString())
        }
    }

    fun setBestRefMonth(type: String, genre: String): DatabaseReference {
        if(type == "Munpia"){
            return setBestRefMunpia(type).child("month").child(DBDate.Month()).child(DBDate.Week())
                .child(DBDate.DayString())
        } else {
            return setBestRef(type, genre).child("month").child(DBDate.Month()).child(DBDate.Week())
                .child(DBDate.DayString())
        }
    }

    fun setBookListDataBestToday(ref: MutableMap<String?, Any>): BookListDataBest {
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
            ref["numberDiff"] as Int,
            ref["date"] as String,
            ref["type"] as String,
            ref["status"] as String,
            ref["trophyCount"] as Int,
            ref["data"] as ArrayList<BookListDataBestAnalyze>,
        )
    }
}