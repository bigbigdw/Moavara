package com.example.moavara.Retrofit

import com.example.moavara.Retrofit.Api.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.joara.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitKakaoStage = Retrofit.Builder()
        .baseUrl("https://api-pagestage.kakao.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitKakao = Retrofit.Builder()
        .baseUrl("https://api2-page.kakao.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitOneStory = Retrofit.Builder()
        .baseUrl("https://onestory.co.kr")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitMoonPia = Retrofit.Builder()
        .baseUrl("https://www.munpia.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitToksoda = Retrofit.Builder()
        .baseUrl("https://www.tocsoda.co.kr")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiJoara: ApiJoara = retrofit.create(ApiJoara::class.java)
    val apiKakaoStage: ApiKakaoStage = retrofitKakaoStage.create(ApiKakaoStage::class.java)
    val apiKakao: ApiKakao = retrofitKakao.create(ApiKakao::class.java)
    val apiOneStory: ApiOneStory = retrofitOneStory.create(ApiOneStory::class.java)
    val apiMoonPia: ApiMoonPia = retrofitMoonPia.create(ApiMoonPia::class.java)
    val apiToksoda: ApiToksoda = retrofitToksoda.create(ApiToksoda::class.java)
}