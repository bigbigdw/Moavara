package com.example.moavara.Util

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.Retrofit.*
import com.example.moavara.Search.EventDetailDataMining
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File
import java.net.SocketTimeoutException

object Mining {

    fun runMiningEvent(context: Context) {
        getNoticeJoara(context)
        getEventJoara(context)
    }

    private fun getNoticeJoara(context: Context) {

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
        param["board"] = ""
        param["page"] = "1"
        param["token"] = context.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
            ?.getString("TOKEN", "").toString()
        var num = 0
        val EventRef = FirebaseDatabase.getInstance().reference.child("Notice")

        apiJoara.getNoticeList(
            param,
            object : RetrofitDataListener<JoaraNoticeResult> {
                override fun onSuccess(it: JoaraNoticeResult) {

                    val data = it.notices

                    if (data != null) {
                        for (item in data) {
                            EventRef.child(DBDate.Week() + ((DBDate.DayInt() * 1000) + num).toString())
                                .setValue(
                                    EventDetailDataMining(
                                        DBDate.DateMMDD(),
                                        item.cnt_read,
                                    )
                                )
                        }
                        num += 1
                    }
                }
            })
    }

    private fun getEventJoara(context: Context) {

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)
        param["page"] = "1"
        param["show_type"] = "android"
        param["event_type"] = "normal"
        param["offset"] = "25"
        param["token"] = context.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
            ?.getString("TOKEN", "").toString()

        var num = 0
        val EventRef = FirebaseDatabase.getInstance().reference.child("Event")

        apiJoara.getJoaraEventList(
            param,
            object : RetrofitDataListener<JoaraEventsResult> {
                override fun onSuccess(it: JoaraEventsResult) {

                    val data = it.data

                    if (data != null) {
                        for (item in data) {
                            for (item in data) {
                                EventRef.child(DBDate.Week() + ((DBDate.DayInt() * 1000) + num).toString())
                                    .setValue(
                                        EventDetailDataMining(
                                            DBDate.DateMMDD(),
                                            item.cnt_read,
                                        )
                                    )
                            }
                            num += 1
                        }
                    }
                }
            })
    }


    fun runMining(context: Context, genre: String) {

        val file = File(File("/storage/self/primary/MOAVARA"), "Today_Search.json")
        if (file.exists()) {
            file.delete()
        }

        val Ridi = Thread {
            for (i in 1..3) {
                getRidiBest(genre, i)
            }
        }

        val OneStore = Thread {
            getOneStoreBest(genre, context)
        }

        val Kakao = Thread {
            for (i in 1..4) {
                getKakaoBest(genre, i, context)
            }
        }

        val KakaoStage1 = Thread {
            getKakaoStageBest(genre, context)
        }

        val Joara = Thread {
            for (i in 1..5) {
                getJoaraBest(context, genre, i)
            }
        }

        val JoaraNobless = Thread {
            for (i in 1..5) {
                getJoaraBestNobless(context, genre, i)
            }
        }

        val JoaraPremium = Thread {
            for (i in 1..5) {
                getJoaraBestPremium(context, genre, i)
            }
        }

        val NaverToday = Thread {
            getNaverToday(genre, context)
        }

        val NaverChallenge = Thread {
            getNaverChallenge(genre, context)
        }

        val NaverBest = Thread {
            getNaverBest(genre, context)
        }

        val Moonpia = Thread {
            for (i in 1..4) {
                getMoonpiaBest(i, context)
            }
        }

        val Toksoda = Thread {
            for (i in 1..5) {
                getToksodaBest(genre, i, context)
            }
        }

        val MrBlue = Thread {
            getMrBlueBest(genre, context)
        }


        try {
            Ridi.start()
            Log.d("####MINING", "리디1 완료")
            Ridi.join()

            OneStore.start()
            OneStore.join()
            Log.d("####MINING", "원스토어1 완료")

            Kakao.start()
            Kakao.join()
            Log.d("####MINING", "카카오1 완료")

            KakaoStage1.start()
            KakaoStage1.join()
            Log.d("####MINING", "카카오 스테이지1 완료")

            Joara.start()
            Joara.join()
            Log.d("####MINING", "조아라1 완료")

            JoaraNobless.start()
            JoaraNobless.join()
            Log.d("####MINING", "조아라 노블레스1 완료")

            JoaraPremium.start()
            JoaraPremium.join()
            Log.d("####MINING", "조아라 프리미엄1 완료")

            NaverToday.start()
            NaverToday.join()
            Log.d("####MINING", "네이버 투데이1 완료")

            NaverChallenge.start()
            NaverChallenge.join()
            Log.d("####MINING", "네이버 챌린지1 완료")

            NaverBest.start()
            NaverBest.join()
            Log.d("####MINING", "네이버1 완료")

            Moonpia.start()
            Moonpia.join()
            Log.d("####MINING", "문피아 완료")

            Toksoda.start()
            Toksoda.join()
            Log.d("####MINING", "톡소다1 완료")

            MrBlue.start()
            MrBlue.join()
            Log.d("####MINING", "미스터 블루 완료")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun getMrBlueBest(platform: String, context: Context) {
        Thread {

            try {
                val doc: Document =
                    Jsoup.connect(Genre.setMrBlueGenre(platform)).post()
                val MrBlue: Elements = doc.select(".list-box li")
                val MrBlueRef: MutableMap<String?, Any> = HashMap()

                for (i in MrBlue.indices) {

                    MrBlueRef["writerName"] =
                        MrBlue.select(".txt-box .name > a")[i].attr("title")
                    MrBlueRef["subject"] = MrBlue.select(".tit > a")[i].attr("title")
                    MrBlueRef["bookImg"] = MrBlue.select(".img img")[i].absUrl("src")
                    MrBlueRef["bookCode"] = MrBlue.select(".img>a")[i].absUrl("href").replace("https://www.mrblue.com/novel/","")
                    MrBlueRef["info1"] = MrBlue.select(".txt-box .name > a")[i].absUrl("href").replace("https://www.mrblue.com/author?id=", "")
                    MrBlueRef["info2"] = " "
                    MrBlueRef["info3"] = " "
                    MrBlueRef["info4"] = " "
                    MrBlueRef["info5"] = " "
                    MrBlueRef["number"] = i
                    MrBlueRef["date"] = DBDate.DateMMDD()
                    MrBlueRef["type"] = "MrBlue"

                    miningValue(MrBlueRef, i, "MrBlue", platform)

                }

                File(File("/storage/self/primary/MOAVARA"), "Today_MrBlue.json").delete()
                File(File("/storage/self/primary/MOAVARA"), "Week_MrBlue.json").delete()
                File(File("/storage/self/primary/MOAVARA"), "Month_MrBlue.json").delete()

            } catch (exception: SocketTimeoutException) {
                Log.d("EXCEPTION", "MRBLUE")
            }
        }.start()

    }

    fun getNaverToday(platform: String, context: Context) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setNaverTodayGenre(platform)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            for (i in Naver.indices) {

                val title = Naver.select(".tit")[i].text()
                NaverRef["writerName"] = Naver.select(".author")[i].text()
                NaverRef["subject"] = title
                NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
                NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href").replace("https://novel.naver.com/webnovel/list?novelId=", "")
                NaverRef["info1"] = Naver.select(".score_area")[i].text()
                NaverRef["info2"] = ""
                NaverRef["info3"] = Naver[i].select(".num_total").first()!!.text()
                NaverRef["info4"] =
                    Naver[i].select(".num_total").next().first()!!.text()
                NaverRef["info5"] = Naver.select(".count")[i].text()
                NaverRef["number"] = i

                NaverRef["date"] = DBDate.DateMMDD()
                NaverRef["type"] = "Naver_Today"

                miningValue(NaverRef, i, "Naver_Today", platform)

            }

            File(File("/storage/self/primary/MOAVARA"), "Today_Naver_Today.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Week_Naver_Today.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Month_Naver_Today.json").delete()


        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER TODAY")
        }

    }

    fun getNaverChallenge(platform: String, context: Context) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setNaverChallengeGenre(platform)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            for (i in Naver.indices) {

                NaverRef["writerName"] = Naver.select(".author")[i].text()
                NaverRef["subject"] = Naver.select(".tit")[i].text()
                NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
                NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href").replace("https://novel.naver.com/challenge/list?novelId=", "")
                NaverRef["info1"] = Naver.select(".score_area")[i].text()
                NaverRef["info2"] = ""
                NaverRef["info3"] = Naver[i].select(".num_total").first()!!.text()
                NaverRef["info4"] =
                    Naver[i].select(".num_total").next().first()!!.text()
                NaverRef["info5"] = Naver.select(".count")[i].text()
                NaverRef["number"] = i
                NaverRef["date"] = DBDate.DateMMDD()
                NaverRef["type"] = "Naver_Challenge"

                miningValue(NaverRef, i, "Naver_Challenge", platform)
            }

            File(File("/storage/self/primary/MOAVARA"), "Today_Naver_Challenge.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Week_Naver_Challenge.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Month_Naver_Challenge.json").delete()

        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER CHALLANGE")
        }
    }

    fun getNaverBest(platform: String, context: Context) {
        try {

            val doc: Document =
                Jsoup.connect(Genre.setNaverGenre(platform)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            for (i in Naver.indices) {

                NaverRef["writerName"] = Naver.select(".author")[i].text()
                NaverRef["subject"] = Naver.select(".tit")[i].text()
                NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
                NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href").replace("https://novel.naver.com/best/list?novelId=", "")
                NaverRef["info1"] = ""
                NaverRef["info2"] = Naver.select(".score_area")[i].text()
                NaverRef["info3"] = Naver[i].select(".num_total").first()!!.text()
                NaverRef["info4"] =
                    Naver[i].select(".num_total").next().first()!!.text()
                NaverRef["info5"] = Naver.select(".count")[i].text()
                NaverRef["number"] = i
                NaverRef["date"] = DBDate.DateMMDD()
                NaverRef["type"] = "Naver"

                miningValue(NaverRef, i, "Naver", platform)
            }

            File(File("/storage/self/primary/MOAVARA"), "Today_Naver.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Week_Naver.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Month_Naver.json").delete()


        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER CHALLANGE")
        }
    }

    fun getRidiBest(platform: String, page: Int) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setRidiGenre(platform, page)).post()
            val Ridi: Elements = doc.select(".book_thumbnail_wrapper")
            val RidiRef: MutableMap<String?, Any> = HashMap()

            for (i in Ridi.indices) {
                if (i > 0) {
                    val uri: Uri =
                        Uri.parse(Ridi.select("a")[i].absUrl("href"))

                    RidiRef["writerName"] =
                        doc.select("div .author_detail_link")[i].text()
                    RidiRef["subject"] = doc.select("div .title_link")[i].text()
                    RidiRef["bookImg"] =
                        Ridi.select(".thumbnail_image .thumbnail")[i].absUrl("data-src")
                    RidiRef["bookCode"] = uri.path?.replace("/books/", "") ?: ""
                    RidiRef["info1"] = ""
                    RidiRef["info2"] = ""
                    RidiRef["info3"] = doc.select(".count_num")[i].text()
                    RidiRef["info4"] =
                        "추천 수 : " + doc.select("span .StarRate_ParticipantCount")[i].text()
                    RidiRef["info5"] =
                        "평점 : " + doc.select("span .StarRate_Score")[i].text()
                    RidiRef["number"] = (i -1) + ((page - 1) * (Ridi.size - 1))
                    RidiRef["date"] = DBDate.DateMMDD()
                    RidiRef["type"] = "Ridi"

                    miningValue(RidiRef, ((i -1) + ((page - 1) * (Ridi.size - 1))), "Ridi", platform)
                }
            }

            File(File("/storage/self/primary/MOAVARA"), "Today_Ridi.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Week_Ridi.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Month_Ridi.json").delete()

        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "RIDI")
        }
    }

    fun getOneStoreBest(cate: String, context: Context) {
        try {
            val OneStoryRef: MutableMap<String?, Any> = HashMap()

            val apiOneStory = RetrofitOnestore()
            val param: MutableMap<String?, Any> = HashMap()

            param["menuId"] = Genre.setOneStoreGenre(cate)

            apiOneStory.getBestOneStore(
                param,
                object : RetrofitDataListener<OneStoreBookResult> {
                    override fun onSuccess(data: OneStoreBookResult) {

                        val productList = data.params?.productList

                        if (productList != null) {
                            for (i in productList.indices) {

                                OneStoryRef["writerName"] = productList[i].artistNm
                                OneStoryRef["subject"] = productList[i].prodNm
                                OneStoryRef["bookImg"] =
                                    "https://img.onestore.co.kr/thumbnails/img_sac/224_320_F10_95/" + productList[i].thumbnailImageUrl
                                OneStoryRef["bookCode"] = productList[i].prodId
                                OneStoryRef["info1"] = " "
                                OneStoryRef["info2"] = " "
                                OneStoryRef["info3"] =
                                    "조회 수 : " + productList[i].totalCount
                                OneStoryRef["info4"] = "평점 : " + productList[i].avgScore
                                OneStoryRef["info5"] =
                                    "댓글 수 : " + productList[i].commentCount
                                OneStoryRef["number"] = i
                                OneStoryRef["date"] = DBDate.DateMMDD()
                                OneStoryRef["type"] = "OneStore"

                                miningValue(
                                    OneStoryRef,
                                    i,
                                    "OneStore",
                                    cate
                                )
                            }
                        }

                        File(File("/storage/self/primary/MOAVARA"), "Today_OneStore.json").delete()
                        File(File("/storage/self/primary/MOAVARA"), "Week_OneStore.json").delete()
                        File(File("/storage/self/primary/MOAVARA"), "Month_OneStore.json").delete()
                    }
                })
        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "ONESTORE")
        }
    }

    fun getKakaoStageBest(platform: String, context: Context) {
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        param["adult"] = "false"
        param["dateRange"] = "YESTERDAY"
        param["genreIds"] = Genre.setKakaoStageGenre(platform)
        param["recentHours"] = "72"

        apiKakao.getBestKakaoStage(
            param,
            object : RetrofitDataListener<List<BestResultKakaoStageNovel>> {
                override fun onSuccess(data: List<BestResultKakaoStageNovel>) {

                    data.let {

                        val list = it

                        for (i in list.indices) {
                            val novel = list[i].novel

                            KakaoRef["writerName"] = novel!!.nickname!!.name
                            KakaoRef["subject"] = novel.title
                            KakaoRef["bookImg"] = novel.thumbnail!!.url
                            KakaoRef["bookCode"] = novel.stageSeriesNumber
                            KakaoRef["info1"] = "줄거리 : " + novel.synopsis
                            KakaoRef["info2"] = ""
                            KakaoRef["info3"] = "총" + novel.publishedEpisodeCount + " 화"
                            KakaoRef["info4"] = "조회 수 : " + novel.viewCount
                            KakaoRef["info5"] = "선호작 수 : " + novel.visitorCount
                            KakaoRef["number"] = i
                            KakaoRef["date"] = DBDate.DateMMDD()
                            KakaoRef["type"] = "Kakao_Stage"

                            miningValue(KakaoRef, i, "Kakao_Stage", platform)
                        }

                        File(File("/storage/self/primary/MOAVARA"), "Today_Kakao_Stage.json").delete()
                        File(File("/storage/self/primary/MOAVARA"), "Week_Kakao_Stage.json").delete()
                        File(File("/storage/self/primary/MOAVARA"), "Month_Kakao_Stage.json").delete()
                    }

                }
            })
    }

    fun getKakaoBest(platform: String, page: Int, context: Context) {
        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        param["category"] = "11"
        param["subcategory"] = "0"
        param["page"] = page
        param["day"] = "2"
        param["bm"] = "A"

        apiKakao.getKakaoBest(
            param,
            object : RetrofitDataListener<BestResultKakao> {
                override fun onSuccess(data: BestResultKakao) {

                    val list = data.list

                    for (i in list!!.indices) {

                        KakaoRef["writerName"] = list[i].author
                        KakaoRef["subject"] = list[i].title
                        KakaoRef["bookImg"] =
                            "https://dn-img-page.kakao.com/download/resource?kid=" + list[i].image
                        KakaoRef["bookCode"] = list[i].series_id
                        KakaoRef["info1"] = "줄거리 : " + list[i].description
                        KakaoRef["info2"] = "부제 : " + list[i].caption
                        KakaoRef["info3"] = "조회 수 : " + list[i].read_count
                        KakaoRef["info4"] = "추천 수 : " + list[i].like_count
                        KakaoRef["info5"] = "평점 : " + list[i].rating
                        KakaoRef["number"] = (i + ((page - 1) * list.size))
                        KakaoRef["date"] = DBDate.DateMMDD()
                        KakaoRef["type"] = "Kakao"
                        miningValue(
                            KakaoRef,
                            (i + ((page - 1) * list.size)),
                            "Kakao",
                            platform
                        )
                    }

                    File(File("/storage/self/primary/MOAVARA"), "Today_Kakao.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Week_Kakao.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Month_Kakao.json").delete()
                }
            })
    }

    fun getJoaraBest(context: Context, platform: String, page: Int) {
        val JoaraRef: MutableMap<String?, Any> = HashMap()
        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = page.toString()
        param["best"] = "today"
        param["store"] = ""
        param["category"] = Genre.setJoaraGenre(platform)

        apiJoara.getJoaraBookBest(
            param,
            object : RetrofitDataListener<JoaraBestListResult> {
                override fun onSuccess(data: JoaraBestListResult) {

                    val books = data.bookLists

                    for (i in books!!.indices) {

                        JoaraRef["writerName"] = books[i].writerName
                        JoaraRef["subject"] = books[i].subject
                        JoaraRef["bookImg"] = books[i].bookImg
                        JoaraRef["bookCode"] = books[i].bookCode
                        JoaraRef["info1"] = "줄거리 : ${books[i].intro}"
                        JoaraRef["info2"] = "총 ${books[i].cntChapter}화"
                        JoaraRef["info3"] = "조회 수 : ${books[i].cntPageRead}"
                        JoaraRef["info4"] = "선호작 수 : ${books[i].cntFavorite}"
                        JoaraRef["info5"] = "추천 수 : ${books[i].cntRecom}"
                        JoaraRef["number"] = i + ((page - 1) * books.size)
                        JoaraRef["date"] = DBDate.DateMMDD()
                        JoaraRef["type"] = "Joara"

                        miningValue(
                            JoaraRef,
                            (i + ((page - 1) * books.size)),
                            "Joara",
                            platform
                        )
                    }

                    File(File("/storage/self/primary/MOAVARA"), "Today_Joara.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Week_Joara.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Month_Joara.json").delete()
                }
            })
    }

    fun getJoaraBestPremium(context: Context, platform: String, page: Int) {

        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = page.toString()
        param["best"] = "today"
        param["store"] = "premium"
        param["category"] = Genre.setJoaraGenre(platform)

        apiJoara.getJoaraBookBest(
            param,
            object : RetrofitDataListener<JoaraBestListResult> {
                override fun onSuccess(data: JoaraBestListResult) {

                    val books = data.bookLists

                    for (i in books!!.indices) {

                        JoaraRef["writerName"] = books[i].writerName
                        JoaraRef["subject"] = books[i].subject
                        JoaraRef["bookImg"] = books[i].bookImg
                        JoaraRef["bookCode"] = books[i].bookCode
                        JoaraRef["info1"] = "줄거리 : ${books[i].intro}"
                        JoaraRef["info2"] = "총 ${books[i].cntChapter}화"
                        JoaraRef["info3"] = "조회 수 : ${books[i].cntPageRead}"
                        JoaraRef["info4"] = "선호작 수 : ${books[i].cntFavorite}"
                        JoaraRef["info5"] = "추천 수 : ${books[i].cntRecom}"
                        JoaraRef["number"] = i + ((page - 1) * books.size)
                        JoaraRef["date"] = DBDate.DateMMDD()
                        JoaraRef["type"] = "Joara_Premium"

                        miningValue(
                            JoaraRef,
                            (i + ((page - 1) * books.size)),
                            "Joara_Premium",
                            platform
                        )
                    }

                    File(File("/storage/self/primary/MOAVARA"), "Today_Joara_Premium.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Week_Joara_Premium.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Month_Joara_Premium.json").delete()
                }
            })

    }

    fun getJoaraBestNobless(context: Context, platform: String, page: Int) {

        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = page.toString()
        param["best"] = "today"
        param["store"] = "nobless"
        param["category"] = Genre.setJoaraGenre(platform)

        apiJoara.getJoaraBookBest(
            param,
            object : RetrofitDataListener<JoaraBestListResult> {
                override fun onSuccess(data: JoaraBestListResult) {

                    val books = data.bookLists

                    for (i in books!!.indices) {

                        JoaraRef["status"] = "NEW"
                        JoaraRef["writerName"] = books[i].writerName
                        JoaraRef["subject"] = books[i].subject
                        JoaraRef["bookImg"] = books[i].bookImg
                        JoaraRef["bookCode"] = books[i].bookCode
                        JoaraRef["info1"] = "줄거리 : ${books[i].intro}"
                        JoaraRef["info2"] = "총 ${books[i].cntChapter}화"
                        JoaraRef["info3"] = "조회 수 : ${books[i].cntPageRead}"
                        JoaraRef["info4"] = "선호작 수 : ${books[i].cntFavorite}"
                        JoaraRef["info5"] = "추천 수 : ${books[i].cntRecom}"
                        JoaraRef["number"] = i + ((page - 1) * books.size)
                        JoaraRef["date"] = DBDate.DateMMDD()
                        JoaraRef["type"] = "Joara_Nobless"

                        miningValue(
                            JoaraRef,
                            (i + ((page - 1) * books.size)),
                            "Joara_Nobless",
                            platform
                        )
                    }

                    File(File("/storage/self/primary/MOAVARA"), "Today_Joara_Nobless.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Week_Joara_Nobless.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Month_Joara_Nobless.json").delete()
                }
            })

    }

    fun getMoonpiaBest(page: Int, context: Context) {
        val MoonpiaRef: MutableMap<String?, Any> = HashMap()

        val apiMoonPia = RetrofitMoonPia()
        val param: MutableMap<String?, Any> = HashMap()

        param["section"] = "today"
        param["exclusive"] = ""
        param["outAdult"] = "true"
        param["offset"] = (page - 1) * 25

        apiMoonPia.postMoonPiaBest(
            param,
            object : RetrofitDataListener<BestMoonpiaResult> {
                override fun onSuccess(data: BestMoonpiaResult) {

                    data.api?.items.let {

                        if (it != null) {
                            for (i in it.indices) {

                                MoonpiaRef["writerName"] = it[i].author
                                MoonpiaRef["subject"] = it[i].nvTitle
                                MoonpiaRef["bookImg"] =
                                    "https://cdn1.munpia.com${it[i].nvCover}"
                                MoonpiaRef["bookCode"] = it[i].nvSrl
                                MoonpiaRef["info1"] = "줄거리 : ${it[i].nvStory}"
                                MoonpiaRef["info2"] = "베스트 시간 : ${it[i].nsrData?.hour}"
                                MoonpiaRef["info3"] = "조회 수 : ${it[i].nsrData?.hit}"
                                MoonpiaRef["info4"] = "방문 수 : ${it[i].nsrData?.number}"
                                MoonpiaRef["info5"] = "선호작 수 : ${it[i].nsrData?.prefer}"
                                MoonpiaRef["number"] = (i + ((page - 1) * it.size))
                                MoonpiaRef["date"] = DBDate.DateMMDD()
                                MoonpiaRef["type"] = "Munpia"

                                miningValue(
                                    MoonpiaRef,
                                    (i + ((page - 1) * it.size)),
                                    "Munpia",
                                    ""
                                )
                            }

                            File(File("/storage/self/primary/MOAVARA"), "Today_Munpia.json").delete()
                            File(File("/storage/self/primary/MOAVARA"), "Week_Munpia.json").delete()
                            File(File("/storage/self/primary/MOAVARA"), "Month_Munpia.json").delete()
                        }
                    }

                }
            })
    }

    fun getToksodaBest(genre: String, page: Int, context: Context) {
        val ToksodaRef: MutableMap<String?, Any> = HashMap()

        val apiToksoda = RetrofitToksoda()
        val param: MutableMap<String?, Any> = HashMap()

        param["page"] = page
        param["lgctgrCd"] = Genre.setToksodaGenre(genre)
        param["mdctgrCd"] = "all"
        param["rookieYn"] = "N"
        param["over19Yn"] = "N"
        param["type"] = "NEW"
        param["freePblserlYn"] = "00431"
        param["_"] = "1657262989944"

        apiToksoda.getBestList(
            param,
            object : RetrofitDataListener<BestToksodaResult> {
                override fun onSuccess(data: BestToksodaResult) {

                    data.resultList?.let {
                        for (i in it.indices) {

                            ToksodaRef["writerName"] = it[i].athrnm
                            ToksodaRef["subject"] = it[i].wrknm
                            ToksodaRef["bookImg"] = "https:${it[i].imgPath}"
                            ToksodaRef["bookCode"] = it[i].brcd
                            ToksodaRef["info1"] = it[i].lnIntro
                            ToksodaRef["info2"] = "총 ${it[i].whlEpsdCnt}화"
                            ToksodaRef["info3"] = "조회 수 : ${it[i].inqrCnt}"
                            ToksodaRef["info4"] = "장르 : ${it[i].lgctgrNm}"
                            ToksodaRef["info5"] = "선호작 수 : ${it[i].intrstCnt}"
                            ToksodaRef["number"] = i
                            ToksodaRef["date"] = DBDate.DateMMDD()
                            ToksodaRef["type"] = "Toksoda"

                            miningValue(
                                ToksodaRef,
                                (i + ((page - 1) * it.size)),
                                "Toksoda",
                                genre
                            )
                        }

                        File(File("/storage/self/primary/MOAVARA"), "Today_Toksoda.json").delete()
                        File(File("/storage/self/primary/MOAVARA"), "Week_Toksoda.json").delete()
                        File(File("/storage/self/primary/MOAVARA"), "Month_Toksoda.json").delete()
                    }
                }
            })
    }

    fun SetBookCodeData(platform: String, genre: String, bookCode: String) {

        val result: ArrayList<BookListDataBestAnalyze>? = null
        var num = 0

        BestRef.getBookCode(platform, genre).child(bookCode)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

//                    java.lang.ClassCastException: java.util.HashMap cannot be cast to java.util.ArrayList
//                    var hi = dataSnapshot.value as ArrayList<BookListDataBestAnalyze>
//
//                  Log.d("####", hi[0].toString())

//                    for (keyItem in dataSnapshot.children) {
//                        val group: BookListDataBestAnalyze? = keyItem.getValue(BookListDataBestAnalyze::class.java)
//                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun RoomDB(context: Context, platform: String, cate: String) {

    }


    fun RoomDBRemove(context: Context, tabType: String, cate: String) {
        val bestDao: DataBaseBestDay = Room.databaseBuilder(
            context,
            DataBaseBestDay::class.java,
            "$tabType $cate"
        ).allowMainThreadQueries().build()

        bestDao.bestDao().initAll()
    }
}

