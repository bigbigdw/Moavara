package com.example.moavara.Firebase

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Mining
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FirebaseWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        const val TYPE = "type"
        const val UID = "uid"
        const val USER = "user"
    }

    override fun doWork(): Result {

        Log.d("####", inputData.getString(TYPE).toString())

        if(inputData.getString(TYPE).equals("BEST")){
            Mining.runMining(applicationContext, "FANTASY")
            Mining.runMining(applicationContext, "ALL")
            Mining.runMining(applicationContext, "ROMANCE")
            Mining.runMining(applicationContext, "BL")
            postFCM()
        } else if(inputData.getString(TYPE).equals("PICK")) {
            Mining.getMyPickMining(applicationContext)
            postFCMPick(inputData.getString(UID).toString(), inputData.getString(USER).toString())
        }

        return Result.success()
    }

    private fun postFCM() {

        val fcmBody = DataFCMBody(
            "/topics/all",
            "high",
            DataFCMBodyData("data", "body"),
            DataFCMBodyNotification("모아바라", "베스트 리스트가 갱신되었습니다-${DBDate.DateMMDDHHMM()}", "default", "ic_stat_ic_notification", "best"),
        )

        val call = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(FirebaseService::class.java)
            .postRetrofit(
                fcmBody
            )

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

    private fun postFCMPick(UID : String, User : String) {

        val fcmBody = DataFCMBody(
            "/topics/${UID}",
            "high",
            DataFCMBodyData("모아바라 PICK 최신화", "${User}님의 마이픽 리스트가 최신화 되었습니다."),
            DataFCMBodyNotification("모아바라 PICK 최신화", "${User}님의 마이픽 리스트가 최신화 되었습니다.", "default", "ic_stat_ic_notification"),
        )

        val call = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(FirebaseService::class.java)
            .postRetrofit(
                fcmBody
            )

        call.enqueue(object : Callback<FWorkManagerResult?> {
            override fun onResponse(
                call: Call<FWorkManagerResult?>,
                response: retrofit2.Response<FWorkManagerResult?>
            ) {
                if (response.isSuccessful) {
                    Mining.getMyPickMining(applicationContext)
                }
            }

            override fun onFailure(call: Call<FWorkManagerResult?>, t: Throwable) {}
        })
    }

}