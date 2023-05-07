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
import androidx.room.Room
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.User.ActivityGuide
import com.example.moavara.Util.Genre
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.ActivityGenreBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase


class ActivityGenre : AppCompatActivity() {

    private lateinit var binding: ActivityGenreBinding
    var mode = ""
    var genre = ""
    var UID = ""
    var Email = ""

    var userDao: DBUser? = null
    var UserInfo : DataBaseUser? = null
    var userGenre = ""
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = Firebase.analytics

        userDao = Room.databaseBuilder(
            this,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        if(userDao?.daoUser() != null){
            UserInfo = userDao?.daoUser()?.get()
            val bundle = Bundle()
            bundle.putString("USER_GENRE", UserInfo?.Genre)
            firebaseAnalytics.logEvent("USER_ActivityGenre", bundle)
        }

        mode = intent.getStringExtra("MODE") ?: "USER"
        UID = intent.getStringExtra("UID") ?: ""
        Email = intent.getStringExtra("EMAIL") ?: ""

        with(binding){

            val llayoutUpperBG = GradientDrawable().apply {
                setColor(Color.parseColor("#121212"))
                shape = GradientDrawable.RECTANGLE
                cornerRadii = floatArrayOf(0f,0f,0f,0f, 50f.dpToPx(), 50f.dpToPx(), 50f.dpToPx(), 50f.dpToPx())
            }

            llayoutUpper.background = llayoutUpperBG

            val llayoutBtnBG = GradientDrawable().apply {
                setColor(Color.parseColor("#0D0E10"))
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 100f.dpToPx()
                setStroke(2f.dpToPx().toInt(), Color.parseColor("#773E424B"))
            }

            val llayoutBtnOnBG = GradientDrawable().apply {
                setColor(Color.parseColor("#0D0E10"))
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 100f.dpToPx()
                setStroke(2f.dpToPx().toInt(), Color.parseColor("#844DF3"))
            }

            llayoutBtn1.background = llayoutBtnBG
            llayoutBtn2.background = llayoutBtnBG
            llayoutBtn3.background = llayoutBtnBG
            llayoutBtn4.background = llayoutBtnBG

            if(mode == "USER" && UID != ""){
                llayoutNickname.visibility = View.GONE
                llayoutNickname.visibility = View.GONE
                llayoutBtnGenre.visibility = View.GONE
                llayoutBtnNickname.visibility = View.GONE
                llayoutGenre.visibility = View.VISIBLE

                if(Genre.getGenre(this@ActivityGenre).toString() == "FANTASY"){
                    llayoutBtn1.background = llayoutBtnOnBG
                } else if(Genre.getGenre(this@ActivityGenre).toString() == "ROMANCE"){
                    llayoutBtn2.background = llayoutBtnOnBG
                }
                else if(Genre.getGenre(this@ActivityGenre).toString() == "ALL"){
                    llayoutBtn3.background = llayoutBtnOnBG
                }
                else if(Genre.getGenre(this@ActivityGenre).toString() == "BL"){
                    llayoutBtn4.background = llayoutBtnOnBG
                }

                tviewTitle.text = "환영합니다."
                tviewUserName.visibility = View.VISIBLE
                tviewUserName.text = getSharedPreferences("pref", MODE_PRIVATE)?.getString("NICKNAME", "")
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
                                llayoutBtnNickname.setBackgroundColor(Color.parseColor("#26292E"))
                            } else {
                                llayoutBtnNickname.setBackgroundColor(Color.parseColor("#844DF3"))
                            }
                        }
                    })

                llayoutBtnNickname.setOnClickListener{
                    if(etviewNickname.text.toString() == ""){
                        Toast.makeText(this@ActivityGenre, "닉네임을 설정해 주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        nicknameConfirm()
                    }
                }
            }

            binding.tviewUserName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.tviewGenre.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            llayoutBtn1.setOnClickListener {
                savePreferences("GENRE","FANTASY")
                mRootRef.child("User").child(UID).child("Genre").setValue("FANTASY")

                userGenre = "FANTASY"

                if(mode == "USER"){
                    if(UserInfo?.Genre != "FANTASY"){
                        llayoutApplyGenre.visibility = View.VISIBLE
                        llayoutApplyGenre.setBackgroundColor(Color.parseColor("#844DF3"))
                    } else {
                        llayoutApplyGenre.visibility = View.GONE
                    }
                } else {
                    genre = "FANTASY"
                    mRootRef.child("User").child(UID).child("Genre").setValue("FANTASY")
                    llayoutBtnGenre.setBackgroundColor(Color.parseColor("#844DF3"))
                }
                llayoutBtn1.background = llayoutBtnOnBG
                llayoutBtn2.background = llayoutBtnBG
                llayoutBtn3.background = llayoutBtnBG
                llayoutBtn4.background = llayoutBtnBG

                val bundle = Bundle()
                bundle.putString("USER_GENRE", "FANTASY")
                firebaseAnalytics.logEvent("USER_ActivityGenre", bundle)

            }

            llayoutBtn2.setOnClickListener {
                savePreferences("GENRE","ROMANCE")
                mRootRef.child("User").child(UID).child("Genre").setValue("ROMANCE")

                val bundle = Bundle()
                bundle.putString("USER_GENRE", "ROMANCE")
                firebaseAnalytics.logEvent("USER_ActivityGenre", bundle)

                userGenre = "ROMANCE"

                if(mode == "USER"){
                    if(UserInfo?.Genre != "ROMANCE"){
                        llayoutApplyGenre.visibility = View.VISIBLE
                        llayoutApplyGenre.setBackgroundColor(Color.parseColor("#844DF3"))
                    } else {
                        llayoutApplyGenre.visibility = View.GONE
                    }
                } else {
                    genre = "ROMANCE"
                    mRootRef.child("User").child(UID).child("Genre").setValue("ROMANCE")
                    llayoutBtnGenre.setBackgroundColor(Color.parseColor("#844DF3"))
                    llayoutBtnGenre.visibility = View.VISIBLE
                }
                llayoutBtn1.background = llayoutBtnBG
                llayoutBtn2.background = llayoutBtnOnBG
                llayoutBtn3.background = llayoutBtnBG
                llayoutBtn4.background = llayoutBtnBG
            }

            llayoutBtn3.setOnClickListener {
                savePreferences("GENRE","ALL")
                mRootRef.child("User").child(UID).child("Genre").setValue("ALL")

                val bundle = Bundle()
                bundle.putString("USER_GENRE", "ALL")
                firebaseAnalytics.logEvent("USER_ActivityGenre", bundle)

                userGenre = "ALL"

                if(mode == "USER"){
                    if(UserInfo?.Genre != "ALL"){
                        llayoutApplyGenre.visibility = View.VISIBLE
                        llayoutApplyGenre.setBackgroundColor(Color.parseColor("#844DF3"))
                    } else {
                        llayoutApplyGenre.visibility = View.GONE
                    }
                } else {
                    genre = "ALL"
                    mRootRef.child("User").child(UID).child("Genre").setValue("ALL")
                    llayoutBtnGenre.setBackgroundColor(Color.parseColor("#844DF3"))
                    llayoutBtnGenre.visibility = View.VISIBLE
                }
                llayoutBtn1.background = llayoutBtnBG
                llayoutBtn2.background = llayoutBtnBG
                llayoutBtn3.background = llayoutBtnOnBG
                llayoutBtn4.background = llayoutBtnBG
            }

            llayoutBtn4.setOnClickListener {
                savePreferences("GENRE","BL")
                mRootRef.child("User").child(UID).child("Genre").setValue("BL")

                val bundle = Bundle()
                bundle.putString("USER_GENRE", "BL")
                firebaseAnalytics.logEvent("USER_ActivityGenre", bundle)

                userGenre = "BL"

                if(mode == "USER"){

                    if(UserInfo?.Genre != "BL"){
                        llayoutApplyGenre.visibility = View.VISIBLE
                        llayoutApplyGenre.setBackgroundColor(Color.parseColor("#844DF3"))
                    } else {
                        llayoutApplyGenre.visibility = View.GONE
                    }
                } else {
                    genre = "BL"
                    mRootRef.child("User").child(UID).child("Genre").setValue("BL")
                    llayoutBtnGenre.setBackgroundColor(Color.parseColor("#844DF3"))
                    llayoutBtnGenre.visibility = View.VISIBLE
                }
                llayoutBtn1.background = llayoutBtnBG
                llayoutBtn2.background = llayoutBtnBG
                llayoutBtn3.background = llayoutBtnBG
                llayoutBtn4.background = llayoutBtnOnBG
            }

            llayoutBtnGenre.setOnClickListener {
                with(binding){
                    if(genre == ""){
                        Toast.makeText(this@ActivityGenre, "장르를 선택해 주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        mRootRef.child("User").child(UID).child("Nickname").setValue(etviewNickname.text.toString())
                        mRootRef.child("User").child(UID).child("Email").setValue(Email)
                        mRootRef.child("User").child(UID).child("isInit").setValue(true)

                        userDao?.daoUser()?.insert(
                            DataBaseUser(
                                etviewNickname.text.toString() ?: "",
                                userGenre,
                                UID
                            )
                        )

                        when (genre) {
                            "ALL" -> {
                                genre = "장르 무관"
                            }
                            "FANTASY" -> {
                                genre = "판타지"
                            }
                            "ROMANCE" -> {
                                genre = "로맨스"
                            }
                            "BL" -> {
                                genre = "BL"
                            }
                        }

                        var dialogLogin: DialogConfirm? = null

                        val leftListener = View.OnClickListener {
                            dialogLogin?.dismiss()
                        }

                        val rightListener = View.OnClickListener {

                            savePreferences("NICKNAME", etviewNickname.text.toString())
                            val intent = Intent(this@ActivityGenre, ActivityGuide::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }

                        // 안내 팝업
                        dialogLogin = DialogConfirm(
                            this@ActivityGenre,
                            "",
                            "닉네임 : " + etviewNickname.text + "\n선호장르 : $genre",
                            leftListener,
                            rightListener,
                            "",
                            ""
                        )

                        dialogLogin.window?.setBackgroundDrawable(
                            ColorDrawable(
                                Color.TRANSPARENT)
                        )
                        dialogLogin.show()
                    }
                }
            }

            llayoutApplyGenre.setOnClickListener {
                var dialogGenre: DialogConfirm? = null

                val leftListener = View.OnClickListener {
                    dialogGenre?.dismiss()
                }

                val rightListener = View.OnClickListener {

                    userDao?.daoUser()?.init()

                    userDao?.daoUser()?.insert(
                        DataBaseUser(
                            UserInfo?.Nickname ?: "",
                            userGenre,
                            UserInfo?.UID ?: ""
                        )
                    )

                    Toast.makeText(this@ActivityGenre, "변경이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    val novelIntent = Intent(this@ActivityGenre, ActivityMain::class.java)
                    novelIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    novelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(novelIntent)
                }

                var genre = ""

                when (userGenre) {
                    "ALL" -> {
                        genre = "장르 무관으"
                    }
                    "FANTASY" -> {
                        genre = "판타지"
                    }
                    "ROMANCE" -> {
                        genre = "로맨스"
                    }
                    "BL" -> {
                        genre = "BL"
                    }
                }

                // 안내 팝업
                dialogGenre = DialogConfirm(
                    this@ActivityGenre,
                    "선호장르를 ${genre}로 변경하시겠습니까?",
                    "",
                    leftListener,
                    rightListener,
                    "",
                    ""
                )

                dialogGenre.window?.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                )
                dialogGenre.show()
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(mode != "USER") {
            if(binding.llayoutNickname.visibility == View.VISIBLE){

                var dialogLogin: DialogConfirm? = null

                val leftListener = View.OnClickListener { v: View? ->
                    dialogLogin?.dismiss()
                }

                val rightListener = View.OnClickListener {
                    savePreferences("NICKNAME", binding.etviewNickname.text.toString())

                    mRootRef.child("User").child(UID).removeValue()

                    val intent = Intent(this@ActivityGenre, ActivityLogin::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                    val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                    val client = GoogleSignIn.getClient(this, opt)
                    client.revokeAccess()
                }

                // 안내 팝업
                dialogLogin = DialogConfirm(
                    this@ActivityGenre,
                    "안내",
                    "가입을 그만두고 로그인 화면으로 돌아가시겠습니까?",
                    leftListener,
                    rightListener,
                    "취소",
                    "확인"
                )

                dialogLogin.window?.setBackgroundDrawable(
                    ColorDrawable(
                        Color.TRANSPARENT)
                )
                dialogLogin.show()

            } else if(binding.llayoutGenre.visibility == View.VISIBLE){
                binding.llayoutNickname.visibility = View.VISIBLE
                binding.llayoutGenre.visibility = View.GONE
                binding.llayoutBtnGenre.visibility = View.GONE
                binding.llayoutBtnNickname.visibility = View.VISIBLE
            }

        } else {
            super.onBackPressed()
        }
    }

    private fun nicknameConfirm(){
        var dialogLogin: DialogConfirm? = null

        val leftListener = View.OnClickListener {
            dialogLogin?.dismiss()
        }

        val rightListener = View.OnClickListener {

            dialogLogin?.dismiss()

            with(binding){
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

        // 안내 팝업
        dialogLogin = DialogConfirm(
            this@ActivityGenre,
            "",
            "닉네임 : ${binding.etviewNickname.text}",
            leftListener,
            rightListener,
            "취소",
            "확인"
        )

        dialogLogin.window?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT)
        )
        dialogLogin.show()
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