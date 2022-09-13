package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import androidx.work.*
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Firebase.*
import com.example.moavara.R
import com.example.moavara.Search.ActivitySearch
import com.example.moavara.User.ActivityUser
import com.example.moavara.Util.ActivityTest
import com.example.moavara.Util.Mining
import com.example.moavara.databinding.ActivityAdminBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ActivityAdmin : AppCompatActivity() {

    var status = ""
    var userDao: DBUser? = null
    var UserInfo : DataBaseUser? = null
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = Room.databaseBuilder(
            this,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        if(userDao?.daoUser() != null){
            UserInfo = userDao?.daoUser()?.get()
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val inputData = Data.Builder()
            .putString(FirebaseWorkManager.TYPE, "BEST")
            .build()

        /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(3, TimeUnit.HOURS)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag("MoavaraBest")
            .setInputData(inputData)
            .build()

        val miningRef = FirebaseDatabase.getInstance().reference.child("Mining")
        val workManager = WorkManager.getInstance(applicationContext)

        with(binding) {
            llayoutBtn1.setOnClickListener {
                workManager.cancelAllWorkByTag("MoavaraBest")
                miningRef.setValue("NULL")
                Toast.makeText(applicationContext, "WorkManager 해제됨", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn2.setOnClickListener {

                miningRef.get().addOnSuccessListener {
                    if (it.value != null && it.value!! != "NULL") {
                        Toast.makeText(applicationContext, "WorkManager 이미 존재함", Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        miningRef.setValue("ALL")

                        workManager.enqueueUniquePeriodicWork(
                            "MoavaraBest",
                            ExistingPeriodicWorkPolicy.REPLACE,
                            workRequest
                        )
                        FirebaseMessaging.getInstance().subscribeToTopic("all")
                        Toast.makeText(applicationContext, "WorkManager 추가됨", Toast.LENGTH_SHORT)
                            .show()
                    }
                }.addOnFailureListener {}
            }

            llayoutBtn3.setOnClickListener {
//                Mining.runMining(applicationContext, "FANTASY")
//                Mining.runMining(applicationContext, "ALL")
//                Mining.runMining(applicationContext, "ROMANCE")
//                Mining.runMining(applicationContext, "BL")
                Mining.getJoaraBest(applicationContext, "ALL")
                Toast.makeText(applicationContext, "데이터 갱신 완료", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn4.setOnClickListener {
                workManager.enqueueUniquePeriodicWork(
                    "MoavaraBest",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    workRequest
                )
                FirebaseMessaging.getInstance().subscribeToTopic("all")
                Toast.makeText(applicationContext, "WorkManager 추가", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn5.setOnClickListener {
                Toast.makeText(applicationContext, "${workManager.getWorkInfosByTag("MoavaraBest").get()}", Toast.LENGTH_SHORT)
            }

            llayoutBtn6.setOnClickListener {

                Mining.runMining(applicationContext, "FANTASY")
                Mining.runMining(applicationContext, "ALL")
                Mining.runMining(applicationContext, "ROMANCE")
                Mining.runMining(applicationContext, "BL")
                Toast.makeText(applicationContext, "데이터 갱신 완료", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn7.setOnClickListener {
                Toast.makeText(applicationContext, "푸시 푸시 베이베", Toast.LENGTH_SHORT).show()
                postFCM()
            }

            llayoutBtn8.setOnClickListener {
                Mining.runMining(applicationContext, "ALL")
                Toast.makeText(applicationContext, "장르 : 전체", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn9.setOnClickListener {
                Mining.runMining(applicationContext, "FANTASY")
                Toast.makeText(applicationContext, "장르 : 판타지", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn10.setOnClickListener {
                Mining.runMining(applicationContext, "BL")
                Toast.makeText(applicationContext, "장르 : BL", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn11.setOnClickListener {
                Mining.runMining(applicationContext, "ROMANCE")
                Toast.makeText(applicationContext, "장르 : 로맨스", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn12.setOnClickListener {
                Mining.runMiningEvent(applicationContext)
                Toast.makeText(applicationContext, "이벤트 최신화", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn13.setOnClickListener {
                Toast.makeText(applicationContext, "테스트 완료", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn14.setOnClickListener {

                val intent = Intent(this@ActivityAdmin, ActivityTest::class.java)
                startActivity(intent)
            }

            llayoutPickFCM.setOnClickListener {
                val fcm = Intent(applicationContext, FCM::class.java)
                startService(fcm)

                val inputData = Data.Builder()
                    .putString(FirebaseWorkManager.TYPE, "PICK")
                    .putString(FirebaseWorkManager.UID, UserInfo?.UID ?: "")
                    .putString(FirebaseWorkManager.USER, UserInfo?.Nickname ?: "")
                    .build()

                /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
                val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(6, TimeUnit.HOURS)
                    .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.MILLISECONDS
                    )
                    .addTag("MoavaraPick")
                    .setInputData(inputData)
                    .build()

                val workManager = WorkManager.getInstance(applicationContext)

                workManager.enqueueUniquePeriodicWork(
                    "MoavaraPick",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    workRequest
                )

                FirebaseMessaging.getInstance().subscribeToTopic(UserInfo?.UID ?: "")
                mRootRef.child("User").child(UserInfo?.UID ?: "").child("Mining").setValue(true)
            }

            llayoutPickFCMStop.setOnClickListener {
                mRootRef.child("User").child(UserInfo?.UID ?: "").child("Mining").removeValue()
                val workManager = WorkManager.getInstance(applicationContext)
                workManager.cancelAllWorkByTag("MoavaraPick")
                Toast.makeText(applicationContext, "선호작 최신화 해제됨", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun postFCM() {

        val fcm = Intent(applicationContext, FCM::class.java)
        startService(fcm)

        val fcmBody = DataFCMBody(
            "/topics/all",
            "high",
            DataFCMBodyData("data", "body"),
            DataFCMBodyNotification("모아바라", "예아 푸시푸시 베이베", "default", "ic_stat_ic_notification"),
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
                        Log.d("@@@@FCM", "성공");
                    }
                } else {
                    Log.d("@@@@FCM", "실패2");
                }
            }

            override fun onFailure(call: Call<FWorkManagerResult?>, t: Throwable) {
                Log.d("@@@@FCM", "실패");
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when (item.itemId) {
            R.id.ActivitySearch -> {
                val intent = Intent(this, ActivitySearch::class.java)
                startActivity(intent)
            }
            R.id.ActivityUser -> {
                val intent = Intent(this, ActivityUser::class.java)
                startActivity(intent)
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}