package com.example.moavara.Util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class TabViewModel : ViewModel() {
    private val mIndex = MutableLiveData<Int>()
    val text = Transformations.map(mIndex) { input: Int -> "TAB$input" }
    fun setIndex(index: Int) {
        mIndex.value = index
    }
}

