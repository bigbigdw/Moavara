package com.example.takealook.Joara

import com.example.takealook.ETC.API
import com.example.takealook.ETC.HELPER
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//조아라 이벤트
interface JoaraEventService {
    @GET(API.EVENT_JOA + HELPER.ETC)
    fun getRetrofit(
        @Query("page") page: String?,
        @Query("banner_type") banner_type: String?,
        @Query("category") category: String?,
    ): Call<JoaraEventResult?>?
}

//조아라 베스트
interface JoaraBestService {
    @GET(API.BEST_BOOK_JOA + HELPER.ETC)
    fun getRetrofit(
        @Query("best") best: String?,
        @Query("store") store: String?,
        @Query("category") category: String?,
    ): Call<JoaraBestListResult?>?
}