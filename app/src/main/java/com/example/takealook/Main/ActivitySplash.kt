package com.example.takealook.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.takealook.DataBase.DataBaseJoara
import com.example.takealook.DataBase.JoaraBest
import com.example.takealook.R
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


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

        Log.d("@@@@","!!!!")
        val doc2: Document = Jsoup.connect("https://ridibooks.com/bestsellers/romance_serial?order=daily").post()
        log(doc2.title())
        val ridiKeyword: Elements = doc2.select(".book_thumbnail_wrapper")

        for (i in ridiKeyword.indices) {
            Log.d("!!!!", "Link = " + ridiKeyword.select("a")[i].absUrl("href"))
            Log.d("!!!!", "Img = " + ridiKeyword.select(".thumbnail_image .thumbnail")[i].absUrl("data-src"))
            Log.d("!!!!", "Title = " + doc2.select("div .title_link")[i].text())
            Log.d("!!!!", "Writer = " + doc2.select("div .author_detail_link")[i].text())
            Log.d("!!!!", "Score = " + doc2.select("span .StarRate_Score")[i].text())
            Log.d("!!!!", "Read = " + doc2.select("span .StarRate_ParticipantCount")[i].text())
            Log.d("!!!!", "Count = " + doc2.select(".count_num")[i].text())
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
