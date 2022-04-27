package com.example.moavara.Retrofit

import com.example.moavara.ETC.HELPER
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
            .create(JoaraEventService::class.java)
            .getNoticeDetail(
                notice_id
            )
    }

    fun getJoaraEventDetail(event_id: String?): Call<JoaraEventDetailResult?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JoaraEventService::class.java)
            .getEventDetail(
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

    fun getBookDetailJoa(map: MutableMap<String?, Any>): Call<JoaraBestDetailResult> {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JoaraBestService::class.java)
            .getBookDetail(map)
    }

    fun getBookCommentJoa(map: MutableMap<String?, Any>): Call<JoaraBestDetailCommentsResult> {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JoaraBestService::class.java)
            .getBookComment(map)
    }

    fun getBookOtherJoa(map: MutableMap<String?, Any>): Call<JoaraBestListResult> {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JoaraBestService::class.java)
            .getBookOther(map)
    }
}