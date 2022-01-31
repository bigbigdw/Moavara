package com.example.moavara.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.room.Room
import com.example.moavara.DataBase.DBDate
import com.example.moavara.DataBase.DataBaseBest
import com.example.moavara.DataBase.DataBest
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.google.firebase.database.FirebaseDatabase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*


val mRootRef = FirebaseDatabase.getInstance().reference


class ActivitySplash : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Thread {
            test()
        }.start()

        startLoading()

    }

    private fun test() {

        getRidiBest()
        getOneStoreBest()

    }

    private fun getRidiBest() {

        val doc: Document =
            Jsoup.connect("https://ridibooks.com/bestsellers/romance_serial?order=daily").post()
        val ridiKeyword: Elements = doc.select(".book_thumbnail_wrapper")

        var bestRef = mRootRef.child("best")
        bestRef = bestRef.child("Ridi")

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

                bestRef.child("today").child(DBDate.Day()).child(i.toString()).setValue(
                    BookListDataBestToday(
                        writerName,
                        subject,
                        bookImg,
                        intro,
                        bookCode,
                        cntChapter,
                        cntPageRead,
                        cntFavorite,
                        cntRecom,
                        i + 1
                    )
                )

                if(i < 10){
                    bestRef.child("week").child(i.toString()).child(DBDate.Day()).setValue(
                        BookListDataBestToday(
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
                            DBDate.Date()
                        )
                    )
                }

                if(i == 0){
                    bestRef.child("month").child(DBDate.Week().toString()).child(DBDate.Day()).setValue(
                        BookListDataBestToday(
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
                            DBDate.Date()
                        )
                    )
                }
            }
        }

    }

    private fun getOneStoreBest() {

        val doc: Document =
            Jsoup.connect("https://onestory.co.kr/display/card/CRD0059596?title=%EC%9D%B4%EA%B1%B4%20%EC%82%AC%EC%95%BC%ED%95%B4!%20%EC%9C%A0%EB%A3%8C%20%EA%B5%AC%EB%A7%A4%EC%9C%A8%20BEST%20%EB%A1%9C%EB%A7%A8%EC%8A%A4").get()
        val ridiKeyword: Elements = doc.select(".ItemRendererInner")

        var bestRef = mRootRef.child("best")
        bestRef = bestRef.child("OneStore")

        for (i in ridiKeyword.indices) {

                val writerName = doc.select("div .ItemRendererTextArtist")[i].text()
                val subject = doc.select("div .ItemRendererTextTitle")[i].text()
                val bookImg = doc.select(".ThumbnailInner img")[i].absUrl("src")
                val intro = doc.select(".tItemRendererTextPublisher")[i].text()
                val bookCode = ""
                val cntChapter = doc.select(".tItemRendererTextSeries")[i].text()
                val cntPageRead = doc.select(".ItemRendererTextComment span span")[i].text()
                val cntFavorite = doc.select(".ItemRendererTextAvgScore")[i].text()
                val cntRecom = doc.select(".ItemRendererTextAvgScore")[i].text()

                bestRef.child("today").child(DBDate.Day()).child(i.toString()).setValue(
                    BookListDataBestToday(
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
                        DBDate.Date()
                    )
                )

            if(i < 10){
                bestRef.child("week").child(i.toString()).child(DBDate.Day()).setValue(
                    BookListDataBestToday(
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
                        DBDate.Date()
                    )
                )
            }

            if(i == 0){
                bestRef.child("month").child(DBDate.Week().toString()).child("0").setValue(
                    BookListDataBestToday(
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
                        DBDate.Date()
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
}
