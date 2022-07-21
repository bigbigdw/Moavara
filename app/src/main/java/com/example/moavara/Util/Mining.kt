package com.example.moavara.Util

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.DataBase.BookListDataBestToday
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
import java.net.SocketTimeoutException
import java.util.*

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

        val Ridi = Thread {
            for (i in 1..3) {
                getRidiBest(genre, i)
            }
        }

        val OneStore = Thread {
            for (i in 1..4) {
                getOneStoreBest(genre, i, context)
            }
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

        val NaverChallenge1 = Thread {
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

        val Toksoda1 = Thread {
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

//            NaverToday.start()
//            NaverToday.join()
//            Log.d("####MINING", "네이버 투데이1 완료")
//
//            NaverChallenge1.start()
//            NaverChallenge1.join()
//            Log.d("####MINING", "네이버 챌린지1 완료")
//
//            NaverBest.start()
//            NaverBest.join()
//            Log.d("####MINING", "네이버1 완료")
//
//            Moonpia.start()
//            Moonpia.join()
//            Log.d("####MINING", "문피아 완료")
//
//            Toksoda1.start()
//            Toksoda1.join()
//            Log.d("####MINING", "톡소다1 완료")
//
//            MrBlue.start()
//            MrBlue.join()
//            Log.d("####MINING", "미스터 블루 완료")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun getMrBlueBest(cate: String, context: Context) {
        Thread {

            try {
                val doc: Document =
                    Jsoup.connect(Genre.setMrBlueGenre(cate)).post()
                val MrBlue: Elements = doc.select(".list-box ul li")
                val MrBlueRef: MutableMap<String?, Any> = HashMap()

                BestRef.setBestRefWeekList("MrBlue", cate).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        for (i in MrBlue.indices) {

                            val title = MrBlue.select(".tit > a")[i].attr("title")
                            val dataList = ArrayList<BookListDataBestAnalyze>()

                            for (weekItem in dataSnapshot.children) {
                                val group: BookListDataBestToday? =
                                    weekItem.getValue(BookListDataBestToday::class.java)
                                if (group != null) {
                                    if (group.title == title) {
                                        dataList.add(
                                            BookListDataBestAnalyze(
                                                group.info3,
                                                group.info4,
                                                group.info5,
                                                group.number,
                                                group.date,
                                            )
                                        )
                                    }
                                }
                            }

                            val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                            Collections.sort(dataList, cmpAsc)

                            if (dataList.size != 0) {
                                val today = DBDate.getToday(dataList[dataList.size - 1].date)
                                val yesterday =
                                    DBDate.getYesterday(dataList[dataList.size - 1].date)

                                if ((today - yesterday) == 1) {
                                    MrBlueRef["numberDiff"] =
                                        calculateNumDiff(i, dataList[dataList.size - 1].number).num
                                    MrBlueRef["status"] = calculateNumDiff(
                                        i,
                                        dataList[dataList.size - 1].number
                                    ).status
                                } else {
                                    MrBlueRef["numberDiff"] = 0
                                    MrBlueRef["status"] = "NEW"
                                }
                                MrBlueRef["trophyCount"] = dataList.size
                            } else {
                                MrBlueRef["numberDiff"] = 0
                                MrBlueRef["status"] = "NEW"
                                MrBlueRef["trophyCount"] = 0
                            }

                            MrBlueRef["writerName"] =
                                MrBlue.select(".txt-box .name > a")[i].attr("title")
                            MrBlueRef["subject"] = title
                            MrBlueRef["bookImg"] = MrBlue.select(".img img")[i].absUrl("src")
                            MrBlueRef["bookCode"] = MrBlue.select(".txt-box a")[i].absUrl("href")
                            MrBlueRef["info1"] =
                                MrBlue.select(".txt-box .name > a")[i].absUrl("href")
                            MrBlueRef["info2"] = " "
                            MrBlueRef["info3"] = " "
                            MrBlueRef["info4"] = " "
                            MrBlueRef["info5"] = " "
                            MrBlueRef["number"] = i
                            MrBlueRef["date"] = DBDate.DateMMDD()
                            MrBlueRef["data"] = dataList
                            MrBlueRef["type"] = "MrBlue"

                            miningValue(MrBlueRef, i, "MrBlue", cate)

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })


            } catch (exception: SocketTimeoutException) {
                Log.d("EXCEPTION", "MRBLUE")
            }
        }.start()

    }

    fun getNaverToday(cate: String, context: Context) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setNaverTodayGenre(cate)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            BestRef.setBestRefWeekList("Naver_Today", cate)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        for (i in Naver.indices) {
                            val title = Naver.select(".tit")[i].text()
                            val dataList = ArrayList<BookListDataBestAnalyze>()

                            for (weekItem in dataSnapshot.children) {
                                val group: BookListDataBestToday? =
                                    weekItem.getValue(BookListDataBestToday::class.java)
                                if (group != null) {
                                    if (group.title == title) {
                                        dataList.add(
                                            BookListDataBestAnalyze(
                                                group.info3,
                                                group.info4,
                                                group.info5,
                                                group.number,
                                                group.date,
                                            )
                                        )
                                    }
                                }
                            }

                            val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                            Collections.sort(dataList, cmpAsc)

                            NaverRef["trophyCount"] = dataList.size

                            NaverRef["writerName"] = Naver.select(".author")[i].text()
                            NaverRef["subject"] = title
                            NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
                            NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
                            NaverRef["info1"] = Naver[i].select(".num_total").first()!!.text()
                            NaverRef["info2"] =
                                Naver[i].select(".num_total").next().first()!!.text()
                            NaverRef["info3"] = Naver.select(".count")[i].text()
                            NaverRef["info4"] = Naver.select(".score_area")[i].text()
                            NaverRef["info5"] = ""
                            NaverRef["number"] = i

                            if (dataList.size != 0) {
                                val today = DBDate.getToday(dataList[dataList.size - 1].date)
                                val yesterday =
                                    DBDate.getYesterday(dataList[dataList.size - 1].date)

                                if ((today - yesterday) == 1) {
                                    NaverRef["numberDiff"] =
                                        calculateNumDiff(i, dataList[dataList.size - 1].number).num
                                    NaverRef["status"] = calculateNumDiff(
                                        i,
                                        dataList[dataList.size - 1].number
                                    ).status
                                } else {
                                    NaverRef["numberDiff"] = 0
                                    NaverRef["status"] = "NEW"
                                }
                            } else {
                                NaverRef["numberDiff"] = 0
                                NaverRef["status"] = "NEW"
                            }

                            NaverRef["date"] = DBDate.DateMMDD()
                            NaverRef["type"] = "Naver_Today"
                            NaverRef["data"] = dataList

                            miningValue(NaverRef, i, "Naver_Today", cate)

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })


        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER TODAY")
        }

    }

    fun getNaverChallenge(cate: String, context: Context) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setNaverChallengeGenre(cate)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            BestRef.setBestRefWeekList("Naver_Challenge", cate)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        for (i in Naver.indices) {

                            val title = Naver.select(".tit")[i].text()
                            val dataList = ArrayList<BookListDataBestAnalyze>()

                            for (weekItem in dataSnapshot.children) {
                                val group: BookListDataBestToday? =
                                    weekItem.getValue(BookListDataBestToday::class.java)
                                if (group != null) {
                                    if (group.title == title) {
                                        dataList.add(
                                            BookListDataBestAnalyze(
                                                group.info3,
                                                group.info4,
                                                group.info5,
                                                group.number,
                                                group.date,
                                            )
                                        )
                                    }
                                }
                            }

                            val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                            Collections.sort(dataList, cmpAsc)

                            if (dataList.size != 0) {
                                val today = DBDate.getToday(dataList[dataList.size - 1].date)
                                val yesterday =
                                    DBDate.getYesterday(dataList[dataList.size - 1].date)

                                if ((today - yesterday) == 1) {
                                    NaverRef["numberDiff"] =
                                        calculateNumDiff(i, dataList[dataList.size - 1].number).num
                                    NaverRef["status"] = calculateNumDiff(
                                        i,
                                        dataList[dataList.size - 1].number
                                    ).status
                                } else {
                                    NaverRef["numberDiff"] = 0
                                    NaverRef["status"] = "NEW"
                                }
                            } else {
                                NaverRef["numberDiff"] = 0
                                NaverRef["status"] = "NEW"
                            }

                            NaverRef["trophyCount"] = dataList.size

                            NaverRef["writerName"] = Naver.select(".author")[i].text()
                            NaverRef["subject"] = title
                            NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
                            NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
                            NaverRef["info1"] = Naver[i].select(".num_total").first()!!.text()
                            NaverRef["info2"] =
                                Naver[i].select(".num_total").next().first()!!.text()
                            NaverRef["info3"] = Naver.select(".count")[i].text()
                            NaverRef["info4"] = Naver.select(".score_area")[i].text()
                            NaverRef["info5"] = ""
                            NaverRef["number"] = i
                            NaverRef["date"] = DBDate.DateMMDD()
                            NaverRef["type"] = "Naver_Challenge"
                            NaverRef["data"] = dataList

                            miningValue(NaverRef, i, "Naver_Challenge", cate)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })


        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER CHALLANGE")
        }
    }

    fun getNaverBest(cate: String, context: Context) {
        try {

            val doc: Document =
                Jsoup.connect(Genre.setNaverGenre(cate)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            BestRef.setBestRefWeekList("Naver", cate).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (i in Naver.indices) {

                        val title = Naver.select(".tit")[i].text()
                        val dataList = ArrayList<BookListDataBestAnalyze>()

                        for (weekItem in dataSnapshot.children) {
                            val group: BookListDataBestToday? =
                                weekItem.getValue(BookListDataBestToday::class.java)
                            if (group!!.title == title) {
                                dataList.add(
                                    BookListDataBestAnalyze(
                                        group.info3,
                                        group.info4,
                                        group.info5,
                                        group.number,
                                        group.date,
                                    )
                                )
                            }
                        }

                        val cmpAsc: Comparator<BookListDataBestAnalyze> =
                            Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                        Collections.sort(dataList, cmpAsc)

                        if (dataList.size != 0) {
                            val today = DBDate.getToday(dataList[dataList.size - 1].date)
                            val yesterday = DBDate.getYesterday(dataList[dataList.size - 1].date)

                            if ((today - yesterday) == 1) {
                                NaverRef["numberDiff"] =
                                    calculateNumDiff(i, dataList[dataList.size - 1].number).num
                                NaverRef["status"] =
                                    calculateNumDiff(i, dataList[dataList.size - 1].number).status
                            } else {
                                NaverRef["numberDiff"] = 0
                                NaverRef["status"] = "NEW"
                            }
                        } else {
                            NaverRef["numberDiff"] = 0
                            NaverRef["status"] = "NEW"
                        }

                        NaverRef["trophyCount"] = dataList.size

                        NaverRef["writerName"] = Naver.select(".author")[i].text()
                        NaverRef["subject"] = title
                        NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
                        NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
                        NaverRef["info1"] = Naver[i].select(".num_total").first()!!.text()
                        NaverRef["info2"] =
                            Naver[i].select(".num_total").next().first()!!.text()
                        NaverRef["info3"] = Naver.select(".count")[i].text()
                        NaverRef["info4"] = Naver.select(".score_area")[i].text()
                        NaverRef["info5"] = ""
                        NaverRef["number"] = i
                        NaverRef["date"] = DBDate.DateMMDD()
                        NaverRef["type"] = "Naver"
                        NaverRef["data"] = dataList

                        miningValue(NaverRef, i, "Naver", cate)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })


        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER CHALLANGE")
        }
    }

    fun getRidiBest(cate: String, page: Int) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setRidiGenre(cate, page)).post()
            val Ridi: Elements = doc.select(".book_thumbnail_wrapper")
            val RidiRef: MutableMap<String?, Any> = HashMap()

            for (i in Ridi.indices) {
                if (i > 0) {

                    RidiRef["writerName"] =
                        doc.select("div .author_detail_link")[i].text()
                    RidiRef["subject"] = doc.select("div .title_link")[i].text()
                    RidiRef["bookImg"] =
                        Ridi.select(".thumbnail_image .thumbnail")[i].absUrl("data-src")
                    RidiRef["bookCode"] = Ridi.select("a")[i].absUrl("href")
                    RidiRef["info1"] = doc.select(".count_num")[i].text()
                    RidiRef["info2"] =
                        "추천 수 : " + doc.select("span .StarRate_ParticipantCount")[i].text()
                    RidiRef["info3"] =
                        "평점 : " + doc.select("span .StarRate_Score")[i].text()
                    RidiRef["info4"] = ""
                    RidiRef["info5"] = ""
                    RidiRef["number"] = i + ((page - 1) * (Ridi.size - 1))
                    RidiRef["date"] = DBDate.DateMMDD()
                    RidiRef["type"] = "Ridi"

                    miningValue(RidiRef, (i + ((page - 1) * Ridi.size)), "Ridi", cate)
                }
            }

        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "RIDI")
        }
    }

    fun getOneStoreBest(cate: String, page: Int, context: Context) {
        try {
            val OneStoryRef: MutableMap<String?, Any> = HashMap()

            val apiOneStory = RetrofitOnestore()
            val param: MutableMap<String?, Any> = HashMap()

            param["menuId"] = Genre.setOneStoreGenre(cate)
            if (page == 1) {
                param["startKey"] = "0/0"
            } else if (page == 2) {
                param["startKey"] = "57/0"
            } else if (page == 3) {
                param["startKey"] = "120/0"
            }

            apiOneStory.getBestOneStore(
                param,
                object : RetrofitDataListener<OneStoreBookResult> {
                    override fun onSuccess(it: OneStoreBookResult) {

                        val productList = it.params?.productList

                        ArrayList<BookListDataBestToday>()

                        BestRef.setBestRefWeekList("OneStore", cate)
                            .addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {

                                    for (i in productList!!.indices) {

                                        val dataList = ArrayList<BookListDataBestAnalyze>()

                                        for (weekItem in dataSnapshot.children) {
                                            val group: BookListDataBestToday? =
                                                weekItem.getValue(BookListDataBestToday::class.java)
                                            if (group!!.title == productList[i].prodNm) {
                                                dataList.add(
                                                    BookListDataBestAnalyze(
                                                        group.info3,
                                                        group.info4,
                                                        group.info5,
                                                        group.number,
                                                        group.date,
                                                    )
                                                )
                                            }
                                        }

                                        val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                            Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                        Collections.sort(dataList, cmpAsc)

                                        if (dataList.size != 0) {
                                            val today =
                                                DBDate.getToday(dataList[dataList.size - 1].date)
                                            val yesterday =
                                                DBDate.getYesterday(dataList[dataList.size - 1].date)

                                            if ((today - yesterday) == 1) {
                                                OneStoryRef["numberDiff"] = calculateNumDiff(
                                                    i,
                                                    dataList[dataList.size - 1].number
                                                ).num
                                                OneStoryRef["status"] = calculateNumDiff(
                                                    i,
                                                    dataList[dataList.size - 1].number
                                                ).status
                                            } else {
                                                OneStoryRef["numberDiff"] = 0
                                                OneStoryRef["status"] = "NEW"
                                            }
                                        } else {
                                            OneStoryRef["numberDiff"] = 0
                                            OneStoryRef["status"] = "NEW"
                                        }

                                        OneStoryRef["trophyCount"] = dataList.size

                                        OneStoryRef["writerName"] = productList[i].artistNm
                                        OneStoryRef["subject"] = productList[i].prodNm
                                        OneStoryRef["bookImg"] =
                                            "https://img.onestore.co.kr/thumbnails/img_sac/224_320_F10_95/" + productList[i].thumbnailImageUrl
                                        OneStoryRef["bookCode"] = productList[i].prodId
                                        OneStoryRef["info1"] =
                                            "조회 수 : " + productList[i].totalCount
                                        OneStoryRef["info2"] = "평점 : " + productList[i].avgScore
                                        OneStoryRef["info3"] =
                                            "댓글 수 : " + productList[i].commentCount
                                        OneStoryRef["info4"] = " "
                                        OneStoryRef["info5"] = " "
                                        OneStoryRef["number"] = i
                                        OneStoryRef["date"] = DBDate.DateMMDD()

                                        OneStoryRef["type"] = "OneStore"
                                        OneStoryRef["data"] = dataList

                                        miningValue(
                                            OneStoryRef,
                                            (i + ((page - 1) * productList.size)),
                                            "OneStore",
                                            cate
                                        )

                                    }


                                }

                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                    }
                })
        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "ONESTORE")
        }
    }

    fun getKakaoStageBest(cate: String, context: Context) {
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        param["adult"] = "false"
        param["dateRange"] = "YESTERDAY"
        param["genreIds"] = Genre.setKakaoStageGenre(cate)
        param["recentHours"] = "72"

        apiKakao.getBestKakaoStage(
            param,
            object : RetrofitDataListener<List<BestResultKakaoStageNovel>> {
                override fun onSuccess(data: List<BestResultKakaoStageNovel>) {

                    data.let {

                        val list = it

                        BestRef.setBestRefWeekList("Kakao_Stage", cate)
                            .addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {

                                    for (i in list.indices) {
                                        val novel = list[i].novel
                                        val dataList = ArrayList<BookListDataBestAnalyze>()

                                        for (weekItem in dataSnapshot.children) {
                                            val group: BookListDataBestToday? =
                                                weekItem.getValue(BookListDataBestToday::class.java)
                                            if (group!!.title == novel?.title) {
                                                dataList.add(
                                                    BookListDataBestAnalyze(
                                                        group.info3,
                                                        group.info4,
                                                        group.info5,
                                                        group.number,
                                                        group.date,
                                                    )
                                                )
                                            }
                                        }

                                        val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                            Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                        Collections.sort(dataList, cmpAsc)

                                        KakaoRef["trophyCount"] = dataList.size

                                        if (dataList.size != 0) {
                                            val today =
                                                DBDate.getToday(dataList[dataList.size - 1].date)
                                            val yesterday =
                                                DBDate.getYesterday(dataList[dataList.size - 1].date)

                                            if ((today - yesterday) == 1) {
                                                KakaoRef["numberDiff"] = calculateNumDiff(
                                                    i,
                                                    dataList[dataList.size - 1].number
                                                ).num
                                                KakaoRef["status"] = calculateNumDiff(
                                                    i,
                                                    dataList[dataList.size - 1].number
                                                ).status
                                            } else {
                                                KakaoRef["numberDiff"] = 0
                                                KakaoRef["status"] = "NEW"
                                            }
                                        } else {
                                            KakaoRef["numberDiff"] = 0
                                            KakaoRef["status"] = "NEW"
                                        }

                                        KakaoRef["writerName"] = novel!!.nickname!!.name
                                        KakaoRef["subject"] = novel.title
                                        KakaoRef["bookImg"] = novel.thumbnail!!.url
                                        KakaoRef["bookCode"] = novel.stageSeriesNumber
                                        KakaoRef["info1"] = "줄거리 : " + novel.synopsis
                                        KakaoRef["info2"] = "총" + novel.publishedEpisodeCount + " 화"
                                        KakaoRef["info3"] = "조회 수 : " + novel.viewCount
                                        KakaoRef["info4"] = "선호작 수 : " + novel.visitorCount
                                        KakaoRef["info5"] = ""
                                        KakaoRef["number"] = i
                                        KakaoRef["date"] = DBDate.DateMMDD()
                                        KakaoRef["type"] = "Kakao_Stage"
                                        KakaoRef["data"] = dataList

                                        miningValue(KakaoRef, i, "Kakao_Stage", cate)
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {}
                            })

                    }

                }
            })
    }

    fun getKakaoBest(cate: String, page: Int, context: Context) {
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
                override fun onSuccess(it: BestResultKakao) {

                    val list = it.list

                    BestRef.setBestRefWeekList("Kakao", cate)
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                for (i in list!!.indices) {

                                    val dataList = ArrayList<BookListDataBestAnalyze>()

                                    for (weekItem in dataSnapshot.children) {
                                        val group: BookListDataBestToday? =
                                            weekItem.getValue(BookListDataBestToday::class.java)
                                        if (group!!.title == list[i].title) {
                                            dataList.add(
                                                BookListDataBestAnalyze(
                                                    group.info3,
                                                    group.info4,
                                                    group.info5,
                                                    group.number,
                                                    group.date,
                                                )
                                            )
                                        }
                                    }

                                    val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                        Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                    Collections.sort(dataList, cmpAsc)

                                    KakaoRef["trophyCount"] = dataList.size

                                    if (dataList.size != 0) {
                                        val today =
                                            DBDate.getToday(dataList[dataList.size - 1].date)
                                        val yesterday =
                                            DBDate.getYesterday(dataList[dataList.size - 1].date)

                                        if ((today - yesterday) == 1) {
                                            KakaoRef["numberDiff"] = calculateNumDiff(
                                                i,
                                                dataList[dataList.size - 1].number
                                            ).num
                                            KakaoRef["status"] = calculateNumDiff(
                                                i,
                                                dataList[dataList.size - 1].number
                                            ).status
                                        } else {
                                            KakaoRef["numberDiff"] = 0
                                            KakaoRef["status"] = "NEW"
                                        }
                                    } else {
                                        KakaoRef["numberDiff"] = 0
                                        KakaoRef["status"] = "NEW"
                                    }

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
                                    KakaoRef["number"] = i
                                    KakaoRef["date"] = DBDate.DateMMDD()
                                    KakaoRef["type"] = "Kakao"
                                    KakaoRef["data"] = dataList
                                    miningValue(
                                        KakaoRef,
                                        (i + ((page - 1) * list.size)),
                                        "Kakao",
                                        cate
                                    )
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })


                }
            })
    }

    fun getJoaraBest(context: Context, cate: String, page: Int) {
        val JoaraRef: MutableMap<String?, Any> = HashMap()
        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = page.toString()
        param["best"] = "today"
        param["store"] = ""
        param["category"] = Genre.setJoaraGenre(cate)

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
                        JoaraRef["info1"] = "줄거리 : " + books[i].intro
                        JoaraRef["info2"] = "총 " + books[i].cntChapter + " 화"
                        JoaraRef["info3"] = "조회 수 : " + books[i].cntPageRead
                        JoaraRef["info4"] = "선호작 수 : " + books[i].cntFavorite
                        JoaraRef["info5"] = "추천 수 : " + books[i].cntRecom
                        JoaraRef["number"] = i + ((page - 1) * books.size)
                        JoaraRef["date"] = DBDate.DateMMDD()
                        JoaraRef["type"] = "Joara"

                        miningValue(
                            JoaraRef,
                            (i + ((page - 1) * books.size)),
                            "Joara",
                            cate
                        )
                    }
                }
            })
    }

    fun getJoaraBestPremium(context: Context, cate: String, page: Int) {

        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = page.toString()
        param["best"] = "today"
        param["store"] = "premium"
        param["category"] = Genre.setJoaraGenre(cate)

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
                        JoaraRef["info1"] = "줄거리 : " + books[i].intro
                        JoaraRef["info2"] = "총 " + books[i].cntChapter + " 화"
                        JoaraRef["info3"] = "조회 수 : " + books[i].cntPageRead
                        JoaraRef["info4"] = "선호작 수 : " + books[i].cntFavorite
                        JoaraRef["info5"] = "추천 수 : " + books[i].cntRecom
                        JoaraRef["number"] = i + ((page - 1) * books.size)
                        JoaraRef["date"] = DBDate.DateMMDD()
                        JoaraRef["type"] = "Joara_Nobless"

                        miningValue(
                            JoaraRef,
                            (i + ((page - 1) * books.size)),
                            "Joara_Premium",
                            cate
                        )
                    }
                }
            })

    }

    fun getJoaraBestNobless(context: Context, cate: String, page: Int) {

        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = page.toString()
        param["best"] = "today"
        param["store"] = "nobless"
        param["category"] = Genre.setJoaraGenre(cate)

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
                        JoaraRef["info1"] = "줄거리 : " + books[i].intro
                        JoaraRef["info2"] = "총 " + books[i].cntChapter + " 화"
                        JoaraRef["info3"] = "조회 수 : " + books[i].cntPageRead
                        JoaraRef["info4"] = "선호작 수 : " + books[i].cntFavorite
                        JoaraRef["info5"] = "추천 수 : " + books[i].cntRecom
                        JoaraRef["number"] = i + ((page - 1) * books.size)
                        JoaraRef["date"] = DBDate.DateMMDD()
                        JoaraRef["type"] = "Joara_Nobless"

                        miningValue(
                            JoaraRef,
                            (i + ((page - 1) * books.size)),
                            "Joara_Nobless",
                            cate
                        )
                    }

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

                    BestRef.setBestRefWeekList("Munpia", "")
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                data.api?.items.let {

                                    if (it != null) {
                                        for (i in it.indices) {

                                            val dataList = ArrayList<BookListDataBestAnalyze>()

                                            for (weekItem in dataSnapshot.children) {
                                                val group: BookListDataBestToday? =
                                                    weekItem.getValue(BookListDataBestToday::class.java)
                                                if (group!!.title == it[i].nvTitle) {
                                                    dataList.add(
                                                        BookListDataBestAnalyze(
                                                            group.info3,
                                                            group.info4,
                                                            group.info5,
                                                            group.number,
                                                            group.date,
                                                        )
                                                    )
                                                }
                                            }

                                            val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                                Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                            Collections.sort(dataList, cmpAsc)

                                            MoonpiaRef["trophyCount"] = dataList.size

                                            if (dataList.size != 0) {
                                                val today =
                                                    DBDate.getToday(dataList[dataList.size - 1].date)
                                                val yesterday =
                                                    DBDate.getYesterday(dataList[dataList.size - 1].date)

                                                if ((today - yesterday) == 1) {
                                                    MoonpiaRef["numberDiff"] = calculateNumDiff(
                                                        i,
                                                        dataList[dataList.size - 1].number
                                                    ).num
                                                    MoonpiaRef["status"] = calculateNumDiff(
                                                        i,
                                                        dataList[dataList.size - 1].number
                                                    ).status
                                                } else {
                                                    MoonpiaRef["numberDiff"] = 0
                                                    MoonpiaRef["status"] = "NEW"
                                                }
                                            } else {
                                                MoonpiaRef["numberDiff"] = 0
                                                MoonpiaRef["status"] = "NEW"
                                            }

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
                                            MoonpiaRef["number"] = i
                                            MoonpiaRef["date"] = DBDate.DateMMDD()
                                            MoonpiaRef["type"] = "Munpia"
                                            MoonpiaRef["data"] = dataList

                                            miningValue(
                                                MoonpiaRef,
                                                (i + ((page - 1) * it.size)),
                                                "Munpia",
                                                ""
                                            )
                                        }
                                    }
                                }


                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
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

                    BestRef.setBestRefWeekList("Toksoda", genre)
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                data.resultList?.let {
                                    for (i in it.indices) {

                                        val dataList = ArrayList<BookListDataBestAnalyze>()

                                        for (weekItem in dataSnapshot.children) {
                                            val group: BookListDataBestToday? =
                                                weekItem.getValue(BookListDataBestToday::class.java)
                                            if (group!!.title == it[i].wrknm) {
                                                dataList.add(
                                                    BookListDataBestAnalyze(
                                                        group.info3,
                                                        group.info4,
                                                        group.info5,
                                                        group.number,
                                                        group.date,
                                                    )
                                                )
                                            }
                                        }

                                        val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                            Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                        Collections.sort(dataList, cmpAsc)

                                        ToksodaRef["trophyCount"] = dataList.size

                                        if (dataList.size != 0) {
                                            val today =
                                                DBDate.getToday(dataList[dataList.size - 1].date)
                                            val yesterday =
                                                DBDate.getYesterday(dataList[dataList.size - 1].date)

                                            if ((today - yesterday) == 1) {
                                                ToksodaRef["numberDiff"] = calculateNumDiff(
                                                    i,
                                                    dataList[dataList.size - 1].number
                                                ).num
                                                ToksodaRef["status"] = calculateNumDiff(
                                                    i,
                                                    dataList[dataList.size - 1].number
                                                ).status
                                            } else {
                                                ToksodaRef["numberDiff"] = 0
                                                ToksodaRef["status"] = "NEW"
                                            }
                                        } else {
                                            ToksodaRef["numberDiff"] = 0
                                            ToksodaRef["status"] = "NEW"
                                        }

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
                                        ToksodaRef["data"] = dataList

                                        miningValue(
                                            ToksodaRef,
                                            (i + ((page - 1) * it.size)),
                                            "Toksoda",
                                            genre
                                        )
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
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

