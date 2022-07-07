package com.example.moavara.Retrofit

class RetrofitMoonPia {
    private val apiMoonPia = com.example.moavara.Retrofit.Retrofit.apiMoonPia

    fun postMoonPiaBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestMoonpiaResult>) {
        apiMoonPia.postMoonPiaBest(map).enqueue(baseCallback(dataListener))
    }

}