package com.example.moavara.Util

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import kotlin.concurrent.thread

class MyJobService : JobService() {
    private val TAG = "Job"

    @SuppressLint("SpecifyJobSchedulerIdRange")
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Create")
    }

    @SuppressLint("SpecifyJobSchedulerIdRange")
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Destroy")
    }

    @SuppressLint("SpecifyJobSchedulerIdRange")
    override
    fun onStartJob(params: JobParameters): Boolean {
        Log.d(TAG, "onStartJob: ${params.jobId}")

        thread(start = true) {
            Thread.sleep(1000)
            Log.d(TAG, "doing Job in other thread")
            Mining.runMining(applicationContext, "ALL")
            Mining.runMining(applicationContext, "ROMANCE")
            Mining.runMining(applicationContext, "BL")
            Mining.runMining(applicationContext, "FANTASY")
            jobFinished(params, true)
        }

        return true
    }

    @SuppressLint("SpecifyJobSchedulerIdRange")
    override
    fun onStopJob(params: JobParameters): Boolean {
        Log.d(TAG, "onStopJob: ${params.jobId}")
        return false
    }
}