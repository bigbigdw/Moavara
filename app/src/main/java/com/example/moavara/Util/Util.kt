package com.example.moavara.Util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.DataBase.TrophyInfo
import com.example.moavara.Search.BookListDataBestMonthNum
import com.example.moavara.Search.CalculNum
import java.text.SimpleDateFormat
import java.util.*


object DBDate {

    var status = ""

    fun Date(): Int {
        return Calendar.getInstance().get(Calendar.DATE)
    }

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
        val format = SimpleDateFormat("YYYYMMdd")
        return format.format(currentTime).toString()
    }

    fun Month(): String {
        return Calendar.getInstance().get(Calendar.MONTH).toString()
    }

    fun Year(): String {
        return Calendar.getInstance().get(Calendar.YEAR).toString()
    }


    fun getDateData(date : String) : TrophyInfo?{
        val parse_date: Date?
        val dateFormat1 = SimpleDateFormat("yyyyMMdd")

        try {
            parse_date = dateFormat1.parse(date)

            val cal = Calendar.getInstance()
            cal.time = parse_date
            val month = cal[Calendar.MONTH]

            val weekmonth = cal[Calendar.WEEK_OF_MONTH]

            val day = cal[Calendar.DAY_OF_WEEK]

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

    fun getYesterday(date : String) : Int {
        return (date.toInt() - 1)
    }

    fun getToday(date : String) : Int {
        return (date.toInt())
    }

    fun setMonthNum(date : Int) : BookListDataBestMonthNum {
        val bookListDataBestMonthNum = BookListDataBestMonthNum()

        if(date == 1){
            return BookListDataBestMonthNum(1,2,3,4,5,6,7)
        } else if (date == 2) {
            return BookListDataBestMonthNum(7,1,2,3,4,5,6)
        } else if (date == 3) {
            return BookListDataBestMonthNum(6, 7,1,2,3,4,5)
        }  else if (date == 4) {
            return BookListDataBestMonthNum(5,6, 7,1,2,3,4)
        }  else if (date == 5) {
            return BookListDataBestMonthNum(4, 5,6, 7,1,2,3)
        }  else if (date == 6) {
            return BookListDataBestMonthNum(3, 4, 5,6, 7,1,2)
        }  else if (date == 7) {
            return BookListDataBestMonthNum(2, 3, 4, 5,6, 7,1)
        }

        return bookListDataBestMonthNum
    }

    fun getMonthDates(date: BookListDataBestMonthNum, position: Int) : BookListDataBestMonthNum {
        if (date.sun == 1) {
            if (position == 0) {
                return BookListDataBestMonthNum(1, 2, 3, 4, 5, 6, 7)
            } else if (position == 1) {
                return BookListDataBestMonthNum(8, 9, 10, 11, 12, 13, 14)
            } else if (position == 2) {
                return BookListDataBestMonthNum(15, 16, 17, 18, 19, 20, 21)
            } else if (position == 3) {
                return BookListDataBestMonthNum(22, 23, 24, 25, 26, 27, 28)
            } else if (position == 4) {
                return BookListDataBestMonthNum(29, 30, 31, 0, 0, 0, 0)
            }
            return date
        } else if (date.mon == 1) {
            if (position == 0) {
                return BookListDataBestMonthNum(0, 1, 2, 3, 4, 5, 6)
            } else if (position == 1) {
                return BookListDataBestMonthNum(7, 8, 9, 10, 11, 12, 13)
            } else if (position == 2) {
                return BookListDataBestMonthNum(14, 15, 16, 17, 18, 19, 20)
            } else if (position == 3) {
                return BookListDataBestMonthNum(21, 22, 23, 24, 25, 26, 27)
            } else if (position == 4) {
                return BookListDataBestMonthNum(28, 29, 30, 31, 0, 0, 0)
            }
            return date
        } else if (date.tue == 1) {
            if (position == 0) {
                return BookListDataBestMonthNum(0, 0, 1, 2, 3, 4, 5)
            } else if (position == 1) {
                return BookListDataBestMonthNum(6, 7, 8, 9, 10, 11, 12)
            } else if (position == 2) {
                return BookListDataBestMonthNum(13, 14, 15, 16, 17, 18, 19)
            } else if (position == 3) {
                return BookListDataBestMonthNum(20, 21, 22, 23, 24, 25, 26)
            } else if (position == 4) {
                return BookListDataBestMonthNum(27, 28, 29, 30, 31, 0, 0)
            }
            return date
        } else if (date.wed == 1) {
            if (position == 0) {
                return BookListDataBestMonthNum(0, 0, 0, 1, 2, 3, 4)
            } else if (position == 1) {
                return BookListDataBestMonthNum(5, 6, 7, 8, 9, 10, 11)
            } else if (position == 2) {
                return BookListDataBestMonthNum(12, 13, 14, 15, 16, 17, 18)
            } else if (position == 3) {
                return BookListDataBestMonthNum(19, 20, 21, 22, 23, 24, 25)
            } else if (position == 4) {
                return BookListDataBestMonthNum(26, 27, 28, 29, 30, 31, 0)
            }
            return date
        } else if (date.thur == 1) {
            if (position == 0) {
                return BookListDataBestMonthNum(0, 0, 0, 0, 1, 2, 3)
            } else if (position == 1) {
                return BookListDataBestMonthNum(4, 5, 6, 7, 8, 9, 10)
            } else if (position == 2) {
                return BookListDataBestMonthNum(11, 12, 13, 14, 15, 16, 17)
            } else if (position == 3) {
                return BookListDataBestMonthNum(18, 19, 20, 21, 22, 23, 24)
            } else if (position == 4) {
                return BookListDataBestMonthNum(25, 26, 27, 28, 29, 30, 31)
            }
            return date
        } else if (date.fri == 1) {
            if (position == 0) {
                return BookListDataBestMonthNum(0, 0, 0, 0, 0, 1, 2)
            } else if (position == 1) {
                return BookListDataBestMonthNum(3, 4, 5, 6, 7, 8, 9)
            } else if (position == 2) {
                return BookListDataBestMonthNum(10, 11, 12, 13, 14, 15, 16)
            } else if (position == 3) {
                return BookListDataBestMonthNum(17, 18, 19, 20, 21, 22, 23)
            } else if (position == 4) {
                return BookListDataBestMonthNum(24, 25, 26, 27, 28, 29, 30)
            } else if (position == 5) {
                return BookListDataBestMonthNum(31, 0, 0, 0, 0, 0, 0)
            }
            return date
        }  else if (date.sat == 1) {
            if (position == 0) {
                return BookListDataBestMonthNum(0, 0, 0, 0, 0, 0, 1)
            } else if (position == 1) {
                return BookListDataBestMonthNum(2, 3, 4, 5, 6, 7, 8)
            } else if (position == 2) {
                return BookListDataBestMonthNum(9, 10, 11, 12, 13, 14, 15)
            } else if (position == 3) {
                return BookListDataBestMonthNum(16, 17, 18, 19, 20, 21, 22)
            } else if (position == 4) {
                return BookListDataBestMonthNum(23, 24, 25, 26, 27, 28, 29)
            } else if (position == 5) {
                return BookListDataBestMonthNum(30, 31, 0, 0, 0, 0, 0)
            }
            return date
        }
        return date
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

    fun setRidiGenre(str: String, page : Int): String {
        return when {
            str == "BL" -> {
                "https://ridibooks.com/bestsellers/bl-webnovel?order=daily&rent=n&adult=n&adult_exclude=y&page=${page}"
            }
            str == "FANTASY" -> {
                "https://ridibooks.com/bestsellers/fantasy_serial?order=daily&page=${page}"
            }
            str == "ROMANCE" -> {
                "https://ridibooks.com/bestsellers/romance_serial?rent=n&adult=n&adult_exclude=y&order=daily&page=${page}"
            }
            else -> {
                //로맨스
                "https://ridibooks.com/bestsellers/romance_serial?rent=n&adult=n&adult_exclude=y&order=daily&page=${page}"
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
                "6,7"
            }
            str == "FANTASY" -> {
                "1,2,3"
            }
            str == "ROMANCE" -> {
                "4,5"
            }
            else -> {
                "6,7,1,2,3,4,5"
            }
        }
    }
}

fun miningValue(ref: MutableMap<String?, Any>, num: Int, platform: String, genre: String) {

    BestRef.setBookCode(platform, genre, ref["bookCode"] as String, num).setValue(BestRef.setBookListDataBestAnalyze(ref))

    BestRef.setBestData(platform, num, genre).setValue(BestRef.setBookListDataBest(ref))

}

fun calculateNumDiff(
    num: Int,
    numYesterday: Int
): CalculNum {
    when {
        numYesterday < num -> {
            return CalculNum(num - numYesterday, "DOWN")
        }
        numYesterday > num -> {
            return CalculNum(num - numYesterday, "UP")
        }
        numYesterday == num -> {
            return CalculNum(0, "-")
        }
        else -> {
            return CalculNum(0, "NEW")
        }
    }
    return CalculNum(0, "-")
}

