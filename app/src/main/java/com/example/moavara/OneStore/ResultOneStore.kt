package com.example.moavara.OneStore

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

//