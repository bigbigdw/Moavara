package com.example.moavara.Retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OneStoreBookResult {
    @SerializedName("params")
    @Expose
    var params: OneStoreBookResultParam? = null
}

class OneStoreBookResultParam {
    @SerializedName("productList")
    @Expose
    var productList: List<OnestoreBookItem>? = null
}

class OnestoreBookItem {
    @SerializedName("prodId")
    @Expose
    var prodId: String = ""

    @SerializedName("prodNm")
    @Expose
    var prodNm: String = ""

    @SerializedName("artistNm")
    @Expose
    var artistNm: String = ""

    @SerializedName("totalCount")
    @Expose
    var totalCount: String = ""

    @SerializedName("avgScore")
    @Expose
    var avgScore: String = ""

    @SerializedName("commentCount")
    @Expose
    var commentCount: String = ""

    @SerializedName("thumbnailImageUrl")
    @Expose
    var thumbnailImageUrl: String = ""
}

class OnestoreBookDetail {
    @SerializedName("params")
    @Expose
    var params: OnestoreBookDetailContents? = null
}

class OnestoreBookDetailContents {
    @SerializedName("artistNm")
    @Expose
    var artistNm: String = ""

    @SerializedName("favoriteCount")
    @Expose
    var favoriteCount: String = ""

    @SerializedName("menuNm")
    @Expose
    var menuNm: String = ""

    @SerializedName("orgFilePos")
    @Expose
    var orgFilePos: String = ""

    @SerializedName("pageViewTotal")
    @Expose
    var pageViewTotal: String = ""

    @SerializedName("ratingAvgScore")
    @Expose
    var ratingAvgScore: String = ""

    @SerializedName("serialCount")
    @Expose
    var serialCount: String = ""

    @SerializedName("prodNm")
    @Expose
    var prodNm: String = ""

}

class OnestoreBookDetailComment {
    @SerializedName("params")
    @Expose
    var params: OnestoreBookDetailCommentContents? = null
}

class OnestoreBookDetailCommentContents{
    @SerializedName("commentList")
    @Expose
    var commentList: List<OnestoreBookDetailCommentContentsList>? = null
}

class OnestoreBookDetailCommentContentsList {
    @SerializedName("commentDscr")
    @Expose
    var commentDscr: String = ""

    @SerializedName("regDate")
    @Expose
    var regDate: String = ""
}

class BestMoonpiaResult {
    @SerializedName("api")
    @Expose
    val api: BestMoonpiaContents? = null
}

class BestMoonpiaContents{
    @SerializedName("items")
    @Expose
    val items: List<BestMoonpiaContentsItems>? = null
}

class BestMoonpiaContentsItems{
    @SerializedName("author")
    @Expose
    var author: String = ""

    @SerializedName("nvStory")
    @Expose
    var nvStory: String = ""

    @SerializedName("nvSumEntryForGuest")
    @Expose
    var nvSumEntryForGuest: String = ""

    @SerializedName("nvSrl")
    @Expose
    var nvSrl: String = ""

    @SerializedName("nvCover")
    @Expose
    var nvCover: String = ""

    @SerializedName("nvTitle")
    @Expose
    var nvTitle: String = ""

    @SerializedName("nsrData")
    @Expose
    val nsrData: BestMoonpiaNsrData? = null
}

class BestMoonpiaNsrData {
    @SerializedName("hit")
    @Expose
    var hit: String = ""

    @SerializedName("hour")
    @Expose
    var hour: String = ""

    @SerializedName("number")
    @Expose
    var number: String = ""

    @SerializedName("prefer")
    @Expose
    var prefer: String = ""
}