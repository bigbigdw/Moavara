package com.example.takealook.Joara

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


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