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