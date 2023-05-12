package com.example.moavara.Best.ViewModel

sealed interface EventBestList{
    object Today: EventBestList
    object Week: EventBestList
    object Month: EventBestList
}