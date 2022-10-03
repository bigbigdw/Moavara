package com.example.moavara.Util

import android.icu.text.DecimalFormat
import com.example.moavara.Search.BookListDataBest
import com.example.moavara.Search.BookListDataBestAnalyze
import com.example.moavara.Search.BookListDataBestInfo
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject

object BestRef {
    private val mRootRef = FirebaseDatabase.getInstance().reference

    fun setBestRef(platform: String, genre: String): DatabaseReference {
        return mRootRef.child("Best").child(platform).child(genre)
    }

    private fun setBestRefMunpia(type: String): DatabaseReference {
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

    fun typeListTitleBookCode(): List<String> {
        return listOf(
            "조아라",
            "시리즈",
            "챌린지",
            "베스트",
            "페이지",
            "스테이지",
            "리디북스",
            "원스토리",
            "문피아",
            "톡소다"
        )
    }

    fun typeListBookcode(): List<String> {
        return listOf(
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
        )
    }

    fun typeListTitleSearch(): List<String> {
        return listOf(
            "통합 검색",
            "조아라",
            "시리즈",
            "챌린지",
            "베스트",
            "페이지",
            "스테이지",
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
            "Munpia",
            "Toksoda",
            "MrBlue"
        )
    }

    fun typeListTitleEvent(): List<String> {
        return listOf(
            "조아라",
            "카카오",
            "스테이지",
            "리디북스",
            "원스토리",
            "문피아",
            "톡소다",
            "미스터블루",
        )
    }

    fun typeListEvent(): List<String> {
        return listOf(
            "Joara",
            "Kakao",
            "Kakao_Stage",
            "Ridi",
            "OneStore",
            "Munpia",
            "Toksoda",
            "MrBlue"
        )
    }


    fun setBookCode(type: String, genre: String, bookCode: String): DatabaseReference {
        return if(type == "Munpia"){
            setBestRefMunpia(type).child("BookCode").child(bookCode).child(DBDate.DateMMDD())
        } else {
            setBestRef(type, genre).child("BookCode").child(bookCode).child(DBDate.DateMMDD())
        }
    }

    fun setBookInfo(type: String, genre: String, bookCode: String): DatabaseReference {
        return if(type == "Munpia"){
            setBestRefMunpia(type).child("BookInfo").child(bookCode)
        } else {
            setBestRef(type, genre).child("BookInfo").child(bookCode)
        }
    }

    fun getBookCode(platform: String, genre: String): DatabaseReference {
        return if(platform == "Munpia"){
            setBestRefMunpia(platform).child("BookCode")
        } else {
            setBestRef(platform, genre).child("BookCode")
        }
    }

    fun getBestRefToday(platform: String, genre: String): DatabaseReference {
        return if(platform == "Munpia"){
            setBestRefMunpia(platform).child("today").child(DBDate.DayInt().toString())
        } else {
            setBestRef(platform, genre).child("today").child(DBDate.DayInt().toString())
        }
    }

    fun setBestData(platform: String, num: Int, genre: String): DatabaseReference {
        return if(platform == "Munpia"){
            setBestRefMunpia(platform).child("Data").child(DBDate.Month()).child(DBDate.Week()).child(DBDate.DayInt().toString()).child(num.toString())
        } else {
            setBestRef(platform, genre).child("Data").child(DBDate.Month()).child(DBDate.Week()).child(DBDate.DayInt().toString()).child(num.toString())
        }
    }

    fun getBestDataToday(platform: String, genre: String): DatabaseReference {
        return if(platform == "Munpia"){
            setBestRefMunpia(platform).child("Data").child(DBDate.Month()).child(DBDate.Week()).child(DBDate.DayInt().toString())
        } else {
            setBestRef(platform, genre).child("Data").child(DBDate.Month()).child(DBDate.Week()).child(DBDate.DayInt().toString())
        }
    }

    fun getBestDataWeekBefore(platform: String, genre: String): DatabaseReference {
        return if(platform == "Munpia"){
            setBestRefMunpia(platform).child("Data")
        } else {
            setBestRef(platform, genre).child("Data")
        }
    }

    fun getBestDataWeek(platform: String, genre: String): DatabaseReference {
        return if(platform == "Munpia"){
            setBestRefMunpia(platform).child("Data").child(DBDate.Month()).child(DBDate.Week())
        } else {
            setBestRef(platform, genre).child("Data").child(DBDate.Month()).child(DBDate.Week())
        }
    }

    fun getBestDataMonthBefore(platform: String, genre: String): DatabaseReference {
        return if(platform == "Munpia"){
            setBestRefMunpia(platform).child("Data")
        } else {
            setBestRef(platform, genre).child("Data")
        }
    }

    fun getBestDataMonth(platform: String, genre: String): DatabaseReference {
        return if(platform == "Munpia"){
            setBestRefMunpia(platform).child("Data")
        } else {
            setBestRef(platform, genre).child("Data")
        }
    }

    fun setBookListDataBestInfo(ref: MutableMap<String?, Any>): BookListDataBestInfo {
        return BookListDataBestInfo(
            ref["keyword"] as ArrayList<*>,
            ref["genre"] as String,
            ref["writerName"] as String,
            ref["subject"] as String,
            ref["bookImg"] as String,
            ref["bookCode"] as String,
            ref["info1"] as String,
            ref["info2"] as String,
            ref["info3"] as String,
            ref["info4"] as String,
            ref["info5"] as String,
            ref["info6"] as String,
            ref["number"] as Int,
            ref["date"] as String,
            ref["type"] as String
        )
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
            ref["info6"] as String,
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
            ref["info6"] as String,
            ref["number"] as Int,
            ref["date"] as String,
            0,
            0
        )
    }

    fun putItem(jsonObject: JSONObject, item: BookListDataBest): JSONObject {

        jsonObject.put("writer", item.writer)
        jsonObject.put("title", item.title)
        jsonObject.put("bookImg", item.bookImg)
        jsonObject.put("bookCode", item.bookCode)
        jsonObject.put("info1", item.info1)
        jsonObject.put("info2", item.info2)
        jsonObject.put("info3", item.info3)
        jsonObject.put("info4", item.info4)
        jsonObject.put("info5", item.info5)
        jsonObject.put("number", item.number)
        jsonObject.put("date", item.date)
        jsonObject.put("type", item.type)
        jsonObject.put("memo", item.memo)

        return jsonObject
    }

    fun getItem(jsonObject: JSONObject): BookListDataBest {

        return BookListDataBest(
            jsonObject.optString("writer"),
            jsonObject.optString("title"),
            jsonObject.optString("bookImg"),
            jsonObject.optString("bookCode"),
            jsonObject.optString("info1"),
            jsonObject.optString("info2"),
            jsonObject.optString("info3"),
            jsonObject.optString("info4"),
            jsonObject.optString("info5"),
            jsonObject.optString("info6"),
            jsonObject.optInt("number"),
            jsonObject.optString("date"),
            jsonObject.optString("type"),
            jsonObject.optString("memo"),
        )
    }

    fun decimalToString(num : Int) : String {
        return if(num in 9999 downTo 999){
            val decFormat = DecimalFormat("#,###")
            return decFormat.format(num).toString()
        } else if(num < 10000){
            "$num"
        } else {
            ("${(num / 1000).toFloat() / 10}만").replace(".0","")
        }
    }

    fun setDetailText(platform : String, type : Int): String {
        if(platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium"){
            if(type == 1){
                return "조회 수 : "
            } else if(type == 2){
                return "선호작 수 : "
            } else if(type == 3){
                return "추천 수 : "
            }
        } else if (platform == "Naver" || platform == "Naver_Today" || platform == "Naver_Challenge") {
            if (type == 1) {
                return "별점 : "
            } else if (type == 2) {
                return "조회 수 : "
            } else if (type == 3) {
                return "관심 : "
            }
        } else {
            return ""
        }
        return ""
    }
}