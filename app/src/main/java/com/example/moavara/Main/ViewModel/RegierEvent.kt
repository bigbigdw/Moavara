package com.example.moavara.Main.ViewModel

sealed interface RegierEvent{
    object BeginRegister: RegierEvent
    object Step1Finish: RegierEvent
    object Step2Finish: RegierEvent
    object Step1Error: RegierEvent
}