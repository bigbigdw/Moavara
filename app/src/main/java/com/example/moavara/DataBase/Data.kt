package com.example.moavara.Search

import com.github.mikephil.charting.data.BarEntry


class BookListData(
    var writer: String?,
    var title: String?,
    var bookImg: String?,
    var isAdult: String?,
    var isFinish: String?,
    var isPremium: String?,
    var isNobless: String?,
    var intro: String?,
    var isFav: String?,
    var cntPageRead: String?,
    var cntRecom: String?,
    var cntFavorite: String?,
    var bookCode: String?,
    var categoryKoName: String?
)

data class BookListDataBestToday(
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
    var status: String = "",
    var memo: String = ""
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


class BookListDataBestWeekend(
    var sun: BookListDataBestToday? = null,
    var mon: BookListDataBestToday? = null,
    var tue: BookListDataBestToday? = null,
    var wed: BookListDataBestToday? = null,
    var thur: BookListDataBestToday? = null,
    var fri: BookListDataBestToday? = null,
    var sat: BookListDataBestToday? = null,
)

class CalculNum(
    var num: Int = 0,
    var status: String = ""
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

data class BookListDataBestToday2(
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
    var status: String = "",
    var memo: String = ""
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
    var memo: String = "",
)

