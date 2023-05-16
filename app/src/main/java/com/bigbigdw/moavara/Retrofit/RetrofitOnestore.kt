package com.bigbigdw.moavara.Retrofit

class RetrofitOnestore {
    private val apiOneStory = com.bigbigdw.moavara.Retrofit.Retrofit.apiOneStory

    fun getBestOneStore(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<OneStoreBookResult>) {
        apiOneStory.getBestOneStore(map).enqueue(baseCallback(dataListener))
    }

    fun getOneStoreDetail(id: String, map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<OnestoreBookDetail>) {
        apiOneStory.getOneStoryBookDetail(id, map).enqueue(baseCallback(dataListener))
    }

    fun getOneStoryBookDetailComment(id: String, map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<OnestoreBookDetailComment>) {
        apiOneStory.getOneStoryBookDetailComment(id, map).enqueue(baseCallback(dataListener))
    }
}
