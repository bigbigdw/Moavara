package com.example.moavara.Main.ViewModel

sealed interface LoginEvent{
    object Loaded: LoginEvent
    object Register: LoginEvent
    object MoveMain: LoginEvent
}