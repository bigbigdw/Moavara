package com.example.moavara.DataBase

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DBDate {

    fun DayInt() : Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }

    fun Day() : String {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK).toString()
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

    fun Month() : Int {
        return Calendar.getInstance().get(Calendar.MONTH)
    }
}