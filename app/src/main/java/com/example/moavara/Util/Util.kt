package com.example.moavara.Util

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.DataBase.DataBestDay
import com.example.moavara.Search.BookListDataBestToday
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

object DBDate {

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

    @SuppressLint("SimpleDateFormat")
    fun Date(): String {
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

    private fun setBestRef(type: String, genre: String): DatabaseReference {
        return mRootRef.child("best").child(type).child(genre)
    }

    fun delBestRefWeekList(type: String, genre: String): DatabaseReference {
        return setBestRef(type, genre).child("week list")
    }

    fun setBestRefWeekList(type: String, num: Int, genre: String): DatabaseReference {
        return setBestRef(type, genre).child("week list")
            .child(((DBDate.DayInt() * 1000) + num).toString())
    }

    fun setBestRefWeekCompared(type: String, num: Int, genre: String): DatabaseReference {
        return setBestRef(type, genre).child("week list")
            .child(((DBDate.DayInt() * 1000) + num).toString())
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
            ref["writerName"] as String?,
            ref["subject"] as String?,
            ref["bookImg"] as String?,
            ref["bookCode"] as String?,
            ref["info1"] as String?,
            ref["info2"] as String?,
            ref["info3"] as String?,
            ref["info4"] as String?,
            ref["info5"] as String?,
            ref["number"] as Int?,
            ref["date"] as String?,
            ref["status"] as String?,
        )
    }

    fun setDataBestDay(ref: MutableMap<String?, Any>): DataBestDay {
        return DataBestDay(
            ref["writerName"] as String?,
            ref["subject"] as String?,
            ref["bookImg"] as String?,
            ref["bookCode"] as String?,
            ref["info1"] as String?,
            ref["info2"] as String?,
            ref["info3"] as String?,
            ref["info4"] as String?,
            ref["info5"] as String?,
            ref["number"] as Int?,
            ref["date"] as String?,
            ref["type"] as String?,
            ref["status"] as String?,
        )
    }
}

object Genre {

    fun getGenre(mContext : Context): String? {
        return if(mContext.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE).getString("GENRE", "ALL") != ""){
            mContext.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE).getString("GENRE", "ALL")
        } else
            "ALL"
    }

    fun setJoaraGenre(mContext : Context): String {
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

    fun setNaverGenre(mContext : Context): String {
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

    fun setRidiGenre(mContext : Context): String {
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

    fun setOneStoreGenre(mContext : Context): String {
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

    fun setNaverTodayGenre(mContext : Context): String {
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

    fun setNaverChallengeGenre(mContext : Context): String {
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

    fun setMrBlueGenre(mContext : Context): String {
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

    fun setKakaoStageGenre(mContext : Context): String {
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