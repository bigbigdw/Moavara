package com.example.moavara.Retrofit


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> baseCallback(retrofitDataListener: RetrofitDataListener<T>): Callback<T> = object :
    Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {

        val result = response.body()
        if (response.isSuccessful && result != null) {
            retrofitDataListener.onSuccess(result)
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {

    }
}