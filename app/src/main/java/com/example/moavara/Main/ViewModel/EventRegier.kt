package com.example.moavara.Main.ViewModel

sealed interface EventRegier{
    object BeginRegister: EventRegier
    object Step1Finish: EventRegier
    object Step2Finish: EventRegier
    object Step1Error: EventRegier
    object OnBackPressed: EventRegier
}