package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
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
import com.example.moavara.Util.DBDate
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

    val Genre = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_setting)

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
        setRoomBest("Naver")
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

        val week = mRootRef.child("best").child(type).child(Genre).child("week list")
        var num = 1

        week.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)

                    dbWeek.bestDao().insert(
                        DataBestDay(
                            group!!.writer,
                            group.title,
                            group.bookImg,
                            group.info1,
                            group.bookCode,
                            group.info2,
                            group.info3,
                            group.info4,
                            group.info5,
                            group.number,
                            group.date,
                            type,
                            ""
                        )
                    )

                    num += 1
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        val yesterdayRef = mRootRef.child("best").child(type).child(Genre).child("today").child(
            DBDate.Yesterday())

        yesterdayRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)

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
                            group.date,
                            type
                        )
                    )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}