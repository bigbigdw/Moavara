package com.example.moavara.Retrofit

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitKaKao {
    private val apiKakaoStage = com.example.moavara.Retrofit.Retrofit.apiKakaoStage
    private val apiKakao = com.example.moavara.Retrofit.Retrofit.apiKakao

    //카카오 검색
    fun postSearchKakao(
        page: Int?,
        word: String?,
        categoryUid: Int?,
    ): Call<SearchResultKakao?>? {
        return Retrofit.Builder()
            .baseUrl("https://api2-page.kakao.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ServiceSearchKakao::class.java)
            .postRetrofit(
                page,
                word,
                categoryUid
            )
    }

    //카카오 베스트
    fun getBestKakao(
        category: String?,
        subcategory: String?,
        page: String?,
        day: String?,
        bm: String?,
    ): Call<BestResultKakao?>? {
        return Retrofit.Builder()
            .baseUrl("https://api2-page.kakao.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(KaKaoBestService::class.java)
            .getRetrofit(
                category,
                subcategory,
                page,
                day,
                bm
            )
    }

    //카카오 스테이지 베스트
    fun getBestKakaoStage(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<List<BestResultKakaoStageNovel>>) {
        apiKakaoStage.getBestKakaoStage(map).enqueue(baseCallback(dataListener))
    }

    fun getBestKakaoStageDetail(bookCode : String, dataListener: RetrofitDataListener<KakaoStageBestBookResult>) {
        apiKakaoStage.getBestKakaoStageDetail(bookCode).enqueue(baseCallback(dataListener))
    }

    fun getBestKakaoStageDetailComment(
        bookCode: String,
        size: String,
        sort: String,
        sort2: String,
        page: String,
        dataListener: RetrofitDataListener<KakaoStageBestBookCommentResult>
    ) {
        apiKakaoStage.getBestKakaoStageDetailComment(bookCode, size, sort, sort2, page)
            .enqueue(baseCallback(dataListener))
    }

    fun postKakaoBookDetail(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestKakaoBookDetail>) {
        apiKakao.postKakaoBookDetail(map).enqueue(baseCallback(dataListener))
    }

    fun postKakaoBookDetailComment(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestKakaoBookDetailComment>) {
        apiKakao.postKakaoBookDetailComment(map).enqueue(baseCallback(dataListener))
    }


}

