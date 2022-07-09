package com.example.moavara.Main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.work.WorkManager
import com.example.moavara.R
import com.example.moavara.Search.ActivitySearch
import com.example.moavara.Search.WeekendDate
import com.example.moavara.User.ActivityUser
import com.example.moavara.Util.*
import com.example.moavara.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess


class ActivityMain : AppCompatActivity() {
    var navController: NavController? = null

    var cate = "ALL"
    var status = ""
    private lateinit var binding: ActivityMainBinding
    var notificationManager: NotificationManager? = null
    var notificationBuilder: NotificationCompat.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cate = Genre.getGenre(this).toString()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.navHostFragmentMain)

        toolbar.setOnClickListener {

            val intent = Intent(this@ActivityMain, ActivityAdmin::class.java)
            startActivity(intent)
        }

        val navView = findViewById<BottomNavigationView>(R.id.nav_bottom)
        NavigationUI.setupWithNavController(navView, navController!!)

        FirebaseDatabase.getInstance().reference.child("Week").child(DBDate.DayString()).setValue(
            DBDate.DateMMDD())

        FirebaseDatabase.getInstance().reference.child("Week").get().addOnSuccessListener {

            val week: WeekendDate? = it.getValue(WeekendDate::class.java)

            if(week != null){
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("SUN", week.sun).apply()
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("MON", week.mon).apply()
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("TUE", week.tue).apply()
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("WED", week.wed).apply()
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("THUR", week.thur).apply()
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("FRI", week.fri).apply()
                getSharedPreferences("WEEK", MODE_PRIVATE).edit().putString("SAT", week.sat).apply()
            }

        }.addOnFailureListener{}

        val parse_date: Date?
        val date = "0707"
        Log.d("!!!!-1", date)
        val dateFormat1 = SimpleDateFormat("MMdd")

        try {
            parse_date = dateFormat1.parse(date)

            val cal = Calendar.getInstance()
            cal.time = parse_date
            val month = cal[Calendar.MONTH]

            Log.d("!!!!-2", month.toString())

            val weekmonth = cal[Calendar.WEEK_OF_MONTH]
            Log.d("!!!!-3", weekmonth.toString())

            val day = cal[Calendar.DAY_OF_WEEK]
            Log.d("!!!!-5", day.toString())

        } catch (e: Exception) {
            e.printStackTrace()
        }

        registNotification()
    }

    fun registNotification(){
        FirebaseMessaging.getInstance().subscribeToTopic("all")
        // NotificationManager 객체 생성
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // API Level 26 버전 이상부터는 NotificationChannel을 사용하여 NotificationCompat.Builder를 생성하기에 분기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "모아바라"
            val channelName = "모아바라 Best"
            val channelDescription = "Channel One Description"

            var notificationChannel: NotificationChannel? = null
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when (item.itemId) {
            R.id.ActivitySearch -> {
                val intent = Intent(this, ActivitySearch::class.java)
                startActivity(intent)
            }
            R.id.ActivityUser -> {
                val intent = Intent(this, ActivityUser::class.java)
                startActivity(intent)
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}