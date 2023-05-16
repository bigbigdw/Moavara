package com.bigbigdw.moavara.Firebase

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bigbigdw.moavara.DataBase.DBUser
import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Main.mRootRef
import com.bigbigdw.moavara.Search.FCMAlert
import com.bigbigdw.moavara.Util.DBDate
import com.bigbigdw.moavara.Util.Mining
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FirebaseWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    var userDao: DBUser? = null
    var UserInfo : DataBaseUser? = null

    companion object {
        const val TYPE = "type"
        const val UID = "uid"
        const val USER = "user"
    }

    override fun doWork(): Result {

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

        val year = DBDate.DateMMDDHHMM().substring(0,4)
        val month = DBDate.DateMMDDHHMM().substring(4,6)
        val day = DBDate.DateMMDDHHMM().substring(6,8)
        val hour = DBDate.DateMMDDHHMM().substring(8,10)
        val min = DBDate.DateMMDDHHMM().substring(10,12)

        val fcmBody = DataFCMBody(
            "/topics/all",
            "high",
            DataFCMBodyData("모아바라", "베스트 리스트가 갱신되었습니다"),
            DataFCMBodyNotification("모아바라", "${year}.${month}.${day} ${hour}:${min} 베스트 리스트가 갱신되었습니다", "default", "ic_stat_ic_notification", "best"),
        )

        miningAlert("모아바라", "${year}.${month}.${day} ${hour}:${min} 베스트 리스트가 갱신되었습니다", "Alert")


        userDao = Room.databaseBuilder(
            applicationContext,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        if(userDao?.daoUser() != null){
            UserInfo = userDao?.daoUser()?.get()
        }

        mRootRef.child("User").child(UserInfo?.UID ?: "").child("isInit").setValue(true)

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

        userDao = Room.databaseBuilder(
            applicationContext,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        if(userDao?.daoUser() != null){
            UserInfo = userDao?.daoUser()?.get()
        }

        mRootRef.child("User").child(UID).child("isInit").setValue(true)

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

    private fun miningAlert(title: String, message: String, child: String) {
        FirebaseDatabase.getInstance().reference.child("Message").child(child)
            .child(DBDate.DateMMDDHHMM()).setValue(
                FCMAlert(
                    DBDate.DateMMDDHHMM(),
                    title, message
                )
            )
    }
}