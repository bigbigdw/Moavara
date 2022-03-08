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
    val content:String? = null


    @SerializedName("title")
    @Expose
    val title:String? = null

    @SerializedName("wdate")
    @Expose
    val wdate:String? = null
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
    val content:String? = null

    @SerializedName("title")
    @Expose
    val title:String? = null

    @SerializedName("endtime")
    @Expose
    val endtime:String? = null

    @SerializedName("starttime")
    @Expose
    val starttime:String? = null
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

    @SerializedName("total_cnt")
    @Expose
    var totalCnt: String? = null
}

//조아라 베스트
class JoaraBestListValue {
    @SerializedName("writer_name")
    @Expose
    var writerName: String? = null

    @SerializedName("subject")
    @Expose
    var subject: String? = null

    @SerializedName("book_img")
    @Expose
    var bookImg: String? = null

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

    @SerializedName("book_code")
    @Expose
    var bookCode: String? = null

    @SerializedName("category_ko_name")
    @Expose
    var categoryKoName: String? = null

    @SerializedName("cnt_chapter")
    @Expose
    var cntChapter: String? = null

    @SerializedName("cnt_favorite")
    @Expose
    var cntFavorite: String? = null

    @SerializedName("cnt_recom")
    @Expose
    var cntRecom: String? = null

    @SerializedName("cnt_page_read")
    @Expose
    var cntPageRead: String? = null
}

// 토큰 체크
class CheckTokenResult {
    @SerializedName("status")
    @Expose
    val status = 0
}

//로그인
class LoginResult {
    @SerializedName("user")
    @Expose
    val user: UserValue? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}

//로그아웃
class LogoutResult {
    @SerializedName("status")
    @Expose
    var status: String? = null
}

//로그인 - 유저 정보
class UserValue {
    @SerializedName("nickname")
    @Expose
    var nickname: String? = null

    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("mana")
    @Expose
    var mana: String? = null

    @SerializedName("expire_cash")
    @Expose
    var expireCash: String? = null

    @SerializedName("cash")
    @Expose
    var cash: String? = null

    @SerializedName("manuscript_coupon")
    @Expose
    var manuscriptCoupon: String? = null

    @SerializedName("support_coupon")
    @Expose
    var supportCoupon: String? = null

    @SerializedName("member_id")
    @Expose
    var memberId: String? = null

    @SerializedName("profile")
    @Expose
    var profile: String? = null

    @SerializedName("grade")
    @Expose
    var grade: String? = null
}

//인덱스 API
class IndexAPIResult {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("banner")
    @Expose
    val banner: List<String>? = null

    @SerializedName("main_menu")
    @Expose
    val mainMenu: List<MainMenuValue>? = null
}

//메인 메뉴
class MainMenuValue {
    @SerializedName("menu_ver")
    @Expose
    var menuVer: String? = null

    @SerializedName("MainTab")
    @Expose
    var MainTab: List<MainTabInfoValue>? = null

    @SerializedName("TabInfo")
    @Expose
    var TabInfo: TabInfoValue? = null
}

//탭 정보
class TabInfoValue {
    @SerializedName("tabname")
    @Expose
    var tabname: String? = null
}

//매인 정보
class MainTabInfoValue {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("position")
    @Expose
    var position: String? = null
}

//검색결과
class JoaraSearchResult {

    @SerializedName("search_total_cnt")
    @Expose
    val joaraSearchTotalCnt: JoaraSearchTotalCntValue? = null

    @SerializedName("search_cnt")
    @Expose
    val searchCnt: String? = null

    @SerializedName("books")
    @Expose
    val books: List<JoaraBooksValue>? = null

    @SerializedName("status")
    @Expose
    val status: String? = null
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
