package com.example.moavara.Retrofit.Api

import com.example.moavara.Retrofit.BestMoonpiaResult
import retrofit2.Call
import retrofit2.http.*

interface ApiMoonPia {

    @FormUrlEncoded
    @POST("/module/api/apiCode/d7dbfddf567dca21794d120f48678360/service/novel/scope/bests/MUNP/brfcr4nqlb2fhj6irrg6uohu75/HTTP_FORCED_AJAX/true")
    fun postMoonPiaBest(@FieldMap queryMap: MutableMap<String?, Any>): Call<BestMoonpiaResult>

}