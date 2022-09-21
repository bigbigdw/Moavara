package com.example.moavara.Util

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.room.Room
import com.example.moavara.DataBase.DBBest
import com.example.moavara.Main.mRootRef
import com.example.moavara.Retrofit.*
import com.example.moavara.Search.BookListDataBest
import com.example.moavara.Search.BookListDataBestAnalyze
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
import java.util.*
import kotlin.collections.ArrayList

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
                override fun onSuccess(data: JoaraNoticeResult) {

                    val data = data.notices

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
                override fun onSuccess(data: JoaraEventsResult) {

                    val data = data.data

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
            getRidiBest(context, genre)
        }

        val OneStore = Thread {
            getOneStoreBest(context, genre)
        }

        val Kakao = Thread {
            getKakaoBest(context, genre)
        }

        val KakaoStage = Thread {
            getKakaoStageBest(context, genre)
        }

        val Joara = Thread {
            getJoaraBest(context, genre)
        }

        val JoaraNobless = Thread {
            getJoaraBestNobless(context, genre)
        }

        val JoaraPremium = Thread {
            getJoaraBestPremium(context, genre)
        }

        val NaverToday = Thread {
            getNaverToday(context, genre)
        }

        val NaverChallenge = Thread {
            getNaverChallenge(context, genre)
        }

        val NaverBest = Thread {
            getNaverBest(context, genre)
        }

        val Moonpia = Thread {
            getMoonpiaBest(context)
        }

        val Toksoda = Thread {
            getToksodaBest(context, genre)
        }

        val MrBlue = Thread {
            getMrBlueBest(context, genre)
        }

        try {
            Ridi.start()
            Log.d("MINING", "리디 완료")
            Ridi.join()

            OneStore.start()
            OneStore.join()
            Log.d("MINING", "원스토어 완료")

            Kakao.start()
            Kakao.join()
            Log.d("MINING", "카카오 완료")

            KakaoStage.start()
            KakaoStage.join()
            Log.d("MINING", "카카오 스테이지1 완료")

            Joara.start()
            Joara.join()
            Log.d("MINING", "조아라 완료")

            JoaraNobless.start()
            JoaraNobless.join()
            Log.d("MINING", "조아라 노블레스 완료")

            JoaraPremium.start()
            JoaraPremium.join()
            Log.d("MINING", "조아라 프리미엄 완료")

            NaverToday.start()
            NaverToday.join()
            Log.d("MINING", "네이버 투데이 완료")

            NaverChallenge.start()
            NaverChallenge.join()
            Log.d("MINING", "네이버 챌린지 완료")

            NaverBest.start()
            NaverBest.join()
            Log.d("MINING", "네이버 완료")

            Moonpia.start()
            Moonpia.join()
            Log.d("MINING", "문피아 완료")

            Toksoda.start()
            Toksoda.join()
            Log.d("MINING", "톡소다 완료")

            MrBlue.start()
            MrBlue.join()
            Log.d("MINING", "미스터 블루 완료")

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun getMrBlueBest(context: Context, genre: String) {
        Thread {

            try {
                val doc: Document =
                    Jsoup.connect(Genre.setMrBlueGenre(genre)).post()
                val MrBlue: Elements = doc.select(".list-box li")
                val MrBlueRef: MutableMap<String?, Any> = HashMap()

                for (i in MrBlue.indices) {

                    MrBlueRef["writerName"] =
                        MrBlue.select(".txt-box .name > a")[i].attr("title")
                    MrBlueRef["subject"] = MrBlue.select(".tit > a")[i].attr("title")
                    MrBlueRef["bookImg"] = MrBlue.select(".img img")[i].absUrl("src")
                    MrBlueRef["bookCode"] = MrBlue.select(".img>a")[i].absUrl("href")
                        .replace("https://www.mrblue.com/novel/", "")
                    MrBlueRef["info1"] = MrBlue.select(".txt-box .name > a")[i].absUrl("href")
                        .replace("https://www.mrblue.com/author?id=", "")
                    MrBlueRef["info2"] = " "
                    MrBlueRef["info3"] = " "
                    MrBlueRef["info4"] = " "
                    MrBlueRef["info5"] = " "
                    MrBlueRef["info6"] = " "
                    MrBlueRef["number"] = i
                    MrBlueRef["date"] = DBDate.DateMMDD()
                    MrBlueRef["type"] = "MrBlue"

                    miningValue(MrBlueRef, i, "MrBlue", genre)
                }

                val bestDaoToday = Room.databaseBuilder(
                    context,
                    DBBest::class.java,
                    "Today_MrBlue_${genre}"
                ).allowMainThreadQueries().build()

                val bestDaoWeek = Room.databaseBuilder(
                    context,
                    DBBest::class.java,
                    "Week_MrBlue_${genre}"
                ).allowMainThreadQueries().build()

                val bestDaoMonth = Room.databaseBuilder(
                    context,
                    DBBest::class.java,
                    "Month_MrBlue_${genre}"
                ).allowMainThreadQueries().build()

                bestDaoToday.bestDao().initAll()
                bestDaoWeek.bestDao().initAll()
                bestDaoMonth.bestDao().initAll()

                File(File("/storage/self/primary/MOAVARA"), "Today_MrBlue_${genre}.json").delete()
                File(File("/storage/self/primary/MOAVARA"), "Week_MrBlue_${genre}.json").delete()
                File(File("/storage/self/primary/MOAVARA"), "Month_MrBlue_${genre}.json").delete()

            } catch (exception: SocketTimeoutException) {
                Log.d("EXCEPTION", "MRBLUE")
            }
        }.start()

    }

    private fun getNaverToday(context : Context, genre: String) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setNaverTodayGenre(genre)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            val books = ArrayList<BookListDataBest>()

            for (i in Naver.indices) {

                val info3 = Naver.select(".score_area")[i].text().replace("별점", "")
                val info4 = Naver[i].select(".num_total").next().first()!!.text().replace("조회 ", "")
                val info5 = Naver.select(".count")[i].text().replace("관심", "")

                val title = Naver.select(".tit")[i].text()
                NaverRef["writerName"] = Naver.select(".author")[i].text()
                NaverRef["subject"] = title
                NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
                NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
                    .replace("https://novel.naver.com/webnovel/list?novelId=", "")
                NaverRef["info1"] = Naver[i].select(".num_total").first()!!.text()
                NaverRef["info2"] = ""
                NaverRef["info3"] = info3
                NaverRef["info4"] = info4
                NaverRef["info5"] = info5
                NaverRef["info6"] = ""
                NaverRef["number"] = i

                NaverRef["date"] = DBDate.DateMMDD()
                NaverRef["type"] = "Naver_Today"

                books.add(BestRef.setBookListDataBest(NaverRef))
                miningValue(NaverRef, i, "Naver_Today", genre)
            }

            val bestDaoToday = Room.databaseBuilder(
                context,
                DBBest::class.java,
                "Today_Naver_Today_${genre}"
            ).allowMainThreadQueries().build()

            val bestDaoWeek = Room.databaseBuilder(
                context,
                DBBest::class.java,
                "Week_Naver_Today_${genre}"
            ).allowMainThreadQueries().build()

            val bestDaoMonth = Room.databaseBuilder(
                context,
                DBBest::class.java,
                "Month_Naver_Today_${genre}"
            ).allowMainThreadQueries().build()

            bestDaoToday.bestDao().initAll()
            bestDaoWeek.bestDao().initAll()
            bestDaoMonth.bestDao().initAll()

            File(File("/storage/self/primary/MOAVARA"), "Today_Naver_Today_${genre}.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Week_Naver_Today_${genre}.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Month_Naver_Today_${genre}.json").delete()


        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER TODAY")
        }

    }

    private fun getNaverChallenge(context : Context, genre: String) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setNaverChallengeGenre(genre)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            val books = ArrayList<BookListDataBest>()
            for (i in Naver.indices) {

                val info3 = Naver.select(".score_area")[i].text().replace("별점", "")
                val info4 = Naver[i].select(".num_total").next().first()!!.text().replace("조회 ", "")
                val info5 = Naver.select(".count")[i].text().replace("관심", "")

                NaverRef["writerName"] = Naver.select(".author")[i].text()
                NaverRef["subject"] = Naver.select(".tit")[i].text()
                NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
                NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
                    .replace("https://novel.naver.com/challenge/list?novelId=", "")
                NaverRef["info1"] = Naver[i].select(".num_total").first()!!.text()
                NaverRef["info2"] = ""
                NaverRef["info3"] = info3
                NaverRef["info4"] = info4
                NaverRef["info5"] = info5
                NaverRef["info6"] = ""
                NaverRef["number"] = i
                NaverRef["date"] = DBDate.DateMMDD()
                NaverRef["type"] = "Naver_Challenge"

                books.add(BestRef.setBookListDataBest(NaverRef))
                miningValue(NaverRef, i, "Naver_Challenge", genre)

            }

            val bestDaoToday = Room.databaseBuilder(
                context,
                DBBest::class.java,
                "Today_Naver_Challenge_${genre}"
            ).allowMainThreadQueries().build()

            val bestDaoWeek = Room.databaseBuilder(
                context,
                DBBest::class.java,
                "Week_Naver_Challenge_${genre}"
            ).allowMainThreadQueries().build()

            val bestDaoMonth = Room.databaseBuilder(
                context,
                DBBest::class.java,
                "Month_Naver_Challenge_${genre}"
            ).allowMainThreadQueries().build()

            bestDaoToday.bestDao().initAll()
            bestDaoWeek.bestDao().initAll()
            bestDaoMonth.bestDao().initAll()

            File(
                File("/storage/self/primary/MOAVARA"),
                "Today_Naver_Challenge_${genre}.json"
            ).delete()
            File(
                File("/storage/self/primary/MOAVARA"),
                "Week_Naver_Challenge_${genre}.json"
            ).delete()
            File(
                File("/storage/self/primary/MOAVARA"),
                "Month_Naver_Challenge_${genre}.json"
            ).delete()

        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER CHALLANGE")
        }
    }

    private fun getNaverBest(context: Context, genre: String) {
        try {

            val doc: Document =
                Jsoup.connect(Genre.setNaverGenre(genre)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            val books = ArrayList<BookListDataBest>()

            for (i in Naver.indices) {

                val info3 = Naver.select(".score_area")[i].text().replace("별점", "")
                val info4 = Naver[i].select(".num_total").next().first()!!.text().replace("조회 ", "")
                val info5 = Naver.select(".count")[i].text().replace("관심", "")

                NaverRef["writerName"] = Naver.select(".author")[i].text()
                NaverRef["subject"] = Naver.select(".tit")[i].text()
                NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
                NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
                    .replace("https://novel.naver.com/best/list?novelId=", "")
                NaverRef["info1"] = ""
                NaverRef["info2"] = Naver[i].select(".num_total").first()!!.text()
                NaverRef["info3"] = info3
                NaverRef["info4"] = info4
                NaverRef["info5"] = info5
                NaverRef["info6"] = ""
                NaverRef["number"] = i
                NaverRef["date"] = DBDate.DateMMDD()
                NaverRef["type"] = "Naver"

                books.add(BestRef.setBookListDataBest(NaverRef))
                miningValue(NaverRef, i, "Naver", genre)
            }

            val bestDaoToday = Room.databaseBuilder(
                context,
                DBBest::class.java,
                "Today_Naver_${genre}"
            ).allowMainThreadQueries().build()

            val bestDaoWeek = Room.databaseBuilder(
                context,
                DBBest::class.java,
                "Week_Naver_${genre}"
            ).allowMainThreadQueries().build()

            val bestDaoMonth = Room.databaseBuilder(
                context,
                DBBest::class.java,
                "Month_Naver_${genre}"
            ).allowMainThreadQueries().build()

            bestDaoToday.bestDao().initAll()
            bestDaoWeek.bestDao().initAll()
            bestDaoMonth.bestDao().initAll()

            File(File("/storage/self/primary/MOAVARA"), "Today_Naver_${genre}.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Week_Naver_${genre}.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Month_Naver_${genre}.json").delete()


        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER CHALLANGE")
        }
    }

    private fun getRidiBest(context : Context, genre: String) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setRidiGenre(genre)).post()
            val Ridi: Elements = doc.select(".book_thumbnail_wrapper")
            val RidiRef: MutableMap<String?, Any> = HashMap()

            val books = ArrayList<BookListDataBest>()

            for (i in Ridi.indices) {
                if (i > 0) {
                    val uri: Uri =
                        Uri.parse(Ridi.select("a")[i].absUrl("href"))

                    val info3 = doc.select("span .StarRate_ParticipantCount")[i].text()
                        .replace(",", "").replace("명", "")

                    val info4 = doc.select("span .StarRate_Score")[i].text().replace("점", "")
                        .replace(".", "")

                    RidiRef["writerName"] =
                        doc.select("div .author_detail_link")[i].text()
                    RidiRef["subject"] = doc.select("div .title_link")[i].text()
                    RidiRef["bookImg"] =
                        Ridi.select(".thumbnail_image .thumbnail")[i].absUrl("data-src")
                    RidiRef["bookCode"] = uri.path?.replace("/books/", "") ?: ""
                    RidiRef["info1"] = doc.select(".count_num")[i].text()
                    RidiRef["info2"] = ""
                    RidiRef["info3"] = info3
                    RidiRef["info4"] = info4
                    RidiRef["info5"] = ""
                    RidiRef["info6"] = ""
                    RidiRef["number"] = i
                    RidiRef["date"] = DBDate.DateMMDD()
                    RidiRef["type"] = "Ridi"

                    books.add(BestRef.setBookListDataBest(RidiRef))

                    miningValue(
                        RidiRef,
                        i -1,
                        "Ridi",
                        genre
                    )
                }
            }

            val bestDaoToday = Room.databaseBuilder(
                context,
                DBBest::class.java,
                "Today_Ridi_${genre}"
            ).allowMainThreadQueries().build()

            val bestDaoWeek = Room.databaseBuilder(
                context,
                DBBest::class.java,
                "Week_Ridi_${genre}"
            ).allowMainThreadQueries().build()

            val bestDaoMonth = Room.databaseBuilder(
                context,
                DBBest::class.java,
                "Month_Ridi_${genre}"
            ).allowMainThreadQueries().build()

            bestDaoToday.bestDao().initAll()
            bestDaoWeek.bestDao().initAll()
            bestDaoMonth.bestDao().initAll()

            File(File("/storage/self/primary/MOAVARA"), "Today_Ridi_${genre}.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Week_Ridi_${genre}.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Month_Ridi_${genre}.json").delete()

        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "RIDI")
        }
    }

    private fun getOneStoreBest(context : Context, genre: String) {
        try {
            val OneStoryRef: MutableMap<String?, Any> = HashMap()

            val apiOneStory = RetrofitOnestore()
            val param: MutableMap<String?, Any> = HashMap()
            param["menuId"] = Genre.setOneStoreGenre(genre)

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
                                OneStoryRef["info3"] = productList[i].totalCount
                                OneStoryRef["info4"] = productList[i].avgScore
                                OneStoryRef["info5"] = productList[i].commentCount
                                OneStoryRef["info6"] = ""
                                OneStoryRef["number"] = i
                                OneStoryRef["date"] = DBDate.DateMMDD()
                                OneStoryRef["type"] = "OneStore"

                                miningValue(
                                    OneStoryRef,
                                    i,
                                    "OneStore",
                                    genre
                                )

                            }
                        }

                        val bestDaoToday = Room.databaseBuilder(
                            context,
                            DBBest::class.java,
                            "Today_OneStore_${genre}"
                        ).allowMainThreadQueries().build()

                        val bestDaoWeek = Room.databaseBuilder(
                            context,
                            DBBest::class.java,
                            "Week_OneStore_${genre}"
                        ).allowMainThreadQueries().build()

                        val bestDaoMonth = Room.databaseBuilder(
                            context,
                            DBBest::class.java,
                            "Month_OneStore_${genre}"
                        ).allowMainThreadQueries().build()

                        bestDaoToday.bestDao().initAll()
                        bestDaoWeek.bestDao().initAll()
                        bestDaoMonth.bestDao().initAll()

                        File(
                            File("/storage/self/primary/MOAVARA"),
                            "Today_OneStore_${genre}.json"
                        ).delete()
                        File(
                            File("/storage/self/primary/MOAVARA"),
                            "Week_OneStore_${genre}.json"
                        ).delete()
                        File(
                            File("/storage/self/primary/MOAVARA"),
                            "Month_OneStore_${genre}.json"
                        ).delete()
                    }
                })
        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "ONESTORE")
        }
    }

    private fun getKakaoStageBest(context : Context, genre: String) {
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        param["adult"] = "false"
        param["dateRange"] = "YESTERDAY"
        param["genreIds"] = Genre.setKakaoStageGenre(genre)
        param["recentHours"] = "72"

        apiKakao.getBestKakaoStage(
            param,
            object : RetrofitDataListener<List<BestResultKakaoStageNovel>> {
                override fun onSuccess(data: List<BestResultKakaoStageNovel>) {

                    data.let {

                        val list = it
                        val books = ArrayList<BookListDataBest>()

                        for (i in list.indices) {
                            val novel = list[i].novel
                            KakaoRef["genre"] = list[i].novel?.subGenre?.name ?: ""
                            KakaoRef["keyword"] = ArrayList<String>()

                            KakaoRef["writerName"] = novel!!.nickname!!.name
                            KakaoRef["subject"] = novel.title
                            KakaoRef["bookImg"] = novel.thumbnail!!.url
                            KakaoRef["bookCode"] = novel.stageSeriesNumber
                            KakaoRef["info1"] = novel.synopsis
                            KakaoRef["info2"] = "총 ${novel.publishedEpisodeCount}화"
                            KakaoRef["info3"] = novel.viewCount
                            KakaoRef["info4"] = novel.visitorCount
                            KakaoRef["info5"] = novel.episodeLikeCount
                            KakaoRef["info6"] = novel.favoriteCount
                            KakaoRef["number"] = i
                            KakaoRef["date"] = DBDate.DateMMDD()
                            KakaoRef["type"] = "Kakao_Stage"

                            miningValue(KakaoRef, i, "Kakao_Stage", genre)

                            miningDataValue(
                                KakaoRef,
                                "Kakao_Stage",
                                genre
                            )

                            miningDataValue(
                                KakaoRef,
                                "Kakao_Stage",
                                genre
                            )

                            books.add(BestRef.setBookListDataBest(KakaoRef))
                        }

                        val bestDaoToday = Room.databaseBuilder(
                            context,
                            DBBest::class.java,
                            "Today_Kakao_Stage_${genre}"
                        ).allowMainThreadQueries().build()

                        val bestDaoWeek = Room.databaseBuilder(
                            context,
                            DBBest::class.java,
                            "Week_Kakao_Stage_${genre}"
                        ).allowMainThreadQueries().build()

                        val bestDaoMonth = Room.databaseBuilder(
                            context,
                            DBBest::class.java,
                            "Month_Kakao_Stage_${genre}"
                        ).allowMainThreadQueries().build()

                        bestDaoToday.bestDao().initAll()
                        bestDaoWeek.bestDao().initAll()
                        bestDaoMonth.bestDao().initAll()

                        File(
                            File("/storage/self/primary/MOAVARA"),
                            "Today_Kakao_Stage_${genre}.json"
                        ).delete()
                        File(
                            File("/storage/self/primary/MOAVARA"),
                            "Week_Kakao_Stage_${genre}.json"
                        ).delete()
                        File(
                            File("/storage/self/primary/MOAVARA"),
                            "Month_Kakao_Stage_${genre}.json"
                        ).delete()
                    }

                }
            })
    }

    private fun getKakaoBest(context : Context, genre: String) {
        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        param["category"] = "11"
        param["subcategory"] = "0"
        param["page"] = 1
        param["day"] = "2"
        param["bm"] = "A"

        apiKakao.getKakaoBest(
            param,
            object : RetrofitDataListener<BestResultKakao> {
                override fun onSuccess(data: BestResultKakao) {

                    val list = data.list

                    if (list != null) {
                        for (i in list.indices) {
                            KakaoRef["genre"] = list[i].sub_category_title
                            KakaoRef["keyword"] = ArrayList<String>()

                            KakaoRef["writerName"] = list[i].author
                            KakaoRef["subject"] = list[i].title
                            KakaoRef["bookImg"] =
                                "https://dn-img-page.kakao.com/download/resource?kid=" + list[i].image
                            KakaoRef["bookCode"] = list[i].series_id
                            KakaoRef["info1"] = list[i].description
                            KakaoRef["info2"] = list[i].caption
                            KakaoRef["info3"] = list[i].read_count
                            KakaoRef["info4"] = list[i].like_count
                            KakaoRef["info5"] = list[i].rating
                            KakaoRef["info6"] = list[i].comment_count
                            KakaoRef["number"] = i
                            KakaoRef["date"] = DBDate.DateMMDD()
                            KakaoRef["type"] = "Kakao"

                            miningValue(
                                KakaoRef,
                                i,
                                "Kakao",
                                genre
                            )

                            miningDataValue(
                                KakaoRef,
                                "Kakao",
                                genre
                            )
                        }
                    }

                    val bestDaoToday = Room.databaseBuilder(
                        context,
                        DBBest::class.java,
                        "Today_Kakao_${genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoWeek = Room.databaseBuilder(
                        context,
                        DBBest::class.java,
                        "Week_Kakao_${genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoMonth = Room.databaseBuilder(
                        context,
                        DBBest::class.java,
                        "Month_Kakao_${genre}"
                    ).allowMainThreadQueries().build()

                    bestDaoToday.bestDao().initAll()
                    bestDaoWeek.bestDao().initAll()
                    bestDaoMonth.bestDao().initAll()


                    File(
                        File("/storage/self/primary/MOAVARA"),
                        "Today_Kakao_${genre}.json"
                    ).delete()
                    File(File("/storage/self/primary/MOAVARA"), "Week_Kakao_${genre}.json").delete()
                    File(
                        File("/storage/self/primary/MOAVARA"),
                        "Month_Kakao_${genre}.json"
                    ).delete()
                }
            })
    }

    fun getJoaraBest(context: Context, genre: String) {
        val JoaraRef: MutableMap<String?, Any> = HashMap()
        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = 1
        param["best"] = "today"
        param["store"] = ""
        param["category"] = Genre.setJoaraGenre(genre)
        param["offset"] = "100"

        apiJoara.getJoaraBookBest(
            param,
            object : RetrofitDataListener<JoaraBestListResult> {
                override fun onSuccess(data: JoaraBestListResult) {

                    val books = data.bookLists

                    if (books != null) {

                        for (i in books.indices) {
                            JoaraRef["genre"] = books[i].category_ko_name
                            JoaraRef["keyword"] = books[i].keyword

                            JoaraRef["writerName"] = books[i].writerName
                            JoaraRef["subject"] = books[i].subject
                            JoaraRef["bookImg"] = books[i].bookImg.replace("http://", "https://")
                            JoaraRef["bookCode"] = books[i].bookCode
                            JoaraRef["info1"] = books[i].intro
                            JoaraRef["info2"] = "총 ${books[i].cntChapter}화"
                            JoaraRef["info3"] = books[i].cntPageRead
                            JoaraRef["info4"] = books[i].cntFavorite
                            JoaraRef["info5"] = books[i].cntRecom
                            JoaraRef["info6"] = books[i].cntTotalComment
                            JoaraRef["number"] = i
                            JoaraRef["date"] = DBDate.DateMMDD()
                            JoaraRef["type"] = "Joara"

                            miningValue(
                                JoaraRef,
                                i,
                                "Joara",
                                genre
                            )

                            miningDataValue(
                                JoaraRef,
                                "Joara",
                                genre
                            )
                        }
                    }

                    val bestDaoToday = Room.databaseBuilder(
                        context,
                        DBBest::class.java,
                        "Today_Joara_${genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoMonth = Room.databaseBuilder(
                        context,
                        DBBest::class.java,
                        "Month_Joara_${genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoWeek = Room.databaseBuilder(
                        context,
                        DBBest::class.java,
                        "Week_Joara_${genre}"
                    ).allowMainThreadQueries().build()

                    bestDaoToday.bestDao().initAll()
                    bestDaoMonth.bestDao().initAll()
                    bestDaoWeek.bestDao().initAll()


                    File(
                        File("/storage/self/primary/MOAVARA"),
                        "Today_Joara_${genre}.json"
                    ).delete()
                    File(File("/storage/self/primary/MOAVARA"), "Week_Joara_${genre}.json").delete()
                    File(
                        File("/storage/self/primary/MOAVARA"),
                        "Month_Joara_${genre}.json"
                    ).delete()
                }
            })
    }

    private fun getJoaraBestPremium(context: Context, genre: String) {

        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = 1
        param["best"] = "today"
        param["store"] = "premium"
        param["category"] = Genre.setJoaraGenre(genre)
        param["offset"] = "100"

        apiJoara.getJoaraBookBest(
            param,
            object : RetrofitDataListener<JoaraBestListResult> {
                override fun onSuccess(data: JoaraBestListResult) {

                    val books = data.bookLists

                    if (books != null) {
                        for (i in books.indices) {
                            JoaraRef["genre"] = books[i].category_ko_name
                            JoaraRef["keyword"] = books[i].keyword

                            JoaraRef["writerName"] = books[i].writerName
                            JoaraRef["subject"] = books[i].subject
                            JoaraRef["bookImg"] = books[i].bookImg.replace("http://", "https://")
                            JoaraRef["bookCode"] = books[i].bookCode
                            JoaraRef["info1"] = books[i].intro
                            JoaraRef["info2"] = "총 ${books[i].cntChapter}화"
                            JoaraRef["info3"] = books[i].cntPageRead
                            JoaraRef["info4"] = books[i].cntFavorite
                            JoaraRef["info5"] = books[i].cntRecom
                            JoaraRef["info6"] = books[i].cntTotalComment
                            JoaraRef["number"] = i
                            JoaraRef["date"] = DBDate.DateMMDD()
                            JoaraRef["type"] = "Joara_Premium"

                            miningValue(
                                JoaraRef,
                                i,
                                "Joara_Premium",
                                genre
                            )

                            miningDataValue(
                                JoaraRef,
                                "Joara_Premium",
                                genre
                            )
                        }
                    }

                    val bestDaoToday = Room.databaseBuilder(
                        context,
                        DBBest::class.java,
                        "Today_Joara_Premium_${genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoWeek = Room.databaseBuilder(
                        context,
                        DBBest::class.java,
                        "Week_Joara_Premium_${genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoMonth = Room.databaseBuilder(
                        context,
                        DBBest::class.java,
                        "Month_Joara_Premium_${genre}"
                    ).allowMainThreadQueries().build()

                    bestDaoToday.bestDao().initAll()
                    bestDaoWeek.bestDao().initAll()
                    bestDaoMonth.bestDao().initAll()

                    File(
                        File("/storage/self/primary/MOAVARA"),
                        "Today_Joara_Premium_${genre}.json"
                    ).delete()
                    File(
                        File("/storage/self/primary/MOAVARA"),
                        "Week_Joara_Premium_${genre}.json"
                    ).delete()
                    File(
                        File("/storage/self/primary/MOAVARA"),
                        "Month_Joara_Premium_${genre}.json"
                    ).delete()
                }
            })

    }

    private fun getJoaraBestNobless(context: Context, genre: String) {

        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = 1
        param["best"] = "today"
        param["store"] = "nobless"
        param["category"] = Genre.setJoaraGenre(genre)
        param["offset"] = "100"

        apiJoara.getJoaraBookBest(
            param,
            object : RetrofitDataListener<JoaraBestListResult> {
                override fun onSuccess(data: JoaraBestListResult) {

                    val books = data.bookLists

                    if (books != null) {
                        for (i in books.indices) {
                            JoaraRef["genre"] = books[i].category_ko_name
                            JoaraRef["keyword"] = books[i].keyword

                            JoaraRef["status"] = "NEW"
                            JoaraRef["writerName"] = books[i].writerName
                            JoaraRef["subject"] = books[i].subject
                            JoaraRef["bookImg"] = books[i].bookImg.replace("http://", "https://")
                            JoaraRef["bookCode"] = books[i].bookCode
                            JoaraRef["info1"] = books[i].intro
                            JoaraRef["info2"] = "총 ${books[i].cntChapter}화"
                            JoaraRef["info3"] = books[i].cntPageRead
                            JoaraRef["info4"] = books[i].cntFavorite
                            JoaraRef["info5"] = books[i].cntRecom
                            JoaraRef["info6"] = books[i].cntTotalComment
                            JoaraRef["number"] = i
                            JoaraRef["date"] = DBDate.DateMMDD()
                            JoaraRef["type"] = "Joara_Nobless"

                            miningValue(
                                JoaraRef,
                                i,
                                "Joara_Nobless",
                                genre
                            )

                            miningDataValue(
                                JoaraRef,
                                "Joara_Nobless",
                                genre
                            )
                        }
                    }

                    val bestDaoToday = Room.databaseBuilder(
                        context,
                        DBBest::class.java,
                        "Today_Joara_Nobless_${genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoWeek = Room.databaseBuilder(
                        context,
                        DBBest::class.java,
                        "Week_Joara_Nobless_${genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoMonth = Room.databaseBuilder(
                        context,
                        DBBest::class.java,
                        "Month_Joara_Nobless_${genre}"
                    ).allowMainThreadQueries().build()

                    bestDaoToday.bestDao().initAll()
                    bestDaoWeek.bestDao().initAll()
                    bestDaoMonth.bestDao().initAll()

                    File(
                        File("/storage/self/primary/MOAVARA"),
                        "Today_Joara_Nobless_${genre}.json"
                    ).delete()
                    File(
                        File("/storage/self/primary/MOAVARA"),
                        "Week_Joara_Nobless_${genre}.json"
                    ).delete()
                    File(
                        File("/storage/self/primary/MOAVARA"),
                        "Month_Joara_Nobless_${genre}.json"
                    ).delete()
                }
            })

    }

    private fun getMoonpiaBest(context : Context) {
        val MoonpiaRef: MutableMap<String?, Any> = HashMap()

        val apiMoonPia = RetrofitMoonPia()
        val param: MutableMap<String?, Any> = HashMap()

        param["section"] = "today"
        param["exclusive"] = ""
        param["outAdult"] = "true"
        param["offset"] = 25

        apiMoonPia.postMoonPiaBest(
            param,
            object : RetrofitDataListener<BestMoonpiaResult> {
                override fun onSuccess(data: BestMoonpiaResult) {

                    data.api?.items.let {

                        if (it != null) {
                            for (i in it.indices) {
                                MoonpiaRef["genre"] = it[i].nvGnMainTitle
                                MoonpiaRef["keyword"] = ArrayList<String>()

                                MoonpiaRef["writerName"] = it[i].author
                                MoonpiaRef["subject"] = it[i].nvTitle
                                MoonpiaRef["bookImg"] =
                                    "https://cdn1.munpia.com${it[i].nvCover}"
                                MoonpiaRef["bookCode"] = it[i].nvSrl
                                MoonpiaRef["info1"] = it[i].nvStory
                                MoonpiaRef["info2"] = it[i].nvGnMainTitle
                                MoonpiaRef["info3"] = it[i].nsrData?.hit!!
                                MoonpiaRef["info4"] = it[i].nsrData?.number!!
                                MoonpiaRef["info5"] = it[i].nsrData?.prefer!!
                                MoonpiaRef["info6"] = it[i].nsrData?.hour!!
                                MoonpiaRef["number"] = i
                                MoonpiaRef["date"] = DBDate.DateMMDD()
                                MoonpiaRef["type"] = "Munpia"

                                miningValue(
                                    MoonpiaRef,
                                    i,
                                    "Munpia",
                                    ""
                                )

                                miningDataValue(
                                    MoonpiaRef,
                                    "Munpia",
                                    ""
                                )
                            }

                            val bestDaoToday = Room.databaseBuilder(
                                context,
                                DBBest::class.java,
                                "Today_Munpia"
                            ).allowMainThreadQueries().build()

                            val bestDaoWeek = Room.databaseBuilder(
                                context,
                                DBBest::class.java,
                                "Week_Munpia"
                            ).allowMainThreadQueries().build()

                            val bestDaoMonth = Room.databaseBuilder(
                                context,
                                DBBest::class.java,
                                "Month_Munpia"
                            ).allowMainThreadQueries().build()

                            bestDaoToday.bestDao().initAll()
                            bestDaoWeek.bestDao().initAll()
                            bestDaoMonth.bestDao().initAll()

                            File(
                                File("/storage/self/primary/MOAVARA"),
                                "Today_Munpia.json"
                            ).delete()
                            File(File("/storage/self/primary/MOAVARA"), "Week_Munpia.json").delete()
                            File(
                                File("/storage/self/primary/MOAVARA"),
                                "Month_Munpia.json"
                            ).delete()
                        }
                    }

                }
            })
    }

    private fun getToksodaBest(context : Context, genre: String) {
        val ToksodaRef: MutableMap<String?, Any> = HashMap()

        val apiToksoda = RetrofitToksoda()
        val param: MutableMap<String?, Any> = HashMap()

        param["page"] = 1
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

                    data.resultList?.let { it ->
                        for (i in it.indices) {
                            ToksodaRef["genre"] = it[i].lgctgrNm
                            ToksodaRef["keyword"] = ArrayList<String>()

                            ToksodaRef["writerName"] = it[i].athrnm
                            ToksodaRef["subject"] = it[i].wrknm
                            ToksodaRef["bookImg"] = "https:${it[i].imgPath}"
                            ToksodaRef["bookCode"] = it[i].brcd
                            ToksodaRef["info1"] = it[i].lnIntro
                            ToksodaRef["info2"] = "총 ${it[i].whlEpsdCnt}화"
                            ToksodaRef["info3"] = it[i].inqrCnt
                            ToksodaRef["info4"] = it[i].goodAllCnt
                            ToksodaRef["info5"] = it[i].intrstCnt
                            ToksodaRef["info6"] = ""
                            ToksodaRef["number"] = i
                            ToksodaRef["date"] = DBDate.DateMMDD()
                            ToksodaRef["type"] = "Toksoda"

                            miningValue(
                                ToksodaRef,
                                i,
                                "Toksoda",
                                genre
                            )

                            miningDataValue(
                                ToksodaRef,
                                "Toksoda",
                                genre
                            )
                        }

                        val bestDaoToday = Room.databaseBuilder(
                            context,
                            DBBest::class.java,
                            "Today_Toksoda_${genre}"
                        ).allowMainThreadQueries().build()

                        val bestDaoWeek = Room.databaseBuilder(
                            context,
                            DBBest::class.java,
                            "Week_Toksoda_${genre}"
                        ).allowMainThreadQueries().build()

                        val bestDaoMonth = Room.databaseBuilder(
                            context,
                            DBBest::class.java,
                            "Month_Toksoda_${genre}"
                        ).allowMainThreadQueries().build()

                        bestDaoToday.bestDao().initAll()
                        bestDaoWeek.bestDao().initAll()
                        bestDaoMonth.bestDao().initAll()

                        File(
                            File("/storage/self/primary/MOAVARA"),
                            "Today_Toksoda_${genre}.json"
                        ).delete()
                        File(
                            File("/storage/self/primary/MOAVARA"),
                            "Week_Toksoda_${genre}.json"
                        ).delete()
                        File(
                            File("/storage/self/primary/MOAVARA"),
                            "Month_Toksoda_${genre}.json"
                        ).delete()
                    }
                }
            })
    }

    fun getMyPickMining(context: Context) {
        val UID = context.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
            ?.getString("UID", "").toString()

        mRootRef.child("User").child(UID).child("Novel").child("book")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (pickedItem in dataSnapshot.children) {
                        val group: BookListDataBest? =
                            pickedItem.getValue(BookListDataBest::class.java)

                        if (group != null) {
                            when (group.type) {
                                "Joara", "Joara_Nobless", "Joara_Premium" -> {
                                    getPickJoara(context, group.bookCode, UID)
                                }
                                "Naver_Today", "Naver_Challenge", "Naver" -> {
                                    getPickNaver(group.bookCode, UID)
                                }
                                "Kakao" -> {
                                    getPickKakao(group.bookCode, UID)
                                }
                                "Kakao_Stage" -> {
                                    getPickKakaoStage(group.bookCode, UID)
                                }
                                "Ridi" -> {
                                    getPickRidi(group.bookCode, UID)
                                }
                                "OneStore" -> {
                                    getPickOneStory(group.bookCode, UID)
                                }
                                "Munpia" -> {
                                    getPickMunpia(group.bookCode, UID)
                                }
                                "Toksoda" -> {
                                    getPickToksoda(group.bookCode, UID)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun getPickJoara(context: Context, bookCode: String, UID: String) {
        val apiJoara = RetrofitJoara()
        val JoaraRef = Param.getItemAPI(context)
        JoaraRef["book_code"] = bookCode
        JoaraRef["category"] = "1"

        var pickBookCodeItem: BookListDataBestAnalyze

        apiJoara.getBookDetailJoa(
            JoaraRef,
            object : RetrofitDataListener<JoaraBestDetailResult> {
                override fun onSuccess(data: JoaraBestDetailResult) {

                    if (data.status == "1" && data.book != null) {

                        pickBookCodeItem = BookListDataBestAnalyze(
                            data.book.cntPageRead,
                            data.book.cntFavorite,
                            data.book.cntRecom,
                            data.book.cntTotalComment,
                            999,
                            DBDate.DateMMDD(),
                            0,
                            0,
                        )

                        mRootRef.child("User").child(UID).child("Novel").child("bookCode")
                            .child(bookCode).child(DBDate.DateMMDD()).setValue(pickBookCodeItem)
                    }
                }
            })
    }

    fun getPickNaver(bookCode: String, UID: String) {
        Thread {
            val pickBookCodeItem: BookListDataBestAnalyze

            val doc: Document =
                Jsoup.connect("https://novel.naver.com/webnovel/list?novelId=${bookCode}").post()

            pickBookCodeItem = BookListDataBestAnalyze(
                doc.select(".info_book .like").text().replace("관심", "").replace("명", ""),
                doc.select(".grade_area em").text(),
                doc.select(".info_book .download").text().replace("다운로드", ""),
                "",
                999,
                DBDate.DateMMDD(),
                0,
                0,
            )

            mRootRef.child("User").child(UID).child("Novel").child("bookCode").child(bookCode)
                .child(DBDate.DateMMDD()).setValue(pickBookCodeItem)
        }.start()
    }

    fun getPickKakao(bookCode: String, UID: String) {
        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()
        var pickBookCodeItem = BookListDataBestAnalyze()

        param["seriesid"] = bookCode

        apiKakao.postKakaoBookDetail(
            param,
            object : RetrofitDataListener<BestKakaoBookDetail> {
                override fun onSuccess(data: BestKakaoBookDetail) {


                    data.home?.let { it ->

                        pickBookCodeItem = BookListDataBestAnalyze(
                            it.page_rating_count,
                            it.page_rating_summary.replace(".0", ""),
                            it.read_count,
                            it.page_comment_count,
                            999,
                            DBDate.DateMMDD(),
                            0,
                            0,
                        )
                    }

                    mRootRef.child("User").child(UID).child("Novel").child("bookCode")
                        .child(bookCode).child(DBDate.DateMMDD()).setValue(pickBookCodeItem)

                }
            })
    }

    fun getPickKakaoStage(bookCode: String, UID: String) {
        val apiKakaoStage = RetrofitKaKao()
        var pickBookCodeItem: BookListDataBestAnalyze

        apiKakaoStage.getBestKakaoStageDetail(
            bookCode,
            object : RetrofitDataListener<KakaoStageBestBookResult> {
                override fun onSuccess(data: KakaoStageBestBookResult) {

                    data.let {
                        pickBookCodeItem = BookListDataBestAnalyze(
                            it.favoriteCount,
                            it.viewCount,
                            it.visitorCount,
                            it.episodeLikeCount,
                            999,
                            DBDate.DateMMDD(),
                            0,
                            0,
                        )

                        mRootRef.child("User").child(UID).child("Novel").child("bookCode")
                            .child(bookCode).child(DBDate.DateMMDD()).setValue(pickBookCodeItem)
                    }
                }
            })
    }

    fun getPickRidi(bookCode: String, UID: String) {
        Thread {
            val pickBookCodeItem: BookListDataBestAnalyze
            val doc: Document = Jsoup.connect("https://ridibooks.com/books/${bookCode}").get()

            pickBookCodeItem = BookListDataBestAnalyze(
                doc.select(".metadata_writer .author_detail_link").text(),
                doc.select(".header_info_wrap .StarRate_ParticipantCount").text(),
                "",
                "",
                999,
                DBDate.DateMMDD(),
                0,
                0,
            )

            mRootRef.child("User").child(UID).child("Novel").child("bookCode")
                .child(bookCode).child(DBDate.DateMMDD()).setValue(pickBookCodeItem)

        }.start()
    }

    fun getPickOneStory(bookCode: String, UID: String) {
        val apiOnestory = RetrofitOnestore()
        val param: MutableMap<String?, Any> = HashMap()
        var pickBookCodeItem: BookListDataBestAnalyze

        param["channelId"] = bookCode
        param["bookpassYn"] = "N"

        apiOnestory.getOneStoreDetail(
            bookCode,
            param,
            object : RetrofitDataListener<OnestoreBookDetail> {
                override fun onSuccess(data: OnestoreBookDetail) {

                    data.params.let {

                        pickBookCodeItem = BookListDataBestAnalyze(
                            it?.ratingAvgScore ?: "",
                            it?.favoriteCount ?: "",
                            it?.pageViewTotal ?: "",
                            it?.commentCount ?: "",
                            999,
                            DBDate.DateMMDD(),
                            0,
                            0,
                        )

                        mRootRef.child("User").child(UID).child("Novel").child("bookCode")
                            .child(bookCode).child(DBDate.DateMMDD()).setValue(pickBookCodeItem)
                    }
                }
            })
    }

    fun getPickMunpia(bookCode: String, UID: String) {
        Thread {
            val doc: Document = Jsoup.connect("https://novel.munpia.com/${bookCode}").get()

            val pickBookCodeItem = BookListDataBestAnalyze(
                doc.select(".meta-etc dd").next().next()[1]?.text() ?: "",
                doc.select(".meta-etc dd").next().next()[2]?.text() ?: "",
                "",
                "",
                999,
                DBDate.DateMMDD(),
                0,
                0,
            )

            mRootRef.child("User").child(UID).child("Novel").child("bookCode")
                .child(bookCode).child(DBDate.DateMMDD()).setValue(pickBookCodeItem)


        }.start()
    }

    fun getPickToksoda(bookCode: String, UID: String) {
        val apiToksoda = RetrofitToksoda()
        val param: MutableMap<String?, Any> = HashMap()

        param["brcd"] = bookCode
        param["_"] = "1657265744728"
        var pickBookCodeItem: BookListDataBestAnalyze

        apiToksoda.getBestDetail(
            param,
            object : RetrofitDataListener<BestToksodaDetailResult> {
                override fun onSuccess(data: BestToksodaDetailResult) {

                    data.result?.let {

                        pickBookCodeItem = BookListDataBestAnalyze(
                            it.inqrCnt,
                            it.goodCnt,
                            it.intrstCnt,
                            "",
                            999,
                            DBDate.DateMMDD(),
                            0,
                            0,
                        )

                        mRootRef.child("User").child(UID).child("Novel").child("bookCode")
                            .child(bookCode).child(DBDate.DateMMDD()).setValue(pickBookCodeItem)
                    }
                }
            })
    }
}

