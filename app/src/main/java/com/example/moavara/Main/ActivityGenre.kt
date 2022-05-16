package com.example.moavara.Main

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.moavara.Firebase.FirebaseWorkManager
import com.example.moavara.R
import com.example.moavara.Search.WeekendDate
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.ActivityGenreBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_genre.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit


class ActivityGenre : AppCompatActivity() {

    private lateinit var binding: ActivityGenreBinding
    var context = this
    var mode = ""
    var userInfo = mRootRef.child("User")
    var genre = ""
    var UID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mode = intent.getStringExtra("MODE") ?: "USER"
        UID = intent.getStringExtra("UID") ?: ""

        with(binding){

            if(mode == "USER" && UID != ""){
                llayoutNickname.visibility = View.GONE
                clayoutBtn.visibility = View.GONE
                llayoutNickname.visibility = View.GONE
                llayoutGenre.visibility = View.VISIBLE
            } else {
                llayoutNickname.visibility = View.VISIBLE
                clayoutBtn.visibility = View.VISIBLE

                btnNext.setOnClickListener{
                    if(etviewNickname.text.toString() == ""){
                        Toast.makeText(context, "닉네임을 설정해 주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        llayoutNickname.visibility = View.GONE
                        llayoutGenre.visibility = View.VISIBLE
                        btnNext.visibility = View.GONE
                        btnSubmit.visibility = View.VISIBLE
                    }
                }

                btnSubmit.setOnClickListener {
                    if(genre == ""){
                        Toast.makeText(context, "장르를 선택해 주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        userInfo.child(UID).child("Nickname").setValue(etviewNickname.text.toString())

                        val myAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(this@ActivityGenre)
                        myAlertBuilder.setTitle("모아바라 가입")
                        myAlertBuilder.setMessage("회원 정보를 확인해주세요 \n 닉네임 : " + etviewNickname.text + "\n선호장르 : $genre \n가 맞나요?")
                        myAlertBuilder.setPositiveButton(
                            "예"
                        ) { _, _ ->
                            val intent = Intent(context, ActivityMain::class.java)
                            Toast.makeText(context, "모아바라에 오신것을 환영합니다", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }
                        myAlertBuilder.setNegativeButton(
                            "아니요"
                        ) { _, _ ->
                            finish()
                        }
                        // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
                        myAlertBuilder.show()
                    }
                }
            }

            tviewMoavara.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tviewGenre.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            llayoutBtn1.setOnClickListener {
                savePreferences("FANTASY")

                if(mode == "USER"){
                    val novelIntent = Intent(context, ActivityMain::class.java)
                    startActivity(novelIntent)
                } else {
                    genre = "FANTASY"
                    llayoutBtn1.setBackgroundResource(R.drawable.selector_genre_on)
                    llayoutBtn2.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn3.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn4.setBackgroundResource(R.drawable.selector_genre)
                    userInfo.child(UID).child("Genre").setValue("FANTASY")
                }

            }

            llayoutBtn2.setOnClickListener {
                if(mode == "USER"){
                    val novelIntent = Intent(context, ActivityMain::class.java)
                    startActivity(novelIntent)
                } else {
                    genre = "ROMANCE"
                    userInfo.child(UID).child("Genre").setValue("ROMANCE")
                    llayoutBtn1.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn2.setBackgroundResource(R.drawable.selector_genre_on)
                    llayoutBtn3.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn4.setBackgroundResource(R.drawable.selector_genre)
                }
            }

            llayoutBtn3.setOnClickListener {

                if(mode == "USER"){
                    val novelIntent = Intent(context, ActivityMain::class.java)
                    startActivity(novelIntent)
                } else {
                    genre = "ALL"
                    userInfo.child(UID).child("Genre").setValue("ALL")
                    llayoutBtn1.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn2.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn3.setBackgroundResource(R.drawable.selector_genre_on)
                    llayoutBtn4.setBackgroundResource(R.drawable.selector_genre)
                }
            }

            llayoutBtn4.setOnClickListener {

                if(mode == "USER"){
                    val novelIntent = Intent(context, ActivityMain::class.java)
                    startActivity(novelIntent)
                } else {
                    genre = "BL"
                    userInfo.child(UID).child("Genre").setValue("BL")
                    llayoutBtn1.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn2.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn3.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn4.setBackgroundResource(R.drawable.selector_genre_on)
                }
            }
        }

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
                if(it.value != null && it.value!! == "MINING"){
                    Toast.makeText(this, "WorkManager 이미 존재함", Toast.LENGTH_SHORT).show()

                } else {
                    miningRef.setValue("MINING")

                    workManager.enqueue(workRequest)
                    FirebaseMessaging.getInstance().subscribeToTopic("all")
                    Toast.makeText(this, "WorkManager 추가됨", Toast.LENGTH_SHORT).show()
                }
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
        if(mode != "USER") {
            if(llayoutNickname.visibility == View.VISIBLE){
                val myAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(this@ActivityGenre)
                myAlertBuilder.setTitle("모아바라 가입")
                myAlertBuilder.setMessage("가입을 그만두고 로그인 화면으로 돌아가시겠습니까?")
                myAlertBuilder.setPositiveButton(
                    "예"
                ) { _, _ ->

                    val currentUser = FirebaseAuth.getInstance().currentUser
                    currentUser?.delete()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                userInfo.child(UID).removeValue()
                                super.onBackPressed()
                                finish()
                            }
                        }

                }
                myAlertBuilder.setNegativeButton(
                    "아니요"
                ) { _, _ ->
                    finish()
                }
                // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
                myAlertBuilder.show()

            } else if(llayoutGenre.visibility == View.VISIBLE){
                llayoutNickname.visibility = View.VISIBLE
                llayoutGenre.visibility = View.GONE
            }

        } else {
            super.onBackPressed()
        }
    }

    private fun savePreferences(genre: String) {
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("GENRE", genre)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()

        overridePendingTransition(0, 0)
    }
}