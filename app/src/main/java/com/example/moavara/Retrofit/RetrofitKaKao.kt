package com.example.moavara.Retrofit

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitKaKao {
    private val apiKakaoStage = com.example.moavara.Retrofit.Retrofit.apiKakaoStage
    private val apiKakao = com.example.moavara.Retrofit.Retrofit.apiKakao

    //카카오 스테이지 베스트
    fun getKakaoBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestResultKakao>) {
        apiKakao.getKakaoBest(map).enqueue(baseCallback(dataListener))
    }

    //카카오 스테이지 베스트
    fun postKakaoSearch(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<SearchResultKakao>) {
        apiKakaoStage.postKakaoSearch(map).enqueue(baseCallback(dataListener))
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

