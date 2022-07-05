package com.example.moavara.Retrofit

import com.example.moavara.ETC.API
import com.example.moavara.ETC.HELPER
import retrofit2.Call
import retrofit2.http.*

//조아라 검색
interface ServiceSearchJoara {
    @GET("v1/search/query_bc_v2.joa" + HELPER.ETC)
    fun getRetrofit(
        @Query("page") page: Int?,
        @Query("query") query: String?,
        @Query("collection") collection: String?,
        @Query("search") search: String?,
        @Query("kind") kind: String?,
        @Query("category") category: String?,
        @Query("min_chapter") min_chapter: String?,
        @Query("max_chapter") max_chapter: String?,
        @Query("interval") interval: String?,
        @Query("orderby") orderby: String?,
        @Query("except_query") except_query: String?,
        @Query("except_search") except_search: String?,
        @Query("expr_point") expr_point: String?,
        @Query("score_point") score_point: String?,
    ): Call<JoaraSearchResult?>?
}

//조아라 이벤트
//조아라 이벤트 상세
interface JoaraEventService {
    @GET(API.EVENT_JOA + HELPER.ETC)
    fun getRetrofit(
        @Query("page") page: String?,
        @Query("banner_type") banner_type: String?,
        @Query("category") category: String?,
    ): Call<JoaraEventResult?>?

    @GET(API.EVENT_DETAIL_JOA + HELPER.ETC)
    fun getEventDetail(
        @Query("event_id") event_id: String?,
    ): Call<JoaraEventDetailResult?>?

    @GET(API.NOTICE_DETAIL_JOA + HELPER.ETC)
    fun getNoticeDetail(
        @Query("notice_id") notice_id: String?,
    ): Call<JoaraNoticeDetailResult?>?
}


//조아라 베스트
interface JoaraBestService {

    @GET("v1/book/detail.joa")
    fun getBookDetail(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestDetailResult>

    @GET("v1/board/book_comment.joa")
    fun getBookComment(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestDetailCommentsResult>

    @GET("v1/book/other")
    fun getBookOther(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestListResult>

    @GET("v1/board/board_list.joa")
    fun getBoardListJoa(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBoardResult>
}

