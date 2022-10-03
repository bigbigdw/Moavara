package com.example.moavara.Main

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.room.Room
import com.example.moavara.DataBase.DBBest
import com.example.moavara.Firebase.FCM
import com.example.moavara.R
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.google.firebase.database.FirebaseDatabase


val mRootRef = FirebaseDatabase.getInstance().reference

class ActivitySplash : Activity() {
    var cate = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val fcm = Intent(applicationContext, FCM::class.java)


        startService(fcm)

        cate = Genre.getGenre(this).toString()


        for(platform in BestRef.typeList()){
            val bestDaoToday = Room.databaseBuilder(
                this@ActivitySplash,
                DBBest::class.java,
                "Today_${platform}_${cate}"
            ).allowMainThreadQueries().build()

            val bestDaoWeek = Room.databaseBuilder(
                this@ActivitySplash,
                DBBest::class.java,
                "Week_${platform}_${cate}"
            ).allowMainThreadQueries().build()

            val bestDaoMonth = Room.databaseBuilder(
                this@ActivitySplash,
                DBBest::class.java,
                "Month_${platform}_${cate}"
            ).allowMainThreadQueries().build()

            bestDaoToday.bestDao().initAll()
            bestDaoWeek.bestDao().initAll()
            bestDaoMonth.bestDao().initAll()
        }

        Looper.myLooper()?.let {
            Handler(it).postDelayed(
                {
                    val intent = Intent(this, ActivityLogin::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    startActivityIfNeeded(intent, 0)
                    finish()
                },
                2000
            )
        }
    }
}
