package com.example.moavara.Search

import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.github.mikephil.charting.data.BarEntry


class BookListData(
    var platform: String = "",
    var title: String = "",
    var writer: String = "",
    var bookImg: String = "",
    var bookCode: String = "",
    var info1: String = "",
    var info2: String = "",
    var info3: String = "",
)

class BestType(
    var title: String? = null,
    var type: String? = null,
)

class WeekendDate(
    var sun: String = "",
    var mon: String = "",
    var tue: String = "",
    var wed: String = "",
    var thur: String = "",
    var fri: String = "",
    var sat: String = "",
)

class BestRankListWeekend(
    var sun: BookListDataBestAnalyze? = null,
    var mon: BookListDataBestAnalyze? = null,
    var tue: BookListDataBestAnalyze? = null,
    var wed: BookListDataBestAnalyze? = null,
    var thur: BookListDataBestAnalyze? = null,
    var fri: BookListDataBestAnalyze? = null,
    var sat: BookListDataBestAnalyze? = null,
    var mode: String = ""
)

class BookListDataBestWeekend(
    var sun: BookListDataBest? = null,
    var mon: BookListDataBest? = null,
    var tue: BookListDataBest? = null,
    var wed: BookListDataBest? = null,
    var thur: BookListDataBest? = null,
    var fri: BookListDataBest? = null,
    var sat: BookListDataBest? = null,
)

class BookListDataBestMonthNum(
    var sun: Int = 0,
    var mon: Int = 0,
    var tue: Int = 0,
    var wed: Int = 0,
    var thur: Int = 0,
    var fri: Int = 0,
    var sat: Int = 0,
)

class CalculNum(
    var num: Int = 0,
    var status: String = ""
)

class EventDetailData(
    var title: String = "",
    var imgFile: String = "",
    var wDate: String = "",
    var sDate: String = "",
    var cntRead: String = "",
    var chart: BestChart? = null
)

class EventDetailDataMining(
    var date: String = "",
    var cntRead: String = "",
)

class EventData(
    var link: String = "",
    var imgfile: String = "",
    var title: String = "",
    var genre: String = "",
    var type: String = "",
    var memo: String = ""
)

data class BestComment(
    var comment: String = "",
    var date: String = ""
)

data class CommunityBoard(
    var title: String = "",
    var nid: String = "",
    var date: String = ""
)

data class BestChart(
    var dateList: MutableList<String>? = null,
    var entryList: MutableList<BarEntry>? = null,
    var title: String = "",
    var color: String = ""
)

data class UserInfo(
    var Nickname: String = "",
    var Genre: String = "",
)

data class UserPickEvent(
    var link: String = "",
    var imgfile: String = "",
    var title: String = "",
    var genre: String = "",
    var type: String = "",
    var memo: String = ""
)

data class UserPickBook(
    var writer: String = "",
    var title: String = "",
    var bookImg: String = "",
    var bookCode: String = "",
    var info1: String = "",
    var info2: String = "",
    var info3: String = "",
    var info4: String = "",
    var info5: String = "",
    var number: Int = 0,
    var numberDiff: Int = 0,
    var date: String = "",
    var type: String = "",
    var status: String = "",
    var trophyCount: Int = 0,
    var data: ArrayList<BookListDataBestAnalyze>? = null,
    var memo: String = "",
)

data class NewsBX(
    var date: String = "",
    var keyWord: String = "",
    var NewsTitle: String = "",
    var NewsUrl: String = "",
    var summary: String = "",
    var opinion: String = "",
)

