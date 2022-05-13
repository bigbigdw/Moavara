package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.CommunityBoard
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Mining
import com.example.moavara.databinding.ActivityBestDetailBinding
import com.example.moavara.databinding.ActivityLoginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class ActivityLogin : AppCompatActivity() {
    private var auth : FirebaseAuth? = null
    private var googleSignInClient : GoogleSignInClient? = null
    private var GOOGLE_LOGIN_CODE = 9001
    private var callbackManager : CallbackManager? = null
    private lateinit var binding: ActivityLoginBinding
    var context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){

            // 구글 로그인 버튼
            btnLogin.setOnClickListener { googleLogin() }

            // 로그인 버튼
            btnRegister.setOnClickListener {
                auth?.signOut()
                googleSignInClient?.signOut()
                Toast.makeText(context,"로그아웃 성공",Toast.LENGTH_SHORT).show()
            }
        }


        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
    }

    // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
    public override fun onStart() {
        super.onStart()
//        moveMainPage(auth?.currentUser)
    }

    // 구글 로그인 함수
    fun googleLogin(){
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent,GOOGLE_LOGIN_CODE)
    }

    fun firebaseAuthWithGoogle(account : GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener{
                    task ->

                Log.d("@@@", task.result.toString());

                if(task.isSuccessful){
                    // 아이디, 비밀번호 맞을 때
//                    moveMainPage(task.result?.user)

                    mRootRef.child("User").child(task.result?.user?.uid.toString()).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(context,"환영한다 기존회원",Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(context,"환영한다 뉴비",Toast.LENGTH_SHORT).show()
                                mRootRef.child("User").child(task.result?.user?.uid.toString()).setValue("HIHI")

                                startActivity(Intent(context,ActivityRegister::class.java))
                                finish()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })


                }else{
                    // 틀렸을 때
                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode,resultCode,data)

        if(requestCode == GOOGLE_LOGIN_CODE){
            var result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }!!
            // 구글API가 넘겨주는 값 받아옴

            if(result.isSuccess) {
                var accout = result.signInAccount
                firebaseAuthWithGoogle(accout)
                Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"로그인 실패",Toast.LENGTH_SHORT).show()
            }
        }
    }



    // 유저정보 넘겨주고 메인 액티비티 호출
    fun moveMainPage(user: FirebaseUser?){
        if(user!= null){
            startActivity(Intent(this,ActivityMain::class.java))
            finish()
        }
    }
}