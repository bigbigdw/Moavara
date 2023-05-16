package com.bigbigdw.moavara.Retrofit

class RetrofitMoonPia {
    private val apiMoonPia = com.bigbigdw.moavara.Retrofit.Retrofit.apiMoonPia

    fun postMoonPiaBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestMoonpiaResult>) {
        apiMoonPia.postMoonPiaBest(map).enqueue(baseCallback(dataListener))
    }

}