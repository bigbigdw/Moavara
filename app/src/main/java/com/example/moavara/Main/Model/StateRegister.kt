package com.example.moavara.Main.Model

data class StateRegister(
    val BeginRegister: Boolean = true,
    val Step1Finish: Boolean = false,
    val Step2Finish: Boolean = false,
    val RegisterDone: Boolean = false,
    val OnBackPressed: Boolean = false,
    val Error: String? = null
)