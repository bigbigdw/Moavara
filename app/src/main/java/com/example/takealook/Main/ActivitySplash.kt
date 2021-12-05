package com.example.takealook.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.takealook.Joara.BookListData
import com.example.takealook.R
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException


class ActivitySplash : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        startLoading()

        Thread {
            test()
        }.start()
    }

    @Throws(IOException::class)
    private fun test() {
//        Log.d("@@@@","@@@@")
//        val doc: Document = Jsoup.connect("https://en.wikipedia.org/").get()
//        log(doc.title())
//        val newsHeadlines: Elements = doc.select("#mp-itn b a")
//        for (headline in newsHeadlines) {
//            log("%s\n\t%s", headline.attr("title"), headline.absUrl("href"))
//        }

        Log.d("@@@@","!!!!")
        val doc2: Document = Jsoup.connect("https://ridibooks.com/romance-serial/").get()
        log(doc2.title())
        val keywordRidi: Elements = doc2.select("div ul li a")
        for (i in keywordRidi) {
            if(i.absUrl("href").contains("keyword")){
                log("%s\n\t%s", i.text(), i.absUrl("href"))
            }
        }
    }

    private fun log(msg: String, vararg vals: String) {
        println(String.format(msg, *vals))
    }

    private fun startLoading() {
        Handler(Looper.myLooper()!!).postDelayed(
            {
                val novelIntent = Intent(this, ActivityMain::class.java)
                novelIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivityIfNeeded(novelIntent, 0)
                finish()
            },
            1000
        )
    }
}
