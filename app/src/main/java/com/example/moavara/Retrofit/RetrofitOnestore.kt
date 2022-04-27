package com.example.moavara.Retrofit

import com.example.moavara.ETC.HELPER
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitOnestore {
    fun getBestOneStore(categoryId : String? ): Call<OneStoreBookResult?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_ONESTORE)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ServiceOnestoreBest::class.java)
            .getRetrofit(
                categoryId
            )
    }
}
