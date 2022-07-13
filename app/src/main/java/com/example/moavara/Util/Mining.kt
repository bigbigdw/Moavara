package com.example.moavara.Util

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.DataBase.BookListDataBestToday
import com.example.moavara.DataBase.DataBaseBestDay
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
import java.util.*

object Mining {
    fun runMining(context: Context, genre: String) {

        val Ridi1 = Thread {
            getRidiBest(genre)
        }
        Ridi1.start()

        val OneStore1 = Thread {
            getOneStoreBest(genre)
        }
        OneStore1.start()

        val Kakao1 = Thread {
            getKakaoBest(genre)
        }
        Kakao1.start()

        val KakaoStage1 = Thread {
            getKakaoStageBest(genre)
        }
        KakaoStage1.start()

        val Joara1 = Thread {
            for (i in 1..5) {
                getJoaraBest(context, genre, i)
            }
        }
        Joara1.start()

        val JoaraNobless1 = Thread {
            for (i in 1..5) {
                getJoaraBestNobless(context, genre, i)
            }
        }
        JoaraNobless1.start()

        val JoaraPremium1 = Thread {
            for (i in 1..5) {
                getJoaraBestPremium(context, genre, i)
            }
        }
        JoaraPremium1.start()

        val NaverToday1 = Thread {
            getNaverToday(genre)
        }
        NaverToday1.start()

        val NaverChallenge1 = Thread {
            getNaverChallenge(genre)
        }
        NaverChallenge1.start()

        val NaverBest1 = Thread {
            getNaverBest(genre)
        }
        NaverBest1.start()

        val Moonpia = Thread {
            for (i in 1..5) {
                getMoonpiaBest(i)
            }
        }
        Moonpia.start()

        val Toksoda1 = Thread {
            for (i in 1..5) {
                getToksodaBest(genre, i)
            }
        }
        Toksoda1.start()

        try {
            Ridi1.join()
            Log.d("####MINING", "리디1 완료")

            OneStore1.join()
            Log.d("####MINING", "원스토어1 완료")

            Kakao1.join()
            Log.d("####MINING", "카카오1 완료")

            KakaoStage1.join()
            Log.d("####MINING", "카카오 스테이지1 완료")

            Joara1.join()
            Log.d("####MINING", "조아라1 완료")

            JoaraNobless1.join()
            Log.d("####MINING", "조아라 노블레스1 완료")

            JoaraPremium1.join()
            Log.d("####MINING", "조아라 프리미엄1 완료")

            NaverToday1.join()
            Log.d("####MINING", "네이버 투데이1 완료")

            NaverChallenge1.join()
            Log.d("####MINING", "네이버 챌린지1 완료")

            NaverBest1.join()
            Log.d("####MINING", "네이버1 완료")

            Moonpia.join()
            Log.d("####MINING", "문피아 완료")

            Toksoda1.join()
            Log.d("####MINING", "톡소다1 완료")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun getNaverToday(cate: String) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setNaverTodayGenre(cate)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            val yesterdayRef =
                mRootRef.child("best").child("Naver Today").child(cate).child("today").child(
                    DBDate.Yesterday()
                )

            val itemsYesterday = ArrayList<BookListDataBest>()

            yesterdayRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val group: BookListDataBest? =
                            postSnapshot.getValue(BookListDataBest::class.java)
                        itemsYesterday.add(
                            BookListDataBest(
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
                                group.data
                            )
                        )
                    }

                    for (i in Naver.indices) {

                        val title = Naver.select(".tit")[i].text()
                        val dataList = ArrayList<BookListDataBestAnalyze>()

                        BestRef.setBestRefWeekList("Naver Today", cate).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                for (weekItem in dataSnapshot.children) {
                                    val group: BookListDataBestToday? = weekItem.getValue(BookListDataBestToday::class.java)
                                    if (group!!.title == title) {
                                        dataList.add(
                                            BookListDataBestAnalyze(
                                                group.info3,
                                                group.info4,
                                                group.info5,
                                                group.number,
                                                group.numberDiff,
                                                group.date,
                                                group.trophyCount,
                                            )
                                        )
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

                                if(dataList.size != 0){
                                    if(DBDate.getToday(dataList[dataList.size -1].date) - DBDate.getYesterday(dataList[dataList.size -1].date) == 1){
                                        NaverRef["numberDiff"] = calculateNumDiff(i, dataList[dataList.size -1].number).num
                                        NaverRef["status"] = calculateNumDiff(i, dataList[dataList.size -1].number).status
                                    } else {
                                        NaverRef["numberDiff"] = 0
                                        NaverRef["status"] = "NEW"
                                    }
                                } else {
                                    NaverRef["numberDiff"] = 0
                                    NaverRef["status"] = "NEW"
                                }

                                NaverRef["date"] = DBDate.DateMMDD()
                                NaverRef["type"] = "Naver Today"
                                NaverRef["data"] = dataList

                                miningValue(NaverRef, i, "Naver Today", cate)
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER TODAY")
        }

    }

    fun getNaverChallenge(cate: String) {
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

            val itemsYesterday = ArrayList<BookListDataBest>()

            yesterdayRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val group: BookListDataBest? =
                            postSnapshot.getValue(BookListDataBest::class.java)
                        itemsYesterday.add(
                            BookListDataBest(
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
                                group.data
                            )
                        )
                    }

                    for (i in Naver.indices) {

                        val title = Naver.select(".tit")[i].text()

                        val dataList = ArrayList<BookListDataBestAnalyze>()

                        BestRef.setBestRefWeekList("Naver Challenge", cate).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                for (weekItem in dataSnapshot.children) {
                                    val group: BookListDataBestToday? = weekItem.getValue(BookListDataBestToday::class.java)
                                    if (group!!.title == title) {
                                        dataList.add(
                                            BookListDataBestAnalyze(
                                                group.info3,
                                                group.info4,
                                                group.info5,
                                                group.number,
                                                group.numberDiff,
                                                group.date,
                                                group.trophyCount,
                                            )
                                        )
                                    }
                                }

                                val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                    Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                Collections.sort(dataList, cmpAsc)

                                if(dataList.size != 0){
                                    if(DBDate.getToday(dataList[dataList.size -1].date) - DBDate.getYesterday(dataList[dataList.size -1].date) == 1){
                                        NaverRef["numberDiff"] = calculateNumDiff(i, dataList[dataList.size -1].number).num
                                        NaverRef["status"] = calculateNumDiff(i, dataList[dataList.size -1].number).status
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
                                NaverRef["type"] = "Naver Challenge"
                                NaverRef["data"] = dataList

                                miningValue(NaverRef, i, "Naver Challenge", cate)
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER CHALLANGE")
        }
    }

    fun getNaverBest(cate: String) {
        try {

            val doc: Document =
                Jsoup.connect(Genre.setNaverGenre(cate)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            val yesterdayRef =
                mRootRef.child("best").child("Naver").child(cate).child("today").child(
                    DBDate.Yesterday()
                )

            val itemsYesterday = ArrayList<BookListDataBest>()

            yesterdayRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val group: BookListDataBest? =
                            postSnapshot.getValue(BookListDataBest::class.java)
                        itemsYesterday.add(
                            BookListDataBest(
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
                                group.data
                            )
                        )
                    }

                    for (i in Naver.indices) {

                        val title = Naver.select(".tit")[i].text()
                        val dataList = ArrayList<BookListDataBestAnalyze>()

                        BestRef.setBestRefWeekList("Naver", cate).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                for (weekItem in dataSnapshot.children) {
                                    val group: BookListDataBestToday? = weekItem.getValue(BookListDataBestToday::class.java)
                                    if (group!!.title == title) {
                                        dataList.add(
                                            BookListDataBestAnalyze(
                                                group.info3,
                                                group.info4,
                                                group.info5,
                                                group.number,
                                                group.numberDiff,
                                                group.date,
                                                group.trophyCount,
                                            )
                                        )
                                    }
                                }

                                val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                    Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                Collections.sort(dataList, cmpAsc)

                                if(dataList.size != 0){
                                    if(DBDate.getToday(dataList[dataList.size -1].date) - DBDate.getYesterday(dataList[dataList.size -1].date) == 1){
                                        NaverRef["numberDiff"] = calculateNumDiff(i, dataList[dataList.size -1].number).num
                                        NaverRef["status"] = calculateNumDiff(i, dataList[dataList.size -1].number).status
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

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER CHALLANGE")
        }
    }

    fun getRidiBest(cate: String) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setRidiGenre(cate)).post()
            val Ridi: Elements = doc.select(".book_thumbnail_wrapper")
            val RidiRef: MutableMap<String?, Any> = HashMap()

            val yesterdayRef =
                mRootRef.child("best").child("Ridi").child(cate).child("today").child(
                    DBDate.Yesterday()
                )
            val itemsYesterday = ArrayList<BookListDataBest>()

            yesterdayRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val group: BookListDataBest? =
                            postSnapshot.getValue(BookListDataBest::class.java)
                        itemsYesterday.add(
                            BookListDataBest(
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
                                group.data
                            )
                        )
                    }

                    for (i in Ridi.indices) {
                        if (i > 0) {
                            val title = doc.select("div .title_link")[i].text()
                            val dataList = ArrayList<BookListDataBestAnalyze>()

                            BestRef.setBestRefWeekList("Ridi", cate).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {

                                    for (weekItem in dataSnapshot.children) {
                                        val group: BookListDataBestToday? = weekItem.getValue(BookListDataBestToday::class.java)
                                        if (group!!.title == title) {
                                            dataList.add(
                                                BookListDataBestAnalyze(
                                                    group.info3,
                                                    group.info4,
                                                    group.info5,
                                                    group.number,
                                                    group.numberDiff,
                                                    group.date,
                                                    group.trophyCount,
                                                )
                                            )
                                        }
                                    }

                                    val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                        Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                    Collections.sort(dataList, cmpAsc)

                                    RidiRef["trophyCount"] = dataList.size

                                    if(dataList.size != 0){
                                        if(DBDate.getToday(dataList[dataList.size -1].date) - DBDate.getYesterday(dataList[dataList.size -1].date) == 1){
                                            RidiRef["numberDiff"] = calculateNumDiff(i, dataList[dataList.size -1].number).num
                                            RidiRef["status"] = calculateNumDiff(i, dataList[dataList.size -1].number).status
                                        } else {
                                            RidiRef["numberDiff"] = 0
                                            RidiRef["status"] = "NEW"
                                        }
                                    } else {
                                        RidiRef["numberDiff"] = 0
                                        RidiRef["status"] = "NEW"
                                    }

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
                                    RidiRef["number"] = i
                                    RidiRef["date"] = DBDate.DateMMDD()
                                    RidiRef["type"] = "Ridi"
                                    RidiRef["data"] = dataList

                                    miningValue(RidiRef, i - 1, "Ridi", cate)
                                }

                                override fun onCancelled(databaseError: DatabaseError) {}
                            })

                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "RIDI")
        }
    }

    fun getOneStoreBest(cate: String) {

        try {
            val OneStoryRef: MutableMap<String?, Any> = HashMap()

            val call: Call<OneStoreBookResult?>? =
                RetrofitOnestore().getBestOneStore(Genre.setOneStoreGenre(cate))

            val yesterdayRef =
                mRootRef.child("best").child("OneStore").child(cate).child("today").child(
                    DBDate.Yesterday()
                )

            val itemsYesterday = ArrayList<BookListDataBest>()

            yesterdayRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val group: BookListDataBest? =
                            postSnapshot.getValue(BookListDataBest::class.java)
                        itemsYesterday.add(
                            BookListDataBest(
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
                                group.data
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

                                    for (i in productList!!.indices) {

                                        val dataList = ArrayList<BookListDataBestAnalyze>()

                                        BestRef.setBestRefWeekList("OneStore", cate).addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                                for (weekItem in dataSnapshot.children) {
                                                    val group: BookListDataBestToday? = weekItem.getValue(BookListDataBestToday::class.java)
                                                    if (group!!.title == productList[i].prodNm) {
                                                        dataList.add(
                                                            BookListDataBestAnalyze(
                                                                group.info3,
                                                                group.info4,
                                                                group.info5,
                                                                group.number,
                                                                group.numberDiff,
                                                                group.date,
                                                                group.trophyCount,
                                                            )
                                                        )
                                                    }
                                                }

                                                val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                                    Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                                Collections.sort(dataList, cmpAsc)

                                                if(dataList.size != 0){
                                                    if(DBDate.getToday(dataList[dataList.size -1].date) - DBDate.getYesterday(dataList[dataList.size -1].date) == 1){
                                                        OneStoryRef["numberDiff"] = calculateNumDiff(i, dataList[dataList.size -1].number).num
                                                        OneStoryRef["status"] = calculateNumDiff(i, dataList[dataList.size -1].number).status
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
                                                OneStoryRef["info1"] = "조회 수 : " + productList[i].totalCount
                                                OneStoryRef["info2"] = "평점 : " + productList[i].avgScore
                                                OneStoryRef["info3"] =
                                                    "댓글 수 : " + productList[i].commentCount
                                                OneStoryRef["info4"] = " "
                                                OneStoryRef["info5"] = " "
                                                OneStoryRef["number"] = i
                                                OneStoryRef["date"] = DBDate.DateMMDD()

                                                OneStoryRef["type"] = "OneStore"
                                                OneStoryRef["data"] = dataList

                                                miningValue(OneStoryRef, i, "OneStore", cate)
                                            }

                                            override fun onCancelled(databaseError: DatabaseError) {}
                                        })
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

    fun getKakaoStageBest(cate: String) {
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        param["adult"] = "false"
        param["dateRange"] = "YESTERDAY"
        param["genreIds"] = Genre.setKakaoStageGenre(cate)
        param["recentHours"] = "72"

        val yesterdayRef =
            mRootRef.child("best").child("Kakao Stage").child(cate).child("today").child(
                DBDate.Yesterday()
            )

        val itemsYesterday = ArrayList<BookListDataBest>()

        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBest? =
                        postSnapshot.getValue(BookListDataBest::class.java)
                    itemsYesterday.add(
                        BookListDataBest(
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
                            group.data
                        )
                    )
                }

                apiKakao.getBestKakaoStage(
                    param,
                    object : RetrofitDataListener<List<BestResultKakaoStageNovel>> {
                        override fun onSuccess(data: List<BestResultKakaoStageNovel>) {

                            data.let {

                                val list = it

                                for (i in list.indices) {
                                    val novel = list[i].novel
                                    val dataList = ArrayList<BookListDataBestAnalyze>()

                                    BestRef.setBestRefWeekList("Kakao Stage", cate).addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                                            for (weekItem in dataSnapshot.children) {
                                                val group: BookListDataBestToday? = weekItem.getValue(BookListDataBestToday::class.java)
                                                if (group!!.title == novel?.title) {
                                                    dataList.add(
                                                        BookListDataBestAnalyze(
                                                            group.info3,
                                                            group.info4,
                                                            group.info5,
                                                            group.number,
                                                            group.numberDiff,
                                                            group.date,
                                                            group.trophyCount,
                                                        )
                                                    )
                                                }
                                            }

                                            val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                                Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                            Collections.sort(dataList, cmpAsc)

                                            KakaoRef["trophyCount"] = dataList.size

                                            if(dataList.size != 0){
                                                if(DBDate.getToday(dataList[dataList.size -1].date) - DBDate.getYesterday(dataList[dataList.size -1].date) == 1){
                                                    KakaoRef["numberDiff"] = calculateNumDiff(i, dataList[dataList.size -1].number).num
                                                    KakaoRef["status"] = calculateNumDiff(i, dataList[dataList.size -1].number).status
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
                                            KakaoRef["type"] = "Kakao Stage"
                                            KakaoRef["data"] = dataList

                                            miningValue(KakaoRef, i, "Kakao Stage", cate)
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {}
                                    })
                                }
                            }

                        }
                    })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getKakaoBest(cate: String) {
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        val call: Call<BestResultKakao?>? = RetrofitKaKao().getBestKakao("11", "0", "0", "2", "A")

        val yesterdayRef = mRootRef.child("best").child("Kakao").child(cate).child("today").child(
            DBDate.Yesterday()
        )

        val itemsYesterday = ArrayList<BookListDataBest>()

        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBest? =
                        postSnapshot.getValue(BookListDataBest::class.java)
                    itemsYesterday.add(
                        BookListDataBest(
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
                            group.data
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

                                for (i in list!!.indices) {

                                    val dataList = ArrayList<BookListDataBestAnalyze>()

                                    BestRef.setBestRefWeekList("Kakao", cate).addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                                            for (weekItem in dataSnapshot.children) {
                                                val group: BookListDataBestToday? = weekItem.getValue(BookListDataBestToday::class.java)
                                                if (group!!.title == list[i].title) {
                                                    dataList.add(
                                                        BookListDataBestAnalyze(
                                                            group.info3,
                                                            group.info4,
                                                            group.info5,
                                                            group.number,
                                                            group.numberDiff,
                                                            group.date,
                                                            group.trophyCount,
                                                        )
                                                    )
                                                }
                                            }

                                            val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                                Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                            Collections.sort(dataList, cmpAsc)

                                            KakaoRef["trophyCount"] = dataList.size

                                            if(dataList.size != 0){
                                                if(DBDate.getToday(dataList[dataList.size -1].date) - DBDate.getYesterday(dataList[dataList.size -1].date) == 1){
                                                    KakaoRef["numberDiff"] = calculateNumDiff(i, dataList[dataList.size -1].number).num
                                                    KakaoRef["status"] = calculateNumDiff(i, dataList[dataList.size -1].number).status
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
                                            miningValue(KakaoRef, i, "Kakao", cate)
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {}
                                    })
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
                .child("today").child(DBDate.Yesterday())

        val itemsYesterday = ArrayList<BookListDataBest>()

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = page.toString()
        param["best"] = "today"
        param["store"] = ""
        param["category"] = Genre.setJoaraGenre(cate)

        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBest? =
                        postSnapshot.getValue(BookListDataBest::class.java)
                    itemsYesterday.add(
                        BookListDataBest(
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
                            group.data
                        )
                    )
                }

                apiJoara.getJoaraBookBest(
                    param,
                    object : RetrofitDataListener<JoaraBestListResult> {
                        override fun onSuccess(data: JoaraBestListResult) {

                            val books = data.bookLists

                            for (i in books!!.indices) {

                                val dataList = ArrayList<BookListDataBestAnalyze>()

                                BestRef.setBestRefWeekList("Joara", cate).addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                                        for (weekItem in dataSnapshot.children) {
                                            val group: BookListDataBestToday? = weekItem.getValue(BookListDataBestToday::class.java)
                                            if (group!!.title == books[i].subject) {
                                                dataList.add(
                                                    BookListDataBestAnalyze(
                                                        group.info3,
                                                        group.info4,
                                                        group.info5,
                                                        group.number,
                                                        group.numberDiff,
                                                        group.date,
                                                        group.trophyCount,
                                                    )
                                                )
                                            }
                                        }

                                        val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                            Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                        Collections.sort(dataList, cmpAsc)

                                        JoaraRef["trophyCount"] = dataList.size

                                        if(dataList.size != 0){
                                            if(DBDate.getToday(dataList[dataList.size -1].date) - DBDate.getYesterday(dataList[dataList.size -1].date) == 1){
                                                JoaraRef["numberDiff"] = calculateNumDiff(i, dataList[dataList.size -1].number).num
                                                JoaraRef["status"] = calculateNumDiff(i, dataList[dataList.size -1].number).status
                                            } else {
                                                JoaraRef["numberDiff"] = 0
                                                JoaraRef["status"] = "NEW"
                                            }
                                        } else {
                                            JoaraRef["numberDiff"] = 0
                                            JoaraRef["status"] = "NEW"
                                        }

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
                                        JoaraRef["date"] = DBDate.DateMMDD()
                                        JoaraRef["type"] = "Joara"
                                        JoaraRef["data"] = dataList

                                        miningValue(
                                            JoaraRef,
                                            (i + ((page - 1) * books.size)),
                                            "Joara",
                                            cate
                                        )
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {}
                                })
                            }
                        }
                    })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

    fun getJoaraBestPremium(context: Context, cate: String, page: Int) {

        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val yesterdayRef =
            FirebaseDatabase.getInstance().reference.child("best").child("Joara Premium")
                .child(cate)
                .child("today").child(
                    DBDate.Yesterday()
                )

        val itemsYesterday = ArrayList<BookListDataBest>()

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = page.toString()
        param["best"] = "today"
        param["store"] = "premium"
        param["category"] = Genre.setJoaraGenre(cate)

        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBest? =
                        postSnapshot.getValue(BookListDataBest::class.java)
                    itemsYesterday.add(
                        BookListDataBest(
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
                            group.data
                        )
                    )
                }

                apiJoara.getJoaraBookBest(
                    param,
                    object : RetrofitDataListener<JoaraBestListResult> {
                        override fun onSuccess(data: JoaraBestListResult) {

                            val books = data.bookLists

                            for (i in books!!.indices) {

                                val dataList = ArrayList<BookListDataBestAnalyze>()

                                BestRef.setBestRefWeekList("Joara Premium", cate).addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                                        for (weekItem in dataSnapshot.children) {
                                            val group: BookListDataBestToday? = weekItem.getValue(BookListDataBestToday::class.java)
                                            if (group!!.title == books[i].subject) {
                                                dataList.add(
                                                    BookListDataBestAnalyze(
                                                        group.info3,
                                                        group.info4,
                                                        group.info5,
                                                        group.number,
                                                        group.numberDiff,
                                                        group.date,
                                                        group.trophyCount,
                                                    )
                                                )
                                            }
                                        }

                                        val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                            Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                        Collections.sort(dataList, cmpAsc)

                                        if(dataList.size != 0){
                                            if(DBDate.getToday(dataList[dataList.size -1].date) - DBDate.getYesterday(dataList[dataList.size -1].date) == 1){
                                                JoaraRef["numberDiff"] = calculateNumDiff(i, dataList[dataList.size -1].number).num
                                                JoaraRef["status"] = calculateNumDiff(i, dataList[dataList.size -1].number).status
                                            } else {
                                                JoaraRef["numberDiff"] = 0
                                                JoaraRef["status"] = "NEW"
                                            }
                                        } else {
                                            JoaraRef["numberDiff"] = 0
                                            JoaraRef["status"] = "NEW"
                                        }

                                        JoaraRef["trophyCount"] = dataList.size

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
                                        JoaraRef["date"] = DBDate.DateMMDD()
                                        JoaraRef["type"] = "Joara Premium"
                                        JoaraRef["data"] = dataList

                                        miningValue(
                                            JoaraRef,
                                            (i + ((page - 1) * books.size)),
                                            "Joara Premium",
                                            cate
                                        )
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {}
                                })
                            }
                        }
                    })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    fun getJoaraBestNobless(context: Context, cate: String, page: Int) {

        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val yesterdayRef =
            FirebaseDatabase.getInstance().reference.child("best").child("Joara Nobless")
                .child(cate)
                .child("today").child(
                    DBDate.Yesterday()
                )

        val itemsYesterday = ArrayList<BookListDataBest>()

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = page.toString()
        param["best"] = "today"
        param["store"] = "nobless"
        param["category"] = Genre.setJoaraGenre(cate)

        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBest? =
                        postSnapshot.getValue(BookListDataBest::class.java)
                    itemsYesterday.add(
                        BookListDataBest(
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
                            group.data
                        )
                    )
                }

                apiJoara.getJoaraBookBest(
                    param,
                    object : RetrofitDataListener<JoaraBestListResult> {
                        override fun onSuccess(data: JoaraBestListResult) {

                            val books = data.bookLists

                            for (i in books!!.indices) {

                                val dataList = ArrayList<BookListDataBestAnalyze>()

                                BestRef.setBestRefWeekList("Joara Nobless", cate).addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                                        for (weekItem in dataSnapshot.children) {
                                            val group: BookListDataBestToday? = weekItem.getValue(BookListDataBestToday::class.java)
                                            if (group!!.title == books[i].subject) {
                                                dataList.add(
                                                    BookListDataBestAnalyze(
                                                        group.info3,
                                                        group.info4,
                                                        group.info5,
                                                        group.number,
                                                        group.numberDiff,
                                                        group.date,
                                                        group.trophyCount,
                                                    )
                                                )
                                            }
                                        }

                                        val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                            Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                        Collections.sort(dataList, cmpAsc)

                                        JoaraRef["trophyCount"] = dataList.size

                                        if(dataList.size != 0){
                                            if(DBDate.getToday(dataList[dataList.size -1].date) - DBDate.getYesterday(dataList[dataList.size -1].date) == 1){
                                                JoaraRef["numberDiff"] = calculateNumDiff(i, dataList[dataList.size -1].number).num
                                                JoaraRef["status"] = calculateNumDiff(i, dataList[dataList.size -1].number).status
                                            } else {
                                                JoaraRef["numberDiff"] = 0
                                                JoaraRef["status"] = "NEW"
                                            }
                                        } else {
                                            JoaraRef["numberDiff"] = 0
                                            JoaraRef["status"] = "NEW"
                                        }

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
                                        JoaraRef["date"] = DBDate.DateMMDD()
                                        JoaraRef["type"] = "Joara Nobless"
                                        JoaraRef["data"] = dataList

                                        miningValue(
                                            JoaraRef,
                                            (i + ((page - 1) * books.size)),
                                            "Joara Nobless",
                                            cate
                                        )
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {}
                                })
                            }
                        }
                    })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    fun getMoonpiaBest(num: Int) {
        val MoonpiaRef: MutableMap<String?, Any> = HashMap()

        val apiMoonPia = RetrofitMoonPia()
        val param: MutableMap<String?, Any> = HashMap()

        param["section"] = "today"
        param["exclusive"] = ""
        param["outAdult"] = "true"
        param["offset"] = (num - 1) * 25

        val yesterdayRef =
            mRootRef.child("best").child("Munpia").child("today").child(
                DBDate.Yesterday()
            )

        val itemsYesterday = ArrayList<BookListDataBest>()

        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBest? =
                        postSnapshot.getValue(BookListDataBest::class.java)
                    itemsYesterday.add(
                        BookListDataBest(
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
                            group.data
                        )
                    )
                }

                apiMoonPia.postMoonPiaBest(
                    param,
                    object : RetrofitDataListener<BestMoonpiaResult> {
                        override fun onSuccess(data: BestMoonpiaResult) {

                            data.api?.items.let {

                                if (it != null) {
                                    for (i in it.indices) {

                                        val dataList = ArrayList<BookListDataBestAnalyze>()

                                        BestRef.setBestRefWeekList("Munpia", "").addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                                for (weekItem in dataSnapshot.children) {
                                                    val group: BookListDataBestToday? = weekItem.getValue(BookListDataBestToday::class.java)
                                                    if (group!!.title == it[i].nvTitle) {
                                                        dataList.add(
                                                            BookListDataBestAnalyze(
                                                                group.info3,
                                                                group.info4,
                                                                group.info5,
                                                                group.number,
                                                                group.numberDiff,
                                                                group.date,
                                                                group.trophyCount,
                                                            )
                                                        )
                                                    }
                                                }

                                                val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                                    Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                                Collections.sort(dataList, cmpAsc)

                                                MoonpiaRef["trophyCount"] = dataList.size

                                                if(dataList.size != 0){
                                                    if(DBDate.getToday(dataList[dataList.size -1].date) - DBDate.getYesterday(dataList[dataList.size -1].date) == 1){
                                                        MoonpiaRef["numberDiff"] = calculateNumDiff(i, dataList[dataList.size -1].number).num
                                                        MoonpiaRef["status"] = calculateNumDiff(i, dataList[dataList.size -1].number).status
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

                                                miningValue(MoonpiaRef, i, "Munpia", "")
                                            }

                                            override fun onCancelled(databaseError: DatabaseError) {}
                                        })
                                    }
                                }
                            }

                        }
                    })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getToksodaBest(genre: String, page: Int) {
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

        val yesterdayRef =
            mRootRef.child("best").child("Toksoda").child(genre).child("today").child(
                DBDate.Yesterday()
            )

        val itemsYesterday = ArrayList<BookListDataBest>()

        yesterdayRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val group: BookListDataBest? =
                        postSnapshot.getValue(BookListDataBest::class.java)
                    itemsYesterday.add(
                        BookListDataBest(
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
                            group.data
                        )
                    )
                }

                apiToksoda.getBestList(
                    param,
                    object : RetrofitDataListener<BestToksodaResult> {
                        override fun onSuccess(data: BestToksodaResult) {

                            data.resultList?.let {

                                for (i in it.indices) {

                                    val dataList = ArrayList<BookListDataBestAnalyze>()

                                    BestRef.setBestRefWeekList("Toksoda", genre).addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                                            for (weekItem in dataSnapshot.children) {
                                                val group: BookListDataBestToday? = weekItem.getValue(BookListDataBestToday::class.java)
                                                if (group!!.title == it[i].wrknm) {
                                                    dataList.add(
                                                        BookListDataBestAnalyze(
                                                            group.info3,
                                                            group.info4,
                                                            group.info5,
                                                            group.number,
                                                            group.numberDiff,
                                                            group.date,
                                                            group.trophyCount,
                                                        )
                                                    )
                                                }
                                            }

                                            val cmpAsc: Comparator<BookListDataBestAnalyze> =
                                                Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
                                            Collections.sort(dataList, cmpAsc)

                                            ToksodaRef["trophyCount"] = dataList.size

                                            if(dataList.size != 0){
                                                if(DBDate.getToday(dataList[dataList.size -1].date) - DBDate.getYesterday(dataList[dataList.size -1].date) == 1){
                                                    ToksodaRef["numberDiff"] = calculateNumDiff(i, dataList[dataList.size -1].number).num
                                                    ToksodaRef["status"] = calculateNumDiff(i, dataList[dataList.size -1].number).status
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

                                        override fun onCancelled(databaseError: DatabaseError) {}
                                    })
                                }
                            }

                        }
                    })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun RoomDB(context: Context, platform: String, cate: String) {
        BestRef.setBestRef(platform, cate).child("week-list")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val bestDao: DataBaseBestDay

                    bestDao = if (platform == "Munpia") {
                        Room.databaseBuilder(
                            context,
                            DataBaseBestDay::class.java,
                            "$platform"
                        ).allowMainThreadQueries().build()

                    } else {
                        Room.databaseBuilder(
                            context,
                            DataBaseBestDay::class.java,
                            "$platform $cate"
                        )
                            .allowMainThreadQueries().build()
                    }

                    for (postSnapshot in dataSnapshot.children) {
                        val group: BookListDataBest? =
                            postSnapshot.getValue(BookListDataBest::class.java)
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




    fun RoomDBRemove(context: Context, tabType: String, cate: String) {
        val bestDao: DataBaseBestDay = Room.databaseBuilder(
            context,
            DataBaseBestDay::class.java,
            "$tabType $cate"
        ).allowMainThreadQueries().build()

        bestDao.bestDao().initAll()
    }
}

