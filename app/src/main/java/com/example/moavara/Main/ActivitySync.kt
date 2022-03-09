package com.example.moavara.Main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.room.Room
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.DataBase.DataBestDay
import com.example.moavara.Joara.JoaraBestListResult
import com.example.moavara.Joara.RetrofitJoara
import com.example.moavara.KaKao.BestResultKakao
import com.example.moavara.KaKao.BestResultKakaoStageNovel
import com.example.moavara.KaKao.RetrofitKaKao
import com.example.moavara.OneStore.OneStoreBookResult
import com.example.moavara.OneStore.RetrofitOnestore
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class ActivitySync : Activity() {

    var cate = "ALL"
    var status = ""
    var tview1: TextView? = null
    var tview2: TextView? = null
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        cate = Genre.getGenre(this).toString()
        tview1 = findViewById(R.id.tview1)
        tview2 = findViewById(R.id.tview2)

        context = this

        tview1!!.text = "선택하신 장르 [$cate] 를 불러오고 있습니다"
        tview2!!.text = "동기화 중..."

        Thread {
            Mining.runMining(context, cate)
        }.start()

        Handler(Looper.myLooper()!!).postDelayed(
            {
                val intent = Intent(this, ActivityMain::class.java)
                startActivity(intent)
            },
            2000
        )


    }

}