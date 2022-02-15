package com.example.moavara.KaKao

import com.example.moavara.ETC.API
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
    ): Call<BestResultKakao?>?
}

interface ServiceSearchKakaoStage {
    @GET("/ranking/realtime")
    fun getRetrofit(
        @Query("adult") adult: String?,
        @Query("dateRange") dateRange: String?,
        @Query("genreIds") genreIds: String?,
        @Query("recentHours") recentHours: String?,
    ): Call<List<BestResultKakaoStageNovel>?>?
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