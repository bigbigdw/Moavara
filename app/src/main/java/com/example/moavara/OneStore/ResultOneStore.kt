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
    var prodId: String? = null

    @SerializedName("prodNm")
    @Expose
    var prodNm: String? = null

    @SerializedName("artistNm")
    @Expose
    var artistNm: String? = null

    @SerializedName("totalCount")
    @Expose
    var totalCount: String? = null

    @SerializedName("avgScore")
    @Expose
    var avgScore: String? = null

    @SerializedName("commentCount")
    @Expose
    var commentCount: String? = null

    @SerializedName("thumbnailImageUrl")
    @Expose
    var thumbnailImageUrl: String? = null
}

//