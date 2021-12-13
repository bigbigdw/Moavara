package com.example.takealook.Best

import com.example.takealook.ETC.HELPER
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBookList {

    fun getBookBest(token: String?, best: String?, store: String?, category: String?): Call<BookListBestResult?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(BookListBestService::class.java)
            .getRetrofit(
                token,
                best,
                store,
                category
            )
    }
}