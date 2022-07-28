package com.example.moavara.Firebase

import retrofit2.Call
import retrofit2.http.*

interface FirebaseService {
    @Headers("Content-Type: application/json", "Authorization: key=AAAAjynPAw4:APA91bF_cLM10IX7pogyaaqQmJ7gIS2xzDP5WJOKLQylQiWax28s2mjnanzkan24O_TQPVuR4-d2osATG9MwGYCkfK-HUM6SQPR1f7P3udGj6YsCUz_1rxJsesLX3fuF2-AFJIt7qUkY")
    @POST("/fcm/send")
    fun postRetrofit(
        @Body body : DataFCMBody
    ): Call<FWorkManagerResult>
}