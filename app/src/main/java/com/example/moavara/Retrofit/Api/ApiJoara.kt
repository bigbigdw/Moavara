package com.example.moavara.Retrofit.Api

import com.example.moavara.ETC.API
import com.example.moavara.ETC.HELPER
import com.example.moavara.Retrofit.*
import retrofit2.Call
import retrofit2.http.*


interface ApiJoara {

    @GET("v1/search/query_bc_v2.joa" + HELPER.ETC)
    fun getRetrofit(
        @Query("page") page: Int?,
        @Query("query") query: String?,
        @Query("collection") collection: String?,
        @Query("search") search: String?,
        @Query("kind") kind: String?,
        @Query("category") category: String?,
        @Query("min_chapter") min_chapter:  String?,
        @Query("max_chapter") max_chapter: String?,
        @Query("interval") interval: String?,
        @Query("orderby") orderby: String?,
        @Query("except_query") except_query: String?,
        @Query("except_search") except_search: String?,
        @Query("expr_point") expr_point: String?,
        @Query("score_point") score_point: String?,
    ): Call<JoaraSearchResult?>?

    @GET("v1/search/query_bc_v2.joa")
    fun getSearch(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraSearchResult>

    @GET("v1/banner/home_banner.joa")
    fun getJoaraEvent(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraEventResult>

    @GET("v1/board/event_detail.joa")
    fun getEventDetail(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraEventDetailResult>

    @GET("v1/board/notice_detail.joa")
    fun getNoticeDetail(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraNoticeDetailResult>

    @GET("v1/best/book.joa")
    fun getJoaraBookBest(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestListResult>

    @GET("v1/book/detail.joa")
    fun getBookDetail(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestDetailResult>

    @GET("v1/board/book_comment.joa")
    fun getBookComment(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestDetailCommentsResult>

    @GET("v1/book/other")
    fun getBookOther(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestListResult>

    @GET("v1/board/board_list.joa")
    fun getBoardListJoa(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBoardResult>

}