package com.example.moavara.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.room.Room
import com.example.moavara.DataBase.DBBest
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Firebase.FCM
import com.example.moavara.R
import com.example.moavara.Util.BestRef
import com.google.firebase.database.FirebaseDatabase


val mRootRef = FirebaseDatabase.getInstance().reference

class ActivitySplash : Activity() {
    var userDao: DBUser? = null
    var UserInfo : DataBaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val fcm = Intent(applicationContext, FCM::class.java)
        startService(fcm)

        userDao = Room.databaseBuilder(
            this,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        if(userDao?.daoUser() != null){
            UserInfo = userDao?.daoUser()?.get()
        }

        mRootRef.child("User").child(UserInfo?.UID ?: "").child("isInit").get().addOnSuccessListener {
            if (it.value == true) {
                Toast.makeText(this@ActivitySplash, "데이터를 새로 받아오고 있습니다.", Toast.LENGTH_SHORT)
                    .show()

                mRootRef.child("User").child(UserInfo?.UID ?: "").child("isInit").setValue(false)

                for(platform in BestRef.typeList()){
                    val bestDaoToday = Room.databaseBuilder(
                        this@ActivitySplash,
                        DBBest::class.java,
                        "Today_${platform}_${UserInfo?.Genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoWeek = Room.databaseBuilder(
                        this@ActivitySplash,
                        DBBest::class.java,
                        "Week_${platform}_${UserInfo?.Genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoMonth = Room.databaseBuilder(
                        this@ActivitySplash,
                        DBBest::class.java,
                        "Month_${platform}_${UserInfo?.Genre}"
                    ).allowMainThreadQueries().build()

                    bestDaoToday.bestDao().initAll()
                    bestDaoWeek.bestDao().initAll()
                    bestDaoMonth.bestDao().initAll()
                }
            }
        }.addOnFailureListener {}

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
