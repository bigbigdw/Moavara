package com.example.moavara.Util

import android.annotation.SuppressLint
import com.example.moavara.Search.BookListDataBestToday
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

object DBDate {

    fun DayInt() : Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }

    fun Day() : String {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK).toString()
    }

    fun Yesterday() : String {

        return if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1){
            "7"
        } else {
            (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) -1).toString()
        }
    }

    fun DayString() : String {

        val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK).toString()

        when (day) {
            "1" -> {
                return "sun"
            }
            "2" -> {
                return "mon"
            }
            "3" -> {
                return "tue"
            }
            "4" -> {
                return "wed"
            }
            "5" -> {
                return "thur"
            }
            "6" -> {
                return "fri"
            }
            "7" -> {
                return "sat"
            }
            else -> {
                return ""
            }
        }
    }

    fun Week() : String {
        return Calendar.getInstance().get(Calendar.WEEK_OF_MONTH).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun Date() : String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("MM-dd")
        return format.format(currentTime).toString()
    }

    fun Month() : String {
        return Calendar.getInstance().get(Calendar.MONTH).toString()
    }
}

object BestRef {
    private val mRootRef = FirebaseDatabase.getInstance().reference

    private fun setBestRef(type: String, genre : String) : DatabaseReference {
        return mRootRef.child("best").child(type).child(genre)
    }

    fun setBestRefWeekList(type: String, num : Int, genre : String) : DatabaseReference {
        return setBestRef(type, genre).child("week list").child((((DBDate.DayInt() - 1) * 20) + num).toString())
    }

    fun setBestRefToday(type: String, num : Int, genre : String) : DatabaseReference {
        return setBestRef(type, genre).child("today").child(DBDate.Day()).child(num.toString())
    }

    fun getBestRefToday(type: String, genre : String) : DatabaseReference {
        return setBestRef(type, genre).child("today").child(DBDate.Day())
    }

    fun setBestRefWeek(type: String, num : Int, genre : String) : DatabaseReference {
        return setBestRef(type, genre).child("week").child(num.toString()).child(DBDate.DayString())
    }

    fun setBestRefMonthWeek(type: String, genre : String) : DatabaseReference {
        return setBestRef(type, genre).child("month").child(DBDate.Month()).child(DBDate.Week()).child(DBDate.DayString())
    }

    fun setBestRefMonthDay(type: String, num : Int, genre : String) : DatabaseReference {
        return setBestRef(type, genre).child("month").child(DBDate.Month()).child(DBDate.Week()).child(DBDate.DayString()).child("day").child(num.toString())
    }

    fun setBestRefMonth(type: String, genre : String) : DatabaseReference {
        return setBestRef(type, genre).child("month").child(DBDate.Month()).child(DBDate.Week()).child(DBDate.DayString())
    }

    fun setBookListDataBestToday(ref: MutableMap<String?, Any>, num : Int) : BookListDataBestToday {
        return BookListDataBestToday(
            ref["writerName"] as String?,
            ref["subject"] as String?,
            ref["bookImg"] as String?,
            ref["bookCode"] as String?,
            ref["info1"] as String?,
            ref["info2"] as String?,
            ref["info3"] as String?,
            ref["info4"] as String?,
            ref["info5"] as String?,
            num + 1,
            DBDate.Date(),
            ""
        )
    }
}