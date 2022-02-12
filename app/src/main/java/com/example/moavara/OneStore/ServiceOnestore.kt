package com.example.moavara.OneStore

import com.example.moavara.KaKao.SearchResultKakao
import retrofit2.Call
import retrofit2.http.*

interface ServiceOnestoreBest {
    @GET("/api/display/product/TAR000016173?menuId=&freepassGrpCd=")
    fun getRetrofit(): Call<OneStoreBookResult?>?
}