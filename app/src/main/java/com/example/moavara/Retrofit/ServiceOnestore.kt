package com.example.moavara.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface ServiceOnestoreBest {
    @GET("api/display/product/RNK05OSBFT0820D001?menuId=DP13041%7CDP13042%7CDP13043%7CDP13044")
    fun getRetrofit(
        @Query("categoryId") categoryId: String?,
    ): Call<OneStoreBookResult?>?
}