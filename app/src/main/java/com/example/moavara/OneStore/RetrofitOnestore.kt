package com.example.moavara.OneStore

import com.example.moavara.ETC.HELPER
import com.example.moavara.KaKao.BestResultKakao
import com.example.moavara.KaKao.KaKaoBestService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitOnestore {
    fun getBestOneStore( ): Call<OneStoreBookResult?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_ONESTORE)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ServiceOnestoreBest::class.java)
            .getRetrofit()
    }
}
