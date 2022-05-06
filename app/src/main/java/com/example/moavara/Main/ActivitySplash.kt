package com.example.moavara.Main

import com.example.moavara.Firebase.FCM
import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.room.Room
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.Firebase.FirebaseWorkManager
import com.example.moavara.R
import com.example.moavara.Search.WeekendDate
import com.example.moavara.Util.ActivityTest
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.TimeUnit


val mRootRef = FirebaseDatabase.getInstance().reference

class ActivitySplash : Activity() {
    var cate = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val fcm = Intent(applicationContext, FCM::class.java)
        startService(fcm)

        cate = Genre.getGenre(this).toString()

        Handler(Looper.myLooper()!!).postDelayed(
            {
                val intent = Intent(this, ActivityGenre::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivityIfNeeded(intent, 0)
                finish()
            },
            2000
        )

    }

}
