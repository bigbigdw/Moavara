package com.example.moavara.Main

import android.app.Notification
import android.app.job.JobInfo
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.moavara.Firebase.FirebaseWorkManager
import com.example.moavara.Search.WeekendDate
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.ActivityGenreBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.TimeUnit

class ActivityGenre : AppCompatActivity() {

    private lateinit var binding: ActivityGenreBinding
    var context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            tviewMoavara.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tviewGenre.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            llayoutBtn1.setOnClickListener {

                savePreferences("FANTASY")

                val novelIntent = Intent(context, ActivityMain::class.java)
                startActivity(novelIntent)
            }

            llayoutBtn2.setOnClickListener {

                savePreferences("ROMANCE")

                val novelIntent = Intent(context, ActivityMain::class.java)
                startActivity(novelIntent)
            }

            llayoutBtn3.setOnClickListener {

                savePreferences("ALL")

                val novelIntent = Intent(context, ActivityMain::class.java)
                startActivity(novelIntent)
            }

            llayoutBtn4.setOnClickListener {

                savePreferences("BL")

                val novelIntent = Intent(context, ActivityMain::class.java)
                startActivity(novelIntent)
            }
        }

        FirebaseDatabase.getInstance().reference.child("Week").child(DBDate.DayString()).setValue(
            DBDate.DateMMDD())

        Handler(Looper.getMainLooper()).postDelayed({

            val mConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
            val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(4, TimeUnit.HOURS)
                .setConstraints(mConstraints)
                .build()

            val miningRef = FirebaseDatabase.getInstance().reference.child("Mining")
            val workManager = WorkManager.getInstance()

            miningRef.get().addOnSuccessListener {
                if(it.value != null && it.value!! == "MINING"){
                    Toast.makeText(this, "WorkManager 이미 존재함", Toast.LENGTH_SHORT).show()

                } else {
                    miningRef.setValue("MINING")

                    workManager.enqueue(workRequest)
                    FirebaseMessaging.getInstance().subscribeToTopic("all")
                    Toast.makeText(this, "WorkManager 추가됨", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{}
        }, 1000) //1초 후 실행

        FirebaseDatabase.getInstance().reference.child("Week").get().addOnSuccessListener {

            val week: WeekendDate? = it.getValue(WeekendDate::class.java)

            if(week != null){
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("SUN", week.sun).apply()
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("MON", week.mon).apply()
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("TUE", week.tue).apply()
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("WED", week.wed).apply()
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("THUR", week.thur).apply()
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("FRI", week.fri).apply()
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("SAT", week.sat).apply()
            }

        }.addOnFailureListener{}

    }

    private fun savePreferences(genre: String) {
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("GENRE", genre)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()

        overridePendingTransition(0, 0)
    }
}