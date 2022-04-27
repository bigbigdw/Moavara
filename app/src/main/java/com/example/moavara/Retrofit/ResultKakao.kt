package com.example.moavara.Retrofit

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
    var author: String = ""

    @SerializedName("image_url")
    @Expose
    var image_url: String = ""

    @SerializedName("publisher_name")
    @Expose
    var publisher_name: String = ""

    @SerializedName("sub_category")
    @Expose
    var sub_category: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""
}

class BestResultKakaoStageNovel {
    @SerializedName("novel")
    @Expose
    val novel: KakaoBestStageResult? = null
}

class KakaoBestStageResult {
    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("stageSeriesNumber")
    @Expose
    var stageSeriesNumber: String = ""

    @SerializedName("nickname")
    @Expose
    var nickname: KakaoBestStageNickNameResult? = null

    @SerializedName("synopsis")
    @Expose
    var synopsis: String = ""

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: KakaoBestStageThumbnailResult? = null

    @SerializedName("publishedEpisodeCount")
    @Expose
    var publishedEpisodeCount: String = ""

    @SerializedName("viewCount")
    @Expose
    var viewCount: String = ""

    @SerializedName("visitorCount")
    @Expose
    var visitorCount: String = ""
}

class KakaoBestStageThumbnailResult {
    @SerializedName("url")
    @Expose
    var url: String = ""
}

class KakaoBestStageNickNameResult {
    @SerializedName("name")
    @Expose
    var name: String = ""
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
    var author: String = ""

    @SerializedName("description")
    @Expose
    var description: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("like_count")
    @Expose
    var like_count: String = ""

    @SerializedName("rating")
    @Expose
    var rating: String = ""

    @SerializedName("series_id")
    @Expose
    var series_id: String = ""

    @SerializedName("read_count")
    @Expose
    var read_count: String = ""

    @SerializedName("caption")
    @Expose
    var caption: String = ""

    @SerializedName("image")
    @Expose
    var image: String = ""
}