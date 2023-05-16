package com.bigbigdw.moavara.Retrofit.Api

import com.bigbigdw.moavara.Retrofit.OneStoreBookResult
import com.bigbigdw.moavara.Retrofit.OnestoreBookDetail
import com.bigbigdw.moavara.Retrofit.OnestoreBookDetailComment
import retrofit2.Call
import retrofit2.http.*

interface ApiOneStory {

    @GET("api/display/product/RNK050700001")
    fun getBestOneStore(@QueryMap queryMap: MutableMap<String?, Any>): Call<OneStoreBookResult>

    @GET("api/detail/{bookcode}")
    fun getOneStoryBookDetail(@Path("bookcode") id: String, @QueryMap queryMap: MutableMap<String?, Any>): Call<OnestoreBookDetail>

    @GET("/api/comment/{bookcode}")
    fun getOneStoryBookDetailComment(@Path("bookcode") id: String, @QueryMap queryMap: MutableMap<String?, Any>): Call<OnestoreBookDetailComment>
}