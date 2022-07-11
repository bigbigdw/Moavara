package com.example.moavara.Util

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.moavara.DataBase.*
import com.example.moavara.Retrofit.*
import com.example.moavara.Search.CalculNum
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


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


    fun getDateData(date : String) : TrophyInfo?{
        var trophyInfo : TrophyInfo

        val parse_date: Date?
        val date = "2022${date.replace("-","")}"
        Log.d("!!!!-1", date)
        val dateFormat1 = SimpleDateFormat("yyyyMMdd")

        try {
            parse_date = dateFormat1.parse(date)

            val cal = Calendar.getInstance()
            cal.time = parse_date
            val month = cal[Calendar.MONTH]

            Log.d("!!!!-2", month.toString())

            val weekmonth = cal[Calendar.WEEK_OF_MONTH]
            Log.d("!!!!-3", weekmonth.toString())

            val day = cal[Calendar.DAY_OF_WEEK]
            Log.d("!!!!-5", day.toString())

            return TrophyInfo(
                month,
                weekmonth,
                day
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
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

    fun setJoaraGenre(str: String): String {
        return when {
            str == "BL" -> {
                "20"
            }
            str == "FANTASY" -> {
                "1"
            }
            str == "ROMANCE" -> {
                "5"
            }
            else -> {
                "0"
            }
        }
    }

    fun setToksodaGenre(str: String): String {
        return when {
            str == "BL" -> {
                "0008"
            }
            str == "FANTASY" -> {
                "0011"
            }
            str == "ROMANCE" -> {
                "0008"
            }
            else -> {
                "0007"
            }
        }
    }

    fun setNaverGenre(str: String): String {
        return when {
            str == "BL" -> {
                //현판
                "https://novel.naver.com/best/ranking?genre=110&periodType=DAILY"
            }
            str == "FANTASY" -> {
                "https://novel.naver.com/best/ranking?genre=102&periodType=DAILY"
            }
            str == "ROMANCE" -> {
                "https://novel.naver.com/best/ranking?genre=101&periodType=DAILY"
            }
            else -> {
                //무협
                "https://novel.naver.com/best/ranking?genre=103&periodType=DAILY"
            }
        }
    }

    fun setRidiGenre(str: String): String {
        return when {
            str == "BL" -> {
                "https://ridibooks.com/bestsellers/bl-webnovel?order=daily&rent=n&adult=n&adult_exclude=y"
            }
            str == "FANTASY" -> {
                "https://ridibooks.com/bestsellers/fantasy_serial?order=daily"
            }
            str == "ROMANCE" -> {
                "https://ridibooks.com/bestsellers/romance_serial?rent=n&adult=n&adult_exclude=y&order=daily"
            }
            else -> {
                //로맨스
                "https://ridibooks.com/bestsellers/romance_serial?rent=n&adult=n&adult_exclude=y&order=daily"
            }
        }
    }

    fun setOneStoreGenre(str: String): String {
        return when {
            str == "BL" -> {
                "DP13043"
            }
            str == "FANTASY" -> {
                "DP13042"
            }
            str == "ROMANCE" -> {
                "DP13041"
            }
            else -> {
                "DP13044"
            }
        }
    }



    fun setNaverTodayGenre(str: String): String {
        return when {
            str == "BL" -> {
                //현판
                "https://novel.naver.com/webnovel/ranking?genre=110&periodType=DAILY"
            }
            str == "FANTASY" -> {
                "https://novel.naver.com/webnovel/ranking?genre=102&periodType=DAILY"
            }
            str == "ROMANCE" -> {
                "https://novel.naver.com/webnovel/ranking?genre=101&periodType=DAILY"
            }
            else -> {
                //무협
                "https://novel.naver.com/webnovel/ranking?genre=103&periodType=DAILY"
            }
        }
    }

    fun setNaverChallengeGenre(str: String): String {
        return when {
            str == "BL" -> {
                //현판
                "https://novel.naver.com/challenge/ranking?genre=110&periodType=DAILY"
            }
            str == "FANTASY" -> {
                "https://novel.naver.com/challenge/ranking?genre=102&periodType=DAILY"
            }
            str == "ROMANCE" -> {
                "https://novel.naver.com/challenge/ranking?genre=101&periodType=DAILY"
            }
            else -> {
                //무협
                "https://novel.naver.com/challenge/ranking?genre=103&periodType=DAILY"
            }
        }
    }

    fun setMrBlueGenre(str: String): String {
        return when {
            str == "BL" -> {
                //현판
                "https://www.mrblue.com/novel/best/bl/daily"
            }
            str == "FANTASY" -> {
                "https://www.mrblue.com/novel/best/fanmu/daily"
            }
            str == "ROMANCE" -> {
                "https://www.mrblue.com/novel/best/romance/daily"
            }
            else -> {
                "https://www.mrblue.com/novel/best/all/realtime"
            }
        }
    }

    fun setKakaoStageGenre(str: String): String {
        return when {
            str == "BL" -> {
                "6"
            }
            str == "FANTASY" -> {
                "1"
            }
            str == "ROMANCE" -> {
                "4"
            }
            else -> {
                "7"
            }
        }
    }
}





fun miningValue(ref: MutableMap<String?, Any>, num: Int, platform: String, genre: String) {

    //Today
    BestRef.setBestRefToday(platform, num, genre).setValue(BestRef.setBookListDataBestToday(ref))

    //Week
    if (num < 10) {
        BestRef.setBestRefWeek(platform, num, genre).setValue(BestRef.setBookListDataBestToday(ref))
    }

    BestRef.setBestRefWeekCompared(platform, num, genre).setValue(BestRef.setBookListDataBestToday(ref))


    //Month - Week
    if (num == 0) {
        //Month - Day
        BestRef.setBestRefMonthWeek(platform, genre).setValue(BestRef.setBookListDataBestToday(ref))
        //Month
        BestRef.setBestRefMonth(platform, genre).setValue(BestRef.setBookListDataBestToday(ref))
    }

    BestRef.setBestRefMonthDay(platform, num, genre).setValue(BestRef.setBookListDataBestToday(ref))

}

fun calculateNum(
    num: Int?,
    title: String?,
    itemsYesterday: ArrayList<BookListDataBest>
): CalculNum {

    for (i in itemsYesterday) {
        if (i.title == title) {
            when {
                i.number < num!! -> {
                    return CalculNum(num - i.number, "DOWN")
                }
                i.number > num -> {
                    return CalculNum(num - i.number, "UP")
                }
                i.number == num -> {
                    CalculNum(0, "-")
                }
                else -> {
                    CalculNum(0, "NEW")
                }
            }
        }
    }
    return CalculNum(0, "-")
}

