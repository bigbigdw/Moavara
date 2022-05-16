package com.example.moavara.Main

import com.example.moavara.Firebase.FCM
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.moavara.R
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

        Handler(Looper.myLooper()!!).postDelayed(
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
