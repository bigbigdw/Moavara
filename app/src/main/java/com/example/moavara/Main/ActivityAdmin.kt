package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.moavara.Firebase.*
import com.example.moavara.R
import com.example.moavara.Search.ActivitySearch
import com.example.moavara.User.ActivityUser
import com.example.moavara.Util.ActivityTest
import com.example.moavara.Util.Genre
import com.example.moavara.Util.Mining
import com.example.moavara.databinding.ActivityAdminBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ActivityAdmin : AppCompatActivity() {

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

        /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(3, TimeUnit.HOURS)
            .build()

        val miningRef = FirebaseDatabase.getInstance().reference.child("Mining")
        val workManager = WorkManager.getInstance(applicationContext)


        with(binding){
            llayoutBtn1.setOnClickListener{
                WorkManager.getInstance().cancelAllWork()
                miningRef.setValue("NULL")
                Toast.makeText(applicationContext, "WorkManager 해제됨", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn2.setOnClickListener{

                miningRef.get().addOnSuccessListener {
                    if(it.value != null && it.value!! != "NULL"){
                        Toast.makeText(applicationContext, "WorkManager 이미 존재함", Toast.LENGTH_SHORT).show()

                    } else {
                        miningRef.setValue("ALL")

                        workManager.enqueue(workRequest)
                        FirebaseMessaging.getInstance().subscribeToTopic("all")
                        Toast.makeText(applicationContext, "WorkManager 추가됨", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener{}
            }

            llayoutBtn3.setOnClickListener{
                isRoom = false

                miningRef.get().addOnSuccessListener {
                    if(it.value != null && it.value != "NULL"){

                        binding.loading.root.visibility = View.VISIBLE

                        Thread{
                            if(it.value == "ALL"){
                                Mining.runMining(applicationContext, "ALL")
                                runOnUiThread {
                                    Toast.makeText(applicationContext, "장르 : 전체", Toast.LENGTH_SHORT).show()
                                    binding.loading.root.visibility = View.GONE
                                }
                                miningRef.setValue("BL")
                            } else if (it.value == "BL") {
                                Mining.runMining(applicationContext, "BL")
                                runOnUiThread {
                                    Toast.makeText(applicationContext, "장르 : BL", Toast.LENGTH_SHORT).show()
                                    binding.loading.root.visibility = View.GONE
                                }
                                miningRef.setValue("FANTASY")
                            } else if (it.value == "FANTASY") {
                                Mining.runMining(applicationContext, "FANTASY")
                                runOnUiThread {
                                    Toast.makeText(applicationContext, "장르 : 판타지", Toast.LENGTH_SHORT).show()
                                    binding.loading.root.visibility = View.GONE
                                }
                                miningRef.setValue("ROMANCE")
                            } else if (it.value == "ROMANCE") {
                                Mining.runMining(applicationContext, "ROMANCE")
                                runOnUiThread {
                                    Toast.makeText(applicationContext, "장르 : 로맨스", Toast.LENGTH_SHORT).show()
                                    binding.loading.root.visibility = View.GONE
                                }
                                miningRef.setValue("ALL")
                            }
                        }.start()
                    } else {
                        miningRef.setValue("ALL")
                        Toast.makeText(applicationContext, "장르 : 전체", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener{}

            }

            llayoutBtn4.setOnClickListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Joara", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Joara", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Joara", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Joara", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Joara_Nobless", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Joara_Nobless", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Joara_Nobless", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Joara_Nobless", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Joara_Premium", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Joara_Premium", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Joara_Premium", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Joara_Premium", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Naver_Today", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Naver_Today", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Naver_Today", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Naver_Today", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Naver_Challenge", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Naver_Challenge", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Naver_Challenge", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Naver_Challenge", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Naver", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Naver", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Naver", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Naver", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Kakao", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Kakao", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Kakao", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Kakao", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Kakao_Stage", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Kakao_Stage", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Kakao_Stage", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Kakao_Stage", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Ridi", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Ridi", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Ridi", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Ridi", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "OneStore", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "OneStore", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "OneStore", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "OneStore", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Munpia", "")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Toksoda", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Toksoda", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Toksoda", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDB(applicationContext, "Toksoda", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Toast.makeText(applicationContext, "Room 완료", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn5.setOnClickListener{

                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Joara", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Joara", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Joara", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Joara", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Joara_Nobless", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Joara_Nobless", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Joara_Nobless", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Joara_Nobless", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Joara_Premium", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Joara_Premium", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Joara_Premium", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Joara_Premium", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Naver_Today", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Naver_Today", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Naver_Today", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Naver_Today", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Naver_Challenge", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Naver_Challenge", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Naver_Challenge", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Naver_Challenge", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Naver", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Naver", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Naver", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Naver", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Kakao", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Kakao", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Kakao", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Kakao", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Kakao_Stage", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Kakao_Stage", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Kakao_Stage", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Kakao_Stage", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Ridi", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Ridi", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Ridi", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Ridi", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "OneStore", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "OneStore", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "OneStore", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "OneStore", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Munpia", "")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Toksoda", "ALL")
                    Log.d("MINING", "ALL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Toksoda", "ROMANCE")
                    Log.d("MINING", "ROMANCE")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Toksoda", "BL")
                    Log.d("MINING", "BL")
                }, 1000) //1초 후 실행
                Handler(Looper.getMainLooper()).postDelayed({
                    Mining.RoomDBRemove(applicationContext, "Toksoda", "FANTASY")
                    Log.d("MINING", "FANTASY")
                }, 1000) //1초 후 실행
                Toast.makeText(applicationContext, "Room 초기화 완료", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn6.setOnClickListener{
                isRoom = true

                miningRef.get().addOnSuccessListener {
                    if(it.value != null && it.value!! != "NULL"){

                        if(it.value == "ALL"){
                            Mining.runMining(applicationContext, "ALL")
                            Toast.makeText(applicationContext, "장르 : 전체", Toast.LENGTH_SHORT).show()
                            miningRef.setValue("BL")
                        } else if (it.value == "BL") {
                            Mining.runMining(applicationContext, "BL")
                            Toast.makeText(applicationContext, "장르 : BL", Toast.LENGTH_SHORT).show()
                            miningRef.setValue("FANTASY")
                        } else if (it.value == "FANTASY") {
                            Mining.runMining(applicationContext, "FANTASY")
                            Toast.makeText(applicationContext, "장르 : 판타지", Toast.LENGTH_SHORT).show()
                            miningRef.setValue("ROMANCE")
                        } else if (it.value == "ROMANCE") {
                            Mining.runMining(applicationContext, "ROMANCE")
                            Toast.makeText(applicationContext, "장르 : 로맨스", Toast.LENGTH_SHORT).show()
                            miningRef.setValue("ALL")
                        }
                    } else {
                        miningRef.setValue("ALL")
                        Toast.makeText(applicationContext, "장르 : 전체", Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener{}
            }

            llayoutBtn7.setOnClickListener {
                Toast.makeText(applicationContext, "푸시 푸시 베이베", Toast.LENGTH_SHORT).show()
                postFCM()
            }

            llayoutBtn8.setOnClickListener {
                Mining.runMining(applicationContext, "ALL")
                Toast.makeText(applicationContext, "장르 : 전체", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn9.setOnClickListener {
                Mining.runMining(applicationContext, "FANTASY")
                Toast.makeText(applicationContext, "장르 : 판타지", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn10.setOnClickListener {
                Mining.runMining(applicationContext, "BL")
                Toast.makeText(applicationContext, "장르 : BL", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn11.setOnClickListener {
//                Mining.runMining(applicationContext, "ROMANCE")
//                Toast.makeText(applicationContext, "장르 : 로맨스", Toast.LENGTH_SHORT).show()

                binding.loading.root.visibility = View.VISIBLE

                Thread{
                    Mining.SetBookCodeData("Joara", "ALL", "959255")

                    runOnUiThread {
                        binding.loading.root.visibility = View.GONE
                        Toast.makeText(applicationContext, "SetBookCodeData", Toast.LENGTH_SHORT).show()
                    }
                }.start()
            }

            llayoutBtn12.setOnClickListener {
                Mining.runMiningEvent(applicationContext)
                Toast.makeText(applicationContext, "이벤트 최신화", Toast.LENGTH_SHORT).show()
            }

            llayoutBtn13.setOnClickListener {

                binding.loading.root.visibility = View.VISIBLE

//                Mining.runMining(applicationContext, "FANTASY")
//                Mining.runMining(applicationContext, "ALL")
//                Mining.runMining(applicationContext, "ROMANCE")
//                Mining.runMining(applicationContext, "BL")

                Mining.getMrBlueBest("ALL", this@ActivityAdmin)
            }

            llayoutBtn14.setOnClickListener {

                val intent = Intent(this@ActivityAdmin, ActivityTest::class.java)
                startActivity(intent)
            }
        }

    }

    private fun postFCM() {

        val fcm = Intent(applicationContext, FCM::class.java)
        startService(fcm)

        val fcmBody = DataFCMBody(
            "/topics/all",
            "high",
            DataFCMBodyData("data", "body"),
            DataFCMBodyNotification("모아바라", "예아 푸시푸시 베이베", "default", "ic_stat_ic_notification"),
        )

        val call = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(FirebaseService::class.java)
            .postRetrofit(
                fcmBody
            )!!

        call.enqueue(object : Callback<FWorkManagerResult?> {
            override fun onResponse(
                call: Call<FWorkManagerResult?>,
                response: retrofit2.Response<FWorkManagerResult?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        Log.d("@@@@FCM", "성공");
                    }
                } else {
                    Log.d("@@@@FCM", "실패2");
                }
            }

            override fun onFailure(call: Call<FWorkManagerResult?>, t: Throwable) {
                Log.d("@@@@FCM", "실패");
            }
        })
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