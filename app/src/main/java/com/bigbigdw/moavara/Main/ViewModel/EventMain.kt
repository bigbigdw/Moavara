package com.bigbigdw.moavara.Main.ViewModel

sealed interface EventMain{
    object Best: EventMain
    object Event: EventMain
    object Pick: EventMain
    object QuckSearch: EventMain
    object Community: EventMain
}