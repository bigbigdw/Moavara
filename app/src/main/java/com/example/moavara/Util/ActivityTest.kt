package com.example.moavara.Util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.R

class ActivityTest : AppCompatActivity() {

    var cate = "ALL"

    companion object {
        private val TAG = "Job"
        val JOB_ID_A = 100
        val JOB_ID_B = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

    }
}