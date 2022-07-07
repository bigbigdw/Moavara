package com.example.moavara.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface ApiOneStory {
    @GET("api/display/product/RNK050700001")
    fun getRetrofit(
        @Query("menuId") categoryId: String?,
    ): Call<OneStoreBookResult?>?

    @GET("api/detail/{bookcode}")
    fun getOneStoryBookDetail(@Path("bookcode") id: String, @QueryMap queryMap: MutableMap<String?, Any>): Call<OnestoreBookDetail>

    @GET("/api/comment/{bookcode}")
    fun getOneStoryBookDetailComment(@Path("bookcode") id: String, @QueryMap queryMap: MutableMap<String?, Any>): Call<OnestoreBookDetailComment>
}