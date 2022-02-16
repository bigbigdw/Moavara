package com.example.moavara.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.moavara.Joara.JoaraBestListResult
import com.example.moavara.Joara.RetrofitJoara
import com.example.moavara.KaKao.BestResultKakao
import com.example.moavara.KaKao.BestResultKakaoStage
import com.example.moavara.KaKao.BestResultKakaoStageNovel
import com.example.moavara.KaKao.RetrofitKaKao
import com.example.moavara.OneStore.OneStoreBookResult
import com.example.moavara.OneStore.RetrofitOnestore
import com.example.moavara.R
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.google.firebase.database.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val mRootRef = FirebaseDatabase.getInstance().reference

class ActivitySplash : Activity() {

    var cate = "ALL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Thread {
            runMining()
        }.start()

        cate = Genre.getGenre(this).toString()

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

    private fun runMining() {
        getRidiBest("Ridi")
        getOneStoreBest("OneStore")
        getKakaoBest("Kakao")
        getKakaoStageBest("Kakao Stage")
        getJoaraBest("Joara")
        getJoaraBestNobless("Joara Nobless")
        getJoaraBestPremium("Joara Premium")
        getNaverToday("Naver Today")
        getNaverChallenge("Naver Challenge")
        getNaverBest("Naver")
        getMrBlueBest("MrBlue")
    }

    private fun getMrBlueBest(type : String) {

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
            MrBlueRef["date"] = DBDate.Date()

            miningValue(MrBlueRef, i, type)

        }

    }

    private fun getNaverToday(type : String) {

        val doc: Document =
            Jsoup.connect(Genre.setNaverTodayGenre(this)).post()
        val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
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

    }

    private fun getNaverChallenge(type : String) {

        val doc: Document =
            Jsoup.connect(Genre.setNaverChallengeGenre(this)).post()
        val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
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

    }

    private fun getNaverBest(type : String) {

        val doc: Document =
            Jsoup.connect(Genre.setNaverGenre(this)).post()
        val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
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

    }

    private fun getRidiBest(type : String) {

        val doc: Document =
            Jsoup.connect(Genre.setRidiGenre(this)).post()
        val Ridi: Elements = doc.select(".book_thumbnail_wrapper")
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

    }

    private fun getOneStoreBest(type : String) {
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
                            OneStoryRef["date"] = DBDate.Date()

                            miningValue(OneStoryRef, i, type)

                        }
                    }
                }
            }

            override fun onFailure(call: Call<OneStoreBookResult?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

    }

    private fun getKakaoStageBest(type : String) {
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
                            KakaoRef["date"] = DBDate.Date()

                            miningValue(KakaoRef, i, type)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<BestResultKakaoStageNovel>?>, t: Throwable) {
                Log.d("onFailure", "실패")
            }
        })

    }

    private fun getKakaoBest(type : String) {
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

    }

    private fun getJoaraBest(type : String) {
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

    }

    private fun getJoaraBestPremium(type : String) {
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

    }

    private fun getJoaraBestNobless(type : String) {
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

    }

    private fun miningValue(ref: MutableMap<String?, Any>, num : Int, type: String){

//        BestRef.delBestRefWeekList(type, Genre).removeValue()

        //Week List
//        BestRef.setBestRefWeekList(type, num, Genre).setValue(BestRef.setBookListDataBestToday(ref))

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
