package com.example.takealook.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.room.Room
import com.example.takealook.DataBase.User
import com.example.takealook.DataBase.UserDatabase
import com.example.takealook.Joara.BookListData
import com.example.takealook.R
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException


private lateinit var db: UserDatabase

class ActivitySplash : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // 싱글톤 패턴을 사용하지 않은 경우
        db = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            "user-database"
        ).allowMainThreadQueries() // 그냥 강제로 실행
            .build()

        addUser()
        refreshUserList()


//        startLoading()

//        Thread {
//            test()
//        }.start()
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

    private fun addUser() {
        var name = "NAME"
        var age = "AGE"
        var phone = "PHONE"

        db.userDao().insert(User(name, age, phone))
    }

    private fun refreshUserList() {
        var userList = "유저 리스트\n"

        val users = db.userDao().getAll()

        for (user in users) {
            userList += "이름: ${user.name}, 나이: ${user.age}, 번호: ${user.phone}\n"
        }

    }
}
