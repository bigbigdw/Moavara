package com.example.moavara.Retrofit

import com.example.moavara.ETC.API
import com.example.moavara.ETC.HELPER
import retrofit2.Call
import retrofit2.http.*


interface ApiKakao {
    @FormUrlEncoded
    @POST("api/v6/store/home")
    fun postKakaoBookDetail(@FieldMap queryMap: MutableMap<String?, Any>): Call<BestKakaoBookDetail>

    @FormUrlEncoded
    @POST("api/v7/store/community/list/comment")
    fun postKakaoBookDetailComment(@FieldMap queryMap: MutableMap<String?, Any>): Call<BestKakaoBookDetailComment>


}

interface ApiKakaoStage {

    @GET("/ranking/realtime")
    fun getBestKakaoStage(@QueryMap queryMap: MutableMap<String?, Any>): Call<List<BestResultKakaoStageNovel>>

}