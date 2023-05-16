package com.bigbigdw.moavara.Retrofit

import com.bigbigdw.moavara.Retrofit.result.RidiBestResult

class RetrofitRidi {
    private val apiRidi = com.bigbigdw.moavara.Retrofit.Retrofit.apiRidi

    fun getBestRidi(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<RidiBestResult>) {
        apiRidi.getBestRidi(map).enqueue(baseCallback(dataListener))
    }
}
