package com.example.moavara.Util

import android.content.res.Resources
import android.view.View

fun View.setViewHeight(h: Int) {
    val newParams = this.layoutParams
    newParams.height = h
    this.layoutParams = newParams
}


fun Float.dpToPx(): Float = this * Resources.getSystem().displayMetrics.density
