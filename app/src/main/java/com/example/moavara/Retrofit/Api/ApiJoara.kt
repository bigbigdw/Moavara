package com.example.moavara.Retrofit.Api

import com.example.moavara.Retrofit.*
import retrofit2.Call
import retrofit2.http.*


interface ApiJoara {

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