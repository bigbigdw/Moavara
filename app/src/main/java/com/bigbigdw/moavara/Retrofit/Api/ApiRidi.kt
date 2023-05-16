package com.bigbigdw.moavara.Retrofit.Api

import com.bigbigdw.moavara.Retrofit.*
import com.bigbigdw.moavara.Retrofit.result.RidiBestResult
import retrofit2.Call
import retrofit2.http.*

interface ApiRidi {
//    _next/data/N6rIdHBRv8uATv_6K0aQu/bestsellers/romance_serial.json?rent=n&adult=n&adult_exclude=y&order=daily&page=1&genre=romance_serial
    @GET("_next/data/N6rIdHBRv8uATv_6K0aQu/bestsellers/romance_serial.json")
    fun getBestRidi(@QueryMap queryMap: MutableMap<String?, Any>): Call<RidiBestResult>

}