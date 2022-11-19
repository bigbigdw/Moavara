package com.example.moavara.Retrofit

class RetrofitKaKao {
    private val apiKakaoStage = com.example.moavara.Retrofit.Retrofit.apiKakaoStage
    private val apiKakao = com.example.moavara.Retrofit.Retrofit.apiKakao
    private val apiKakao2 = com.example.moavara.Retrofit.Retrofit.apiKakao2

    //카카오 스테이지 베스트
    fun getKakaoBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestResultKakao>) {
        apiKakao.getKakaoBest(map).enqueue(baseCallback(dataListener))
    }

    //카카오 스테이지 베스트
    fun postKakaoSearch(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<SearchResultKakao>) {
        apiKakao.postKakaoSearch(map).enqueue(baseCallback(dataListener))
    }

    //카카오 스테이지 베스트
    fun getBestKakaoStage(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<List<BestResultKakaoStageNovel>>) {
        apiKakaoStage.getBestKakaoStage(map).enqueue(baseCallback(dataListener))
    }

    fun getBestKakaoStageDetail(bookCode : String, dataListener: RetrofitDataListener<KakaoStageBestBookResult>) {
        apiKakaoStage.getBestKakaoStageDetail(bookCode).enqueue(baseCallback(dataListener))
    }

    fun getSearchKakaoStage(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<KakaoStageSearchResult>) {
        apiKakaoStage.getSearchKakaoStage(map).enqueue(baseCallback(dataListener))
    }

    fun getBestKakao2(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestKakao2Result>) {
        apiKakao2.getBestKakao2(map).enqueue(baseCallback(dataListener))
    }

    fun getBestKakaoStageDetailComment(
        bookCode: String,
        size: String,
        sort: String,
        sort2: String,
        page: String,
        dataListener: RetrofitDataListener<KakaoStageBestBookCommentResult>
    ) {
        apiKakaoStage.getBestKakaoStageDetailComment(bookCode, size, sort, sort2, page)
            .enqueue(baseCallback(dataListener))
    }

    fun postKakaoBookDetail(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestKakaoBookDetail>) {
        apiKakao.postKakaoBookDetail(map).enqueue(baseCallback(dataListener))
    }

    fun postKakaoBookDetailComment(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestKakaoBookDetailComment>) {
        apiKakao.postKakaoBookDetailComment(map).enqueue(baseCallback(dataListener))
    }

    fun getKakaoStageEventList(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<KakaoStageEventList>) {
        apiKakaoStage.getKakaoStageEventList(map).enqueue(baseCallback(dataListener))
    }
}

