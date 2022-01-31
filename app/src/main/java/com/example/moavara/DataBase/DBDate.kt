package com.example.moavara.DataBase

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DBDate {

    fun Day() : String {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK).toString()
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