package com.example.takealook.KaKao

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

//베스트 결과
class BestResultKakao {
    @SerializedName("list")
    @Expose
    val list: List<KakaoBestBookResult>? = null
}

class KakaoBestBookResult {
    @SerializedName("author")
    @Expose
    var author: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("like_count")
    @Expose
    var like_count: String? = null

    @SerializedName("rating")
    @Expose
    var rating: String? = null

    @SerializedName("series_id")
    @Expose
    var series_id: String? = null

    @SerializedName("read_count")
    @Expose
    var read_count: String? = null

    @SerializedName("promotion_rate")
    @Expose
    var promotion_rate: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null
}