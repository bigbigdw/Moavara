package com.example.moavara.Main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.work.*
import com.example.moavara.Firebase.FirebaseWorkManager
import com.example.moavara.R
import com.example.moavara.Util.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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

        /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(12, TimeUnit.HOURS)
            .build()

        val workManager = WorkManager.getInstance()

        workManager.enqueue(workRequest)

        Handler(Looper.myLooper()!!).postDelayed(
            {
                val intent = Intent(this, ActivityMain::class.java)
                startActivity(intent)
            },
            2000
        )

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("!!!!", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("!!!!", token)
        })

        FirebaseMessaging.getInstance().subscribeToTopic("all");

    }

}