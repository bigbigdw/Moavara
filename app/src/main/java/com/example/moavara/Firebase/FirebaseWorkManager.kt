package com.example.moavara.Firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.moavara.Search.BestType
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Mining
import com.example.moavara.Util.Mining.getWeekCompared
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.thread

class FirebaseWorkManager(context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {
    override fun doWork(): Result {
        /* 처리해야할 작업에 관한 코드들 */
        postFCM()
        return Result.success()
    }

    private fun postFCM() {

        Log.d("FCM", "대기");

        Thread {
            Mining.runMining(applicationContext, "ALL")
            Mining.runMining(applicationContext, "ROMANCE")
            Mining.runMining(applicationContext, "BL")
            Mining.runMining(applicationContext, "FANTASY")

//            for(i in BestRef.typeList().indices){
//                getWeekCompared(BestRef.typeList()[i], "ALL")
//                getWeekCompared(BestRef.typeList()[i], "ROMANCE")
//                getWeekCompared(BestRef.typeList()[i], "BL")
//                getWeekCompared(BestRef.typeList()[i], "FANTASY")
//            }
        }.start()

        val fcmBody = DataFCMBody(
            "/topics/all",
            "high",
            DataFCMBodyData("data", "body"),
            DataFCMBodyNotification("모아바라", "베스트 리스트가 갱신되었습니다", "default", "ic_stat_ic_notification"),
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