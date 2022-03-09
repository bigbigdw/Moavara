package com.example.moavara.Joara

import android.content.Context
import com.example.moavara.ETC.HELPER
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitJoara {

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

    fun getJoaraNoticeDetail(notice_id: String?): Call<JoaraNoticeDetailResult?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JoaraNoticeDetailService::class.java)
            .getRetrofit(
                notice_id
            )
    }

    fun getJoaraEventDetail(event_id: String?): Call<JoaraEventDetailResult?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JoaraEventDetailService::class.java)
            .getRetrofit(
                event_id
            )
    }

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