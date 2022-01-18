package com.example.takealook.Joara

import android.content.Context
import com.example.takealook.ETC.HELPER
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitJoara {

    //조아라
    fun getSearchJoara(
        page: Int?,
        query: String?,
        collection: String?,
        search: String?,
        kind: String?,
        category: String?,
        min_chapter: String?,
        max_chapter: String?,
        interval: String?,
        orderby: String?,
        except_query: String?,
        except_search: String?,
        expr_point: String?,
        score_point: String?,
    ): Call<JoaraSearchResult?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ServiceSearchJoara::class.java)
            .getRetrofit(
                page,
                query,
                collection,
                search,
                kind,
                category,
                min_chapter,
                max_chapter,
                interval,
                orderby,
                except_query,
                except_search,
                expr_point,
                score_point
            )
    }

    fun getJoaraEvent(page: String?, banner_type: String?, category: String?): Call<JoaraEventResult?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JoaraEventService::class.java)
            .getRetrofit(
                page,
                banner_type,
                category
            )
    }

    fun getJoaraBookBest(best: String?, store: String?, category: String?): Call<JoaraBestListResult?>? {
        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(JoaraBestService::class.java)
            .getRetrofit(
                best,
                store,
                category
            )
    }

    fun postLogin(idCheck : String?, pwCheck: String?, mContext: Context?): Call<LoginResult?>? {

        val call = Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(LoginService::class.java)
            .postRetrofit(
                idCheck,
                pwCheck,
                HELPER.API_KEY,
                HELPER.VER,
                HELPER.DEVICE,
                HELPER.DEVICE_ID,
                HELPER.DEVICE_TOKEN
            )
        return call
    }

    fun onClickLogout(token: String?, mContext: Context?): Call<LogoutResult?>? {

        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(LogoutService::class.java)
            .getRetrofit(
                "22%2C2",
                token,
                HELPER.API_KEY,
                HELPER.VER,
                HELPER.DEVICE,
                HELPER.DEVICE_ID,
                HELPER.DEVICE_TOKEN
            )
    }

    fun loginCheck(token: String?, mContext: Context?): Call<CheckTokenResult?>? {

        return Retrofit.Builder()
            .baseUrl(HELPER.API_JOARA)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(CheckTokenService::class.java)
            .getRetrofit(token)
    }
}