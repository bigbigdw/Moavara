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
