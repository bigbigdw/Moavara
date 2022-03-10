package com.example.moavara.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.room.Room
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.Joara.JoaraBestListResult
import com.example.moavara.Joara.RetrofitJoara
import com.example.moavara.KaKao.BestResultKakao
import com.example.moavara.KaKao.BestResultKakaoStageNovel
import com.example.moavara.KaKao.RetrofitKaKao
import com.example.moavara.OneStore.OneStoreBookResult
import com.example.moavara.OneStore.RetrofitOnestore
import com.example.moavara.R
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.google.firebase.database.FirebaseDatabase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

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
