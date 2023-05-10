package com.example.moavara.Main.ViewModel

sealed interface SpalshEvent{
    object Loading: SpalshEvent
    object Loaded: SpalshEvent
}