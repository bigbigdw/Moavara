package com.example.moavara.Retrofit

class RetrofitToksoda {
    private val apiToksoda = com.example.moavara.Retrofit.Retrofit.apiToksoda

    fun getBestList(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestToksodaResult>) {
        apiToksoda.getBestList(map).enqueue(baseCallback(dataListener))
    }

    fun getBestDetail(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestToksodaDetailResult>) {
        apiToksoda.getBestDetail(map).enqueue(baseCallback(dataListener))
    }

    fun getBestDetailComment(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestToksodaDetailCommentResult>) {
        apiToksoda.getBestDetailComment(map).enqueue(baseCallback(dataListener))
    }

    fun getSearch(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestToksodaSearchResult>) {
        apiToksoda.getSearch(map).enqueue(baseCallback(dataListener))
    }



}