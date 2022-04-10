package com.example.moavara.Util

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class SomeWorker(ctx: Context, params: WorkerParameters) :
    Worker(ctx, params) { // If you want coroutines, CoroutineWorker()

    override fun doWork(): Result {
        return try {
            Log.d("@@@","I'm hard worker, but i will sleep")
            Thread.sleep(3_000)
            Log.d("@@@","Who woke up. I'm tired.")

            Result.success() // return statement
        } catch (e: Exception) {
            Log.d("@@@","Worker Exception $e")
            Result.failure() // return statement
        }

    }
}