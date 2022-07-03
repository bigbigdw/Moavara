package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.moavara.Best.BottomDialogMain
import com.example.moavara.DataBase.BookListDataBestToday
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.Firebase.FirebaseWorkManager
import com.example.moavara.Pick.ActivityPick
import com.example.moavara.R
import com.example.moavara.Search.ActivitySearch
import com.example.moavara.Search.WeekendDate
import com.example.moavara.User.ActivityUser
import com.example.moavara.Util.*
import com.example.moavara.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess


class ActivityMain : AppCompatActivity() {
    var navController: NavController? = null

    var cate = "ALL"
    var status = ""
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cate = Genre.getGenre(this).toString()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.navHostFragmentMain)

        toolbar.setOnClickListener {
//            WorkManager.getInstance().cancelAllWork()
//            val miningRef = mRootRef.child("Mining")
//            miningRef.setValue("NULL")
//            Toast.makeText(this, "WorkManager 해제됨", Toast.LENGTH_SHORT).show()

            /* 처리해야할 작업에 관한 코드들 */
            Handler(Looper.getMainLooper()).postDelayed({
                Mining.runMining(applicationContext, "ALL")
                Log.d("MINING", "ALL")
            }, 1000) //1초 후 실행
            Handler(Looper.getMainLooper()).postDelayed({
                Mining.runMining(applicationContext, "ROMANCE")
                Log.d("MINING", "ROMANCE")
            }, 1000) //1초 후 실행
            Handler(Looper.getMainLooper()).postDelayed({
                Mining.runMining(applicationContext, "BL")
                Log.d("MINING", "BL")
            }, 1000) //1초 후 실행
            Handler(Looper.getMainLooper()).postDelayed({
                Mining.runMining(applicationContext, "FANTASY")
                Log.d("MINING", "FANTASY")
            }, 1000) //1초 후 실행
        }

        val navView = findViewById<BottomNavigationView>(R.id.nav_bottom)
        NavigationUI.setupWithNavController(navView, navController!!)

        FirebaseDatabase.getInstance().reference.child("Week").child(DBDate.DayString()).setValue(
            DBDate.DateMMDD())

        Handler(Looper.getMainLooper()).postDelayed({

            val mConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
            val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(4, TimeUnit.HOURS)
                .setConstraints(mConstraints)
                .build()

            val miningRef = FirebaseDatabase.getInstance().reference.child("Mining")
            val workManager = WorkManager.getInstance()

            miningRef.get().addOnSuccessListener {
//                if(it.value != null && it.value!! == "MINING"){
//                    Toast.makeText(this, "WorkManager 이미 존재함", Toast.LENGTH_SHORT).show()
//
//                } else {
//                    miningRef.setValue("MINING")
//
//                    workManager.enqueue(workRequest)
//                    FirebaseMessaging.getInstance().subscribeToTopic("all")
//                    Toast.makeText(this, "WorkManager 추가됨", Toast.LENGTH_SHORT).show()
//                }
            }.addOnFailureListener{}
        }, 1000) //1초 후 실행

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
    }

    override fun onBackPressed() {

        val myAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(this@ActivityMain)
        myAlertBuilder.setTitle("모아바라 종료")
        myAlertBuilder.setMessage("모아바라를 종료하시겠습니까?")
        myAlertBuilder.setPositiveButton(
            "예"
        ) { _, _ ->

            finishAffinity();
            System.runFinalization();
            exitProcess(0);
        }
        myAlertBuilder.setNegativeButton(
            "아니요"
        ) { _, _ ->
        }
        // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
        myAlertBuilder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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