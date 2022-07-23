package com.example.moavara.Main

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.User.ActivityGuide
import com.example.moavara.Util.Genre
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.ActivityGenreBinding


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

            val llayoutUpperBG = GradientDrawable().apply {
                setColor(Color.parseColor("#121212"))
                shape = GradientDrawable.RECTANGLE
                cornerRadii = floatArrayOf(0f,0f,0f,0f, 50f.dpToPx(), 50f.dpToPx(), 50f.dpToPx(), 50f.dpToPx())
            }

            llayoutUpper.background = llayoutUpperBG

            val llayout_btnBG = GradientDrawable().apply {
                setColor(Color.parseColor("#0D0E10"))
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 100f.dpToPx()
                setStroke(2f.dpToPx().toInt(), Color.parseColor("#773E424B"))
            }

            val llayout_btnOnBG = GradientDrawable().apply {
                setColor(Color.parseColor("#0D0E10"))
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 100f.dpToPx()
                setStroke(2f.dpToPx().toInt(), Color.parseColor("#844DF3"))
            }

            llayoutBtn1.background = llayout_btnBG
            llayoutBtn2.background = llayout_btnBG
            llayoutBtn3.background = llayout_btnBG
            llayoutBtn4.background = llayout_btnBG

            if(mode == "USER" && UID != ""){
                llayoutNickname.visibility = View.GONE
                llayoutNickname.visibility = View.GONE
                llayoutBtnGenre.visibility = View.GONE
                btnNickname.visibility = View.GONE
                llayoutGenre.visibility = View.VISIBLE

                if(Genre.getGenre(context).toString() == "FANTASY"){
                    llayoutBtn1.background = llayout_btnOnBG
                } else if(Genre.getGenre(context).toString() == "ROMANCE"){
                    llayoutBtn2.background = llayout_btnOnBG
                }
                else if(Genre.getGenre(context).toString() == "ALL"){
                    llayoutBtn3.background = llayout_btnOnBG
                }
                else if(Genre.getGenre(context).toString() == "BL"){
                    llayoutBtn4.background = llayout_btnOnBG
                }

                tviewTitle.text = "환영합니다."
                tviewUserName.visibility = View.VISIBLE
                tviewUserName.text = context.getSharedPreferences("pref", MODE_PRIVATE)?.getString("NICKNAME", "")
                tviewUserName2.text = "님"

                llayoutNickname.visibility = View.GONE
                llayoutGenre.visibility = View.VISIBLE

            } else {
                llayoutNickname.visibility = View.VISIBLE
                llayoutGenre.visibility = View.GONE

                etviewNickname.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            text: CharSequence,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            text: CharSequence,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                        }

                        override fun afterTextChanged(s: Editable) {
                            if (s.toString().isEmpty()) {
                                btnNickname.setBackgroundColor(Color.parseColor("#26292E"))
                            } else {
                                btnNickname.setBackgroundColor(Color.parseColor("#844DF3"))
                            }
                        }
                    })

                btnNickname.setOnClickListener{
                    if(etviewNickname.text.toString() == ""){
                        Toast.makeText(context, "닉네임을 설정해 주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        llayoutNickname.visibility = View.GONE
                        llayoutNickname.visibility = View.GONE
                        llayoutGenre.visibility = View.VISIBLE
                        llayoutBtnGenre.visibility = View.VISIBLE
                        tviewTitle.text = "환영합니다."
                        tviewUserName.visibility = View.VISIBLE
                        tviewUserName.text = etviewNickname.text
                        tviewUserName2.text = "님 모아바라 입니다."
                        tviewTitle.visibility = View.GONE
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
                    llayoutBtn1.background = llayout_btnOnBG
                    llayoutBtn2.background = llayout_btnBG
                    llayoutBtn3.background = llayout_btnBG
                    llayoutBtn4.background = llayout_btnBG
                    userInfo.child(UID).child("Genre").setValue("FANTASY")
                    llayoutBtnGenre.setBackgroundColor(Color.parseColor("#844DF3"))
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
                    llayoutBtn1.background = llayout_btnBG
                    llayoutBtn2.background = llayout_btnOnBG
                    llayoutBtn3.background = llayout_btnBG
                    llayoutBtn4.background = llayout_btnBG
                    llayoutBtnGenre.setBackgroundColor(Color.parseColor("#844DF3"))
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
                    llayoutBtn1.background = llayout_btnBG
                    llayoutBtn2.background = llayout_btnBG
                    llayoutBtn3.background = llayout_btnOnBG
                    llayoutBtn4.background = llayout_btnBG
                    llayoutBtnGenre.setBackgroundColor(Color.parseColor("#844DF3"))
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
                    llayoutBtn1.background = llayout_btnBG
                    llayoutBtn2.background = llayout_btnBG
                    llayoutBtn3.background = llayout_btnBG
                    llayoutBtn4.background = llayout_btnOnBG
                    llayoutBtnGenre.setBackgroundColor(Color.parseColor("#844DF3"))
                }
            }

            llayoutBtnGenre.setOnClickListener {
                with(binding){
                    if(genre == ""){
                        Toast.makeText(context, "장르를 선택해 주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        userInfo.child(UID).child("Nickname").setValue(etviewNickname.text.toString())

                        var dialogLogin: DialogConfirmLogin? = null

                        val leftListener = View.OnClickListener { v: View? ->
                            dialogLogin?.dismiss()
                        }

                        val rightListener = View.OnClickListener { v: View? ->

                            savePreferences("NICKNAME", etviewNickname.text.toString())
                            val intent = Intent(context, ActivityGuide::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }

                        // 안내 팝업
                        dialogLogin = DialogConfirmLogin(
                            context,
                            "",
                            "닉네임 : " + etviewNickname.text + "\n선호장르 : $genre",
                            leftListener,
                            rightListener
                        )

                        dialogLogin.window?.setBackgroundDrawable(
                            ColorDrawable(
                                Color.TRANSPARENT)
                        )
                        dialogLogin.show()
                    }
                }
            }
        }
    }


    override fun onBackPressed() {
        if(mode != "USER") {
            if(binding.llayoutNickname.visibility == View.VISIBLE){

                var dialogLogin: DialogConfirmLogin? = null

                val leftListener = View.OnClickListener { v: View? ->
                    dialogLogin?.dismiss()
                }

                val rightListener = View.OnClickListener { v: View? ->
                    savePreferences("NICKNAME", binding.etviewNickname.text.toString())
                    val intent = Intent(context, ActivityGuide::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

                // 안내 팝업
                dialogLogin = DialogConfirmLogin(
                    context,
                    "안내",
                    "가입을 그만두고 로그인 화면으로 돌아가시겠습니까?",
                    leftListener,
                    rightListener
                )

                dialogLogin.window?.setBackgroundDrawable(
                    ColorDrawable(
                        Color.TRANSPARENT)
                )
                dialogLogin.show()

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