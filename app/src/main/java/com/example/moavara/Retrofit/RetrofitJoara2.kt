package com.example.moavara.Retrofit

import com.example.moavara.ETC.HELPER
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitJoara2 {
    private val apiJoara = Retrofit.apiJoara

    fun getBookDetailJoa(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestDetailResult>) {
        apiJoara.getBookDetail(map).enqueue(baseCallback(dataListener))
    }

    fun getJoaraBookBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestListResult>) {
        apiJoara.getJoaraBookBest(map).enqueue(baseCallback(dataListener))
    }

    fun getBookCommentJoa(map: MutableMap<String?, Any>): Call<JoaraBestDetailCommentsResult> {
        return retrofit2.Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JoaraBestService::class.java)
            .getBookComment(map)
    }

    fun getBookOtherJoa(map: MutableMap<String?, Any>): Call<JoaraBestListResult> {
        return retrofit2.Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JoaraBestService::class.java)
            .getBookOther(map)
    }
}