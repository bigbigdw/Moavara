package com.example.moavara.Main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.work.*
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.DataBase.DataBestDay
import com.example.moavara.Firebase.FirebaseWorkManager
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import java.util.concurrent.TimeUnit

class ActivitySync : Activity() {

    var cate = "ALL"
    var status = ""
    var tview1: TextView? = null
    var tview2: TextView? = null
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        cate = Genre.getGenre(this).toString()
        tview1 = findViewById(R.id.tview1)
        tview2 = findViewById(R.id.tview2)

        context = this

        tview1!!.text = "선택하신 장르 [$cate] 를 불러오고 있습니다"
        tview2!!.text = "동기화 중..."

        Handler(Looper.myLooper()!!).postDelayed(
            {
                val intent = Intent(this, ActivityMain::class.java)
                startActivity(intent)
            },
            1000
        )

        Thread {
            Mining.runMining(applicationContext, "ALL")
            Mining.runMining(applicationContext, "ROMANCE")
            Mining.runMining(applicationContext, "BL")
            Mining.runMining(applicationContext, "FANTASY")
        }.start()

        mRootRef.child("Week").child(DBDate.DayString()).setValue(DBDate.DateMMDD())

        /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(4, TimeUnit.HOURS)
            .build()

        val miningRef = mRootRef.child("Mining")
        val workManager = WorkManager.getInstance()

        miningRef.get().addOnSuccessListener {
            if(it.value != null && it.value!! == "MINING"){

                Log.d("####", "NOT NULL " + it.value)
                Toast.makeText(this, "WorkManager 이미 존재함", Toast.LENGTH_SHORT).show()

            } else {
                Log.d("####", "NULL")
                miningRef.setValue("MINING")

                workManager.enqueue(workRequest)
                FirebaseMessaging.getInstance().subscribeToTopic("all")
                Toast.makeText(this, "WorkManager 추가됨", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{}
    }
}