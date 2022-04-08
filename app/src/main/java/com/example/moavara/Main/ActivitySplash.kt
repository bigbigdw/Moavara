package com.example.moavara.Main

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
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.R
import com.example.moavara.Util.*
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

val mRootRef = FirebaseDatabase.getInstance().reference

class ActivitySplash : Activity() {

    private lateinit var dbWeek: DataBaseBestDay
    private lateinit var dbWeekList: DataBaseBestDay
    private lateinit var dbYesterday: DataBaseBestDay
    var cate = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        dbYesterday = Room.databaseBuilder(
            this.applicationContext,
            DataBaseBestDay::class.java,
            "best-yesterday"
        ).allowMainThreadQueries().build()

        dbWeek =
            Room.databaseBuilder(this.applicationContext, DataBaseBestDay::class.java, "best-week")
                .allowMainThreadQueries().build()

        dbWeekList = Room.databaseBuilder(this.applicationContext, DataBaseBestDay::class.java, "week-list")
            .allowMainThreadQueries().build()

        dbWeek.bestDao().initAll()
        dbWeekList.bestDao().initAll()
        dbYesterday.bestDao().initAll()

        cate = Genre.getGenre(this).toString()

        val js = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val serviceComponent = ComponentName(this, MyJobService::class.java)
        val jobInfo = JobInfo.Builder(ActivityTest.JOB_ID_B, serviceComponent)
            .setPeriodic(TimeUnit.HOURS.toMillis(12))
            .build()
        js.schedule(jobInfo)
        Toast.makeText(this, "12시간 뒤 업데이트", Toast.LENGTH_SHORT).show()

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
