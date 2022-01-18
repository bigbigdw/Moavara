package com.example.takealook.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.room.Room
import com.example.takealook.DataBase.DataBaseBest
import com.example.takealook.DataBase.DataBest
import com.example.takealook.R
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*


private lateinit var db: DataBaseBest
var dayOfMonth = 0

class ActivitySplash : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        db = Room.databaseBuilder(
            this,
            DataBaseBest::class.java,
            "user-database"
        ).allowMainThreadQueries()
            .build()

        if (db.bestDao().selectWeekTotal(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) != null) {
            db.bestDao().deleteWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
        }



        Thread {
            test()
        }.start()

        startLoading()

    }

    private fun test() {

        getRidiBest()

    }

    private fun getRidiBest() {

        val doc: Document =
            Jsoup.connect("https://ridibooks.com/bestsellers/romance_serial?order=daily").post()
        val ridiKeyword: Elements = doc.select(".book_thumbnail_wrapper")

        for (i in ridiKeyword.indices) {
            if (i < 20) {

                val writerName = doc.select("div .author_detail_link")[i].text()
                val subject = doc.select("div .title_link")[i].text()
                val bookImg =
                    ridiKeyword.select(".thumbnail_image .thumbnail")[i].absUrl("data-src")
                val intro = ""
                val bookCode = ridiKeyword.select("a")[i].absUrl("href")
                val cntChapter = doc.select(".count_num")[i].text()
                val cntPageRead = doc.select("span .StarRate_ParticipantCount")[i].text()
                val cntFavorite = ""
                val cntRecom = doc.select("span .StarRate_Score")[i].text()

                db.bestDao().insert(
                    DataBest(
                        writerName,
                        subject,
                        bookImg,
                        intro,
                        bookCode,
                        cntChapter,
                        cntPageRead,
                        cntFavorite,
                        cntRecom,
                        i + 1,
                        Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
                        Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH),
                        Calendar.getInstance().get(Calendar.DATE),
                        Calendar.getInstance().get(Calendar.MONTH),
                        10 - i,
                        "Ridi"
                    )
                )
            }
        }

    }


    private fun startLoading() {
        Handler(Looper.myLooper()!!).postDelayed(
            {
                val novelIntent = Intent(this, ActivityLogin::class.java)
                novelIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivityIfNeeded(novelIntent, 0)
                finish()
            },
            1000
        )
    }

    private fun addJoaraBestDB() {
        var writer = "NAME"
        var title = "AGE"
        var bookImg = "PHONE"
        var intro = "NAME"
        var bookCode = "AGE"
        var cntChapter = "PHONE"
        var cntPageRead = "NAME"
        var cntFavorite = "AGE"
        var cntRecom = "PHONE"
        var number = 1


        db.bestDao().insert(
            DataBest(
                writer,
                title,
                bookImg,
                intro,
                bookCode,
                cntChapter,
                cntPageRead,
                cntFavorite,
                cntRecom,
                number
            )
        )
    }


}
