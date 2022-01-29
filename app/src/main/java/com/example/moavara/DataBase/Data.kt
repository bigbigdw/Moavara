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
    var writer: String ? = null,
    var title: String ? = null,
    var bookImg: String ? = null,
    var intro: String ? = null,
    var bookCode: String ? = null,
    var cntChapter: String ? = null,
    var cntPageRead: String ? = null,
    var cntFavorite: String ? = null,
    var cntRecom: String ? = null,
    var number: Int ? = null,
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
    var idx: String ? = null,
    var imgfile: String ? = null,
    var is_banner_cnt: String ? = null,
    var joaralink: String ? = null
)