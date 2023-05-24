package com.bigbigdw.moavara.Main.ViewModel

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.bigbigdw.moavara.DataBase.DBBest
import com.bigbigdw.moavara.DataBase.DBUser
import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Firebase.FCM
import com.bigbigdw.moavara.Main.ActivityLogin
import com.bigbigdw.moavara.Main.ActivityMain
import com.bigbigdw.moavara.Main.Model.StateSplash
import com.bigbigdw.moavara.Main.mRootRef
import com.bigbigdw.moavara.Util.BestRef
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class ViewModelSplash @Inject constructor() : ViewModel() {

    private val events = Channel<EventSpalsh>()

    val state: StateFlow<StateSplash> = events.receiveAsFlow()
        .runningFold(StateSplash(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateSplash())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateSplash, event: EventSpalsh): StateSplash {
        return when(event){
            EventSpalsh.Loading -> {
                current.copy(loading = true)
            }
            is EventSpalsh.Loaded -> {
                current.copy(loading = false, init = true)
            }
        }
    }

    fun loadingSplash(activity: ComponentActivity, callback : (Boolean) -> Unit){

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

        if(UserInfo != null){
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
                callback.invoke(true)
            }.addOnFailureListener {
                callback.invoke(false)
            }
        } else {
            callback.invoke(true)
        }
    }


    fun fetchGuide(activity: ComponentActivity) {
        viewModelScope.launch {
            moveToGuide(activity = activity)
            _sideEffects.send("LoginEvent.Loaded")
        }
    }

    fun moveToGuide(activity: ComponentActivity){
        val intent = Intent(activity, ActivityMain::class.java)
        Toast.makeText(activity, "모아바라에 오신것을 환영합니다", Toast.LENGTH_SHORT).show()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

    fun finishSplash(activity: ComponentActivity){
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