package com.example.moavara.Main.ViewModel

sealed interface EventSpalsh{
    object Loading: EventSpalsh
    object Loaded: EventSpalsh
}