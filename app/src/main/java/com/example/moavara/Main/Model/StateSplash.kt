package com.example.moavara.Main.Model

data class StateSplash(
    val loading: Boolean = false,
    val init: Boolean = false,
    val finishInit: Boolean = false,
    val error: String? = null
)