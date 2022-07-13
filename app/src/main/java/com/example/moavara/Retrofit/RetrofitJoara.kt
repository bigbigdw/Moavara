package com.example.moavara.Retrofit

import com.example.moavara.ETC.HELPER
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitJoara {
    private val apiJoara = Retrofit.apiJoara

    fun getBookDetailJoa(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestDetailResult>) {
        apiJoara.getBookDetail(map).enqueue(baseCallback(dataListener))
    }

    fun getJoaraBookBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestListResult>) {
        apiJoara.getJoaraBookBest(map).enqueue(baseCallback(dataListener))
    }

    fun getBookCommentJoa(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestDetailCommentsResult>) {
        apiJoara.getBookComment(map).enqueue(baseCallback(dataListener))
    }

    fun getBookOtherJoa(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestListResult>) {
        apiJoara.getBookOther(map).enqueue(baseCallback(dataListener))
    }

    fun getBoardListJoa(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBoardResult>) {
        apiJoara.getBoardListJoa(map).enqueue(baseCallback(dataListener))
    }

    fun getNoticeDetail(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraNoticeDetailResult>) {
        apiJoara.getNoticeDetail(map).enqueue(baseCallback(dataListener))
    }

    fun getJoaraEventDetail(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraEventDetailResult>) {
        apiJoara.getEventDetail(map).enqueue(baseCallback(dataListener))
    }

    fun getJoaraEvent(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraEventResult>) {
        apiJoara.getJoaraEvent(map).enqueue(baseCallback(dataListener))
    }

    fun getSearchJoara(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraSearchResult>) {
        apiJoara.getSearch(map).enqueue(baseCallback(dataListener))
    }

}