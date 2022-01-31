package com.example.moavara.DataBase

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DBDate {

    fun Day() : String {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK).toString()
    }

    fun DayString() : String {

        val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK).toString()

        if (day == "1") {
            return "sun"
        } else if (day == "2") {
            return "mon"
        } else if (day == "3") {
            return "tue"
        } else if (day == "4") {
            return "wed"
        } else if (day == "5") {
            return "thur"
        } else if (day == "6") {
            return "fri"
        } else if (day == "7") {
            return "sat"
        } else {
            return ""
        }
    }

    fun Week() : String {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH).toString()
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