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
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.google.firebase.database.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


val mRootRef = FirebaseDatabase.getInstance().reference
private lateinit var db: DataBaseBestWeek
private lateinit var dbYesterday: DataBaseBestWeek
private lateinit var dbMonth: DataBaseBestMonth

class ActivitySplash : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        db = Room.databaseBuilder(this, DataBaseBestWeek::class.java, "user-database")
            .allowMainThreadQueries().build()
        dbYesterday = Room.databaseBuilder(this, DataBaseBestWeek::class.java, "best-yesterday")
            .allowMainThreadQueries().build()

        dbMonth = Room.databaseBuilder(
            this,
            DataBaseBestMonth::class.java,
            "user-databaseM"
        ).allowMainThreadQueries()
            .build()

        Thread {
            runMining()
        }.start()

        startLoading()

    }

    private fun runMining() {
        getRidiBest("Ridi")
        getOneStoreBest("OneStore")
        getBookListBestKakao("Kakao")
        getBookListBestJoara("Joara")
    }

    private fun getRidiBest(type : String) {

        val doc: Document =
            Jsoup.connect("https://ridibooks.com/bestsellers/romance_serial?order=daily").post()
        val Ridi: Elements = doc.select(".book_thumbnail_wrapper")
        val bestRef = mRootRef.child("best").child(type).child("ALL")
        val RidiRef: MutableMap<String?, Any> = HashMap()

        for (i in Ridi.indices) {

            RidiRef["writerName"] = doc.select("div .author_detail_link")[i].text()
            RidiRef["subject"] = doc.select("div .title_link")[i].text()
            RidiRef["bookImg"] = Ridi.select(".thumbnail_image .thumbnail")[i].absUrl("data-src")
            RidiRef["intro"] = " "
            RidiRef["bookCode"] = Ridi.select("a")[i].absUrl("href")
            RidiRef["cntChapter"] = doc.select(".count_num")[i].text()
            RidiRef["cntPageRead"] = doc.select("span .StarRate_ParticipantCount")[i].text()
            RidiRef["cntFavorite"] = " "
            RidiRef["cntRecom"] = doc.select("span .StarRate_Score")[i].text()

            miningValue(RidiRef, i, type)

        }

        setRoomBest(bestRef,type)

    }

    private fun getOneStoreBest(type : String) {

        val doc: Document =
            Jsoup.connect("https://onestory.co.kr/display/card/CRD0045029?title=%EC%8B%A0%EC%9E%91%20%EC%97%B0%EC%9E%AC%20%EB%B2%A0%EC%8A%A4%ED%8A%B8")
                .get()
        val OneStory: Elements = doc.select(".ItemRendererInner")
        val bestRef = mRootRef.child("best").child(type).child("ALL")
        val OneStoryRef: MutableMap<String?, Any> = HashMap()

        for (i in OneStory.indices) {

            OneStoryRef["writerName"] = doc.select("div .ItemRendererTextArtist")[i].text()
            OneStoryRef["subject"] = doc.select("div .ItemRendererTextTitle")[i].text()
            OneStoryRef["bookImg"] = doc.select(".ThumbnailInner img")[i].absUrl("src")
            OneStoryRef["intro"] = doc.select(".tItemRendererTextPublisher")[i].text()
            OneStoryRef["bookCode"] = " "
            OneStoryRef["cntChapter"] = doc.select(".tItemRendererTextSeries")[i].text()
            OneStoryRef["cntPageRead"] = doc.select(".ItemRendererTextComment span span")[i].text()
            OneStoryRef["cntFavorite"] = doc.select(".ItemRendererTextAvgScore")[i].text()
            OneStoryRef["cntRecom"] = doc.select(".ItemRendererTextAvgScore")[i].text()

            miningValue(OneStoryRef, i, type)

        }

        setRoomBest(bestRef,type)

    }

    private fun getBookListBestKakao(type : String) {

        val bestRef = mRootRef.child("best").child(type).child("ALL")
        val KakaoRef: MutableMap<String?, Any> = HashMap()

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

                            KakaoRef["writerName"] = list[i].author!!
                            KakaoRef["subject"] = list[i].title!!
                            KakaoRef["bookImg"] = "https://dn-img-page.kakao.com/download/resource?kid=" + list[i].image!!
                            KakaoRef["intro"] = list[i].description!!
                            KakaoRef["bookCode"] = list[i].series_id!!
                            KakaoRef["cntChapter"] = list[i].promotion_rate!!
                            KakaoRef["cntPageRead"] = list[i].read_count!!
                            KakaoRef["cntFavorite"] = list[i].like_count!!
                            KakaoRef["cntRecom"] = list[i].rating!!

                            miningValue(KakaoRef, i, type)

                        }
                    }
                }
            }

            override fun onFailure(call: Call<BestResultKakao?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

        setRoomBest(bestRef,type)

    }

    private fun getBookListBestJoara(type : String) {
        val bestRef = mRootRef.child("best").child(type).child("ALL")
        val JoaraRef: MutableMap<String?, Any> = HashMap()

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

                            JoaraRef["writerName"] = books[i].writerName!!
                            JoaraRef["subject"] = books[i].subject!!
                            JoaraRef["bookImg"] = books[i].bookImg!!
                            JoaraRef["intro"] = books[i].intro!!
                            JoaraRef["bookCode"] = books[i].bookCode!!
                            JoaraRef["cntChapter"] = books[i].cntChapter!!
                            JoaraRef["cntPageRead"] = books[i].cntPageRead!!
                            JoaraRef["cntFavorite"] = books[i].cntFavorite!!
                            JoaraRef["cntRecom"] = books[i].cntRecom!!

                            miningValue(JoaraRef, i, type)

                        }
                    }
                }
            }

            override fun onFailure(call: Call<JoaraBestListResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

        setRoomBest(bestRef,type)

    }

    private fun setBookListDataBestToday(ref: MutableMap<String?, Any>, num : Int) : BookListDataBestToday {
        return BookListDataBestToday(
            ref["writerName"] as String?,
            ref["subject"] as String?,
            ref["bookImg"] as String?,
            ref["intro"] as String?,
            ref["bookCode"] as String?,
            ref["cntChapter"] as String?,
            ref["cntPageRead"] as String?,
            ref["cntFavorite"] as String?,
            ref["cntRecom"] as String?,
            num + 1,
            DBDate.Date(),
            ""
        )
    }

    private fun miningValue(ref: MutableMap<String?, Any>, num : Int, type: String){
        //WeekList
        BestRef.setBestRefWeekList(type, num).setValue(setBookListDataBestToday(ref, num))

        //Today
        BestRef.setBestRefToday(type, num).setValue(setBookListDataBestToday(ref, num))

        //Week
        if (num < 10) {
            BestRef.setBestRefWeek(type, num).setValue(setBookListDataBestToday(ref, num))
        }

        //Month - Week
        if (num == 0) {
            BestRef.setBestRefMonthWeek(type).setValue(setBookListDataBestToday(ref, num))
        }

        //Month - Day
        BestRef.setBestRefMonthDay(type, num).setValue(setBookListDataBestToday(ref, num))

        //Month
        BestRef.setBestRefMonth(type).setValue(setBookListDataBestToday(ref, num))


//            if (dbMonth.bestDaoMonth().getAllTypes("Ridi").toString() != "[]") {
//                dbMonth.bestDaoMonth().initTypes("Ridi")
//            } else {
//                bestRef.child("month list").child((i).toString())
//                    .addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(dataSnapshot: DataSnapshot) {
//                            for (postSnapshot in dataSnapshot.children) {
//
//                                val group: BookListDataBestToday? =
//                                    postSnapshot.getValue(BookListDataBestToday::class.java)
//
//                                dbMonth.bestDaoMonth().insert(
//                                    DataBestMonth(
//                                        group!!.writer,
//                                        group.title,
//                                        group.bookImg,
//                                        group.intro,
//                                        group.bookCode,
//                                        group.cntChapter,
//                                        group.cntPageRead,
//                                        group.cntFavorite,
//                                        group.cntRecom,
//                                        group.number,
//                                        DBDate.Date(),
//                                        "Ridi",
//                                        i.toString(),
//                                    )
//                                )
//                            }
//                        }
//
//                        override fun onCancelled(databaseError: DatabaseError) {
//                        }
//                    })
//            }



    }

    private fun setRoomBest(bestRef : DatabaseReference, type: String){
        val week = bestRef.child(type).child("week list")
        var num = 1

        db.bestDao().initAll()
        dbYesterday.bestDao().initAll()

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
                            type
                        )
                    )

                    num += 1
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        val yesterdayRef = bestRef.child("today").child(DBDate.Yesterday())

        yesterdayRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)

                    dbYesterday.bestDao().insert(
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
                            type
                        )
                    )
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
