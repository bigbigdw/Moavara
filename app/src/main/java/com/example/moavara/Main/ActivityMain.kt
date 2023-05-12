package com.example.moavara.Main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import androidx.work.*
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Firebase.FCM
import com.example.moavara.Firebase.FirebaseWorkManager
import com.example.moavara.Firebase.FirebaseWorkManager.Companion.UID
import com.example.moavara.Main.Screen.BackOnPressedRegister
import com.example.moavara.Main.Screen.MainScreenView
import com.example.moavara.Main.Screen.RegsisterScreen
import com.example.moavara.Main.ViewModel.ViewModelMain
import com.example.moavara.Main.ViewModel.ViewModelRegister
import com.example.moavara.R
import com.example.moavara.Search.ActivitySearch
import com.example.moavara.User.ActivityAdmin
import com.example.moavara.User.ActivityUser
import com.example.moavara.Util.BackOnPressed
import com.example.moavara.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit


class ActivityMain : ComponentActivity() {

    private val viewModelMain: ViewModelMain by viewModels()

    var notificationManager: NotificationManager? = null
    var notificationBuilder: NotificationCompat.Builder? = null

    var userDao: DBUser? = null
    var UserInfo : DataBaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelMain.sideEffects
            .onEach { Toast.makeText(this@ActivityMain, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        userDao = Room.databaseBuilder(
            this,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        if(userDao?.daoUser() != null){
            UserInfo = userDao?.daoUser()?.get()
        }

        setContent {
            MainScreenView()
            BackOnPressed()
        }

        setPersonalFCM()
        registNotification()
        registPickNotification()
    }

    private fun registPickNotification(){

        FirebaseMessaging.getInstance().subscribeToTopic(UserInfo?.UID ?: "")
        // NotificationManager 객체 생성
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // API Level 26 버전 이상부터는 NotificationChannel을 사용하여 NotificationCompat.Builder를 생성하기에 분기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "모아바라 Pick"
            val channelName = "모아바라 Pick"
            val channelDescription = "모아바라 선호작 최신화 알림"

            val notificationChannel: NotificationChannel?
            // HeadsUp은 Importance를 High로 설정해야하기에 분기
            // NotificationChannel 객체 생성
            notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.description = channelDescription
            // NotificationChannel이 등록된 Builder
            // 이 Builder에 의해 만들어진 Notification은 이곳에 등록된 Channel에 의해 관리
            notificationManager?.createNotificationChannel(notificationChannel)
            notificationBuilder = NotificationCompat.Builder(this, channelId)
        }
    }

    private fun registNotification(){
        FirebaseMessaging.getInstance().subscribeToTopic("all")
        // NotificationManager 객체 생성
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // API Level 26 버전 이상부터는 NotificationChannel을 사용하여 NotificationCompat.Builder를 생성하기에 분기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "모아바라 Best"
            val channelName = "모아바라 Best"
            val channelDescription = "Channel One Description"

            val notificationChannel: NotificationChannel?
            // HeadsUp은 Importance를 High로 설정해야하기에 분기
            // NotificationChannel 객체 생성
            notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.description = channelDescription
            // NotificationChannel이 등록된 Builder
            // 이 Builder에 의해 만들어진 Notification은 이곳에 등록된 Channel에 의해 관리
            notificationManager?.createNotificationChannel(notificationChannel)
            notificationBuilder = NotificationCompat.Builder(this, channelId)
        }
    }

    private fun setPersonalFCM(){
        mRootRef.child("User").child(UserInfo?.UID ?: "").get().addOnSuccessListener {
            val Novel = it.child("Novel")
            val Mining = it.child("Mining")

            if(Novel.exists() && !Mining.exists()){
                val fcm = Intent(applicationContext, FCM::class.java)
                startService(fcm)

                val inputData = Data.Builder()
                    .putString(FirebaseWorkManager.TYPE, "PICK")
                    .putString(FirebaseWorkManager.UID, UserInfo?.UID ?: "")
                    .putString(FirebaseWorkManager.USER, UserInfo?.Nickname ?: "")
                    .build()

                /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
                val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(6, TimeUnit.HOURS)
                    .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                        TimeUnit.MILLISECONDS
                    )
                    .addTag("MoavaraPick")
                    .setInputData(inputData)
                    .build()

                val workManager = WorkManager.getInstance(applicationContext)

                workManager.enqueueUniquePeriodicWork(
                    "MoavaraPick",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    workRequest
                )

                FirebaseMessaging.getInstance().subscribeToTopic(UserInfo?.UID ?: "")
                mRootRef.child("User").child(UserInfo?.UID ?: "").child("Mining").setValue(true)
            }

        }.addOnFailureListener {}
    }
}