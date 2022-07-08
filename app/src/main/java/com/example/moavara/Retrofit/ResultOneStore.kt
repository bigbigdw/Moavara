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

class BestToksodaResult {
    @SerializedName("resultList")
    @Expose
    val resultList: List<BestToksodaToksodaResultList>? = null
}

class BestToksodaToksodaResultList{
    @SerializedName("athrnm")
    @Expose
    var athrnm: String = ""

    @SerializedName("brcd")
    @Expose
    var brcd: String = ""

    @SerializedName("wrknm")
    @Expose
    var wrknm: String = ""

    @SerializedName("whlEpsdCnt")
    @Expose
    var whlEpsdCnt: String = ""

    @SerializedName("lnIntro")
    @Expose
    var lnIntro: String = ""

    @SerializedName("lgctgrNm")
    @Expose
    var lgctgrNm: String = ""

    @SerializedName("inqrCnt")
    @Expose
    var inqrCnt: String = ""

    @SerializedName("imgPath")
    @Expose
    var imgPath: String = ""

    @SerializedName("intrstCnt")
    @Expose
    var intrstCnt: String = ""
}

class BestToksodaDetailResult {
    @SerializedName("result")
    @Expose
    val result: BestToksodaDetailResultContents? = null
}

class BestToksodaDetailResultContents{
    @SerializedName("athrnm")
    @Expose
    var athrnm: String = ""

    @SerializedName("cmntCnt")
    @Expose
    var cmntCnt: String = ""

    @SerializedName("goodCnt")
    @Expose
    var goodCnt: String = ""

    @SerializedName("hashTagList")
    @Expose
    val hashTagList: List<BestToksodaDetailHashTagList>? = null

    @SerializedName("imgPath")
    @Expose
    var imgPath: String = ""

    @SerializedName("inqrCnt")
    @Expose
    var inqrCnt: String = ""

    @SerializedName("intrstCnt")
    @Expose
    var intrstCnt: String = ""

    @SerializedName("lnIntro")
    @Expose
    var lnIntro: String = ""

    @SerializedName("wrknm")
    @Expose
    var wrknm: String = ""

    @SerializedName("lgctgrNm")
    @Expose
    var lgctgrNm: String = ""
}

class BestToksodaDetailHashTagList {
    @SerializedName("hashtagNm")
    @Expose
    var hashtagNm: String = ""
}

class BestToksodaDetailCommentResult {
    @SerializedName("result")
    @Expose
    val result: BestToksodaDetailCommentContents? = null
}

class BestToksodaDetailCommentContents{
    @SerializedName("commentList")
    @Expose
    val commentList: List<BestToksodaDetailCommentList>? = null
}

class BestToksodaDetailCommentList{
    @SerializedName("cmntCntts")
    @Expose
    var cmntCntts: String = ""

    @SerializedName("rgstDtime")
    @Expose
    var rgstDtime: String = ""
}

class BestToksodaSearchResult {
    @SerializedName("resultList")
    @Expose
    val resultList: List<BestToksodaSearchList>? = null
}

class BestToksodaSearchList{
    @SerializedName("BOOK_NM")
    @Expose
    var BOOK_NM: String = ""

    @SerializedName("AUTHOR")
    @Expose
    var AUTHOR: String = ""

    @SerializedName("HASHTAG_NM")
    @Expose
    var HASHTAG_NM: String = ""

    @SerializedName("IMG_PATH")
    @Expose
    var IMG_PATH: String = ""

    @SerializedName("BARCODE")
    @Expose
    var BARCODE: String = ""

    @SerializedName("INTRO")
    @Expose
    var INTRO: String = ""

    @SerializedName("LGCTGR_NM")
    @Expose
    var LGCTGR_NM: String = ""

    @SerializedName("PUB_NM")
    @Expose
    var PUB_NM: String = ""
}

class BestBannerListResult {
    @SerializedName("resultList")
    @Expose
    val resultList: List<BestBannerListContents>? = null
}

class BestBannerListContents{
    @SerializedName("imgPath")
    @Expose
    var imgPath: String = ""

    @SerializedName("linkInfo")
    @Expose
    var linkInfo: String = ""

    @SerializedName("bnnrNm")
    @Expose
    var bnnrNm: String = ""
}