package com.bigbigdw.moavara.Main.Model

data class StateMain(
    val Best: Boolean = false,
    val Event: Boolean = false,
    val Pick: Boolean = false,
    val QuckSearch: Boolean = false,
    val Community: Boolean = false,
    val Loading: Boolean = true,
)