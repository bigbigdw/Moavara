package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.R
import com.example.moavara.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class ActivityLogin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
    public override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)

        binding.IDtext

        Objects.requireNonNull(binding.IDtext.editText)?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                Log.d("idtext", "beforeTextChanged")
            }

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("idtext", "onTextChanged")
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isEmpty()) {
                    binding.IDtext.error = "빈칸임"
                    binding.IDtext.isErrorEnabled = true
                } else {
                    binding.IDtext.isErrorEnabled = false
                }
            }
        })

        Objects.requireNonNull(binding.PWtext.editText)
            ?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    text: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    Log.d("pwtext", "beforeTextChanged")
                }

                override fun onTextChanged(
                    text: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    Log.d("pwtext", "onTextChanged")
                }

                override fun afterTextChanged(s: Editable) {
                    if (s.toString().isEmpty()) {
                        if (s.toString().isEmpty()) {
                            binding.PWtext.error = "빈칸임"
                            binding.PWtext.isErrorEnabled = true
                        } else {
                            binding.PWtext.isErrorEnabled = false
                        }
                    }
                }
            })

        binding.LoginBtn.setOnClickListener {
            signIn(Objects.requireNonNull(binding.IDtext.editText)?.text.toString(),Objects.requireNonNull(binding.PWtext.editText)?.text.toString())
        }

        binding.RegisterBtn.setOnClickListener {
            startActivity(Intent(this,ActivityRegister::class.java))
            finish()
        }
    }

    // 로그인
    private fun signIn(email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "로그인에 성공 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        moveMainPage(auth?.currentUser)
                    } else {
                        Toast.makeText(
                            baseContext, "로그인에 실패 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }


    // 유저정보 넘겨주고 메인 액티비티 호출
    fun moveMainPage(user: FirebaseUser?){
        if( user!= null){
            startActivity(Intent(this,ActivityMain::class.java))
            finish()
        }
    }
}