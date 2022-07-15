package com.example.moavara.Retrofit

import com.example.moavara.Retrofit.Api.ApiOneStory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitOnestore {
    private val apiOneStory = com.example.moavara.Retrofit.Retrofit.apiOneStory

    fun getBestOneStore(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<OneStoreBookResult>) {
        apiOneStory.getBestOneStore(map).enqueue(baseCallback(dataListener))
    }

    fun getBestKakaoStageDetail(id: String, map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<OnestoreBookDetail>) {
        apiOneStory.getOneStoryBookDetail(id, map).enqueue(baseCallback(dataListener))
    }

    fun getOneStoryBookDetailComment(id: String, map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<OnestoreBookDetailComment>) {
        apiOneStory.getOneStoryBookDetailComment(id, map).enqueue(baseCallback(dataListener))
    }
}
