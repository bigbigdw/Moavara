package com.example.moavara.Firebase

import retrofit2.Call
import retrofit2.http.*

interface FirebaseService {
    @Headers("Content-Type: application/json", "Authorization: key=AAAAjynPAw4:APA91bH7kxmeRDe2zm02KhUrySYvnclJMB9G3rTkxWJpnezJvx3_SUdNIQ2oYDCd8hhD_yUzNUMaMY6WBkoqC7UnkB5OLYHVws_Up4quLZ0NI81LdMhUtAPwUYXaf7dMlbXHrYA8ZZlV")
    @POST("/fcm/send")
    fun postRetrofit(
        @Body body : DataFCMBody
    ): Call<FWorkManagerResult>
}