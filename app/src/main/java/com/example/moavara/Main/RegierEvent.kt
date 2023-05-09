package com.example.moavara.Main

sealed interface RegierEvent{
    object BeginRegister:RegierEvent
    object Step1Finish:RegierEvent
    object Step2Finish:RegierEvent
    object RegisterDone:RegierEvent
    object Step1Error:RegierEvent
}