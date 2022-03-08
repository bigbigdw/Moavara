package com.example.moavara.Search


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

class BookListDataBestToday(
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
)

class BestType(
    var title: String ? = null,
    var type: String ? = null,
)

class BestRank(
    var number: String = "",
    var type: String = "",
    var date: String = "",
    var status: String = "",
)


class BookListDataBestWeekend(
    var sun: BookListDataBestToday ? = null,
    var mon: BookListDataBestToday ? = null,
    var tue: BookListDataBestToday ? = null,
    var wed: BookListDataBestToday ? = null,
    var thur: BookListDataBestToday ? = null,
    var fri: BookListDataBestToday ? = null,
    var sat: BookListDataBestToday ? = null,
)

class EventData(
    var link: String = "",
    var imgfile: String = "",
    var title: String = "",
    var genre: String = "",
    var type: String = "",
    var memo: String = ""
)