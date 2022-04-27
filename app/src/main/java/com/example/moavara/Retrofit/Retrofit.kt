package com.example.moavara.Retrofit

import com.example.moavara.ETC.HELPER
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {

    private val retrofit = Retrofit.Builder()
        .baseUrl(HELPER.API_JOARA)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiJoara: ApiJoara = retrofit.create(ApiJoara::class.java)
}