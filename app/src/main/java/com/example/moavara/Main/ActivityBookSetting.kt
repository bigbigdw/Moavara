package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.DataBase.DataBestDay
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*


class ActivityBookSetting : AppCompatActivity() {
    var llayout_step1: LinearLayout? = null
    var llayout_step2: LinearLayout? = null
    var llayout_step3: LinearLayout? = null

    private lateinit var dbWeek: DataBaseBestDay
    private lateinit var dbYesterday: DataBaseBestDay

    var button_next: Button? = null

    var cate = "ALL"
    var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_setting)

        cate = Genre.getGenre(this).toString()

        llayout_step1 = findViewById(R.id.llayout_step1)
        llayout_step2 = findViewById(R.id.llayout_step2)
        llayout_step3 = findViewById(R.id.llayout_step3)

        dbYesterday = Room.databaseBuilder(this, DataBaseBestDay::class.java, "best-yesterday")
            .allowMainThreadQueries().build()

        dbWeek = Room.databaseBuilder(this, DataBaseBestDay::class.java, "best-week")
            .allowMainThreadQueries().build()


        button_next = findViewById(R.id.button_next)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)

        setLayout()

        dbWeek.bestDao().initAll()
        dbYesterday.bestDao().initAll()

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

    private fun setLayout() {


        button_next!!.setOnClickListener {
            if (llayout_step1!!.visibility == View.VISIBLE) {
                llayout_step2!!.visibility = View.VISIBLE
                llayout_step1!!.visibility = View.GONE
                button_next!!.text = "다음으로"
            } else if (llayout_step2!!.visibility == View.VISIBLE) {
                llayout_step3!!.visibility = View.VISIBLE
                llayout_step2!!.visibility = View.GONE
                button_next!!.text = "완료"
            } else if (llayout_step3!!.visibility == View.VISIBLE) {
                val intent = Intent(applicationContext, ActivityGuide::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setRoomBest(type: String){

        val yesterdayRef = mRootRef.child("best").child(type).child(cate).child("today").child(
            DBDate.Yesterday())

        yesterdayRef.get().addOnSuccessListener {

            for(i in it.children){
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

        }.addOnFailureListener{}

        var num = 1

        BestRef.getBestRefToday(type, cate).get().addOnSuccessListener {

            for(i in it.children){
                val group: BookListDataBestToday? = i.getValue(BookListDataBestToday::class.java)
                val ref: MutableMap<String?, Any> = HashMap()

                if(dbWeek.bestDao().getAll(type) != null && dbWeek.bestDao().findDay(type, group!!.title!!, group.number!!) == null){
                    if (calculateNum(group.number, group.title, type) != 0) {

                        ref["writerName"] = group.writer!!
                        ref["subject"] = group.title!!
                        ref["bookImg"] = group.bookImg!!
                        ref["bookCode"] = group.bookCode!!
                        ref["info1"] = group.info1!!
                        ref["info2"] = group.info2!!
                        ref["info3"] = group.info3!!
                        ref["info4"] = group.info4!!
                        ref["info5"] = group.info5!!
                        ref["number"] = calculateNum(group.number, group.title, type)
                        ref["numberDiff"] = 0
                        ref["date"] = group.date!!
                        ref["type"] = type
                        ref["status"] = status

                        dbWeek.bestDao().insert(BestRef.setDataBestDay(ref))
                        BestRef.setBestRefWeekCompared(type, num, cate).setValue(BestRef.setBookListDataBestToday(ref))
                    }
                }

                num += 1
            }

        }.addOnFailureListener{}

    }

    private fun calculateNum(num : Int?, title : String?, tabType: String) : Int{

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