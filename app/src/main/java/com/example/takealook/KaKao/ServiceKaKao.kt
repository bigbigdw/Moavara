package com.example.takealook.KaKao

import com.example.takealook.ETC.API
import com.example.takealook.Joara.JoaraBestListResult
import retrofit2.Call
import retrofit2.http.*


//카카오 베스트
interface KaKaoBestService {
    @GET(API.BEST_BOOK_KAKAO)
    fun getRetrofit(
        @Query("category") category: String?,
        @Query("subcategory") subcategory: String?,
        @Query("page") page: String?,
        @Query("day") day: String?,
        @Query("bm") bm: String?,
    ): Call<JoaraBestListResult?>?
}

interface ServiceSearchKakao {
    @FormUrlEncoded
    @POST("/api/v5/store/search")
    fun postRetrofit(
        @Field("page") page: Int?,
        @Field("word") word: String?,
        @Field("category_uid") category_uid: Int?,
    ): Call<SearchResultKakao?>?
}