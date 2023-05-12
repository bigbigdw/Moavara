package com.example.moavara.Main.ViewModel

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Main.Model.StateMain
import com.example.moavara.Search.ActivitySearch
import com.example.moavara.User.ActivityAdmin
import com.example.moavara.User.ActivityUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class ViewModelMain @Inject constructor() : ViewModel() {

    private val events = Channel<EventMain>()

    val state: StateFlow<StateMain> = events.receiveAsFlow()
        .runningFold(StateMain(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateMain())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateMain, event: EventMain): StateMain {
        return when(event){
            EventMain.Best -> {
                current.copy(Best = true)
            }
            EventMain.Event -> {
                current.copy(Event = true)
            }
            is EventMain.Pick -> {
                current.copy(Pick = true)
            }
            is EventMain.QuckSearch -> {
                current.copy(QuckSearch = true)
            }
            is EventMain.Community -> {
                current.copy(Community = true)
            }
        }
    }

    fun fetchMain(activity: ComponentActivity, getter: DataBaseUser) {
        viewModelScope.launch {
            events.send(EventMain.Best)
        }
    }

    fun goToActivityAdmin(activity: ComponentActivity){
        val intent = Intent(activity, ActivityAdmin::class.java)
        activity.startActivity(intent)
    }

    fun goToActivitySearch(activity: ComponentActivity){
        val intent = Intent(activity, ActivitySearch::class.java)
        activity.startActivity(intent)
    }

    fun goToActivityUser(activity: ComponentActivity){
        val intent = Intent(activity, ActivityUser::class.java)
        activity.startActivity(intent)
    }
}
