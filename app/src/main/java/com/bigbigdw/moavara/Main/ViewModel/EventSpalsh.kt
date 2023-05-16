package com.bigbigdw.moavara.Main.ViewModel

sealed interface EventSpalsh{
    object Loading: EventSpalsh
    object Loaded: EventSpalsh
}