package com.example.moavara.Util

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.moavara.DataBase.BookListDataBestToday
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.Main.ActivityAdmin
import com.example.moavara.Main.mRootRef
import com.example.moavara.Retrofit.*
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
import java.net.SocketTimeoutException
import java.util.ArrayList
import java.util.HashMap

object Mining {
    fun runMining(context: Context, cate: String) {
        getRidiBest(cate, context)
        getOneStoreBest(cate, context)
        getKakaoBest(cate, context)
        getKakaoStageBest(cate, context)
        for (i in 1..5) {
            getJoaraBest(context, cate, i)
        }
        for (i in 1..5) {
            getJoaraBestNobless(context, cate, i)
        }
        for (i in 1..5) {
            getJoaraBestPremium(context, cate, i)
        }
        getNaverToday(cate, context)
        getNaverChallenge(cate, context)
        getNaverBest(cate, context)
//        getMrBlueBest(cate, context)
        for (i in 1..5) {
            getMoonpiaBest(cate, context, i)
        }
        for (i in 1..5) {
            getToksodaBest(cate, context, i)
        }
    }

    fun getMrBlueBest(cate: String, context: Context) {
        Thread {

            try {
                val doc: Document =
                    Jsoup.connect(Genre.setMrBlueGenre(cate)).post()
                val MrBlue: Elements = doc.select(".list-box ul li")
                val MrBlueRef: MutableMap<String?, Any> = HashMap()

                val yesterdayRef =
                    mRootRef.child("best").child("MrBlue").child(cate).child("today").child(
                        DBDate.Yesterday()
                    )

                val itemsYesterday = ArrayList<BookListDataBestToday?>()

                yesterdayRef.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in dataSnapshot.children) {
                            val group: BookListDataBestToday? =
                                postSnapshot.getValue(BookListDataBestToday::class.java)
                            itemsYesterday.add(
                                BookListDataBestToday(
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
                                    group.numberDiff,
                                    group.date,
                                    group.type,
                                    group.status,
                                    group.trophyCount,
                                )
                            )
                        }

                        val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "MrBlue $cate")
                            .allowMainThreadQueries().build()

                        for (i in MrBlue.indices) {

                            val title = MrBlue.select(".tit > a")[i].attr("title")

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
                            MrBlueRef["numberDiff"] = calculateNum(i, title, itemsYesterday).num
                            MrBlueRef["date"] = DBDate.DateMMDD()
                            MrBlueRef["status"] = calculateNum(i, title, itemsYesterday).status
                            MrBlueRef["type"] = "MrBlue"

                            if(context is ActivityAdmin){
                                if(context.isRoom){
                                    MrBlueRef["trophyCount"] = bestDao.bestDao().countTrophy(title)
                                } else {
                                    MrBlueRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                }
                            } else {
                                MrBlueRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                            }

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
        Thread {

            try {
                val doc: Document =
                    Jsoup.connect(Genre.setNaverTodayGenre(cate)).post()
                val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
                val NaverRef: MutableMap<String?, Any> = HashMap()

                val yesterdayRef =
                    mRootRef.child("best").child("Naver Today").child(cate).child("today").child(
                        DBDate.Yesterday()
                    )

                val itemsYesterday = ArrayList<BookListDataBestToday?>()

                yesterdayRef.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in dataSnapshot.children) {
                            val group: BookListDataBestToday? =
                                postSnapshot.getValue(BookListDataBestToday::class.java)
                            itemsYesterday.add(
                                BookListDataBestToday(
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
                                    group.numberDiff,
                                    group.date,
                                    group.type,
                                    group.status,
                                    group.trophyCount,
                                )
                            )
                        }

                        val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "Naver Today $cate")
                            .allowMainThreadQueries().build()

                        for (i in Naver.indices) {

                            val title = Naver.select(".tit")[i].text()

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
                            NaverRef["numberDiff"] = calculateNum(i, title, itemsYesterday).num
                            NaverRef["date"] = DBDate.DateMMDD()
                            NaverRef["status"] = calculateNum(i, title, itemsYesterday).status
                            NaverRef["type"] = "Naver Today"

                            if(context is ActivityAdmin){
                                if(context.isRoom){
                                    NaverRef["trophyCount"] = bestDao.bestDao().countTrophy(title)
                                } else {
                                    NaverRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                }
                            } else {
                                try{
                                    NaverRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                                } catch (exception: IndexOutOfBoundsException){
                                    NaverRef["trophyCount"] = 1
                                }
                            }

                            miningValue(NaverRef, i, "Naver Today", cate)

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            } catch (exception: SocketTimeoutException) {
                Log.d("EXCEPTION", "NAVER TODAY")
            }
        }.start()


    }

    fun getNaverChallenge(cate: String, context: Context) {
        Thread {

            try {
                val doc: Document =
                    Jsoup.connect(Genre.setNaverChallengeGenre(cate)).post()
                val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
                val NaverRef: MutableMap<String?, Any> = HashMap()

                val yesterdayRef =
                    mRootRef.child("best").child("Naver Challenge").child(cate).child("today")
                        .child(
                            DBDate.Yesterday()
                        )

                val itemsYesterday = ArrayList<BookListDataBestToday?>()

                yesterdayRef.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in dataSnapshot.children) {
                            val group: BookListDataBestToday? =
                                postSnapshot.getValue(BookListDataBestToday::class.java)
                            itemsYesterday.add(
                                BookListDataBestToday(
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
                                    group.numberDiff,
                                    group.date,
                                    group.type,
                                    group.status,
                                    group.trophyCount,
                                )
                            )
                        }

                        val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "Naver Challenge $cate")
                            .allowMainThreadQueries().build()

                        for (i in Naver.indices) {

                            val title = Naver.select(".tit")[i].text()

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
                            NaverRef["numberDiff"] = calculateNum(i, title, itemsYesterday).num
                            NaverRef["date"] = DBDate.DateMMDD()
                            NaverRef["status"] = calculateNum(i, title, itemsYesterday).status
                            NaverRef["type"] = "Naver Challenge"

                            if(context is ActivityAdmin){
                                if(context.isRoom){
                                    NaverRef["trophyCount"] = bestDao.bestDao().countTrophy(title)
                                } else {
                                    NaverRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                }
                            } else {
                                NaverRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                            }

                            miningValue(NaverRef, i, "Naver Challenge", cate)

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            } catch (exception: SocketTimeoutException) {
                Log.d("EXCEPTION", "NAVER CHALLANGE")
            }
        }.start()


    }

    fun getNaverBest(cate: String, context: Context) {
        Thread {

            try {

                val doc: Document =
                    Jsoup.connect(Genre.setNaverGenre(cate)).post()
                val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
                val NaverRef: MutableMap<String?, Any> = HashMap()


                val yesterdayRef =
                    mRootRef.child("best").child("Naver").child(cate).child("today").child(
                        DBDate.Yesterday()
                    )

                val itemsYesterday = ArrayList<BookListDataBestToday?>()

                yesterdayRef.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in dataSnapshot.children) {
                            val group: BookListDataBestToday? =
                                postSnapshot.getValue(BookListDataBestToday::class.java)
                            itemsYesterday.add(
                                BookListDataBestToday(
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
                                    group.numberDiff,
                                    group.date,
                                    group.type,
                                    group.status,
                                    group.trophyCount,
                                )
                            )
                        }

                        val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "Naver $cate")
                            .allowMainThreadQueries().build()

                        for (i in Naver.indices) {

                            val title = Naver.select(".tit")[i].text()

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
                            NaverRef["numberDiff"] = calculateNum(i, title, itemsYesterday).num
                            NaverRef["date"] = DBDate.DateMMDD()
                            NaverRef["status"] = calculateNum(i, title, itemsYesterday).status
                            NaverRef["type"] = "Naver"

                            if(context is ActivityAdmin){
                                if(context.isRoom){
                                    NaverRef["trophyCount"] = bestDao.bestDao().countTrophy(title)
                                } else {
                                    NaverRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                }
                            } else {
                                NaverRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                            }

                            miningValue(NaverRef, i, "Naver", cate)

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            } catch (exception: SocketTimeoutException) {
                Log.d("EXCEPTION", "NAVER CHALLANGE")
            }
        }.start()
    }

    fun getRidiBest(cate: String, context: Context) {

        Thread {
            try {
                val doc: Document =
                    Jsoup.connect(Genre.setRidiGenre(cate)).post()
                val Ridi: Elements = doc.select(".book_thumbnail_wrapper")
                val RidiRef: MutableMap<String?, Any> = HashMap()

                val yesterdayRef =
                    mRootRef.child("best").child("Ridi").child(cate).child("today").child(
                        DBDate.Yesterday()
                    )
                val itemsYesterday = ArrayList<BookListDataBestToday?>()

                yesterdayRef.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in dataSnapshot.children) {
                            val group: BookListDataBestToday? =
                                postSnapshot.getValue(BookListDataBestToday::class.java)
                            itemsYesterday.add(
                                BookListDataBestToday(
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
                                    group.numberDiff,
                                    group.date,
                                    group.type,
                                    group.status,
                                    group.trophyCount,
                                )
                            )
                        }

                        val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "Ridi $cate")
                            .allowMainThreadQueries().build()

                        for (i in Ridi.indices) {
                            if(i > 0){
                                val title = doc.select("div .title_link")[i].text()

                                RidiRef["writerName"] = doc.select("div .author_detail_link")[i].text()
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
                                RidiRef["number"] = i
                                RidiRef["numberDiff"] = calculateNum(i, title, itemsYesterday).num
                                RidiRef["date"] = DBDate.DateMMDD()
                                RidiRef["status"] = calculateNum(i, title, itemsYesterday).status
                                RidiRef["type"] = "Ridi"

                                if(context is ActivityAdmin){
                                    if(context.isRoom){
                                        RidiRef["trophyCount"] = bestDao.bestDao().countTrophy(doc.select("div .title_link")[i].text())
                                    } else {
                                        RidiRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                    }
                                } else {
                                    try{
                                        RidiRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                                    } catch (exception: IndexOutOfBoundsException){
                                        RidiRef["trophyCount"] = 1
                                    }
                                }

                                miningValue(RidiRef, i - 1, "Ridi", cate)
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            } catch (exception: SocketTimeoutException) {
                Log.d("EXCEPTION", "RIDI")
            }
        }.start()


    }

    fun getOneStoreBest(cate: String, context: Context) {

        try {
            val OneStoryRef: MutableMap<String?, Any> = HashMap()

            val call: Call<OneStoreBookResult?>? =
                RetrofitOnestore().getBestOneStore(Genre.setOneStoreGenre(cate))

            val yesterdayRef =
                mRootRef.child("best").child("OneStore").child(cate).child("today").child(
                    DBDate.Yesterday()
                )

            val itemsYesterday = ArrayList<BookListDataBestToday?>()

            yesterdayRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val group: BookListDataBestToday? =
                            postSnapshot.getValue(BookListDataBestToday::class.java)
                        itemsYesterday.add(
                            BookListDataBestToday(
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
                                group.numberDiff,
                                group.date,
                                group.type,
                                group.status,
                                group.trophyCount,
                            )
                        )
                    }

                    call!!.enqueue(object : Callback<OneStoreBookResult?> {
                        override fun onResponse(
                            call: Call<OneStoreBookResult?>,
                            response: Response<OneStoreBookResult?>
                        ) {

                            if (response.isSuccessful) {
                                response.body()?.let { it ->
                                    val productList = it.params!!.productList

                                    val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "OneStore $cate")
                                        .allowMainThreadQueries().build()

                                    for (i in productList!!.indices) {

                                        OneStoryRef["writerName"] = productList[i].artistNm
                                        OneStoryRef["subject"] = productList[i].prodNm
                                        OneStoryRef["bookImg"] =
                                            "https://img.onestore.co.kr/thumbnails/img_sac/224_320_F10_95/" + productList[i].thumbnailImageUrl!!
                                        OneStoryRef["bookCode"] = productList[i].prodId
                                        OneStoryRef["info1"] = "조회 수 : " + productList[i].totalCount
                                        OneStoryRef["info2"] = "평점 : " + productList[i].avgScore
                                        OneStoryRef["info3"] =
                                            "댓글 수 : " + productList[i].commentCount
                                        OneStoryRef["info4"] = " "
                                        OneStoryRef["info5"] = " "
                                        OneStoryRef["number"] = i
                                        OneStoryRef["numberDiff"] =
                                            calculateNum(
                                                i,
                                                productList[i].prodNm,
                                                itemsYesterday
                                            ).num
                                        OneStoryRef["date"] = DBDate.DateMMDD()
                                        OneStoryRef["status"] = calculateNum(
                                            i,
                                            productList[i].prodNm,
                                            itemsYesterday
                                        ).status

                                        OneStoryRef["type"] = "OneStore"

                                        if(context is ActivityAdmin){
                                            if(context.isRoom){
                                                OneStoryRef["trophyCount"] = bestDao.bestDao().countTrophy(productList[i].prodNm)
                                            } else {
                                                OneStoryRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                            }
                                        } else {
                                            OneStoryRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                                        }

                                        miningValue(OneStoryRef, i, "OneStore", cate)

                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<OneStoreBookResult?>, t: Throwable) {
                            Log.d("onFailure", "실패")
                        }
                    })
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "ONESTORE")
        }
    }

    fun getKakaoStageBest(cate: String, context: Context) {
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        val apiKakao = RetrofitKaKao()
        val param : MutableMap<String?, Any> = HashMap()

        param["adult"] = "false"
        param["dateRange"] = "YESTERDAY"
        param["genreIds"] = Genre.setKakaoStageGenre(cate)
        param["recentHours"] = "72"

        val yesterdayRef =
            mRootRef.child("best").child("Kakao Stage").child(cate).child("today").child(
                DBDate.Yesterday()
            )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)
                    itemsYesterday.add(
                        BookListDataBestToday(
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
                            group.numberDiff,
                            group.date,
                            group.type,
                            group.status,
                            group.trophyCount,
                        )
                    )
                }

                apiKakao.getBestKakaoStage(
                    param,
                    object : RetrofitDataListener<List<BestResultKakaoStageNovel>> {
                        override fun onSuccess(data: List<BestResultKakaoStageNovel>) {

                            data.let {

                                val list = it

                                val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "Kakao Stage $cate")
                                    .allowMainThreadQueries().build()

                                for (i in list.indices) {
                                    val novel = list[i].novel
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
                                    KakaoRef["numberDiff"] =
                                        calculateNum(i, novel.title, itemsYesterday).num
                                    KakaoRef["date"] = DBDate.DateMMDD()
                                    KakaoRef["status"] =
                                        calculateNum(i, novel.title, itemsYesterday).status
                                    KakaoRef["type"] = "Kakao Stage"

                                    if(context is ActivityAdmin){
                                        if(context.isRoom){
                                            KakaoRef["trophyCount"] = bestDao.bestDao().countTrophy(novel.title)
                                        } else {
                                            KakaoRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                        }
                                    } else {
                                        KakaoRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                                    }

                                    miningValue(KakaoRef, i, "Kakao Stage", cate)
                                }
                            }

                        }
                    })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getKakaoBest(cate: String, context: Context) {
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        val call: Call<BestResultKakao?>? = RetrofitKaKao().getBestKakao("11", "0", "0", "2", "A")

        val yesterdayRef = mRootRef.child("best").child("Kakao").child(cate).child("today").child(
            DBDate.Yesterday()
        )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)
                    itemsYesterday.add(
                        BookListDataBestToday(
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
                            group.numberDiff,
                            group.date,
                            group.type,
                            group.status,
                            group.trophyCount,
                        )
                    )
                }


                call!!.enqueue(object : Callback<BestResultKakao?> {
                    override fun onResponse(
                        call: Call<BestResultKakao?>,
                        response: Response<BestResultKakao?>
                    ) {

                        if (response.isSuccessful) {
                            response.body()?.let { it ->
                                val list = it.list

                                val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "Kakao $cate")
                                    .allowMainThreadQueries().build()

                                for (i in list!!.indices) {

                                    KakaoRef["writerName"] = list[i].author
                                    KakaoRef["subject"] = list[i].title
                                    KakaoRef["bookImg"] =
                                        "https://dn-img-page.kakao.com/download/resource?kid=" + list[i].image!!
                                    KakaoRef["bookCode"] = list[i].series_id
                                    KakaoRef["info1"] = "줄거리 : " + list[i].description
                                    KakaoRef["info2"] = "부제 : " + list[i].caption
                                    KakaoRef["info3"] = "조회 수 : " + list[i].read_count
                                    KakaoRef["info4"] = "추천 수 : " + list[i].like_count
                                    KakaoRef["info5"] = "평점 : " + list[i].rating
                                    KakaoRef["number"] = i
                                    KakaoRef["numberDiff"] =
                                        calculateNum(i, list[i].title, itemsYesterday).num
                                    KakaoRef["date"] = DBDate.DateMMDD()
                                    KakaoRef["status"] =
                                        calculateNum(i, list[i].title, itemsYesterday).status
                                    KakaoRef["type"] = "Kakao"

                                    if(context is ActivityAdmin){
                                        if(context.isRoom){
                                            KakaoRef["trophyCount"] = bestDao.bestDao().countTrophy(list[i].title)
                                        } else {
                                            KakaoRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                        }
                                    } else {
                                        KakaoRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                                    }

                                    miningValue(KakaoRef, i, "Kakao", cate)

                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<BestResultKakao?>, t: Throwable) {
                        Log.d("onFailure", "실패")
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

    fun getJoaraBest(context: Context, cate: String, page: Int) {
        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val yesterdayRef =
            FirebaseDatabase.getInstance().reference.child("best").child("Joara").child(cate)
                .child("today").child(
                    DBDate.Yesterday()
                )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        val apiJoara = RetrofitJoara2()
        val param = Param.getItemAPI(context)

        param["page"] = page.toString()
        param["best"] = "today"
        param["store"] = ""
        param["category"] = Genre.setJoaraGenre(cate)

        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)
                    itemsYesterday.add(
                        BookListDataBestToday(
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
                            group.numberDiff,
                            group.date,
                            group.type,
                            group.status,
                            group.trophyCount
                        )
                    )
                }

                apiJoara.getJoaraBookBest(
                    param,
                    object : RetrofitDataListener<JoaraBestListResult> {
                        override fun onSuccess(data: JoaraBestListResult) {

                            val books = data.bookLists

                            val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "Joara $cate")
                                .allowMainThreadQueries().build()

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
                                JoaraRef["number"] = i
                                JoaraRef["numberDiff"] =
                                    calculateNum(i, books[i].subject, itemsYesterday).num
                                JoaraRef["date"] = DBDate.DateMMDD()
                                JoaraRef["status"] =
                                    calculateNum(i, books[i].subject, itemsYesterday).status
                                JoaraRef["type"] = "Joara"

                                if(context is ActivityAdmin){
                                    if(context.isRoom){
                                        JoaraRef["trophyCount"] = bestDao.bestDao().countTrophy(books[i].subject)
                                    } else {
                                        JoaraRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                    }
                                } else {
                                    JoaraRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                                }

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

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    fun getJoaraBestPremium(context: Context, cate: String,  page: Int) {

        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val yesterdayRef =
            FirebaseDatabase.getInstance().reference.child("best").child("Joara Premium").child(cate)
                .child("today").child(
                    DBDate.Yesterday()
                )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        val apiJoara = RetrofitJoara2()
        val param = Param.getItemAPI(context)

        param["page"] = page.toString()
        param["best"] = "today"
        param["store"] = "premium"
        param["category"] = Genre.setJoaraGenre(cate)


        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)
                    itemsYesterday.add(
                        BookListDataBestToday(
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
                            group.numberDiff,
                            group.date,
                            group.type,
                            group.status,
                            group.trophyCount,
                        )
                    )
                }

                apiJoara.getJoaraBookBest(
                    param,
                    object : RetrofitDataListener<JoaraBestListResult> {
                        override fun onSuccess(data: JoaraBestListResult) {

                            val books = data.bookLists

                            val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "Joara Premium $cate")
                                .allowMainThreadQueries().build()

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
                                JoaraRef["number"] = i
                                JoaraRef["numberDiff"] =
                                    calculateNum(i, books[i].subject, itemsYesterday).num
                                JoaraRef["date"] = DBDate.DateMMDD()
                                JoaraRef["status"] =
                                    calculateNum(i, books[i].subject, itemsYesterday).status
                                JoaraRef["type"] = "Joara Premium"

                                if(context is ActivityAdmin){
                                    if(context.isRoom){
                                        JoaraRef["trophyCount"] = bestDao.bestDao().countTrophy(books[i].subject)
                                    } else {
                                        JoaraRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                    }
                                } else {
                                    JoaraRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                                }

                                miningValue(
                                    JoaraRef,
                                    (i + ((page - 1) * books.size)),
                                    "Joara Premium",
                                    cate
                                )
                            }
                        }
                    })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    fun getJoaraBestNobless(context: Context, cate: String,  page: Int) {

        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val yesterdayRef =
            FirebaseDatabase.getInstance().reference.child("best").child("Joara Nobless").child(cate)
                .child("today").child(
                    DBDate.Yesterday()
                )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        val apiJoara = RetrofitJoara2()
        val param = Param.getItemAPI(context)

        param["page"] = page.toString()
        param["best"] = "today"
        param["store"] = "nobless"
        param["category"] = Genre.setJoaraGenre(cate)


        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)
                    itemsYesterday.add(
                        BookListDataBestToday(
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
                            group.numberDiff,
                            group.date,
                            group.type,
                            group.status,
                            group.trophyCount,
                        )
                    )
                }

                apiJoara.getJoaraBookBest(
                    param,
                    object : RetrofitDataListener<JoaraBestListResult> {
                        override fun onSuccess(data: JoaraBestListResult) {

                            val books = data.bookLists

                            val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "Joara Nobless $cate")
                                .allowMainThreadQueries().build()

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
                                JoaraRef["number"] = i
                                JoaraRef["numberDiff"] =
                                    calculateNum(i, books[i].subject, itemsYesterday).num
                                JoaraRef["date"] = DBDate.DateMMDD()
                                JoaraRef["status"] =
                                    calculateNum(i, books[i].subject, itemsYesterday).status
                                JoaraRef["type"] = "Joara Nobless"

                                if(context is ActivityAdmin){
                                    if(context.isRoom){
                                        JoaraRef["trophyCount"] = bestDao.bestDao().countTrophy(books[i].subject)
                                    } else {
                                        JoaraRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                    }
                                } else {
                                    JoaraRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                                }

                                miningValue(
                                    JoaraRef,
                                    (i + ((page - 1) * books.size)),
                                    "Joara Nobless",
                                    cate
                                )
                            }
                        }
                    })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    fun getMoonpiaBest(cate: String, context: Context, num: Int) {
        val MoonpiaRef: MutableMap<String?, Any> = HashMap()

        val apiMoonPia = RetrofitMoonPia()
        val param : MutableMap<String?, Any> = HashMap()

        param["section"] = "today"
        param["exclusive"] = ""
        param["outAdult"] = "true"
        param["offset"] = (num -1) * 25

        val yesterdayRef =
            mRootRef.child("best").child("Munpia").child("today").child(
                DBDate.Yesterday()
            )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)
                    itemsYesterday.add(
                        BookListDataBestToday(
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
                            group.numberDiff,
                            group.date,
                            group.type,
                            group.status,
                            group.trophyCount,
                        )
                    )
                }

                apiMoonPia.postMoonPiaBest(
                    param,
                    object : RetrofitDataListener<BestMoonpiaResult> {
                        override fun onSuccess(data: BestMoonpiaResult) {

                            data.api?.items.let {

                                val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "Munpia")
                                    .allowMainThreadQueries().build()

                                if (it != null) {
                                    for (i in it.indices) {
                                        MoonpiaRef["writerName"] = it[i].author
                                        MoonpiaRef["subject"] = it[i].nvTitle
                                        MoonpiaRef["bookImg"] = "https://cdn1.munpia.com${it[i].nvCover}"
                                        MoonpiaRef["bookCode"] = it[i].nvSrl
                                        MoonpiaRef["info1"] = "줄거리 : ${it[i].nvStory}"
                                        MoonpiaRef["info2"] = "베스트 시간 : ${it[i].nsrData?.hour}"
                                        MoonpiaRef["info3"] = "조회 수 : ${it[i].nsrData?.hit}"
                                        MoonpiaRef["info4"] = "방문 수 : ${it[i].nsrData?.number}"
                                        MoonpiaRef["info5"] = "선호작 수 : ${it[i].nsrData?.prefer}"
                                        MoonpiaRef["number"] = i
                                        MoonpiaRef["date"] = DBDate.DateMMDD()
                                        MoonpiaRef["type"] = "Munpia"
                                        MoonpiaRef["numberDiff"] =
                                            calculateNum(i, it[i].nvTitle, itemsYesterday).num
                                        MoonpiaRef["status"] =
                                            calculateNum(i, it[i].nvTitle, itemsYesterday).status

                                        if(context is ActivityAdmin){
                                            if (context.isRoom) {
                                                MoonpiaRef["trophyCount"] = bestDao.bestDao().countTrophy(it[i].nvTitle)
                                            } else {
                                                MoonpiaRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                            }
                                        } else {
                                            MoonpiaRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                                        }

                                        miningValue(MoonpiaRef, i, "Munpia", cate)
                                    }
                                }
                            }

                        }
                    })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getToksodaBest(cate: String, context: Context, page: Int) {
        val ToksodaRef: MutableMap<String?, Any> = HashMap()

        val apiToksoda = RetrofitToksoda()
        val param : MutableMap<String?, Any> = HashMap()

        param["page"] = page
        param["lgctgrCd"] = Genre.setToksodaGenre(cate)
        param["mdctgrCd"] = "all"
        param["rookieYn"] = "N"
        param["over19Yn"] = "N"
        param["type"] = "NEW"
        param["freePblserlYn"] = "00431"
        param["_"] = "1657262989944"

        val yesterdayRef =
            mRootRef.child("best").child("Toksoda").child(cate).child("today").child(
                DBDate.Yesterday()
            )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBestToday? =
                        postSnapshot.getValue(BookListDataBestToday::class.java)
                    itemsYesterday.add(
                        BookListDataBestToday(
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
                            group.numberDiff,
                            group.date,
                            group.type,
                            group.status,
                            group.trophyCount,
                        )
                    )
                }

                apiToksoda.getBestList(
                    param,
                    object : RetrofitDataListener<BestToksodaResult> {
                        override fun onSuccess(data: BestToksodaResult) {

                            data.resultList?.let {

                                val bestDao: DataBaseBestDay = Room.databaseBuilder(context, DataBaseBestDay::class.java, "Toksoda $cate")
                                    .allowMainThreadQueries().build()

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
                                    ToksodaRef["numberDiff"] = calculateNum(i, it[i].wrknm, itemsYesterday).num
                                    ToksodaRef["status"] = calculateNum(i, it[i].wrknm, itemsYesterday).status

                                    if(context is ActivityAdmin){
                                        if (context.isRoom) {
                                            ToksodaRef["trophyCount"] = bestDao.bestDao().countTrophy(it[i].wrknm)
                                        } else {
                                            ToksodaRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0)
                                        }
                                    } else {
                                        ToksodaRef["trophyCount"] = (itemsYesterday[i]?.trophyCount ?: 0) + 1
                                    }

                                    miningValue(
                                        ToksodaRef,
                                        (i + ((page - 1) * it.size)),
                                        "Toksoda",
                                        cate
                                    )
                                }
                            }

                        }
                    })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun RoomDB(context: Context, platform: String, cate: String){
        BestRef.setBestRef(platform, cate).child("week-list")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val bestDao: DataBaseBestDay

                    if(platform == "Munpia"){
                        bestDao = Room.databaseBuilder(
                            context,
                            DataBaseBestDay::class.java,
                            "$platform"
                        )
                            .allowMainThreadQueries().build()
                    } else {
                        bestDao = Room.databaseBuilder(
                            context,
                            DataBaseBestDay::class.java,
                            "$platform $cate"
                        )
                            .allowMainThreadQueries().build()
                    }

                    for (postSnapshot in dataSnapshot.children) {
                        val group: BookListDataBestToday? =
                            postSnapshot.getValue(BookListDataBestToday::class.java)
                        bestDao.bestDao().insert(
                            BookListDataBestToday(
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
                                group.numberDiff,
                                group.date,
                                group.type,
                                group.status,
                                group.trophyCount,
                            )
                        )
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun RoomDBRemove(context: Context, tabType: String, cate: String){
        val bestDao: DataBaseBestDay = Room.databaseBuilder(
            context,
            DataBaseBestDay::class.java,
            "$tabType $cate"
        ).allowMainThreadQueries().build()

        bestDao.bestDao().initAll()
    }
}