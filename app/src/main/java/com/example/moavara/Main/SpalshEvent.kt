package com.example.moavara.Main

sealed interface SpalshEvent{
    object Loading:SpalshEvent
    object Loaded:SpalshEvent
}