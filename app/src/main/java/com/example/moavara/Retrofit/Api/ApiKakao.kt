package com.example.moavara.Retrofit.Api

import com.example.moavara.Retrofit.*
import retrofit2.Call
import retrofit2.http.*


interface ApiKakao {
    @FormUrlEncoded
    @POST("api/v6/store/home")
    fun postKakaoBookDetail(@FieldMap queryMap: MutableMap<String?, Any>): Call<BestKakaoBookDetail>

    @FormUrlEncoded
    @POST("api/v7/store/community/list/comment")
    fun postKakaoBookDetailComment(@FieldMap queryMap: MutableMap<String?, Any>): Call<BestKakaoBookDetailComment>
}

interface ApiKakaoStage {

    @GET("ranking/realtime")
    fun getBestKakaoStage(@QueryMap queryMap: MutableMap<String?, Any>): Call<List<BestResultKakaoStageNovel>>

    @GET("novels/{bookcode}")
    fun getBestKakaoStageDetail(@Path("bookcode") id: String): Call<KakaoStageBestBookResult>

    @GET("novels/{bookcode}/comments")
    fun getBestKakaoStageDetailComment(@Path("bookcode") id: String, @Query("size") size: String, @Query("sort") sort: String, @Query("sort") sort2: String, @Query("page") page: String): Call<KakaoStageBestBookCommentResult>
}