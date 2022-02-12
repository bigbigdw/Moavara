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
private lateinit var dbWeek: DataBaseBestDay
private lateinit var dbYesterday: DataBaseBestDay

class ActivitySplash : Activity() {

    val Genre = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        dbWeek = Room.databaseBuilder(this, DataBaseBestDay::class.java, "best-week")
            .allowMainThreadQueries().build()
        dbYesterday = Room.databaseBuilder(this, DataBaseBestDay::class.java, "best-yesterday")
            .allowMainThreadQueries().build()

        dbWeek.bestDao().initAll()
        dbYesterday.bestDao().initAll()

        Thread {
            runMining()
        }.start()

        val novelIntent = Intent(this, ActivityLogin::class.java)
        novelIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivityIfNeeded(novelIntent, 0)
        finish()

    }

    private fun runMining() {
        getRidiBest("Ridi")
        getOneStoreBest("OneStore")
        getBookListBestKakao("Kakao")
        getBookListBestJoara("Joara")
        getNaver("Naver")
        getMrBlue("MrBlue")
    }

    private fun getMrBlue(type : String) {

        val doc: Document =
            Jsoup.connect("https://www.mrblue.com/novel/best/all/realtime").post()
        val bestRef = mRootRef.child("best").child(type).child(Genre)
        val MrBlue: Elements = doc.select(".list-box ul li")
        val MrBlueRef: MutableMap<String?, Any> = HashMap()

        for (i in MrBlue.indices) {

            MrBlueRef["writerName"] = MrBlue.select(".txt-box .name > a")[i].attr("title")
            MrBlueRef["subject"] = MrBlue.select(".tit > a")[i].attr("title")
            MrBlueRef["bookImg"] = MrBlue.select(".img img")[i].absUrl("src")
            MrBlueRef["bookCode"] = MrBlue.select(".txt-box a")[i].absUrl("href")
            MrBlueRef["info1"] = MrBlue.select(".txt-box .name > a")[i].absUrl("href")
            MrBlueRef["info2"] = " "
            MrBlueRef["info3"] = " "
            MrBlueRef["info4"] = " "
            MrBlueRef["info5"] = " "
            MrBlueRef["number"] = i
            MrBlueRef["date"] = DBDate.Date()

            miningValue(MrBlueRef, i, type)

        }

        setRoomBest(bestRef,type)

    }

    private fun getNaver(type : String) {

        val doc: Document =
            Jsoup.connect("https://novel.naver.com/best/ranking?genre=102&periodType=DAILY").post()
        val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
        val bestRef = mRootRef.child("best").child(type).child(Genre)
        val NaverRef: MutableMap<String?, Any> = HashMap()

        for (i in Naver.indices) {

            NaverRef["writerName"] = Naver.select(".author")[i].text()
            NaverRef["subject"] = Naver.select(".tit")[i].text()
            NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
            NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
            NaverRef["info1"] = " "
            NaverRef["info2"] = Naver[i].select(".num_total").first()!!.text()
            NaverRef["info3"] = Naver[i].select(".num_total").next().first()!!.text()
            NaverRef["info4"] = Naver.select(".count")[i].text()
            NaverRef["info5"] = Naver.select(".score_area")[i].text()
            NaverRef["number"] = i
            NaverRef["date"] = DBDate.Date()

            miningValue(NaverRef, i, type)

        }

        setRoomBest(bestRef,type)

    }

    private fun getRidiBest(type : String) {

        val doc: Document =
            Jsoup.connect("https://ridibooks.com/bestsellers/romance_serial?order=daily").post()
        val Ridi: Elements = doc.select(".book_thumbnail_wrapper")
        val bestRef = mRootRef.child("best").child(type).child(Genre)
        val RidiRef: MutableMap<String?, Any> = HashMap()

        for (i in Ridi.indices) {

            RidiRef["writerName"] = doc.select("div .author_detail_link")[i].text()
            RidiRef["subject"] = doc.select("div .title_link")[i].text()
            RidiRef["bookImg"] = Ridi.select(".thumbnail_image .thumbnail")[i].absUrl("data-src")
            RidiRef["bookCode"] = Ridi.select("a")[i].absUrl("href")
            RidiRef["info1"] = " "
            RidiRef["info2"] = doc.select(".count_num")[i].text()
            RidiRef["info3"] = doc.select("span .StarRate_ParticipantCount")[i].text()
            RidiRef["info4"] = " "
            RidiRef["info5"] = doc.select("span .StarRate_Score")[i].text()
            RidiRef["number"] = i
            RidiRef["date"] = DBDate.Date()

            miningValue(RidiRef, i, type)

        }

        setRoomBest(bestRef,type)

    }

    private fun getOneStoreBest(type : String) {

        val doc: Document =
            Jsoup.connect("https://onestory.co.kr/display/card/CRD0045029?title=%EC%8B%A0%EC%9E%91%20%EC%97%B0%EC%9E%AC%20%EB%B2%A0%EC%8A%A4%ED%8A%B8")
                .get()
        val OneStory: Elements = doc.select(".ItemRendererInner")
        val bestRef = mRootRef.child("best").child(type).child(Genre)
        val OneStoryRef: MutableMap<String?, Any> = HashMap()

        for (i in OneStory.indices) {

            OneStoryRef["writerName"] = doc.select("div .ItemRendererTextArtist")[i].text()
            OneStoryRef["subject"] = doc.select("div .ItemRendererTextTitle")[i].text()
            OneStoryRef["bookImg"] = doc.select(".ThumbnailInner img")[i].absUrl("src")
            OneStoryRef["bookCode"] = " "
            OneStoryRef["info1"] = doc.select(".tItemRendererTextPublisher")[i].text()
            OneStoryRef["info2"] = doc.select(".tItemRendererTextSeries")[i].text()
            OneStoryRef["info3"] = doc.select(".ItemRendererTextComment span span")[i].text()
            OneStoryRef["info4"] = doc.select(".ItemRendererTextAvgScore")[i].text()
            OneStoryRef["info5"] = doc.select(".ItemRendererTextAvgScore")[i].text()
            OneStoryRef["number"] = i
            OneStoryRef["date"] = DBDate.Date()

            miningValue(OneStoryRef, i, type)

        }

        setRoomBest(bestRef,type)

    }

    private fun getBookListBestKakao(type : String) {

        val bestRef = mRootRef.child("best").child(type).child(Genre)
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
                            KakaoRef["bookCode"] = list[i].series_id!!
                            KakaoRef["info1"] = list[i].description!!
                            KakaoRef["info2"] = list[i].promotion_rate!!
                            KakaoRef["info3"] = list[i].read_count!!
                            KakaoRef["info4"] = list[i].like_count!!
                            KakaoRef["info5"] = list[i].rating!!
                            KakaoRef["number"] = i
                            KakaoRef["date"] = DBDate.Date()

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
        val bestRef = mRootRef.child("best").child(type).child(Genre)
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
                            JoaraRef["bookCode"] = books[i].bookCode!!
                            JoaraRef["info1"] = books[i].intro!!
                            JoaraRef["info2"] = books[i].cntChapter!!
                            JoaraRef["info3"] = books[i].cntPageRead!!
                            JoaraRef["info4"] = books[i].cntFavorite!!
                            JoaraRef["info5"] = books[i].cntRecom!!
                            JoaraRef["number"] = i
                            JoaraRef["date"] = DBDate.Date()

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

    private fun miningValue(ref: MutableMap<String?, Any>, num : Int, type: String){
        //WeekList
//        BestRef.setBestRefWeekList(type, num, Genre).setValue(BestRef.setBookListDataBestToday(ref, num))

        //Today
        BestRef.setBestRefToday(type, num, Genre).setValue(BestRef.setBookListDataBestToday(ref, num))

        //Week
        if (num < 10) {
            BestRef.setBestRefWeek(type, num, Genre).setValue(BestRef.setBookListDataBestToday(ref, num))
        }

        //Month - Week
        if (num == 0) {
            //Month - Day
            BestRef.setBestRefMonthWeek(type, Genre).setValue(BestRef.setBookListDataBestToday(ref, num))
            //Month
            BestRef.setBestRefMonth(type, Genre).setValue(BestRef.setBookListDataBestToday(ref, num))
        }

        BestRef.setBestRefMonthDay(type, num, Genre).setValue(BestRef.setBookListDataBestToday(ref, num))

    }

    private fun setRoomBest(bestRef : DatabaseReference, type: String){

        val yesterdayRef = bestRef.child("today").child(DBDate.Yesterday())

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
