package com.example.moavara.Retrofit

import com.example.moavara.Retrofit.Retrofit.apiOneStory
import com.example.moavara.Retrofit.result.RidiBestResult
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RetrofitRidi {
    private val apiRidi = com.example.moavara.Retrofit.Retrofit.apiRidi

    fun getBestRidi(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<RidiBestResult>) {
        apiRidi.getBestRidi(map).enqueue(baseCallback(dataListener))
    }
}
