package com.example.moavara.Firebase

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.moavara.Util.Mining
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FirebaseWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        /* 처리해야할 작업에 관한 코드들 */
        Handler(Looper.getMainLooper()).postDelayed({
            Mining.runMining(applicationContext, "ALL")
            Log.d("MINING", "ALL")
        }, 1000) //1초 후 실행
        Handler(Looper.getMainLooper()).postDelayed({
            Mining.runMining(applicationContext, "ROMANCE")
            Log.d("MINING", "ROMANCE")
        }, 1000) //1초 후 실행
        Handler(Looper.getMainLooper()).postDelayed({
            Mining.runMining(applicationContext, "BL")
            Log.d("MINING", "BL")
        }, 1000) //1초 후 실행
        Handler(Looper.getMainLooper()).postDelayed({
            Mining.runMining(applicationContext, "FANTASY")
            Log.d("MINING", "FANTASY")
        }, 1000) //1초 후 실행


        postFCM()

        return Result.success()
    }

    private fun postFCM() {

        val fcmBody = DataFCMBody(
            "/topics/all",
            "high",
            DataFCMBodyData("data", "body"),
            DataFCMBodyNotification("모아바라", "베스트 리스트가 갱신되었습니다-0707", "default", "ic_stat_ic_notification"),
        )

        val call = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(FirebaseService::class.java)
            .postRetrofit(
                fcmBody
            )!!

        call.enqueue(object : Callback<FWorkManagerResult?> {
            override fun onResponse(
                call: Call<FWorkManagerResult?>,
                response: retrofit2.Response<FWorkManagerResult?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        Log.d("FCM", "성공");
                    }
                } else {
                    Log.d("FCM", "실패2");
                }
            }

            override fun onFailure(call: Call<FWorkManagerResult?>, t: Throwable) {
                Log.d("FCM", "실패");
            }
        })
    }

}