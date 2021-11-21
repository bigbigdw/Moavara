package com.example.takealook.Joara

import com.example.takealook.ETC.HELPER
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSearch {
    fun getSearchResultJoara(
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
            .baseUrl(HELPER.API)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ServiceJoara::class.java)
            .getRetrofit(
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
}

