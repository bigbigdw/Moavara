package com.example.takealook.KaKao

import com.example.takealook.ETC.HELPER
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitKaKao {

    //카카오
    fun postSearchKakao(
        page: Int?,
        word: String?,
        categoryUid: Int?
    ): Call<SearchResultKakao?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_KAKAO)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ServiceSearchKakao::class.java)
            .postRetrofit(
                page,
                word,
                categoryUid
            )
    }


}

