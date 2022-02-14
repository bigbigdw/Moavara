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
    var bookCode: String ? = null,
    var info1: String ? = null,
    var info2: String ? = null,
    var info3: String ? = null,
    var info4: String ? = null,
    var info5: String ? = null,
    var number: Int ? = null,
    var date: String ? = null,
    var status: String ? = null,
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
    var link: String ? = null,
    var imgfile: String ? = null,
    var title: String ? = null,
    var startDate: String ? = null,
    var endDate: String ? = null,
    var type: String ? = null,
    var memo: String ? = null
)