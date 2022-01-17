package com.example.takealook.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.room.Room
import com.example.takealook.DataBase.DataBaseJoara
import com.example.takealook.DataBase.JoaraBest
import com.example.takealook.R
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.time.DayOfWeek
import java.util.*


private lateinit var db: DataBaseJoara
var dayOfMonth = 0

class ActivitySplash : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        startLoading()

        Thread {
            test()
        }.start()
    }

    private fun test() {
//        Log.d("@@@@","@@@@")
//        val doc: Document = Jsoup.connect("https://en.wikipedia.org/").get()
//        log(doc.title())
//        val newsHeadlines: Elements = doc.select("#mp-itn b a")
//        for (headline in newsHeadlines) {
//            log("%s\n\t%s", headline.attr("title"), headline.absUrl("href"))
//        }

        Log.d("@@@@","!!!!")
        val doc2: Document = Jsoup.connect("https://ridibooks.com/bestsellers/romance_serial?order=daily").post()
        log(doc2.title())
        val keywordRidi: Elements = doc2.select(".book_thumbnail a")
        for (i in keywordRidi) {
            log("%s\n\t%s", i.text(), i.absUrl("href"))
        }

        val keywordRidi2: Elements = doc2.select(".book_thumbnail .thumbnail_image .thumbnail")
        val keywordRidi3: Elements = doc2.select("div .title_link")
        for (i in keywordRidi2) {
            log("%s\n\t%s", i.text(), i.absUrl("data-src"))
        }

        for (i in keywordRidi3) {
            log("%s\n\t%s", i.text(), i.absUrl("href"))
        }

    }

    private fun log(msg: String, vararg vals: String) {
        println(String.format(msg, *vals))
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


        db.bestDao().insert(JoaraBest(writer, title, bookImg, intro, bookCode, cntChapter, cntPageRead, cntFavorite, cntRecom, number))
    }


}
