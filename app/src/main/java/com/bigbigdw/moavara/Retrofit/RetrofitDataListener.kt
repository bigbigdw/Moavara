package com.bigbigdw.moavara.Retrofit

interface RetrofitDataListener<T> {
    fun onSuccess(data: T)
}