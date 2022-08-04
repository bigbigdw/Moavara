package com.example.moavara.Util

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.DataBase.BestTodayAverage
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.Retrofit.*
import com.example.moavara.Search.EventDetailDataMining
import com.google.firebase.database.FirebaseDatabase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File
import java.net.SocketTimeoutException
import java.util.*
import kotlin.math.roundToInt

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
            getRidiBest(genre)
        }

        val OneStore = Thread {
            getOneStoreBest(genre)
        }

        val Kakao = Thread {
            getKakaoBest(genre)
        }

        val KakaoStage = Thread {
            getKakaoStageBest(genre)
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
            getNaverToday(genre)
        }

        val NaverChallenge = Thread {
            getNaverChallenge(genre)
        }

        val NaverBest = Thread {
            getNaverBest(genre)
        }

        val Moonpia = Thread {
            getMoonpiaBest()
        }

        val Toksoda = Thread {
            getToksodaBest(genre)
        }

        val MrBlue = Thread {
            getMrBlueBest(genre)
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

    fun getMrBlueBest(genre: String) {
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

                File(File("/storage/self/primary/MOAVARA"), "Today_MrBlue.json").delete()
                File(File("/storage/self/primary/MOAVARA"), "Week_MrBlue.json").delete()
                File(File("/storage/self/primary/MOAVARA"), "Month_MrBlue.json").delete()

            } catch (exception: SocketTimeoutException) {
                Log.d("EXCEPTION", "MRBLUE")
            }
        }.start()

    }

    fun getNaverToday(genre: String) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setNaverTodayGenre(genre)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            var average1 = 1
            var average2 = 1
            var average3 = 1

            val books = ArrayList<BookListDataBest>()

            for (i in Naver.indices) {

                val info3 = Naver.select(".score_area")[i].text().replace("별점", "").toFloat().roundToInt()
                val info4 = Naver[i].select(".num_total").next().first()!!.text().replace("조회 ", "")
                    .strToInt()
                val info5 = Naver.select(".count")[i].text().replace("관심", "").strToInt()

                val title = Naver.select(".tit")[i].text()
                NaverRef["writerName"] = Naver.select(".author")[i].text()
                NaverRef["subject"] = title
                NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
                NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
                    .replace("https://novel.naver.com/webnovel/list?novelId=", "")
                NaverRef["info1"] = Naver[i].select(".num_total").first()!!.text()
                NaverRef["info2"] = ""
                NaverRef["info3"] = info3.toString()
                NaverRef["info4"] = info4.toString()
                NaverRef["info5"] = info5.toString()
                NaverRef["info6"] = ""
                NaverRef["number"] = i

                NaverRef["date"] = DBDate.DateMMDD()
                NaverRef["type"] = "Naver_Today"

                books.add(BestRef.setBookListDataBest(NaverRef))
                miningValue(NaverRef, i, "Naver_Today", genre)

                average1 += info3
                average2 += info4
                average3 += info5
            }

            val sort1: Comparator<BookListDataBest> =
                Comparator { o1, o2 -> o1.info3.compareTo(o2.info3) }
            Collections.sort(books, sort1)

            for(i in books.indices){
                BestRef.setBookCode("Naver_Today", genre, books[i].bookCode).child("numInfo1").setValue(i)
            }

            val sort2: Comparator<BookListDataBest> =
                Comparator { o1, o2 -> o1.info4.compareTo(o2.info4) }
            Collections.sort(books, sort2)

            for(i in books.indices){
                BestRef.setBookCode("Naver_Today", genre, books[i].bookCode).child("numInfo2").setValue(i)
            }

            val sort3: Comparator<BookListDataBest> =
                Comparator { o1, o2 -> o1.info5.compareTo(o2.info5) }
            Collections.sort(books, sort3)

            for(i in books.indices){
                BestRef.setBookCode("Naver_Today", genre, books[i].bookCode).child("numInfo3").setValue(i)
            }

            FirebaseDatabase.getInstance().reference.child("Best").child("Naver_Today")
                .child(genre).child("Average").child(DBDate.DateMMDD())
                .setValue(BestTodayAverage(average1, average2, average3))

            File(File("/storage/self/primary/MOAVARA"), "Today_Naver_Today.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Week_Naver_Today.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Month_Naver_Today.json").delete()


        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER TODAY")
        }

    }

    fun getNaverChallenge(genre: String) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setNaverChallengeGenre(genre)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            val books = ArrayList<BookListDataBest>()

            var average1 = 1
            var average2 = 1
            var average3 = 1

            for (i in Naver.indices) {

                val info3 = Naver.select(".score_area")[i].text().replace("별점", "").toFloat().roundToInt()
                val info4 = Naver[i].select(".num_total").next().first()!!.text().replace("조회 ", "")
                    .strToInt()
                val info5 = Naver.select(".count")[i].text().replace("관심", "").strToInt()

                NaverRef["writerName"] = Naver.select(".author")[i].text()
                NaverRef["subject"] = Naver.select(".tit")[i].text()
                NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
                NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
                    .replace("https://novel.naver.com/challenge/list?novelId=", "")
                NaverRef["info1"] = Naver[i].select(".num_total").first()!!.text()
                NaverRef["info2"] = ""
                NaverRef["info3"] = info3.toString()
                NaverRef["info4"] = info4.toString()
                NaverRef["info5"] = info5.toString()
                NaverRef["info6"] = ""
                NaverRef["number"] = i
                NaverRef["date"] = DBDate.DateMMDD()
                NaverRef["type"] = "Naver_Challenge"

                books.add(BestRef.setBookListDataBest(NaverRef))
                miningValue(NaverRef, i, "Naver_Challenge", genre)

                average1 += info3
                average2 += info4
                average3 += info5

            }

            val sort1: Comparator<BookListDataBest> =
                Comparator { o1, o2 -> o1.info3.compareTo(o2.info3) }
            Collections.sort(books, sort1)

            for(i in books.indices){
                BestRef.setBookCode("Naver_Challenge", genre, books[i].bookCode).child("numInfo1").setValue(i)
            }

            val sort2: Comparator<BookListDataBest> =
                Comparator { o1, o2 -> o1.info4.compareTo(o2.info4) }
            Collections.sort(books, sort2)

            for(i in books.indices){
                BestRef.setBookCode("Naver_Challenge", genre, books[i].bookCode).child("numInfo2").setValue(i)
            }

            val sort3: Comparator<BookListDataBest> =
                Comparator { o1, o2 -> o1.info5.compareTo(o2.info5) }
            Collections.sort(books, sort3)

            for(i in books.indices){
                BestRef.setBookCode("Naver_Challenge", genre, books[i].bookCode).child("numInfo3").setValue(i)
            }

            FirebaseDatabase.getInstance().reference.child("Best").child("Naver_Challenge")
                .child(genre).child("Average").child(DBDate.DateMMDD())
                .setValue(BestTodayAverage(average1, average2, average3))

            File(File("/storage/self/primary/MOAVARA"), "Today_Naver_Challenge.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Week_Naver_Challenge.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Month_Naver_Challenge.json").delete()

        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER CHALLANGE")
        }
    }

    fun getNaverBest(genre: String) {
        try {

            val doc: Document =
                Jsoup.connect(Genre.setNaverGenre(genre)).post()
            val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
            val NaverRef: MutableMap<String?, Any> = HashMap()

            var average1 = 1
            var average2 = 1
            var average3 = 1

            val books = ArrayList<BookListDataBest>()

            for (i in Naver.indices) {

                val info3 = Naver.select(".score_area")[i].text().replace("별점", "").toFloat()
                    .roundToInt()
                val info4 = Naver[i].select(".num_total").next().first()!!.text().replace("조회 ", "")
                    .strToInt()
                val info5 = Naver.select(".count")[i].text().replace("관심", "").strToInt()

                NaverRef["writerName"] = Naver.select(".author")[i].text()
                NaverRef["subject"] = Naver.select(".tit")[i].text()
                NaverRef["bookImg"] = Naver.select("div img")[i].absUrl("src")
                NaverRef["bookCode"] = Naver.select("a")[i].absUrl("href")
                    .replace("https://novel.naver.com/best/list?novelId=", "")
                NaverRef["info1"] = ""
                NaverRef["info2"] = Naver[i].select(".num_total").first()!!.text()
                NaverRef["info3"] = info3.toString()
                NaverRef["info4"] = info4.toString()
                NaverRef["info5"] = info5.toString()
                NaverRef["info6"] = ""
                NaverRef["number"] = i
                NaverRef["date"] = DBDate.DateMMDD()
                NaverRef["type"] = "Naver"

                books.add(BestRef.setBookListDataBest(NaverRef))
                miningValue(NaverRef, i, "Naver", genre)

                average1 += info3
                average2 += info4
                average3 += info5
            }

            val sort1: Comparator<BookListDataBest> =
                Comparator { o1, o2 -> o1.info3.compareTo(o2.info3) }
            Collections.sort(books, sort1)

            for(i in books.indices){
                BestRef.setBookCode("Naver", genre, books[i].bookCode).child("numInfo1").setValue(i)
            }

            val sort2: Comparator<BookListDataBest> =
                Comparator { o1, o2 -> o1.info4.compareTo(o2.info4) }
            Collections.sort(books, sort2)

            for(i in books.indices){
                BestRef.setBookCode("Naver", genre, books[i].bookCode).child("numInfo2").setValue(i)
            }

            val sort3: Comparator<BookListDataBest> =
                Comparator { o1, o2 -> o1.info5.compareTo(o2.info5) }
            Collections.sort(books, sort3)

            for(i in books.indices){
                BestRef.setBookCode("Naver", genre, books[i].bookCode).child("numInfo3").setValue(i)
            }

            FirebaseDatabase.getInstance().reference.child("Best").child("Naver")
                .child(genre).child("Average").child(DBDate.DateMMDD())
                .setValue(BestTodayAverage(average1, average2, average3))

            File(File("/storage/self/primary/MOAVARA"), "Today_Naver.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Week_Naver.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Month_Naver.json").delete()


        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "NAVER CHALLANGE")
        }
    }

    fun getRidiBest(genre: String) {
        try {
            val doc: Document =
                Jsoup.connect(Genre.setRidiGenre(genre)).post()
            val Ridi: Elements = doc.select(".book_thumbnail_wrapper")
            val RidiRef: MutableMap<String?, Any> = HashMap()

            var average1 = 1
            var average2 = 1

            val books = ArrayList<BookListDataBest>()

            for (i in Ridi.indices) {
                if (i > 0) {
                    val uri: Uri =
                        Uri.parse(Ridi.select("a")[i].absUrl("href"))

                    val info3 = doc.select("span .StarRate_ParticipantCount")[i].text()
                        .replace(",", "").replace("명", "").toInt()

                    val info4 = doc.select("span .StarRate_Score")[i].text().replace("점", "")
                        .replace(".", "")
                        .toInt()

                    RidiRef["writerName"] =
                        doc.select("div .author_detail_link")[i].text()
                    RidiRef["subject"] = doc.select("div .title_link")[i].text()
                    RidiRef["bookImg"] =
                        Ridi.select(".thumbnail_image .thumbnail")[i].absUrl("data-src")
                    RidiRef["bookCode"] = uri.path?.replace("/books/", "") ?: ""
                    RidiRef["info1"] = doc.select(".count_num")[i].text()
                    RidiRef["info2"] = ""
                    RidiRef["info3"] = info3.toString()
                    RidiRef["info4"] = info4.toString()
                    RidiRef["info5"] = ""
                    RidiRef["info6"] = ""
                    RidiRef["number"] = i
                    RidiRef["date"] = DBDate.DateMMDD()
                    RidiRef["type"] = "Ridi"

                    books.add(BestRef.setBookListDataBest(RidiRef))

                    average1 += info3
                    average2 += info4

                    miningValue(
                        RidiRef,
                        i,
                        "Ridi",
                        genre
                    )
                }
            }

            val sort1: Comparator<BookListDataBest> =
                Comparator { o1, o2 -> o1.info3.compareTo(o2.info3) }
            Collections.sort(books, sort1)

            for(i in books.indices){
                BestRef.setBookCode("Ridi", genre, books[i].bookCode).child("numInfo1").setValue(i)
            }

            val sort2: Comparator<BookListDataBest> =
                Comparator { o1, o2 -> o1.info4.compareTo(o2.info4) }
            Collections.sort(books, sort2)

            for(i in books.indices){
                BestRef.setBookCode("Ridi", genre, books[i].bookCode).child("numInfo2").setValue(i)
            }

            FirebaseDatabase.getInstance().reference.child("Best").child("Ridi")
                .child(genre).child("Average").child(DBDate.DateMMDD())
                .setValue(BestTodayAverage(average1, average2))

            File(File("/storage/self/primary/MOAVARA"), "Today_Ridi.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Week_Ridi.json").delete()
            File(File("/storage/self/primary/MOAVARA"), "Month_Ridi.json").delete()

        } catch (exception: SocketTimeoutException) {
            Log.d("EXCEPTION", "RIDI")
        }
    }

    fun getOneStoreBest(genre: String) {
        try {
            val OneStoryRef: MutableMap<String?, Any> = HashMap()

            val apiOneStory = RetrofitOnestore()
            val param: MutableMap<String?, Any> = HashMap()

            var average1 = 1
            var average2 = 1
            var average3 = 1

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
                                OneStoryRef["info4"] =
                                    productList[i].avgScore.toFloat().roundToInt().toString()
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

                                average1 += productList[i].totalCount.toInt()
                                average2 += productList[i].avgScore.toFloat().roundToInt()
                                average3 += productList[i].commentCount.toInt()

                            }

                            val sort1: Comparator<OnestoreBookItem> =
                                Comparator { o1, o2 -> o1.totalCount.compareTo(o2.totalCount) }
                            Collections.sort(productList, sort1)

                            for(i in productList.indices){
                                BestRef.setBookCode("OneStore", genre, productList[i].prodId).child("numInfo1").setValue(i)
                            }

                            val sort2: Comparator<OnestoreBookItem> =
                                Comparator { o1, o2 -> o1.avgScore.compareTo(o2.avgScore) }
                            Collections.sort(productList, sort2)

                            for(i in productList.indices){
                                BestRef.setBookCode("OneStore", genre, productList[i].prodId).child("numInfo2").setValue(i)
                            }

                            val sort3: Comparator<OnestoreBookItem> =
                                Comparator { o1, o2 -> o1.commentCount.compareTo(o2.commentCount) }
                            Collections.sort(productList, sort3)

                            for(i in productList.indices){
                                BestRef.setBookCode("OneStore", genre, productList[i].prodId).child("numInfo3").setValue(i)
                            }

                            FirebaseDatabase.getInstance().reference.child("Best").child("OneStore")
                                .child(genre).child("Average").child(DBDate.DateMMDD())
                                .setValue(BestTodayAverage(average1, average2))
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

    fun getKakaoStageBest(genre: String) {
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        var average1 = 1
        var average2 = 1
        var average3 = 1
        var average4 = 1

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

                            books.add(BestRef.setBookListDataBest(KakaoRef))

                            average1 += novel.viewCount.toInt()
                            average2 += novel.visitorCount.toInt()
                            average3 += novel.episodeLikeCount.toInt()
                            average4 += novel.favoriteCount.toInt()
                        }

                        val sort1: Comparator<BookListDataBest> =
                            Comparator { o1, o2 -> o1.info3.compareTo(o2.info3) }
                        Collections.sort(books, sort1)

                        for(i in books.indices){
                            BestRef.setBookCode("Kakao_Stage", genre, books[i].bookCode).child("numInfo1").setValue(i)
                        }

                        val sort2: Comparator<BookListDataBest> =
                            Comparator { o1, o2 -> o1.info4.compareTo(o2.info4) }
                        Collections.sort(books, sort2)

                        for(i in books.indices){
                            BestRef.setBookCode("Kakao_Stage", genre, books[i].bookCode).child("numInfo2").setValue(i)
                        }

                        val sort3: Comparator<BookListDataBest> =
                            Comparator { o1, o2 -> o1.info5.compareTo(o2.info5) }
                        Collections.sort(books, sort3)

                        for(i in books.indices){
                            BestRef.setBookCode("Kakao_Stage", genre, books[i].bookCode).child("numInfo3").setValue(i)
                        }

                        val sort4: Comparator<BookListDataBest> =
                            Comparator { o1, o2 -> o1.info6.compareTo(o2.info6) }
                        Collections.sort(books, sort4)

                        for(i in books.indices){
                            BestRef.setBookCode("Kakao_Stage", genre, books[i].bookCode).child("numInfo4").setValue(i)
                        }

                        FirebaseDatabase.getInstance().reference.child("Best").child("Kakao_Stage")
                            .child(genre).child("Average").child(DBDate.DateMMDD())
                            .setValue(BestTodayAverage(average1, average2, average3, average4))

                        File(
                            File("/storage/self/primary/MOAVARA"),
                            "Today_Kakao_Stage.json"
                        ).delete()
                        File(
                            File("/storage/self/primary/MOAVARA"),
                            "Week_Kakao_Stage.json"
                        ).delete()
                        File(
                            File("/storage/self/primary/MOAVARA"),
                            "Month_Kakao_Stage.json"
                        ).delete()
                    }

                }
            })
    }

    fun getKakaoBest(genre: String) {
        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        var average1 = 1
        var average2 = 1
        var average3 = 1
        var average4 = 1

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

                            KakaoRef["writerName"] = list[i].author
                            KakaoRef["subject"] = list[i].title
                            KakaoRef["bookImg"] =
                                "https://dn-img-page.kakao.com/download/resource?kid=" + list[i].image
                            KakaoRef["bookCode"] = list[i].series_id
                            KakaoRef["info1"] = list[i].description
                            KakaoRef["info2"] = list[i].caption
                            KakaoRef["info3"] = list[i].read_count
                            KakaoRef["info4"] = list[i].like_count
                            KakaoRef["info5"] = list[i].rating.toFloat().roundToInt().toString()
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

                            average1 += list[i].read_count.toInt()
                            average2 += list[i].like_count.toInt()
                            average3 += list[i].rating.toFloat().roundToInt()
                            average4 += list[i].comment_count.toInt()
                        }

                        val sort1: Comparator<KakaoBestBookResult> =
                            Comparator { o1, o2 -> o1.read_count.compareTo(o2.read_count) }
                        Collections.sort(list, sort1)

                        for(i in list.indices){
                            BestRef.setBookCode("Kakao", genre, list[i].series_id).child("numInfo1").setValue(i)
                        }

                        val sort2: Comparator<KakaoBestBookResult> =
                            Comparator { o1, o2 -> o1.like_count.compareTo(o2.like_count) }
                        Collections.sort(list, sort2)

                        for(i in list.indices){
                            BestRef.setBookCode("Kakao", genre, list[i].series_id).child("numInfo2").setValue(i)
                        }

                        val sort3: Comparator<KakaoBestBookResult> =
                            Comparator { o1, o2 -> o1.rating.toFloat().roundToInt().compareTo(o2.rating.toFloat().roundToInt()) }
                        Collections.sort(list, sort3)

                        for(i in list.indices){
                            BestRef.setBookCode("Kakao", genre, list[i].series_id).child("numInfo3").setValue(i)
                        }

                        val sort4: Comparator<KakaoBestBookResult> =
                            Comparator { o1, o2 -> o1.comment_count.compareTo(o2.comment_count) }
                        Collections.sort(list, sort4)

                        for(i in list.indices){
                            BestRef.setBookCode("Kakao", genre, list[i].series_id).child("numInfo4").setValue(i)
                        }
                    }

                    FirebaseDatabase.getInstance().reference.child("Best").child("Kakao")
                        .child(genre).child("Average").child(DBDate.DateMMDD())
                        .setValue(BestTodayAverage(average1, average2, average3, average4))

                    File(File("/storage/self/primary/MOAVARA"), "Today_Kakao.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Week_Kakao.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Month_Kakao.json").delete()
                }
            })
    }

    fun getJoaraBest(context: Context, genre: String) {
        val JoaraRef: MutableMap<String?, Any> = HashMap()
        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        var average1 = 1
        var average2 = 1
        var average3 = 1
        var average4 = 1

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

                            JoaraRef["writerName"] = books[i].writerName
                            JoaraRef["subject"] = books[i].subject
                            JoaraRef["bookImg"] = books[i].bookImg
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

                            average1 += books[i].cntPageRead.toInt()
                            average2 += books[i].cntFavorite.toInt()
                            average3 += books[i].cntRecom.toInt()
                            average4 += books[i].cntTotalComment.toInt()

                            miningValue(
                                JoaraRef,
                                i,
                                "Joara",
                                genre
                            )
                        }

                        val sort1: Comparator<JoaraBestListValue> =
                            Comparator { o1, o2 -> o1.cntPageRead.compareTo(o2.cntPageRead) }
                        Collections.sort(books, sort1)

                        for(i in books.indices){
                            BestRef.setBookCode("Joara", genre, books[i].bookCode).child("numInfo1").setValue(i)
                        }

                        val sort2: Comparator<JoaraBestListValue> =
                            Comparator { o1, o2 -> o1.cntFavorite.compareTo(o2.cntFavorite) }
                        Collections.sort(books, sort2)

                        for(i in books.indices){
                            BestRef.setBookCode("Joara", genre, books[i].bookCode).child("numInfo2").setValue(i)
                        }

                        val sort3: Comparator<JoaraBestListValue> =
                            Comparator { o1, o2 -> o1.cntRecom.compareTo(o2.cntRecom) }
                        Collections.sort(books, sort3)

                        for(i in books.indices){
                            BestRef.setBookCode("Joara", genre, books[i].bookCode).child("numInfo3").setValue(i)
                        }
                    }

                    FirebaseDatabase.getInstance().reference.child("Best").child("Joara")
                        .child(genre).child("Average").child(DBDate.DateMMDD())
                        .setValue(BestTodayAverage(average1, average2, average3, average4))

                    File(File("/storage/self/primary/MOAVARA"), "Today_Joara.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Week_Joara.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Month_Joara.json").delete()
                }
            })
    }

    fun getJoaraBestPremium(context: Context, genre: String) {

        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = 1
        param["best"] = "today"
        param["store"] = "premium"
        param["category"] = Genre.setJoaraGenre(genre)
        param["offset"] = "100"

        var average1 = 1
        var average2 = 1
        var average3 = 1
        var average4 = 1

        apiJoara.getJoaraBookBest(
            param,
            object : RetrofitDataListener<JoaraBestListResult> {
                override fun onSuccess(data: JoaraBestListResult) {

                    val books = data.bookLists

                    if (books != null) {
                        for (i in books.indices) {

                            JoaraRef["writerName"] = books[i].writerName
                            JoaraRef["subject"] = books[i].subject
                            JoaraRef["bookImg"] = books[i].bookImg
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

                            average1 += books[i].cntPageRead.toInt()
                            average2 += books[i].cntFavorite.toInt()
                            average3 += books[i].cntRecom.toInt()
                            average4 += books[i].cntTotalComment.toInt()
                        }

                        val sort1: Comparator<JoaraBestListValue> =
                            Comparator { o1, o2 -> o1.cntPageRead.compareTo(o2.cntPageRead) }
                        Collections.sort(books, sort1)

                        for(i in books.indices){
                            BestRef.setBookCode("Joara_Premium", genre, books[i].bookCode).child("numInfo1").setValue(i)
                        }

                        val sort2: Comparator<JoaraBestListValue> =
                            Comparator { o1, o2 -> o1.cntFavorite.compareTo(o2.cntFavorite) }
                        Collections.sort(books, sort2)

                        for(i in books.indices){
                            BestRef.setBookCode("Joara_Premium", genre, books[i].bookCode).child("numInfo2").setValue(i)
                        }

                        val sort3: Comparator<JoaraBestListValue> =
                            Comparator { o1, o2 -> o1.cntRecom.compareTo(o2.cntRecom) }
                        Collections.sort(books, sort3)

                        for(i in books.indices){
                            BestRef.setBookCode("Joara_Premium", genre, books[i].bookCode).child("numInfo3").setValue(i)
                        }
                    }

                    FirebaseDatabase.getInstance().reference.child("Best").child("Joara_Premium")
                        .child(genre).child("Average").child(DBDate.DateMMDD())
                        .setValue(BestTodayAverage(average1, average2, average3, average4))

                    File(File("/storage/self/primary/MOAVARA"), "Today_Joara_Premium.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Week_Joara_Premium.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Month_Joara_Premium.json").delete()
                }
            })

    }

    fun getJoaraBestNobless(context: Context, genre: String) {

        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val apiJoara = RetrofitJoara()
        val param = Param.getItemAPI(context)

        param["page"] = 1
        param["best"] = "today"
        param["store"] = "nobless"
        param["category"] = Genre.setJoaraGenre(genre)
        param["offset"] = "100"

        var average1 = 1
        var average2 = 1
        var average3 = 1
        var average4 = 1

        apiJoara.getJoaraBookBest(
            param,
            object : RetrofitDataListener<JoaraBestListResult> {
                override fun onSuccess(data: JoaraBestListResult) {

                    val books = data.bookLists

                    if (books != null) {
                        for (i in books.indices) {

                            JoaraRef["status"] = "NEW"
                            JoaraRef["writerName"] = books[i].writerName
                            JoaraRef["subject"] = books[i].subject
                            JoaraRef["bookImg"] = books[i].bookImg
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

                            average1 += books[i].cntPageRead.toInt()
                            average2 += books[i].cntFavorite.toInt()
                            average3 += books[i].cntRecom.toInt()
                            average4 += books[i].cntTotalComment.toInt()
                        }

                        val sort1: Comparator<JoaraBestListValue> =
                            Comparator { o1, o2 -> o1.cntPageRead.compareTo(o2.cntPageRead) }
                        Collections.sort(books, sort1)

                        for(i in books.indices){
                            BestRef.setBookCode("Joara_Nobless", genre, books[i].bookCode).child("numInfo1").setValue(i)
                        }

                        val sort2: Comparator<JoaraBestListValue> =
                            Comparator { o1, o2 -> o1.cntFavorite.compareTo(o2.cntFavorite) }
                        Collections.sort(books, sort2)

                        for(i in books.indices){
                            BestRef.setBookCode("Joara_Nobless", genre, books[i].bookCode).child("numInfo2").setValue(i)
                        }

                        val sort3: Comparator<JoaraBestListValue> =
                            Comparator { o1, o2 -> o1.cntRecom.compareTo(o2.cntRecom) }
                        Collections.sort(books, sort3)

                        for(i in books.indices){
                            BestRef.setBookCode("Joara_Nobless", genre, books[i].bookCode).child("numInfo3").setValue(i)
                        }
                    }

                    FirebaseDatabase.getInstance().reference.child("Best").child("Joara_Nobless")
                        .child(genre).child("Average").child(DBDate.DateMMDD())
                        .setValue(BestTodayAverage(average1, average2, average3, average4))

                    File(File("/storage/self/primary/MOAVARA"), "Today_Joara_Nobless.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Week_Joara_Nobless.json").delete()
                    File(File("/storage/self/primary/MOAVARA"), "Month_Joara_Nobless.json").delete()
                }
            })

    }

    fun getMoonpiaBest() {
        val MoonpiaRef: MutableMap<String?, Any> = HashMap()

        val apiMoonPia = RetrofitMoonPia()
        val param: MutableMap<String?, Any> = HashMap()

        param["section"] = "today"
        param["exclusive"] = ""
        param["outAdult"] = "true"
        param["offset"] = 25

        var average1 = 1
        var average2 = 1
        var average3 = 1
        var average4 = 1

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

                                average1 += (it[i].nsrData?.hit?.toInt() ?: 0)
                                average2 += (it[i].nsrData?.number?.toInt() ?: 0)
                                average3 += (it[i].nsrData?.prefer?.toInt() ?: 0)
                                average4 += (it[i].nsrData?.hour?.toInt() ?: 0)
                            }

                            data.api?.items?.let { it1 ->
                                val sort1: Comparator<BestMoonpiaContentsItems> =
                                    Comparator { o1, o2 -> (o1.nsrData?.hit?.toInt() ?: 0).compareTo(o2.nsrData?.hit?.toInt() ?: 0) }
                                Collections.sort(it1, sort1)

                                for(i in it1.indices){
                                    BestRef.setBookCode("Munpia", "ALL", it1[i].nvSrl).child("numInfo1").setValue(i)
                                }

                                val sort2: Comparator<BestMoonpiaContentsItems> =
                                    Comparator { o1, o2 -> (o1.nsrData?.number?.toInt() ?: 0).compareTo(o2.nsrData?.number?.toInt() ?: 0) }
                                Collections.sort(it1, sort2)

                                for(i in it1.indices){
                                    BestRef.setBookCode("Munpia", "ALL", it1[i].nvSrl).child("numInfo2").setValue(i)
                                }

                                val sort3: Comparator<BestMoonpiaContentsItems> =
                                    Comparator { o1, o2 -> (o1.nsrData?.prefer?.toInt() ?: 0).compareTo(o2.nsrData?.prefer?.toInt() ?: 0) }
                                Collections.sort(it1, sort3)

                                for(i in it1.indices){
                                    BestRef.setBookCode("Munpia", "ALL", it1[i].nvSrl).child("numInfo3").setValue(i)
                                }

                                val sort4: Comparator<BestMoonpiaContentsItems> =
                                    Comparator { o1, o2 -> (o1.nsrData?.hour?.toInt() ?: 0).compareTo(o2.nsrData?.hour?.toInt() ?: 0) }
                                Collections.sort(it1, sort4)

                                for(i in it1.indices){
                                    BestRef.setBookCode("Munpia", "ALL", it1[i].nvSrl).child("numInfo4").setValue(i)
                                }
                            }

                            FirebaseDatabase.getInstance().reference.child("Best").child("Munpia")
                                .child("Average").child(DBDate.DateMMDD())
                                .setValue(BestTodayAverage(average1, average2, average3, average4))

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

    fun getToksodaBest(genre: String) {
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

        var average1 = 1
        var average2 = 1
        var average3 = 1

        apiToksoda.getBestList(
            param,
            object : RetrofitDataListener<BestToksodaResult> {
                override fun onSuccess(data: BestToksodaResult) {

                    data.resultList?.let { it ->
                        for (i in it.indices) {

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

                            average1 += it[i].inqrCnt.toInt()
                            average2 += it[i].goodAllCnt.toInt()
                            average3 += it[i].intrstCnt.toInt()
                        }

                        data.resultList.let { it1 ->
                            val sort1: Comparator<BestToksodaToksodaResultList> =
                                Comparator { o1, o2 -> o1.inqrCnt.compareTo(o2.inqrCnt) }
                            Collections.sort(it1, sort1)

                            for(i in it1.indices){
                                BestRef.setBookCode("Toksoda", genre, it1[i].brcd).child("numInfo1").setValue(i)
                            }

                            val sort2: Comparator<BestToksodaToksodaResultList> =
                                Comparator { o1, o2 -> o1.goodAllCnt.compareTo(o2.goodAllCnt) }
                            Collections.sort(it1, sort2)

                            for(i in it1.indices){
                                BestRef.setBookCode("Toksoda", genre, it1[i].brcd).child("numInfo2").setValue(i)
                            }

                            val sort3: Comparator<BestToksodaToksodaResultList> =
                                Comparator { o1, o2 -> o1.intrstCnt.compareTo(o2.intrstCnt) }
                            Collections.sort(it1, sort3)

                            for(i in it1.indices){
                                BestRef.setBookCode("Toksoda", genre, it1[i].brcd).child("numInfo3").setValue(i)
                            }
                        }

                        FirebaseDatabase.getInstance().reference.child("Best").child("Toksoda")
                            .child(genre).child("Average").child(DBDate.DateMMDD())
                            .setValue(BestTodayAverage(average1, average2, average3))

                        File(File("/storage/self/primary/MOAVARA"), "Today_Toksoda.json").delete()
                        File(File("/storage/self/primary/MOAVARA"), "Week_Toksoda.json").delete()
                        File(File("/storage/self/primary/MOAVARA"), "Month_Toksoda.json").delete()
                    }
                }
            })
    }
}

