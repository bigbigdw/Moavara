package com.example.takealook.Search

import com.example.takealook.ETC.HELPER
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSearch {

    //조아라
    fun getSearchJoara(
        page: Int?,
        query: String?,
        collection: String?,
        search: String?,
        kind: String?,
        category: String?,
        min_chapter: String?,
        max_chapter: String?,
        interval: String?,
        orderby: String?,
        except_query: String?,
        except_search: String?,
        expr_point: String?,
        score_point: String?,
    ): Call<JoaraSearchResult?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ServiceSearchJoara::class.java)
            .getRetrofit(
                page,
                query,
                collection,
                search,
                kind,
                category,
                min_chapter,
                max_chapter,
                interval,
                orderby,
                except_query,
                except_search,
                expr_point,
                score_point
            )
    }

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

