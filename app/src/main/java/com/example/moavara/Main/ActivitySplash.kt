package com.example.moavara.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.room.Room
import com.example.moavara.DataBase.*
import com.example.moavara.Joara.JoaraBestListResult
import com.example.moavara.Joara.RetrofitJoara
import com.example.moavara.KaKao.BestResultKakao
import com.example.moavara.KaKao.RetrofitKaKao
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


val mRootRef = FirebaseDatabase.getInstance().reference
private lateinit var db: DataBaseBestWeek
private lateinit var dbMonth: DataBaseBestMonth

class ActivitySplash : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        db = Room.databaseBuilder( this, DataBaseBestWeek::class.java, "user-database").allowMainThreadQueries().build()

        dbMonth = Room.databaseBuilder(
            this,
            DataBaseBestMonth::class.java,
            "user-databaseM"
        ).allowMainThreadQueries()
            .build()

        Thread {
            test()
        }.start()

        startLoading()

    }

    private fun test() {

        getRidiBest()
        getOneStoreBest()
        getBookListBestKakao()
        getBookListBestJoara()
    }

    private fun getRidiBest() {

        val doc: Document =
            Jsoup.connect("https://ridibooks.com/bestsellers/romance_serial?order=daily").post()
        val ridiKeyword: Elements = doc.select(".book_thumbnail_wrapper")

        val bestRef = mRootRef.child("best").child("Ridi")

        for (i in ridiKeyword.indices) {

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

            bestRef.child("week list").child((((DBDate.DayInt() - 1) * 20 ) + i).toString()).setValue(
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

                if (i < 10) {
                    bestRef.child("week").child(i.toString()).child(DBDate.DayString())
                        .setValue(
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

                if (i == 0) {
                    bestRef.child("month").child(DBDate.Week())
                        .child(DBDate.DayString()).setValue(
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

                bestRef.child("month").child(DBDate.Week())
                    .child(DBDate.DayString()).child("day").child(i.toString()).setValue(
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


                bestRef.child("month").child(DBDate.Week())
                    .child(DBDate.DayString()).setValue(
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
                            DBDate.Date(),
                        )
                    )

            if(dbMonth.bestDaoMonth().getAllTypes("Ridi") != null){
                dbMonth.bestDaoMonth().initTypes("Ridi")
            } else {
                bestRef.child("month list").child((i).toString()).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in dataSnapshot.children) {

                            val group: BookListDataBestToday? =
                                postSnapshot.getValue(BookListDataBestToday::class.java)

                            dbMonth.bestDaoMonth().insert(
                                DataBestMonth(
                                    group!!.writer,
                                    group.title,
                                    group.bookImg,
                                    group.intro,
                                    group.bookCode,
                                    group.cntChapter,
                                    group.cntPageRead,
                                    group.cntFavorite,
                                    group.cntRecom,
                                    group.number,
                                    DBDate.Date(),
                                    "Ridi",
                                    i.toString(),
                                )
                            )
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
            }


            }

        val week = mRootRef.child("best").child("Ridi").child("week list")
        var num = 1

        db.bestDao().initAll()

        week.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)

                    db.bestDao().insert(
                        DataBestWeek(
                            group!!.writer,
                            group.title,
                            group.bookImg,
                            group.intro,
                            group.bookCode,
                            group.cntChapter,
                            group.cntPageRead,
                            group.cntFavorite,
                            group.cntRecom,
                            group.number,
                            DBDate.Date(),
                            "Ridi"
                        )
                    )

                    num += 1
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

    }

    private fun getOneStoreBest() {

        val doc: Document =
            Jsoup.connect("https://onestory.co.kr/display/card/CRD0059596?title=%EC%9D%B4%EA%B1%B4%20%EC%82%AC%EC%95%BC%ED%95%B4!%20%EC%9C%A0%EB%A3%8C%20%EA%B5%AC%EB%A7%A4%EC%9C%A8%20BEST%20%EB%A1%9C%EB%A7%A8%EC%8A%A4")
                .get()
        val ridiKeyword: Elements = doc.select(".ItemRendererInner")

        var bestRef = mRootRef.child("best").child("OneStore")

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

            bestRef.child("week list").child((((DBDate.DayInt() - 1) * 20 ) + i).toString()).setValue(
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

            if (i < 10) {
                bestRef.child("week").child(i.toString()).child(DBDate.DayString())
                    .setValue(
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

            if (i == 0) {
                bestRef.child("month").child(DBDate.Week())
                    .child(DBDate.DayString()).setValue(
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

            bestRef.child("month").child(DBDate.Week())
                .child(DBDate.DayString()).child("day").child(i.toString()).setValue(
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


            bestRef.child("month").child(DBDate.Week())
                .child(DBDate.DayString()).child("day").setValue(
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

//                bestRef.child("month list").child((i + 1).toString()).addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        for (postSnapshot in dataSnapshot.children) {
//
//                            val group: BookListDataBestToday? =
//                                postSnapshot.getValue(BookListDataBestToday::class.java)
//
//                            dbMonth.bestDaoMonth().insert(
//                                DataBestMonth(
//                                    group!!.writer,
//                                    group.title,
//                                    group.bookImg,
//                                    group.intro,
//                                    group.bookCode,
//                                    group.cntChapter,
//                                    group.cntPageRead,
//                                    group.cntFavorite,
//                                    group.cntRecom,
//                                    group.number,
//                                    DBDate.Date(),
//                                    "OneStore",
//                                    i.toString(),
//                                )
//                            )
//                        }
//                    }
//
//                    override fun onCancelled(databaseError: DatabaseError) {
//                    }
//                })

        }


        val week = mRootRef.child("best").child("OneStore").child("week list")
        var num = 1

        db.bestDao().initAll()

        week.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)

                    db.bestDao().insert(
                        DataBestWeek(
                            group!!.writer,
                            group.title,
                            group.bookImg,
                            group.intro,
                            group.bookCode,
                            group.cntChapter,
                            group.cntPageRead,
                            group.cntFavorite,
                            group.cntRecom,
                            group.number,
                            DBDate.Date(),
                            "OneStore"
                        )
                    )

                    num += 1
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun getBookListBestKakao() {

        val bestRef = mRootRef.child("best").child("Kakao")

        val call: Call<BestResultKakao?>? = RetrofitKaKao.getBestKakao("11", "0", "0", "2", "A")

        call!!.enqueue(object : Callback<BestResultKakao?> {
            override fun onResponse(
                call: Call<BestResultKakao?>,
                response: Response<BestResultKakao?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val list = it.list

                        for (i in list!!.indices) {

                            val writerName = list[i].author
                            val subject = list[i].title
                            val bookImg =
                                "https://dn-img-page.kakao.com/download/resource?kid=" + list[i].image
                            val intro = list[i].description
                            val bookCode = list[i].series_id
                            val cntChapter = list[i].promotion_rate
                            val cntPageRead = list[i].read_count
                            val cntFavorite = list[i].like_count
                            val cntRecom = list[i].rating

                            bestRef.child("week list").child((((DBDate.DayInt() - 1) * 29 ) + i).toString()).setValue(
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

                            if (i < 10) {
                                bestRef.child("week").child(i.toString()).child(DBDate.DayString())
                                    .setValue(
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

                            if (i == 0) {
                                bestRef.child("month").child(DBDate.Week())
                                    .child(DBDate.DayString()).setValue(
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

                            bestRef.child("month").child(DBDate.Week())
                                .child(DBDate.DayString()).child("day").child(i.toString()).setValue(
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

//                                bestRef.child("month list").child((i + 1).toString()).addValueEventListener(object : ValueEventListener {
//                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                        for (postSnapshot in dataSnapshot.children) {
//
//                                            val group: BookListDataBestToday? =
//                                                postSnapshot.getValue(BookListDataBestToday::class.java)
//
//                                            dbMonth.bestDaoMonth().insert(
//                                                DataBestMonth(
//                                                    group!!.writer,
//                                                    group.title,
//                                                    group.bookImg,
//                                                    group.intro,
//                                                    group.bookCode,
//                                                    group.cntChapter,
//                                                    group.cntPageRead,
//                                                    group.cntFavorite,
//                                                    group.cntRecom,
//                                                    group.number,
//                                                    DBDate.Date(),
//                                                    "Kakao",
//                                                    DBDate.Week(),
//                                                )
//                                            )
//                                        }
//                                    }
//
//                                    override fun onCancelled(databaseError: DatabaseError) {
//                                    }
//                                })


                        }
                    }
                }
            }

            override fun onFailure(call: Call<BestResultKakao?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

        val week = mRootRef.child("best").child("Kakao").child("week list")
        var num = 1

        db.bestDao().initAll()

        week.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)

                    db.bestDao().insert(
                        DataBestWeek(
                            group!!.writer,
                            group.title,
                            group.bookImg,
                            group.intro,
                            group.bookCode,
                            group.cntChapter,
                            group.cntPageRead,
                            group.cntFavorite,
                            group.cntRecom,
                            group.number,
                            DBDate.Date(),
                            "Kakao"
                        )
                    )

                    num += 1
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

    }

    private fun getBookListBestJoara() {
        val bestRef = mRootRef.child("best").child("Joara")
        val call: Call<JoaraBestListResult?>? = RetrofitJoara.getJoaraBookBest("today", "", "0")

        call!!.enqueue(object : Callback<JoaraBestListResult?> {
            override fun onResponse(
                call: Call<JoaraBestListResult?>,
                response: Response<JoaraBestListResult?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val books = it.bookLists

                        for (i in books!!.indices) {

                            val writerName = books[i].writerName
                            val subject = books[i].subject
                            val bookImg = books[i].bookImg
                            val intro = books[i].intro
                            val bookCode = books[i].bookCode
                            val cntChapter = books[i].cntChapter
                            val cntPageRead = books[i].cntPageRead
                            val cntFavorite = books[i].cntFavorite
                            val cntRecom = books[i].cntRecom

                            bestRef.child("week list").child((((DBDate.DayInt() - 1) * 20 ) + i).toString()).setValue(
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

                            if (i < 10) {
                                bestRef.child("week").child(i.toString()).child(DBDate.DayString())
                                    .setValue(
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

                            if (i == 0) {
                                bestRef.child("month").child(DBDate.Week())
                                    .child(DBDate.DayString()).setValue(
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

                            bestRef.child("month").child(DBDate.Week())
                                .child(DBDate.DayString()).child("day").child(i.toString()).setValue(
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

//                                bestRef.child("month list").child((i + 1).toString()).addValueEventListener(object : ValueEventListener {
//                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                        for (postSnapshot in dataSnapshot.children) {
//
//                                            val group: BookListDataBestToday? =
//                                                postSnapshot.getValue(BookListDataBestToday::class.java)
//
//                                            dbMonth.bestDaoMonth().insert(
//                                                DataBestMonth(
//                                                    group!!.writer,
//                                                    group.title,
//                                                    group.bookImg,
//                                                    group.intro,
//                                                    group.bookCode,
//                                                    group.cntChapter,
//                                                    group.cntPageRead,
//                                                    group.cntFavorite,
//                                                    group.cntRecom,
//                                                    group.number,
//                                                    DBDate.Date(),
//                                                    "Joara",
//                                                    i.toString(),
//                                                )
//                                            )
//                                        }
//                                    }
//
//                                    override fun onCancelled(databaseError: DatabaseError) {
//                                    }
//                                })

                        }
                    }
                }
            }

            override fun onFailure(call: Call<JoaraBestListResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

        val week = mRootRef.child("best").child("Joara").child("week list")
        var num = 1

        db.bestDao().initAll()

        week.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)

                    db.bestDao().insert(
                        DataBestWeek(
                            group!!.writer,
                            group.title,
                            group.bookImg,
                            group.intro,
                            group.bookCode,
                            group.cntChapter,
                            group.cntPageRead,
                            group.cntFavorite,
                            group.cntRecom,
                            group.number,
                            DBDate.Date(),
                            "Joara"
                        )
                    )

                    num += 1
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })



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
