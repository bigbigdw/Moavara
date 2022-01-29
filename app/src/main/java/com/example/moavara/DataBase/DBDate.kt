package com.example.moavara.DataBase

import java.util.*

object DBDate {

    fun Day() : Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }

    fun Week() : Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH)
    }

    fun Date() : Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH)
    }

    fun Month() : Int {
        return Calendar.getInstance().get(Calendar.MONTH)
    }
}