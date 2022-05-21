package com.example.moavara.Main

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.R
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.ActivityGenreBinding
import com.google.firebase.auth.FirebaseAuth


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

        val context = this

        mode = intent.getStringExtra("MODE") ?: "USER"
        UID = intent.getStringExtra("UID") ?: ""

        with(binding){

            if(mode == "USER" && UID != ""){
                llayoutNickname.visibility = View.GONE
                llayoutNickname.visibility = View.GONE
                llayoutGenre.visibility = View.VISIBLE

                if(Genre.getGenre(context).toString() == "FANTASY"){
                    llayoutBtn1.setBackgroundResource(R.drawable.selector_genre_on)
                } else if(Genre.getGenre(context).toString() == "ROMANCE"){
                    llayoutBtn2.setBackgroundResource(R.drawable.selector_genre_on)
                }
                else if(Genre.getGenre(context).toString() == "ALL"){
                    llayoutBtn3.setBackgroundResource(R.drawable.selector_genre_on)
                }
                else if(Genre.getGenre(context).toString() == "BL"){
                    llayoutBtn4.setBackgroundResource(R.drawable.selector_genre_on)
                }

                tviewTitle.text = "환영합니다."
                tviewUserName.visibility = View.VISIBLE
                tviewUserName.text = context.getSharedPreferences("pref", androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE)?.getString("NICKNAME", "")
                tviewUserName2.text = " 님"

                llayoutNickname.visibility = View.GONE
                llayoutGenre.visibility = View.VISIBLE

            } else {
                llayoutNickname.visibility = View.VISIBLE
                llayoutGenre.visibility = View.GONE

                btnNickname.setOnClickListener{
                    if(etviewNickname.text.toString() == ""){
                        Toast.makeText(context, "닉네임을 설정해 주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        llayoutNickname.visibility = View.GONE
                        llayoutGenre.visibility = View.VISIBLE
                        tviewTitle.text = "환영합니다."
                        tviewUserName.visibility = View.VISIBLE
                        tviewUserName.text = etviewNickname.text
                        tviewUserName2.text = " 님"
                    }
                }
            }

            binding.tviewUserName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.tviewGenre.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            llayoutBtn1.setOnClickListener {
                savePreferences("GENRE","FANTASY")
                userInfo.child(UID).child("Genre").setValue("FANTASY")

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
                    submit()
                }

            }

            llayoutBtn2.setOnClickListener {
                savePreferences("GENRE","ROMANCE")
                userInfo.child(UID).child("Genre").setValue("ROMANCE")

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
                    submit()
                }
            }

            llayoutBtn3.setOnClickListener {
                savePreferences("GENRE","ALL")
                userInfo.child(UID).child("Genre").setValue("ALL")

                if(mode == "USER"){
                    val novelIntent = Intent(context, ActivityMain::class.java)
                    startActivity(novelIntent)
                } else {
                    genre = "ALL"
                    llayoutBtn1.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn2.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn3.setBackgroundResource(R.drawable.selector_genre_on)
                    llayoutBtn4.setBackgroundResource(R.drawable.selector_genre)
                    submit()
                }
            }

            llayoutBtn4.setOnClickListener {
                savePreferences("GENRE","BL")
                userInfo.child(UID).child("Genre").setValue("BL")

                if(mode == "USER"){
                    val novelIntent = Intent(context, ActivityMain::class.java)
                    startActivity(novelIntent)
                } else {
                    genre = "BL"
                    llayoutBtn1.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn2.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn3.setBackgroundResource(R.drawable.selector_genre)
                    llayoutBtn4.setBackgroundResource(R.drawable.selector_genre_on)
                    submit()
                }
            }
        }
    }

    fun submit(){

        with(binding){
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
                    savePreferences("NICKNAME", etviewNickname.text.toString())
                    val intent = Intent(context, ActivityMain::class.java)
                    Toast.makeText(context, "모아바라에 오신것을 환영합니다", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
                myAlertBuilder.setNegativeButton("아니요") { _, _ -> }

                myAlertBuilder.show()
            }
        }

    }


    override fun onBackPressed() {
        if(mode != "USER") {
            if(binding.llayoutNickname.visibility == View.VISIBLE){
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

            } else if(binding.llayoutGenre.visibility == View.VISIBLE){
                binding.llayoutNickname.visibility = View.VISIBLE
                binding.llayoutGenre.visibility = View.GONE
            }

        } else {
            super.onBackPressed()
        }
    }

    private fun savePreferences(key : String, value: String) {
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()

        overridePendingTransition(0, 0)
    }
}