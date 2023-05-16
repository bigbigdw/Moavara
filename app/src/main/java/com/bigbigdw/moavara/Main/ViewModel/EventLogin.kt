package com.bigbigdw.moavara.Main.ViewModel

sealed interface EventLogin{
    object Loaded: EventLogin
    object Register: EventLogin
    object MoveMain: EventLogin
}