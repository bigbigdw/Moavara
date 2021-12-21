package com.example.takealook.Search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//검색결과
class SearchResultKakao {

    @SerializedName("results")
    @Expose
    val results: List<KakaoBookResult>? = null

}

class KakaoBookResult {
    @SerializedName("items")
    @Expose
    var items: List<KakaoBookItem>? = null
}

class KakaoBookItem {
    @SerializedName("author")
    @Expose
    var author: String? = null

    @SerializedName("image_url")
    @Expose
    var image_url: String? = null

    @SerializedName("publisher_name")
    @Expose
    var publisher_name: String? = null

    @SerializedName("sub_category")
    @Expose
    var sub_category: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null
}