package com.example.moavara.Main

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.moavara.DataBase.DBBest
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Firebase.FCM
import com.example.moavara.Util.BestRef
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val events = Channel<SpalshEvent>()

    val state: StateFlow<SplashState> = events.receiveAsFlow()
        .runningFold(SplashState(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, SplashState())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: SplashState, event:SpalshEvent):SplashState{
        return when(event){
            SpalshEvent.Loading -> {
                current.copy(loading = true)
            }
            is SpalshEvent.Loaded -> {
                current.copy(loading = false, init = true)
            }
        }
    }

    fun fetchSplash(activity : ComponentActivity) {
        viewModelScope.launch {
            events.send(SpalshEvent.Loading)
            loadingSplash(activity) { isFinish ->
                if(isFinish){
                    Log.d("MOAVARA!!!!!", "TRUE??")
//                    events.send(MainEvent.Loaded)
                }
            }
            Log.d("MOAVARA!!!!!", "TRUE!!")
            events.send(SpalshEvent.Loaded)
            _sideEffects.send("로딩 완료")
            loadingSplash(activity)
        }
    }

    private fun loadingSplash(activity: ComponentActivity, callback : (Boolean) -> Unit){

        val userDao: DBUser?
        val UserInfo: DataBaseUser?

        val fcm = Intent(activity, FCM::class.java)
        activity.startService(fcm)

        userDao = Room.databaseBuilder(
            activity,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        UserInfo = userDao.daoUser().get()

        mRootRef.child("User").child(UserInfo.UID ?: "").child("isInit").get().addOnSuccessListener {
            if (it.value == true && !it.exists()) {
                Toast.makeText(activity, "데이터를 새로 받아오고 있습니다.", Toast.LENGTH_SHORT)
                    .show()

                mRootRef.child("User").child(UserInfo.UID ?: "").child("isInit").setValue(false)

                for(platform in BestRef.typeList()){
                    val bestDaoToday = Room.databaseBuilder(
                        activity,
                        DBBest::class.java,
                        "Today_${platform}_${UserInfo.Genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoWeek = Room.databaseBuilder(
                        activity,
                        DBBest::class.java,
                        "Week_${platform}_${UserInfo.Genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoMonth = Room.databaseBuilder(
                        activity,
                        DBBest::class.java,
                        "Month_${platform}_${UserInfo.Genre}"
                    ).allowMainThreadQueries().build()

                    bestDaoToday.bestDao().initAll()
                    bestDaoWeek.bestDao().initAll()
                    bestDaoMonth.bestDao().initAll()
                    callback.invoke(true)
                }
            }
        }.addOnFailureListener {}
    }

    fun loadingSplash(activity: ComponentActivity){
        Looper.myLooper()?.let {
            Handler(it).postDelayed(
                {
                    val intent = Intent(activity, ActivityLogin::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    activity.startActivityIfNeeded(intent, 0)
                    activity.finish()
                },
                2000
            )
        }
    }
}