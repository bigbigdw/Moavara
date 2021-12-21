package com.example.takealook.Joara

import com.example.takealook.ETC.HELPER
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitJoara {

    fun getJoaraEvent(page: String?, banner_type: String?, category: String?): Call<JoaraEventResult?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JoaraEventService::class.java)
            .getRetrofit(
                page,
                banner_type,
                category
            )
    }

    fun getJoaraBookBest(best: String?, store: String?, category: String?): Call<JoaraBestListResult?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JoaraBestService::class.java)
            .getRetrofit(
                best,
                store,
                category
            )
    }
}