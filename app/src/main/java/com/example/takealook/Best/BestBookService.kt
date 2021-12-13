package com.example.takealook.Best

import com.example.takealook.ETC.API
import com.example.takealook.ETC.HELPER
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


//베스트
interface BookListBestService {
    @GET(API.BEST_BOOK_JOA + HELPER.ETC)
    fun getRetrofit(
        @Query("token") token: String?,
        @Query("best") best: String?,
        @Query("store") store: String?,
        @Query("category") category: String?,
    ): Call<BookListBestResult?>?
}