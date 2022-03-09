package com.example.moavara.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.room.Room
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.Joara.JoaraBestListResult
import com.example.moavara.Joara.RetrofitJoara
import com.example.moavara.KaKao.BestResultKakao
import com.example.moavara.KaKao.BestResultKakaoStageNovel
import com.example.moavara.KaKao.RetrofitKaKao
import com.example.moavara.OneStore.OneStoreBookResult
import com.example.moavara.OneStore.RetrofitOnestore
import com.example.moavara.R
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.google.firebase.database.FirebaseDatabase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

val mRootRef = FirebaseDatabase.getInstance().reference

class ActivitySplash : Activity() {

    private lateinit var dbWeek: DataBaseBestDay
    private lateinit var dbWeekList: DataBaseBestDay
    private lateinit var dbYesterday: DataBaseBestDay
    var cate = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        dbYesterday = Room.databaseBuilder(
            this.applicationContext,
            DataBaseBestDay::class.java,
            "best-yesterday"
        ).allowMainThreadQueries().build()

        dbWeek =
            Room.databaseBuilder(this.applicationContext, DataBaseBestDay::class.java, "best-week")
                .allowMainThreadQueries().build()

        dbWeekList = Room.databaseBuilder(this.applicationContext, DataBaseBestDay::class.java, "week-list")
            .allowMainThreadQueries().build()

        dbWeek.bestDao().initAll()
        dbWeekList.bestDao().initAll()
        dbYesterday.bestDao().initAll()

        Thread {
            runMining()
        }.start()

        cate = Genre.getGenre(this).toString()

        Handler(Looper.myLooper()!!).postDelayed(
            {
                val intent = Intent(this, ActivityGenre::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivityIfNeeded(intent, 0)
                finish()
            },
            1000
        )

    }

    private fun runMining() {
        getRidiBest()
        getOneStoreBest()
        getKakaoBest()
        getKakaoStageBest()
        getJoaraBest()
        getJoaraBestNobless()
        getJoaraBestPremium()
        getNaverToday()
        getNaverChallenge()
        getNaverBest()
        getMrBlueBest()
    }

    private fun getMrBlueBest() {

        val doc: Document =
            Jsoup.connect(Genre.setMrBlueGenre(this)).post()
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
            MrBlueRef["numberDiff"] = 0
            MrBlueRef["date"] = DBDate.DateMMDD()
            MrBlueRef["status"] = ""

            miningValue(MrBlueRef, i, "MrBlue")

        }

    }

    private fun getNaverToday() {

        val doc: Document =
            Jsoup.connect(Genre.setNaverTodayGenre(this)).post()
        val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
        val NaverRef: MutableMap<String?, Any> = HashMap()

        for (i in Naver.indices) {

            NaverRef["writerName"] = Naver.select(".author")[i].text()
            NaverRef["subject"] = Naver.select(".tit")[i].text()
            NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
            NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
            NaverRef["info1"] = Naver[i].select(".num_total").first()!!.text()
            NaverRef["info2"] = Naver[i].select(".num_total").next().first()!!.text()
            NaverRef["info3"] = Naver.select(".count")[i].text()
            NaverRef["info4"] = Naver.select(".score_area")[i].text()
            NaverRef["info5"] = ""
            NaverRef["number"] = i
            NaverRef["numberDiff"] = 0
            NaverRef["date"] = DBDate.DateMMDD()
            NaverRef["status"] = ""

            miningValue(NaverRef, i, "Naver Today")

        }

    }

    private fun getNaverChallenge() {

        val doc: Document =
            Jsoup.connect(Genre.setNaverChallengeGenre(this)).post()
        val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
        val NaverRef: MutableMap<String?, Any> = HashMap()

        for (i in Naver.indices) {

            NaverRef["writerName"] = Naver.select(".author")[i].text()
            NaverRef["subject"] = Naver.select(".tit")[i].text()
            NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
            NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
            NaverRef["info1"] = Naver[i].select(".num_total").first()!!.text()
            NaverRef["info2"] = Naver[i].select(".num_total").next().first()!!.text()
            NaverRef["info3"] = Naver.select(".count")[i].text()
            NaverRef["info4"] = Naver.select(".score_area")[i].text()
            NaverRef["info5"] = ""
            NaverRef["number"] = i
            NaverRef["numberDiff"] = 0
            NaverRef["date"] = DBDate.DateMMDD()
            NaverRef["status"] = ""

            miningValue(NaverRef, i, "Naver Challenge")

        }

    }

    private fun getNaverBest() {

        val doc: Document =
            Jsoup.connect(Genre.setNaverGenre(this)).post()
        val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
        val NaverRef: MutableMap<String?, Any> = HashMap()

        for (i in Naver.indices) {

            NaverRef["writerName"] = Naver.select(".author")[i].text()
            NaverRef["subject"] = Naver.select(".tit")[i].text()
            NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
            NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
            NaverRef["info1"] = Naver[i].select(".num_total").first()!!.text()
            NaverRef["info2"] = Naver[i].select(".num_total").next().first()!!.text()
            NaverRef["info3"] = Naver.select(".count")[i].text()
            NaverRef["info4"] = Naver.select(".score_area")[i].text()
            NaverRef["info5"] = ""
            NaverRef["number"] = i
            NaverRef["numberDiff"] = 0
            NaverRef["date"] = DBDate.DateMMDD()
            NaverRef["status"] = ""

            miningValue(NaverRef, i, "Naver")

        }

    }

    private fun getRidiBest() {

        val doc: Document =
            Jsoup.connect(Genre.setRidiGenre(this)).post()
        val Ridi: Elements = doc.select(".book_thumbnail_wrapper")
        val RidiRef: MutableMap<String?, Any> = HashMap()

        for (i in Ridi.indices) {

            RidiRef["writerName"] = doc.select("div .author_detail_link")[i].text()
            RidiRef["subject"] = doc.select("div .title_link")[i].text()
            RidiRef["bookImg"] = Ridi.select(".thumbnail_image .thumbnail")[i].absUrl("data-src")
            RidiRef["bookCode"] = Ridi.select("a")[i].absUrl("href")
            RidiRef["info1"] = doc.select(".count_num")[i].text()
            RidiRef["info2"] = doc.select("span .StarRate_ParticipantCount")[i].text()
            RidiRef["info3"] = doc.select("span .StarRate_Score")[i].text()
            RidiRef["info4"] = " "
            RidiRef["info5"] = " "
            RidiRef["number"] = i
            RidiRef["numberDiff"] = 0
            RidiRef["date"] = DBDate.DateMMDD()
            RidiRef["status"] = ""

            miningValue(RidiRef, i, "Ridi")

        }

    }

    private fun getOneStoreBest() {
        val OneStoryRef: MutableMap<String?, Any> = HashMap()

        val call: Call<OneStoreBookResult?>? = RetrofitOnestore.getBestOneStore(Genre.setOneStoreGenre(this))

        call!!.enqueue(object : Callback<OneStoreBookResult?> {
            override fun onResponse(
                call: Call<OneStoreBookResult?>,
                response: Response<OneStoreBookResult?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val productList = it.params!!.productList

                        for (i in productList!!.indices) {

                            OneStoryRef["writerName"] = productList[i].artistNm!!
                            OneStoryRef["subject"] = productList[i].prodNm!!
                            OneStoryRef["bookImg"] = "https://img.onestore.co.kr/thumbnails/img_sac/224_320_F10_95/" + productList[i].thumbnailImageUrl!!
                            OneStoryRef["bookCode"] = productList[i].prodId!!
                            OneStoryRef["info1"] = productList[i].totalCount!!
                            OneStoryRef["info2"] = productList[i].avgScore!!
                            OneStoryRef["info3"] = productList[i].commentCount!!
                            OneStoryRef["info4"] = " "
                            OneStoryRef["info5"] = " "
                            OneStoryRef["number"] = i
                            OneStoryRef["numberDiff"] = 0
                            OneStoryRef["date"] = DBDate.DateMMDD()
                            OneStoryRef["status"] = ""

                            miningValue(OneStoryRef, i, "OneStore")

                        }
                    }
                }
            }

            override fun onFailure(call: Call<OneStoreBookResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

    }

    private fun getKakaoStageBest() {
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        val call: Call<List<BestResultKakaoStageNovel>?>? = RetrofitKaKao.getBestKakaoStage("false", "YESTERDAY", Genre.setKakaoStageGenre(this), "72")

        call!!.enqueue(object : Callback<List<BestResultKakaoStageNovel>?> {
            override fun onResponse(
                call: Call<List<BestResultKakaoStageNovel>?>,
                response: Response<List<BestResultKakaoStageNovel>?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->

                        val list = it

                        for(i in list.indices){
                            val novel = list[i].novel
                            KakaoRef["writerName"] = novel!!.nickname!!.name!!
                            KakaoRef["subject"] = novel.title!!
                            KakaoRef["bookImg"] = novel.thumbnail!!.url!!
                            KakaoRef["bookCode"] = novel.stageSeriesNumber!!
                            KakaoRef["info1"] = novel.synopsis!!
                            KakaoRef["info2"] = novel.publishedEpisodeCount!!
                            KakaoRef["info3"] = novel.viewCount!!
                            KakaoRef["info4"] = novel.visitorCount!!
                            KakaoRef["info5"] = ""
                            KakaoRef["number"] = i
                            KakaoRef["numberDiff"] = 0
                            KakaoRef["date"] = DBDate.DateMMDD()
                            KakaoRef["status"] = ""

                            miningValue(KakaoRef, i, "Kakao Stage")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<BestResultKakaoStageNovel>?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

    }

    private fun getKakaoBest() {
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
                            KakaoRef["numberDiff"] = 0
                            KakaoRef["date"] = DBDate.DateMMDD()
                            KakaoRef["status"] = ""

                            miningValue(KakaoRef, i, "Kakao")

                        }
                    }
                }
            }

            override fun onFailure(call: Call<BestResultKakao?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

    }

    private fun getJoaraBest() {
        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val call: Call<JoaraBestListResult?>? = RetrofitJoara.getJoaraBookBest("today", "", Genre.setJoaraGenre(this))

        call!!.enqueue(object : Callback<JoaraBestListResult?> {
            override fun onResponse(
                call: Call<JoaraBestListResult?>,
                response: Response<JoaraBestListResult?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val books = it.bookLists

                        for (i in books!!.indices) {

                            JoaraRef["writerName"] = books[i].writerName
                            JoaraRef["subject"] = books[i].subject
                            JoaraRef["bookImg"] = books[i].bookImg
                            JoaraRef["bookCode"] = books[i].bookCode
                            JoaraRef["info1"] = books[i].intro
                            JoaraRef["info2"] = books[i].cntChapter
                            JoaraRef["info3"] = books[i].cntPageRead
                            JoaraRef["info4"] = books[i].cntFavorite
                            JoaraRef["info5"] = books[i].cntRecom
                            JoaraRef["number"] = i
                            JoaraRef["numberDiff"] = 0
                            JoaraRef["date"] = DBDate.DateMMDD()
                            JoaraRef["status"] = ""

                            miningValue(JoaraRef, i, "Joara")

                        }
                    }
                }
            }

            override fun onFailure(call: Call<JoaraBestListResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

    }

    private fun getJoaraBestPremium() {
        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val call: Call<JoaraBestListResult?>? = RetrofitJoara.getJoaraBookBest("today", "premium", Genre.setJoaraGenre(this))

        call!!.enqueue(object : Callback<JoaraBestListResult?> {
            override fun onResponse(
                call: Call<JoaraBestListResult?>,
                response: Response<JoaraBestListResult?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val books = it.bookLists

                        for (i in books!!.indices) {

                            JoaraRef["writerName"] = books[i].writerName
                            JoaraRef["subject"] = books[i].subject
                            JoaraRef["bookImg"] = books[i].bookImg
                            JoaraRef["bookCode"] = books[i].bookCode
                            JoaraRef["info1"] = books[i].intro
                            JoaraRef["info2"] = books[i].cntChapter
                            JoaraRef["info3"] = books[i].cntPageRead
                            JoaraRef["info4"] = books[i].cntFavorite
                            JoaraRef["info5"] = books[i].cntRecom
                            JoaraRef["number"] = i
                            JoaraRef["numberDiff"] = 0
                            JoaraRef["date"] = DBDate.DateMMDD()
                            JoaraRef["status"] = ""

                            miningValue(JoaraRef, i, "Joara Premium")

                        }
                    }
                }
            }

            override fun onFailure(call: Call<JoaraBestListResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

    }

    private fun getJoaraBestNobless() {
        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val call: Call<JoaraBestListResult?>? = RetrofitJoara.getJoaraBookBest("today", "nobless", Genre.setJoaraGenre(this))

        call!!.enqueue(object : Callback<JoaraBestListResult?> {
            override fun onResponse(
                call: Call<JoaraBestListResult?>,
                response: Response<JoaraBestListResult?>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val books = it.bookLists

                        for (i in books!!.indices) {

                            JoaraRef["writerName"] = books[i].writerName
                            JoaraRef["subject"] = books[i].subject
                            JoaraRef["bookImg"] = books[i].bookImg
                            JoaraRef["bookCode"] = books[i].bookCode
                            JoaraRef["info1"] = books[i].intro
                            JoaraRef["info2"] = books[i].cntChapter
                            JoaraRef["info3"] = books[i].cntPageRead
                            JoaraRef["info4"] = books[i].cntFavorite
                            JoaraRef["info5"] = books[i].cntRecom
                            JoaraRef["number"] = i
                            JoaraRef["numberDiff"] = 0
                            JoaraRef["date"] = DBDate.DateMMDD()
                            JoaraRef["status"] = ""

                            miningValue(JoaraRef, i, "Joara Nobless")

                        }
                    }
                }
            }

            override fun onFailure(call: Call<JoaraBestListResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

    }

    private fun miningValue(ref: MutableMap<String?, Any>, num : Int, type: String){

        //Today
        BestRef.setBestRefToday(type, num, cate).setValue(BestRef.setBookListDataBestToday(ref))

        //Week
        if (num < 10) {
            BestRef.setBestRefWeek(type, num, cate).setValue(BestRef.setBookListDataBestToday(ref))
        }

        //Month - Week
        if (num == 0) {
            //Month - Day
            BestRef.setBestRefMonthWeek(type, cate).setValue(BestRef.setBookListDataBestToday(ref))
            //Month
            BestRef.setBestRefMonth(type, cate).setValue(BestRef.setBookListDataBestToday(ref))
        }

        BestRef.setBestRefMonthDay(type, num, cate).setValue(BestRef.setBookListDataBestToday(ref))
    }

}
