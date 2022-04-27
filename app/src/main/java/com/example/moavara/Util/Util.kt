package com.example.moavara.Util

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.DataBase.DataBestDay
import com.example.moavara.Main.mRootRef
import com.example.moavara.Retrofit.*
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.CalculNum
import com.google.firebase.database.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Param {

    fun getItemAPI(mContext : Context?) : MutableMap<String?, Any> {

        val Param: MutableMap<String?, Any> = HashMap()

        mContext ?: return Param

        Param["api_key"] = "mw_8ba234e7801ba288554ca07ae44c7"
        Param["ver"] = "2.6.3"
        Param["device"] = "mw"
        Param["deviceuid"] = "5127d5951c983034a16980c8a893ac99d16dbef988ee36882b793aa14ad33604"
        Param["devicetoken"] =  "mw"

        return Param
    }
}

object DBDate {

    var status = ""

    fun DayInt(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }

    fun Day(): String {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK).toString()
    }

    fun Yesterday(): String {

        return if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1) {
            "7"
        } else {
            (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).toString()
        }
    }

    fun DayString(): String {

        val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK).toString()

        when (day) {
            "1" -> {
                return "sun"
            }
            "2" -> {
                return "mon"
            }
            "3" -> {
                return "tue"
            }
            "4" -> {
                return "wed"
            }
            "5" -> {
                return "thur"
            }
            "6" -> {
                return "fri"
            }
            "7" -> {
                return "sat"
            }
            else -> {
                return ""
            }
        }
    }

    fun Week(): String {
        return Calendar.getInstance().get(Calendar.WEEK_OF_MONTH).toString()
    }

    fun DateMMDD(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("MM-dd")
        return format.format(currentTime).toString()
    }

    fun Month(): String {
        return Calendar.getInstance().get(Calendar.MONTH).toString()
    }
}

object BestRef {
    private val mRootRef = FirebaseDatabase.getInstance().reference

    fun setBestRef(type: String, genre: String): DatabaseReference {
        return mRootRef.child("best").child(type).child(genre)
    }

    fun typeListTitle(): List<String> {
        return listOf(
            "조아라",
            "J.노블레스",
            "J.프리미엄",
            "네이버 오늘의 웹소설",
            "N.챌린지리그",
            "N.베스트리그",
            "카카오 페이지",
            "K.스테이지",
            "리디북스",
            "원스토어",
            "미스터블루"
        )
    }

    fun typeList(): List<String> {
        return listOf(
            "Joara",
            "Joara Nobless",
            "Joara Premium",
            "Naver Today",
            "Naver Challenge",
            "Naver",
            "Kakao",
            "Kakao Stage",
            "Ridi",
            "OneStore",
            "MrBlue"
        )
    }


    fun setBestRefWeekList(type: String, genre: String): DatabaseReference {
        return setBestRef(type, genre).child("week-list")
    }

    fun setBestRefWeekCompared(type: String, num: Int, genre: String): DatabaseReference {
        return setBestRef(type, genre).child("week-list")
            .child(((DBDate.DayInt() * 1000) + num).toString())
    }

    fun delBestRefWeekCompared(type: String, genre: String): DatabaseReference {
        return setBestRef(type, genre).child("week-list")
    }

    fun setBestRefToday(type: String, num: Int, genre: String): DatabaseReference {
        return setBestRef(type, genre).child("today").child(DBDate.Day()).child(num.toString())
    }

    fun getBestRefToday(type: String, genre: String): DatabaseReference {
        return setBestRef(type, genre).child("today").child(DBDate.Day())
    }

    fun setBestRefWeek(type: String, num: Int, genre: String): DatabaseReference {
        return setBestRef(type, genre).child("week").child(num.toString()).child(DBDate.DayString())
    }

    fun setBestRefMonthWeek(type: String, genre: String): DatabaseReference {
        return setBestRef(type, genre).child("month").child(DBDate.Month()).child(DBDate.Week())
            .child(DBDate.DayString())
    }

    fun setBestRefMonthDay(type: String, num: Int, genre: String): DatabaseReference {
        return setBestRef(type, genre).child("month").child(DBDate.Month()).child(DBDate.Week())
            .child(DBDate.DayString()).child("day").child(num.toString())
    }

    fun setBestRefMonth(type: String, genre: String): DatabaseReference {
        return setBestRef(type, genre).child("month").child(DBDate.Month()).child(DBDate.Week())
            .child(DBDate.DayString())
    }

    fun setBookListDataBestToday(ref: MutableMap<String?, Any>): BookListDataBestToday {
        return BookListDataBestToday(
            ref["writerName"] as String,
            ref["subject"] as String,
            ref["bookImg"] as String,
            ref["bookCode"] as String,
            ref["info1"] as String,
            ref["info2"] as String,
            ref["info3"] as String,
            ref["info4"] as String,
            ref["info5"] as String,
            ref["number"] as Int,
            ref["numberDiff"] as Int,
            ref["date"] as String,
            ref["status"] as String,
        )
    }

    fun setDataBestDay(ref: MutableMap<String?, Any>): DataBestDay {
        return DataBestDay(
            ref["writerName"] as String,
            ref["subject"] as String,
            ref["bookImg"] as String,
            ref["bookCode"] as String,
            ref["info1"] as String,
            ref["info2"] as String,
            ref["info3"] as String,
            ref["info4"] as String,
            ref["info5"] as String,
            ref["number"] as Int,
            ref["numberDiff"] as Int,
            ref["date"] as String,
            ref["status"] as String,
        )
    }
}

object Genre {

    fun getGenre(mContext: Context): String? {
        return if (mContext.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
                .getString("GENRE", "ALL") != ""
        ) {
            mContext.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
                .getString("GENRE", "ALL")
        } else
            "ALL"
    }

    fun setJoaraGenre(mContext: Context): String {
        return when {
            getGenre(mContext) == "BL" -> {
                "20"
            }
            getGenre(mContext) == "FANTASY" -> {
                "1"
            }
            getGenre(mContext) == "ROMANCE" -> {
                "5"
            }
            else -> {
                "0"
            }
        }
    }

    fun setNaverGenre(mContext: Context): String {
        return when {
            getGenre(mContext) == "BL" -> {
                //현판
                "https://novel.naver.com/best/ranking?genre=110&periodType=DAILY"
            }
            getGenre(mContext) == "FANTASY" -> {
                "https://novel.naver.com/best/ranking?genre=102&periodType=DAILY"
            }
            getGenre(mContext) == "ROMANCE" -> {
                "https://novel.naver.com/best/ranking?genre=101&periodType=DAILY"
            }
            else -> {
                //무협
                "https://novel.naver.com/best/ranking?genre=103&periodType=DAILY"
            }
        }
    }

    fun setRidiGenre(mContext: Context): String {
        return when {
            getGenre(mContext) == "BL" -> {
                "https://ridibooks.com/bestsellers/bl-webnovel?order=daily&rent=n&adult=n&adult_exclude=y"
            }
            getGenre(mContext) == "FANTASY" -> {
                "https://ridibooks.com/bestsellers/fantasy_serial?order=daily"
            }
            getGenre(mContext) == "ROMANCE" -> {
                "https://ridibooks.com/bestsellers/romance_serial?rent=n&adult=n&adult_exclude=y&order=daily"
            }
            else -> {
                //로맨스
                "https://ridibooks.com/bestsellers/romance_serial?rent=n&adult=n&adult_exclude=y&order=daily"
            }
        }
    }

    fun setOneStoreGenre(mContext: Context): String {
        return when {
            getGenre(mContext) == "BL" -> {
                "fantasy"
            }
            getGenre(mContext) == "FANTASY" -> {
                "fantasy"
            }
            getGenre(mContext) == "ROMANCE" -> {
                "romance"
            }
            else -> {
                "fantasy"
            }
        }
    }

    fun setNaverTodayGenre(mContext: Context): String {
        return when {
            getGenre(mContext) == "BL" -> {
                //현판
                "https://novel.naver.com/webnovel/ranking?genre=110&periodType=DAILY"
            }
            getGenre(mContext) == "FANTASY" -> {
                "https://novel.naver.com/webnovel/ranking?genre=102&periodType=DAILY"
            }
            getGenre(mContext) == "ROMANCE" -> {
                "https://novel.naver.com/webnovel/ranking?genre=101&periodType=DAILY"
            }
            else -> {
                //무협
                "https://novel.naver.com/webnovel/ranking?genre=103&periodType=DAILY"
            }
        }
    }

    fun setNaverChallengeGenre(mContext: Context): String {
        return when {
            getGenre(mContext) == "BL" -> {
                //현판
                "https://novel.naver.com/challenge/ranking?genre=110&periodType=DAILY"
            }
            getGenre(mContext) == "FANTASY" -> {
                "https://novel.naver.com/challenge/ranking?genre=102&periodType=DAILY"
            }
            getGenre(mContext) == "ROMANCE" -> {
                "https://novel.naver.com/challenge/ranking?genre=101&periodType=DAILY"
            }
            else -> {
                //무협
                "https://novel.naver.com/challenge/ranking?genre=103&periodType=DAILY"
            }
        }
    }

    fun setMrBlueGenre(mContext: Context): String {
        return when {
            getGenre(mContext) == "BL" -> {
                //현판
                "https://www.mrblue.com/novel/best/bl/daily"
            }
            getGenre(mContext) == "FANTASY" -> {
                "https://www.mrblue.com/novel/best/fanmu/daily"
            }
            getGenre(mContext) == "ROMANCE" -> {
                "https://www.mrblue.com/novel/best/romance/daily"
            }
            else -> {
                "https://www.mrblue.com/novel/best/all/realtime"
            }
        }
    }

    fun setKakaoStageGenre(mContext: Context): String {
        return when {
            getGenre(mContext) == "BL" -> {
                "6"
            }
            getGenre(mContext) == "FANTASY" -> {
                "1"
            }
            getGenre(mContext) == "ROMANCE" -> {
                "4"
            }
            else -> {
                "7"
            }
        }
    }

}

object Mining {
    fun runMining(context: Context, cate: String) {
//        getRidiBest(context, cate)
//        getOneStoreBest(context, cate)
//        getKakaoBest(cate)
//        getKakaoStageBest(context, cate)
        getJoaraBest(context, cate)
//        getJoaraBestNobless(context, cate)
//        getJoaraBestPremium(context, cate)
//        getNaverToday(context, cate)
//        getNaverChallenge(context, cate)
//        getNaverBest(context, cate)
//        getMrBlueBest(context, cate)
    }

    fun getMrBlueBest(context: Context, cate: String) {

        val doc: Document =
            Jsoup.connect(Genre.setMrBlueGenre(context)).post()
        val MrBlue: Elements = doc.select(".list-box ul li")
        val MrBlueRef: MutableMap<String?, Any> = HashMap()

        val yesterdayRef = mRootRef.child("best").child("MrBlue").child(cate).child("today").child(
            DBDate.Yesterday()
        )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addValueEventListener(object : ValueEventListener {
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

                    MrBlueRef["writerName"] = MrBlue.select(".txt-box .name > a")[i].attr("title")
                    MrBlueRef["subject"] = title
                    MrBlueRef["bookImg"] = MrBlue.select(".img img")[i].absUrl("src")
                    MrBlueRef["bookCode"] = MrBlue.select(".txt-box a")[i].absUrl("href")
                    MrBlueRef["info1"] = MrBlue.select(".txt-box .name > a")[i].absUrl("href")
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



    }

    fun getNaverToday(context: Context, cate: String) {

        val doc: Document =
            Jsoup.connect(Genre.setNaverTodayGenre(context)).post()
        val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
        val NaverRef: MutableMap<String?, Any> = HashMap()

        val yesterdayRef = mRootRef.child("best").child("Naver Today").child(cate).child("today").child(
            DBDate.Yesterday()
        )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addValueEventListener(object : ValueEventListener {
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
                    NaverRef["info2"] = Naver[i].select(".num_total").next().first()!!.text()
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
    }

    fun getNaverChallenge(context: Context, cate: String) {

        val doc: Document =
            Jsoup.connect(Genre.setNaverChallengeGenre(context)).post()
        val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
        val NaverRef: MutableMap<String?, Any> = HashMap()

        val yesterdayRef = mRootRef.child("best").child("Naver Challenge").child(cate).child("today").child(
            DBDate.Yesterday()
        )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addValueEventListener(object : ValueEventListener {
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
                    NaverRef["info2"] = Naver[i].select(".num_total").next().first()!!.text()
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
    }

    fun getNaverBest(context: Context, cate: String) {

        val doc: Document =
            Jsoup.connect(Genre.setNaverGenre(context)).post()
        val Naver: Elements = doc.select(".ranking_wrap_left .list_ranking li")
        val NaverRef: MutableMap<String?, Any> = HashMap()


        val yesterdayRef = mRootRef.child("best").child("Naver").child(cate).child("today").child(
            DBDate.Yesterday()
        )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addValueEventListener(object : ValueEventListener {
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
                    NaverRef["info2"] = Naver[i].select(".num_total").next().first()!!.text()
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



    }

    fun getRidiBest(context: Context, cate: String) {

        val doc: Document =
            Jsoup.connect(Genre.setRidiGenre(context)).post()
        val Ridi: Elements = doc.select(".book_thumbnail_wrapper")
        val RidiRef: MutableMap<String?, Any> = HashMap()

        val yesterdayRef = mRootRef.child("best").child("Ridi").child(cate).child("today").child(
            DBDate.Yesterday()
        )

        try{
            val itemsYesterday = ArrayList<BookListDataBestToday?>()

            yesterdayRef.addValueEventListener(object : ValueEventListener {
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
                        RidiRef["bookImg"] = Ridi.select(".thumbnail_image .thumbnail")[i].absUrl("data-src")
                        RidiRef["bookCode"] = Ridi.select("a")[i].absUrl("href")
                        RidiRef["info1"] = doc.select(".count_num")[i].text()
                        RidiRef["info2"] = "추천 수 : " + doc.select("span .StarRate_ParticipantCount")[i].text()
                        RidiRef["info3"] = "평점 : " + doc.select("span .StarRate_Score")[i].text()
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
        } catch (exception : IOException){

        }
    }

    fun getOneStoreBest(context: Context, cate: String) {
        val OneStoryRef: MutableMap<String?, Any> = HashMap()

        val call: Call<OneStoreBookResult?>? =
            RetrofitOnestore.getBestOneStore(Genre.setOneStoreGenre(context))

        val yesterdayRef = mRootRef.child("best").child("OneStore").child(cate).child("today").child(
            DBDate.Yesterday()
        )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addValueEventListener(object : ValueEventListener {
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
                                    OneStoryRef["info3"] = "댓글 수 : " + productList[i].commentCount
                                    OneStoryRef["info4"] = " "
                                    OneStoryRef["info5"] = " "
                                    OneStoryRef["number"] = i
                                    OneStoryRef["numberDiff"] = calculateNum(i, productList[i].prodNm, itemsYesterday).num
                                    OneStoryRef["date"] = DBDate.DateMMDD()
                                    OneStoryRef["status"] = calculateNum(i, productList[i].prodNm, itemsYesterday).status

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

    }

    fun getKakaoStageBest(context: Context, cate: String) {
        val KakaoRef: MutableMap<String?, Any> = HashMap()

        val call: Call<List<BestResultKakaoStageNovel>?>? = RetrofitKaKao.getBestKakaoStage(
            "false",
            "YESTERDAY",
            Genre.setKakaoStageGenre(context),
            "72"
        )


        val yesterdayRef = mRootRef.child("best").child("Kakao Stage").child(cate).child("today").child(
            DBDate.Yesterday()
        )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addValueEventListener(object : ValueEventListener {
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

                call!!.enqueue(object : Callback<List<BestResultKakaoStageNovel>?> {
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
                                    KakaoRef["numberDiff"] = calculateNum(i, novel.title, itemsYesterday).num
                                    KakaoRef["date"] = DBDate.DateMMDD()
                                    KakaoRef["status"] = calculateNum(i, novel.title, itemsYesterday).status

                                    miningValue(KakaoRef, i, "Kakao Stage", cate)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<BestResultKakaoStageNovel>?>, t: Throwable) {
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

        yesterdayRef.addValueEventListener(object : ValueEventListener {
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
                                    KakaoRef["numberDiff"] = calculateNum(i, list[i].title, itemsYesterday).num
                                    KakaoRef["date"] = DBDate.DateMMDD()
                                    KakaoRef["status"] = calculateNum(i, list[i].title, itemsYesterday).status

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

    fun getJoaraBest(context: Context, cate: String) {
        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val param = Param.getItemAPI(context)
        param["best"] = "today"
        param["store"] = ""
        param["category"] = Genre.setJoaraGenre(context)

        val yesterdayRef = FirebaseDatabase.getInstance().reference.child("best").child("Joara").child(cate).child("today").child(
            DBDate.Yesterday()
        )

        val items = ArrayList<BookListDataBestToday?>()

        val apiJoara = RetrofitJoara2()
        var books: List<JoaraBestListValue>? = null

        apiJoara.getJoaraBookBest(param, object : RetrofitDataListener<JoaraBestListResult> {
            override fun onSuccess(data: JoaraBestListResult) {

                books = data.bookLists

                Log.d("####", "####-1" + books.toString())

                for (i in books!!.indices) {

                    JoaraRef["writerName"] = books!![i].writerName
                    JoaraRef["subject"] = books!![i].subject
                    JoaraRef["bookImg"] = books!![i].bookImg
                    JoaraRef["bookCode"] = books!![i].bookCode
                    JoaraRef["info1"] = "줄거리 : " + books!![i].intro
                    JoaraRef["info2"] = "총 " + books!![i].cntChapter + " 화"
                    JoaraRef["info3"] = "조회 수 : " + books!![i].cntPageRead
                    JoaraRef["info4"] = "선호작 수 : " + books!![i].cntFavorite
                    JoaraRef["info5"] = "추천 수 : " + books!![i].cntRecom
                    JoaraRef["number"] = i
                    JoaraRef["numberDiff"] = calculateNum(i, books!![i].subject, items).num
                    JoaraRef["date"] = DBDate.DateMMDD()
                    JoaraRef["status"] = calculateNum(i, books!![i].subject, items).status

                    miningValue(JoaraRef, i, "Joara", cate)
                }
            }
        })

        Log.d("####", "####-2" + books.toString())

//        yesterdayRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                var num = 0
//
//                for (postSnapshot in dataSnapshot.children) {
//                    val group: BookListDataBestToday? =
//                        postSnapshot.getValue(BookListDataBestToday::class.java)
//
//                    BookListDataBestToday(
//                        group!!.writer,
//                        group.title,
//                        group.bookImg,
//                        group.bookCode,
//                        group.info1,
//                        group.info2,
//                        group.info3,
//                        group.info4,
//                        group.info5,
//                        group.number,
//                        group.numberDiff,
//                        group.date,
//                        group.status
//                    )
//
//                    if(items[num] != null){
//
//                        val title = items[num]?.title ?: ""
//
//                        JoaraRef["writer"] = items[num]?.writer ?: ""
//                        JoaraRef["title"] = title
//                        JoaraRef["bookImg"] = items[num]?.bookImg ?: ""
//                        JoaraRef["bookCode"] = items[num]?.bookCode ?: ""
//                        JoaraRef["info1"] = items[num]?.info1 ?: ""
//                        JoaraRef["info2"] = items[num]?.info1 ?: ""
//                        JoaraRef["info3"] = items[num]?.info1 ?: ""
//                        JoaraRef["info4"] = items[num]?.info1 ?: ""
//                        JoaraRef["info5"] = items[num]?.info1 ?: ""
//                        JoaraRef["number"] = items[num]?.number ?: ""
//                        JoaraRef["numberDiff"] = calculateNum(num, title, items).num
//                        JoaraRef["date"] = DBDate.DateMMDD()
//                        JoaraRef["status"] = calculateNum(num, title, items).status
//
//                        miningValue(JoaraRef, num, "Joara", cate)
//                        num += 1
//                    }
//                }
//            }
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
    }

    fun getJoaraBestPremium(context: Context, cate: String) {
        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val call: Call<JoaraBestListResult?>? =
            RetrofitJoara.getJoaraBookBest("today", "premium", Genre.setJoaraGenre(context))

        val yesterdayRef = FirebaseDatabase.getInstance().reference.child("best").child("Joara Premium").child(cate).child("today").child(
            DBDate.Yesterday()
        )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addValueEventListener(object : ValueEventListener {
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
                                    JoaraRef["info1"] = "줄거리 : " + books[i].intro
                                    JoaraRef["info2"] = "총 " + books[i].cntChapter + " 화"
                                    JoaraRef["info3"] = "조회 수 : " + books[i].cntPageRead
                                    JoaraRef["info4"] = "선호작 수 : " + books[i].cntFavorite
                                    JoaraRef["info5"] = "추천 수 : " + books[i].cntRecom
                                    JoaraRef["number"] = i
                                    JoaraRef["numberDiff"] = calculateNum(i, books[i].subject, itemsYesterday).num
                                    JoaraRef["date"] = DBDate.DateMMDD()
                                    JoaraRef["status"] = calculateNum(i, books[i].subject, itemsYesterday).status

                                    miningValue(JoaraRef, i, "Joara Premium", cate)

                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<JoaraBestListResult?>, t: Throwable) {
                        Log.d("onFailure", "실패")
                    }
                })
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    fun getJoaraBestNobless(context: Context, cate: String) {
        val JoaraRef: MutableMap<String?, Any> = HashMap()

        val call: Call<JoaraBestListResult?>? =
            RetrofitJoara.getJoaraBookBest("today", "nobless", Genre.setJoaraGenre(context))


        val yesterdayRef = FirebaseDatabase.getInstance().reference.child("best").child("Joara Nobless").child(cate).child("today").child(
            DBDate.Yesterday()
        )

        val itemsYesterday = ArrayList<BookListDataBestToday?>()

        yesterdayRef.addValueEventListener(object : ValueEventListener {
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
                                    JoaraRef["info1"] = "줄거리 : " + books[i].intro
                                    JoaraRef["info2"] = "총 " + books[i].cntChapter + " 화"
                                    JoaraRef["info3"] = "조회 수 : " + books[i].cntPageRead
                                    JoaraRef["info4"] = "선호작 수 : " + books[i].cntFavorite
                                    JoaraRef["info5"] = "추천 수 : " + books[i].cntRecom
                                    JoaraRef["number"] = i
                                    JoaraRef["numberDiff"] = calculateNum(i, books[i].subject, itemsYesterday).num
                                    JoaraRef["date"] = DBDate.DateMMDD()
                                    JoaraRef["status"] = calculateNum(i, books[i].subject, itemsYesterday).status

                                    miningValue(JoaraRef, i, "Joara Nobless", cate)

                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<JoaraBestListResult?>, t: Throwable) {
                        Log.d("onFailure", "실패")
                    }
                })
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun miningValue(ref: MutableMap<String?, Any>, num: Int, type: String, cate: String) {

        //Today
        BestRef.setBestRefToday(type, num, cate).setValue(BestRef.setBookListDataBestToday(ref))

        //Week
        if (num < 10) {
            BestRef.setBestRefWeek(type, num, cate).setValue(BestRef.setBookListDataBestToday(ref))
        }

        for (i in (1000 * DBDate.DayInt())..((1000 * DBDate.DayInt()) + 999)) {
            BestRef.setBestRef(type, cate).child("week-list")
                .child(i.toString()).removeValue()
        }

        BestRef.setBestRefWeekCompared(type, num, cate).setValue(BestRef.setBookListDataBestToday(ref))

        //Month - Week
        if (num == 0) {
            //Month - Day
            BestRef.setBestRefMonthWeek(type, cate).setValue(BestRef.setBookListDataBestToday(ref))
            //Month
            BestRef.setBestRefMonth(type, cate).setValue(BestRef.setBookListDataBestToday(ref))
        }

        BestRef.setBestRefMonthDay(type, num, cate).setValue(BestRef.setBookListDataBestToday(ref))

    }

    fun calculateNum(num: Int?, title: String?, itemsYesterday : ArrayList<BookListDataBestToday?>): CalculNum {

        for (i in itemsYesterday) {
            if (i != null) {
                if (i.title == title) {
                    when {
                        i.number < num!! -> {
                            return CalculNum(num - i.number, "DOWN")
                        }
                        i.number > num -> {
                            return  CalculNum(num - i.number, "UP")
                        }
                        i.number == num -> {
                            CalculNum(0, "-")
                        } else -> {
                        CalculNum(0, "NEW")
                        }
                    }
                }
            }
        }
        return CalculNum(0, "-")
    }

}