package com.example.moavara.KaKao

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

class BestResultKakaoStage {
    val list: List<BestResultKakaoStageNovel>? = null
}

class BestResultKakaoStageNovel {
    @SerializedName("novel")
    @Expose
    val novel: KakaoBestStageResult? = null
}

class KakaoBestStageResult {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("stageSeriesNumber")
    @Expose
    var stageSeriesNumber: String? = null

    @SerializedName("nickname")
    @Expose
    var nickname: KakaoBestStageNickNameResult? = null

    @SerializedName("synopsis")
    @Expose
    var synopsis: String? = null

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: KakaoBestStageThumbnailResult? = null

    @SerializedName("publishedEpisodeCount")
    @Expose
    var publishedEpisodeCount: String? = null

    @SerializedName("viewCount")
    @Expose
    var viewCount: String? = null

    @SerializedName("visitorCount")
    @Expose
    var visitorCount: String? = null
}

class KakaoBestStageThumbnailResult {
    @SerializedName("url")
    @Expose
    var url: String? = null
}

class KakaoBestStageNickNameResult {
    @SerializedName("name")
    @Expose
    var name: String? = null
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