package com.example.moavara.Joara

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//조아라 이밴트 결과
class JoaraEventResult {
    @SerializedName("banner")
    @Expose
    val banner: List<JoaraEventValue>? = null
}

//공지사항 결과
class JoaraNoticeDetailResult {
    @SerializedName("notice")
    @Expose
    val notice: NoticeContents? = null
}

class NoticeContents {
    @SerializedName("content")
    @Expose
    val content:String = ""

    @SerializedName("title")
    @Expose
    val title:String = ""
}

//조아라 이밴트 상세 결과
class JoaraEventDetailResult {
    @SerializedName("event")
    @Expose
    val event: EventContents? = null

}

class EventContents{
    @SerializedName("content")
    @Expose
    val content:String = ""

    @SerializedName("title")
    @Expose
    val title:String = ""
}

//조아라 이벤트 결과 상세
class JoaraEventValue {
    @SerializedName("joaralink")
    @Expose
    var joaralink: String = ""

    @SerializedName("imgfile")
    @Expose
    var imgfile: String = ""
}

//조아라 베스트
class JoaraBestListResult {
    @SerializedName("books")
    @Expose
    val bookLists: List<JoaraBestListValue>? = null
}

//조아라 베스트
class JoaraBestListValue {
    @SerializedName("writer_name")
    @Expose
    var writerName: String = ""

    @SerializedName("subject")
    @Expose
    var subject: String = ""

    @SerializedName("book_img")
    @Expose
    var bookImg: String = ""

    @SerializedName("intro")
    @Expose
    var intro: String = ""

    @SerializedName("book_code")
    @Expose
    var bookCode: String = ""

    @SerializedName("cnt_chapter")
    @Expose
    var cntChapter: String = ""

    @SerializedName("cnt_favorite")
    @Expose
    var cntFavorite: String = ""

    @SerializedName("cnt_recom")
    @Expose
    var cntRecom: String = ""

    @SerializedName("cnt_page_read")
    @Expose
    var cntPageRead: String = ""
}

// 토큰 체크
class CheckTokenResult {
    @SerializedName("status")
    @Expose
    val status = 0
}

//검색결과
class JoaraSearchResult {

    @SerializedName("search_total_cnt")
    @Expose
    val joaraSearchTotalCnt: JoaraSearchTotalCntValue? = null

    @SerializedName("search_cnt")
    @Expose
    val searchCnt: String = ""

    @SerializedName("books")
    @Expose
    val books: List<JoaraBooksValue>? = null

    @SerializedName("status")
    @Expose
    val status: String = ""
}

//검색 토탈 카운트
class JoaraSearchTotalCntValue {
    @SerializedName("keyword_cnt")
    @Expose
    var keyword_cntJoara: JoaraKeyWordCntValue? = null

    @SerializedName("all")
    @Expose
    val all: String? = null

    @SerializedName("nobless")
    @Expose
    var nobless: String? = null

    @SerializedName("premium")
    @Expose
    var premium: String? = null

    @SerializedName("series")
    @Expose
    var series: String? = null
}

//키워드 카운트
class JoaraKeyWordCntValue {
    @SerializedName("all")
    @Expose
    var all: String? = null

    @SerializedName("intro")
    @Expose
    var intro: String? = null

    @SerializedName("keyword")
    @Expose
    var keyword: String? = null

    @SerializedName("subject")
    @Expose
    var subject: String? = null

    @SerializedName("writer_nickname")
    @Expose
    var writerNickname: String? = null
}

//북 Values
class JoaraBooksValue {
    @SerializedName("writer_name")
    @Expose
    var writer_name: String? = null

    @SerializedName("subject")
    @Expose
    var subject: String? = null

    @SerializedName("book_img")
    var book_img: String? = null

    @SerializedName("is_adult")
    @Expose
    var isAdult: String? = null

    @SerializedName("is_finish")
    @Expose
    var isFinish: String? = null

    @SerializedName("is_premium")
    @Expose
    var isPremium: String? = null

    @SerializedName("is_nobless")
    @Expose
    var isNobless: String? = null

    @SerializedName("intro")
    @Expose
    var intro: String? = null

    @SerializedName("is_favorite")
    @Expose
    var isFavorite: String? = null

    @SerializedName("cnt_page_read")
    @Expose
    var cntPageRead: String? = null

    @SerializedName("cnt_recom")
    @Expose
    var cntRecom: String? = null

    @SerializedName("cnt_favorite")
    @Expose
    var cntFavorite: String? = null

    @SerializedName("bookCode")
    @Expose
    var bookCode: String? = null

    @SerializedName("category_ko_name")
    @Expose
    var categoryKoName: String? = null
}
