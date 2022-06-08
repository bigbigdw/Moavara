package com.example.moavara.Util

import android.content.Context
import android.util.Log
import com.example.moavara.Main.mRootRef
import com.example.moavara.Retrofit.*
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
import java.net.SocketTimeoutException
import java.util.ArrayList
import java.util.HashMap

object Mining {
    fun runMining(context: Context, cate: String) {
        getRidiBest(cate)
        getOneStoreBest(cate)
        getKakaoBest(cate)
        getKakaoStageBest(cate)
        for (i in 1..5) {
            getJoaraBest(context, cate, i)
        }
        for (i in 1..5) {
            getJoaraBestNobless(context, cate, i)
        }
        for (i in 1..5) {
            getJoaraBestPremium(context, cate, i)
        }
        getNaverToday(cate)
        getNaverChallenge(cate)
        getNaverBest(cate)
        getMrBlueBest(cate)
    }

    fun getMrBlueBest(cate: String) {
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
                                    group.status
                                )
                            )
                        }

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

    fun getNaverToday(cate: String) {
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
                                    group.status
                                )
                            )
                        }

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

    fun getNaverChallenge(cate: String) {
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
                                    group.status
                                )
                            )
                        }

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

    fun getNaverBest(cate: String) {
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
                                    group.status
                                )
                            )
                        }

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

    fun getRidiBest(cate: String) {

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
                                    group.status
                                )
                            )
                        }

                        for (i in Ridi.indices) {

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

                            miningValue(RidiRef, i, "Ridi", cate)

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            } catch (exception: SocketTimeoutException) {
                Log.d("EXCEPTION", "RIDI")
            }
        }.start()


    }

    fun getOneStoreBest(cate: String) {

        try {
            val OneStoryRef: MutableMap<String?, Any> = HashMap()

            val call: Call<OneStoreBookResult?>? =
                RetrofitOnestore.getBestOneStore(Genre.setOneStoreGenre(cate))

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
                                group.status
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

    fun getKakaoStageBest(cate: String) {
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        val call: Call<List<BestResultKakaoStageNovel>?>? = RetrofitKaKao.getBestKakaoStage(
            "false",
            "YESTERDAY",
            Genre.setKakaoStageGenre(cate),
            "72"
        )


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
                            group.status
                        )
                    )
                }

                call!!.clone().enqueue(object : Callback<List<BestResultKakaoStageNovel>?> {
                    override fun onResponse(
                        call: Call<List<BestResultKakaoStageNovel>?>,
                        response: Response<List<BestResultKakaoStageNovel>?>
                    ) {

                        if (response.isSuccessful) {
                            response.body()?.let { it ->

                                val list = it

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

                                    miningValue(KakaoRef, i, "Kakao Stage", cate)
                                }
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<List<BestResultKakaoStageNovel>?>,
                        t: Throwable
                    ) {
                        Log.d("onFailure", "실패")
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getKakaoBest(cate: String) {
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        val call: Call<BestResultKakao?>? = RetrofitKaKao.getBestKakao("11", "0", "0", "2", "A")


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
                            group.status
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
                            group.status
                        )
                    )
                }

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
                                JoaraRef["number"] = i
                                JoaraRef["numberDiff"] =
                                    calculateNum(i, books[i].subject, itemsYesterday).num
                                JoaraRef["date"] = DBDate.DateMMDD()
                                JoaraRef["status"] =
                                    calculateNum(i, books[i].subject, itemsYesterday).status

                                miningValue(JoaraRef, (i + ((page - 1) * books.size)), "Joara", cate)
                            }
                        }
                    })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }
}