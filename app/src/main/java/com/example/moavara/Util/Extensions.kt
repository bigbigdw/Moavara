package com.example.moavara.Util

import android.content.res.Resources
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View

fun View.setViewHeight(h: Int) {
    val newParams = this.layoutParams
    newParams.height = h
    this.layoutParams = newParams
}

/**
 * 텍스트 문자중 일부를 컬러 텍스트로 변경
 * @param str 변경 문자
 * @param color 변경할 색
 * */
fun SpannableStringBuilder.applyingTextColor(str: String, color: String) {
    try{
        this.setSpan(
            ForegroundColorSpan(Color.parseColor(color)),
            indexOf(str),
            indexOf(str) + str.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    } catch (e: IndexOutOfBoundsException){}
}


fun Float.dpToPx(): Float = this * Resources.getSystem().displayMetrics.density
