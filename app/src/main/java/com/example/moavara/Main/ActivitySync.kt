package com.example.moavara.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.room.Room
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.DataBase.DataBestDay
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import java.util.HashMap

class ActivitySync : Activity() {

    private lateinit var dbWeek: DataBaseBestDay
    private lateinit var dbWeekList: DataBaseBestDay
    private lateinit var dbYesterday: DataBaseBestDay
    var cate = "ALL"
    var status = ""
    var tview1: TextView? = null
    var tview2: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        cate = Genre.getGenre(this).toString()

        dbYesterday = Room.databaseBuilder(
            this.applicationContext,
            DataBaseBestDay::class.java,
            "best-yesterday"
        )
            .allowMainThreadQueries().build()

        dbWeek =
            Room.databaseBuilder(this.applicationContext, DataBaseBestDay::class.java, "best-week")
                .allowMainThreadQueries().build()

        dbWeekList =
            Room.databaseBuilder(this.applicationContext, DataBaseBestDay::class.java, "week-list")
                .allowMainThreadQueries().build()

        setRoomBest("Joara")
        setRoomBest("Joara Nobless")
        setRoomBest("Joara Premium")
        setRoomBest("Naver")
        setRoomBest("Naver Today")
        setRoomBest("Naver Challenge")
        setRoomBest("Kakao Stage")
        setRoomBest("Kakao")
        setRoomBest("Ridi")
        setRoomBest("OneStore")
        setRoomBest("MrBlue")
        tview1 = findViewById(R.id.tview1)
        tview2 = findViewById(R.id.tview2)

        tview1!!.text = "선택하신 장르 [$cate] 를 불러오고 있습니다"
        tview2!!.text = "동기화 중..."


        Handler(Looper.myLooper()!!).postDelayed(
            {
                val novelIntent = Intent(this, ActivityMain::class.java)
                novelIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivityIfNeeded(novelIntent, 0)
                finish()
            },
            5000
        )

    }

    private fun setRoomBest(type: String) {

        val yesterdayRef = mRootRef.child("best").child(type).child(cate).child("today").child(
            DBDate.Yesterday()
        )

        yesterdayRef.get().addOnSuccessListener {

            for (i in it.children) {
                val group: BookListDataBestToday? = i.getValue(BookListDataBestToday::class.java)

                dbYesterday.bestDao().insert(
                    DataBestDay(
                        group!!.writer,
                        group.title,
                        group.bookImg,
                        group.bookCode,
                        group.info1,
                        group.info2,
                        group.info3,
                        group.info4,
                        group.info5,
                        group.number,
                        0,
                        group.date,
                        type
                    )
                )
            }
        }.addOnFailureListener {}

        var num = 1

        BestRef.getBestRefToday(type, cate).get().addOnSuccessListener {

            for (i in it.children) {
                val group: BookListDataBestToday? = i.getValue(BookListDataBestToday::class.java)
                val ref: MutableMap<String?, Any> = HashMap()

                if (calculateNum(group!!.number, group.title, type) != 0) {

                    ref["writerName"] = group.writer!!
                    ref["subject"] = group.title!!
                    ref["bookImg"] = group.bookImg!!
                    ref["bookCode"] = group.bookCode!!
                    ref["info1"] = group.info1!!
                    ref["info2"] = group.info2!!
                    ref["info3"] = group.info3!!
                    ref["info4"] = group.info4!!
                    ref["info5"] = group.info5!!
                    ref["number"] = group.number!!
                    ref["numberDiff"] = calculateNum(group.number, group.title, type)
                    ref["date"] = DBDate.DateMMDD()
                    ref["type"] = type
                    ref["status"] = status

                    dbWeek.bestDao().insert(BestRef.setDataBestDay(ref))
                    BestRef.setBestRefWeekCompared(type, num, cate)
                        .setValue(BestRef.setBookListDataBestToday(ref))
                }
                num += 1
            }
        }.addOnFailureListener {}

        var weekNum = 0

        BestRef.setBestRefWeekList(type, cate).get().addOnSuccessListener {

            for (i in it.children) {
                val group: BookListDataBestToday? = i.getValue(BookListDataBestToday::class.java)
                val ref: MutableMap<String?, Any> = HashMap()

                ref["writerName"] = group!!.writer!!
                ref["subject"] = group.title!!
                ref["bookImg"] = group.bookImg!!
                ref["bookCode"] = group.bookCode!!
                ref["info1"] = group.info1!!
                ref["info2"] = group.info2!!
                ref["info3"] = group.info3!!
                ref["info4"] = group.info4!!
                ref["info5"] = group.info5!!
                ref["number"] = group.number
                ref["numberDiff"] = group.numberDiff
                ref["date"] = group.date!!
                ref["type"] = type
                ref["status"] = status

                dbWeekList.bestDao().insert(BestRef.setDataBestDay(ref))

                weekNum += 1
            }

        }.addOnFailureListener {}


    }

    private fun calculateNum(num: Int?, title: String?, tabType: String): Int {

        val yesterdayNum = dbYesterday.bestDao().findName(tabType, title!!)

        if (yesterdayNum == 0) {
            status = "NEW"
            return 0
        } else {
            return when {
                num!! > yesterdayNum -> {
                    status = "DOWN"
                    num - yesterdayNum
                }
                num < yesterdayNum -> {
                    status = "UP"
                    num - yesterdayNum
                }
                num == yesterdayNum -> {
                    status = "SAME"
                    num - yesterdayNum
                }
                else -> {
                    status = "SAME"
                    -1
                }
            }
        }
    }

}