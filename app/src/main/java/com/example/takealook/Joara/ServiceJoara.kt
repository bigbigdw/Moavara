package com.example.takealook.Joara

import com.example.takealook.ETC.HELPER
import retrofit2.Call
import retrofit2.http.*

//로그아웃
interface ServiceJoara {
    //TODO:기본 파라메터(api_key, ver...어떻게 붙일지 고민)
    @GET("v1/search/query_bc_v2.joa" + HELPER.ETC)
    fun getRetrofit(
        @Query("query") query: String?,
        @Query("collection") collection: String?,
        @Query("search") search: String?,
        @Query("kind") kind: String?,
        @Query("category") category: String?,
        @Query("min_chapter") min_chapter: String?,
        @Query("max_chapter") max_chapter: String?,
        @Query("interval") interval: String?,
        @Query("orderby") orderby: String?,
        @Query("except_query") except_query: String?,
        @Query("except_search") except_search: String?,
        @Query("expr_point") expr_point: String?,
        @Query("score_point") score_point: String?,
    ): Call<JoaraSearchResult?>?
}
