package com.example.moavara.Util

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.Main.ActivitySplash
import com.example.moavara.R
import kotlinx.android.synthetic.main.activity_test.*
import java.util.concurrent.TimeUnit

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