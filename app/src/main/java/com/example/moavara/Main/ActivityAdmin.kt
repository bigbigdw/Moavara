package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.Firebase.FirebaseWorkManager
import com.example.moavara.R
import com.example.moavara.Search.ActivitySearch
import com.example.moavara.User.ActivityUser
import com.example.moavara.Util.Genre
import com.example.moavara.Util.Mining
import com.example.moavara.databinding.ActivityAdminBinding
import com.example.moavara.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_admin.*
import java.util.concurrent.TimeUnit

class ActivityAdmin : AppCompatActivity() {
    var navController: NavController? = null

    var cate = "ALL"
    var status = ""
    var isRoom = false
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cate = Genre.getGenre(this).toString()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        with(binding){
            llayoutBtn1.setOnClickListener{
                WorkManager.getInstance().cancelAllWork()
                val miningRef = mRootRef.child("Mining")
                miningRef.setValue("NULL")
                Toast.makeText(this@ActivityAdmin, "WorkManager 해제됨", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn2.setOnClickListener{
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
                    if(it.value != null && it.value!! == "MINING"){
                        Toast.makeText(this@ActivityAdmin, "WorkManager 이미 존재함", Toast.LENGTH_SHORT).show()

                    } else {
                        miningRef.setValue("MINING")

                        workManager.enqueue(workRequest)
                        FirebaseMessaging.getInstance().subscribeToTopic("all")
                        Toast.makeText(this@ActivityAdmin, "WorkManager 추가됨", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener{}
            }

            llayoutBtn3.setOnClickListener{
                isRoom = false

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
                Toast.makeText(this@ActivityAdmin, "데이터 갱신 완료", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn4.setOnClickListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Joara", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Joara", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Joara", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Joara", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Joara Nobless", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Joara Nobless", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Joara Nobless", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Joara Nobless", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Joara Premium", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Joara Premium", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Joara Premium", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Joara Premium", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Naver Today", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Naver Today", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Naver Today", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Naver Today", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Naver Challenge", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Naver Challenge", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Naver Challenge", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Naver Challenge", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Naver", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Naver", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Naver", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Naver", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Kakao", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Kakao", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Kakao", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Kakao", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Kakao Stage", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Kakao Stage", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Kakao Stage", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Kakao Stage", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Ridi", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Ridi", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Ridi", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "Ridi", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "OneStore", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "OneStore", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "OneStore", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "OneStore", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "MrBlue", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "MrBlue", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "MrBlue", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(this@ActivityAdmin, "MrBlue", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Toast.makeText(this@ActivityAdmin, "Room 완료", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn5.setOnClickListener{

                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Joara", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Joara", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Joara", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Joara", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Joara Nobless", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Joara Nobless", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Joara Nobless", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Joara Nobless", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Joara Premium", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Joara Premium", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Joara Premium", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Joara Premium", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Naver Today", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Naver Today", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Naver Today", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Naver Today", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Naver Challenge", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Naver Challenge", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Naver Challenge", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Naver Challenge", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Naver", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Naver", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Naver", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Naver", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Kakao", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Kakao", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Kakao", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Kakao", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Kakao Stage", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Kakao Stage", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Kakao Stage", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Kakao Stage", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Ridi", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Ridi", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Ridi", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "Ridi", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "OneStore", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "OneStore", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "OneStore", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "OneStore", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "MrBlue", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "MrBlue", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "MrBlue", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(this@ActivityAdmin, "MrBlue", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Toast.makeText(this@ActivityAdmin, "Room 초기화 완료", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn7.setOnClickListener{
                isRoom = true

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
                Toast.makeText(this@ActivityAdmin, "데이터 갱신 완료", Toast.LENGTH_SHORT).show()
            }
        }

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