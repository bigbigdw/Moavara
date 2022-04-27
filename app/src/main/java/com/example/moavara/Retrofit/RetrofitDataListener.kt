package com.example.moavara.Retrofit

interface RetrofitDataListener<T> {
    fun onSuccess(data: T)
}