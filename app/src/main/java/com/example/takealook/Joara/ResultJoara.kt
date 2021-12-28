package com.example.takealook.Joara

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//조아라 이밴트 결과
class JoaraEventResult {
    @SerializedName("banner")
    @Expose
    val banner: List<JoaraEventValue>? = null

}

//조아라 이밴트 결과 상세
class JoaraEventValue {
    @SerializedName("idx")
    @Expose
    var idx: String? = null

    @SerializedName("imgfile")
    @Expose
    var imgfile: String? = null

    @SerializedName("is_banner_cnt")
    @Expose
    var is_banner_cnt: String? = null

    @SerializedName("joaralink")
    @Expose
    var joaralink: String? = null
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
