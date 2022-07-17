package com.example.moavara.Firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.moavara.Util.Mining
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FirebaseWorkManager(private var context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    val miningRef = FirebaseDatabase.getInstance().reference.child("Mining")

    override fun doWork(): Result {

        miningRef.get().addOnSuccessListener {
            if(it.value != null && it.value!! != "NULL"){

                Thread{
                    if(it.value == "ALL"){
                        Mining.runMining(applicationContext, "ALL")
                        miningRef.setValue("BL")
                    } else if (it.value == "BL") {
                        Mining.runMining(applicationContext, "BL")
                        miningRef.setValue("FANTASY")
                    } else if (it.value == "FANTASY") {
                        Mining.runMining(applicationContext, "FANTASY")
                        miningRef.setValue("ROMANCE")
                    } else if (it.value == "ROMANCE") {
                        Mining.runMining(applicationContext, "ROMANCE")
                        miningRef.setValue("ALL")
                    }
                }.start()

            } else {
                miningRef.setValue("ALL")
                Toast.makeText(context, "장르 : 전체", Toast.LENGTH_SHORT).show()
            }
            postFCM(it.value as String)
        }.addOnFailureListener{}

        return Result.success()
    }

    private fun postFCM(value: String) {

        val fcmBody = DataFCMBody(
            "/topics/all",
            "high",
            DataFCMBodyData("data", "body"),
            DataFCMBodyNotification("모아바라", "베스트 리스트가 갱신되었습니다-0710 $value", "default", "ic_stat_ic_notification"),
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