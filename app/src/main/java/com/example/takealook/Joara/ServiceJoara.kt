package com.example.takealook.Joara

import com.example.takealook.ETC.API
import com.example.takealook.ETC.HELPER
import retrofit2.Call
import retrofit2.http.*

//조아라 이벤트
interface JoaraEventService {
    @GET(API.EVENT_JOA + HELPER.ETC)
    fun getRetrofit(
        @Query("page") page: String?,
        @Query("banner_type") banner_type: String?,
        @Query("category") category: String?,
    ): Call<JoaraEventResult?>?
}

//조아라 베스트
interface JoaraBestService {
    @GET(API.BEST_BOOK_JOA + HELPER.ETC)
    fun getRetrofit(
        @Query("best") best: String?,
        @Query("store") store: String?,
        @Query("category") category: String?,
    ): Call<JoaraBestListResult?>?
}

//토큰 체크
interface CheckTokenService {
    @GET(API.USER_TOKEN_CHECK_JOA + HELPER.ETC)
    fun getRetrofit(@Query("token") token: String?): Call<CheckTokenResult?>?
}

//로그인
interface LoginService {
    @FormUrlEncoded
    @POST(API.USER_AUTH_JOA)
    fun postRetrofit(
        @Field("member_id") memberId: String?,
        @Field("passwd") passwd: String?,
        @Field("api_key") apiKey: String?,
        @Field("ver") ver: String?,
        @Field("device") device: String?,
        @Field("deviceuid") deviceuid: String?,
        @Field("devicetoken") devicetoken: String?,
    ): Call<LoginResult?>?
}

//로그아웃
interface LogoutService {
    @GET(API.USER_DEAUTH_JOA)
    fun getRetrofit(
        @Query("category") category: String?,
        @Query("token") token: String?,
        @Query("api_key") apiKey: String?,
        @Query("ver") ver: String?,
        @Query("device") device: String?,
        @Query("deviceuid") deviceuid: String?,
        @Query("devicetoken") devicetoken: String?,
    ): Call<LogoutResult?>?
}

//인덱스 API
interface IndexAPIService {
    @GET(API.INFO_INDEX_JOA)
    fun getRetrofit(
        @Query("menu_ver") menuVer: String?,
        @Query("api_key") apiKey: String?,
        @Query("ver") ver: String?,
        @Query("device") device: String?,
        @Query("deviceuid") deviceuid: String?,
        @Query("devicetoken") devicetoken: String?,
    ): Call<IndexAPIResult?>?
}