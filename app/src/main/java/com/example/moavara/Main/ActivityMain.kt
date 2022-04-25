package com.example.moavara.Main

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import androidx.work.WorkManager
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.DataBase.DataBestDay
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.example.moavara.Util.Mining
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import kotlin.collections.HashMap
import kotlin.system.exitProcess


class ActivityMain : AppCompatActivity() {
    var navController: NavController? = null
    private lateinit var dbWeek: DataBaseBestDay
    private lateinit var dbWeekList: DataBaseBestDay
    private lateinit var dbYesterday: DataBaseBestDay
    var toolbar : Toolbar? = null

    var cate = "ALL"
    var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cate = Genre.getGenre(this).toString()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.navHostFragmentMain)

        toolbar.setOnClickListener {
            WorkManager.getInstance().cancelAllWork()
            val miningRef = mRootRef.child("Mining")
            miningRef.setValue("HAHA")
            Toast.makeText(this, "WorkManager 해제됨", Toast.LENGTH_SHORT).show()
        }


//        setLayout()
    }

    private fun setLayout() {

        val navView = findViewById<BottomNavigationView>(R.id.nav_bottom)
        NavigationUI.setupWithNavController(navView, navController!!)

        dbWeek =
            Room.databaseBuilder(this.applicationContext, DataBaseBestDay::class.java, "best-week")
                .allowMainThreadQueries().build()

        dbWeekList =
            Room.databaseBuilder(this.applicationContext, DataBaseBestDay::class.java, "week-list")
                .allowMainThreadQueries().build()

        dbYesterday = Room.databaseBuilder(
            this.applicationContext,
            DataBaseBestDay::class.java,
            "best-yesterday"
        ).allowMainThreadQueries().build()

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

//        for (i in BestRef.typeList().indices) {
//            Mining.getWeekCompared(BestRef.typeList()[i], "ALL")
//            Mining.getWeekCompared(BestRef.typeList()[i], "ROMANCE")
//            Mining.getWeekCompared(BestRef.typeList()[i], "BL")
//            Mining.getWeekCompared(BestRef.typeList()[i], "FANTASY")
//        }
        
        BestRef.getBestRefToday(type, cate).get().addOnSuccessListener {

            for (i in (1000 * DBDate.DayInt())..((1000 * DBDate.DayInt()) + 999)) {
                BestRef.setBestRef(type, cate).child("week-list")
                    .child(i.toString()).removeValue()
            }

            for (i in it.children) {
                val group: BookListDataBestToday? = i.getValue(BookListDataBestToday::class.java)
                val ref: MutableMap<String?, Any> = HashMap()

                if (calculateNum(group!!.number, group.title, type) != 999) {

                    ref["writerName"] = group.writer
                    ref["subject"] = group.title
                    ref["bookImg"] = group.bookImg
                    ref["bookCode"] = group.bookCode
                    ref["info1"] = group.info1
                    ref["info2"] = group.info2
                    ref["info3"] = group.info3
                    ref["info4"] = group.info4
                    ref["info5"] = group.info5
                    ref["number"] = group.number
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

                if (group != null) {
                    ref["writerName"] = group.writer
                    ref["subject"] = group.title
                    ref["bookImg"] = group.bookImg
                    ref["bookCode"] = group.bookCode
                    ref["info1"] = group.info1
                    ref["info2"] = group.info2
                    ref["info3"] = group.info3
                    ref["info4"] = group.info4
                    ref["info5"] = group.info5
                    ref["number"] = group.number
                    ref["numberDiff"] = group.numberDiff
                    ref["date"] = group.date
                    ref["type"] = type
                    ref["status"] = status
                }

//                dbWeekList.bestDao().insert(BestRef.setDataBestDay(ref))

                weekNum += 1
            }

        }.addOnFailureListener {}


    }

    private fun calculateNum(num: Int?, title: String?, tabType: String): Int {

        val yesterdayNum = dbYesterday.bestDao().findName(tabType, title!!)

        if (yesterdayNum == 0) {
            status = "NEW"
            return 999
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
                    0
                }
            }
        }
    }

    override fun onBackPressed() {

        val myAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(this@ActivityMain)
        myAlertBuilder.setTitle("모아바라 종료")
        myAlertBuilder.setMessage("모아바라를 종료하시겠습니까?")
        myAlertBuilder.setPositiveButton(
            "예"
        ) { _, _ ->

            finishAffinity();
            System.runFinalization();
            exitProcess(0);
        }
        myAlertBuilder.setNegativeButton(
            "아니요"
        ) { _, _ ->
        }
        // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
        myAlertBuilder.show()


    }

}