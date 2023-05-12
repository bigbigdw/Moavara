package com.example.moavara.Best.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moavara.Best.Model.StateBestList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class ViewModelBestList @Inject constructor() : ViewModel() {

    private val events = Channel<EventBestList>()

    val state: StateFlow<StateBestList> = events.receiveAsFlow()
        .runningFold(StateBestList(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateBestList())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateBestList, event: EventBestList): StateBestList {
        return when(event){
            EventBestList.Today -> {
                current.copy(Today = true)
            }
            EventBestList.Month -> {
                current.copy(Month = true)
            }
            is EventBestList.Week -> {
                current.copy(Week = true)
            }
        }
    }

    fun fetchBestList() {
        viewModelScope.launch {
            events.send(EventBestList.Today)
        }
    }
    
}
