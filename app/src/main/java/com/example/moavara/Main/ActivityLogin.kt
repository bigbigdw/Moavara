package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.moavara.Best.DialogLoginAlert
import com.example.moavara.Firebase.FirebaseWorkManager
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.UserInfo
import com.example.moavara.Search.WeekendDate
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class ActivityLogin : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private var googleSignInClient: GoogleSignInClient? = null
    private var GOOGLE_LOGIN_CODE = 9001
    private var callbackManager: CallbackManager? = null
    private lateinit var binding: ActivityLoginBinding

    var context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            // 구글 로그인 버튼
            btnLogin.setOnClickListener { googleLogin() }

            // 로그인 버튼
            btnRegister.setOnClickListener {
                auth?.signOut()
                googleSignInClient?.signOut()
            }


        }


        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


    }

    // 구글 로그인 함수
    fun googleLogin() {
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        var credential = GoogleAuthProvider.getCredential(account?.idToken, null)

        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->

                if (task.isSuccessful) {

                   val user =  mRootRef.child("User").child(task.result?.user?.uid.toString())

                    user.child("Nickname").setValue("USER_NAME")
                    user.child("Genre").setValue("USER_GENRE")

                    user.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val group: UserInfo? = dataSnapshot.getValue(UserInfo::class.java)

                                if (group != null) {
                                    if (group.Nickname != "USER_NAME") {
                                        if(dataSnapshot.getValue(UserInfo::class.java) != null){


                                            savePreferences("NICKNAME", group?.Nickname ?: "")
                                            savePreferences("GENRE", group?.Genre ?: "")
                                            savePreferences("UID", task.result?.user?.uid.toString())

                                            Toast.makeText(context, "환영한다 " + group?.Nickname, Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(context, ActivityMain::class.java))
                                            finish()
                                        }

                                    } else {

                                        var dialogLoginAlert: DialogLoginAlert?

                                        val btnStep1 = View.OnClickListener { v: View? ->

                                            val currentUser = FirebaseAuth.getInstance().currentUser
                                            currentUser?.delete()
                                                ?.addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        auth?.signOut()
                                                        googleSignInClient?.signOut()
                                                        Toast.makeText(
                                                            context,
                                                            "계정 정보 삭제",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                        }

                                        val btnStep2 = View.OnClickListener { v: View? ->

                                            Toast.makeText(context, "환영한다 뉴비", Toast.LENGTH_SHORT).show()
                                            mRootRef.child("User").child(task.result?.user?.uid.toString()).setValue("HIHI")

                                            val intent = Intent(context, ActivityGenre::class.java)
                                            intent.putExtra("MODE", "NEWBIE")
                                            intent.putExtra("UID", task.result?.user?.uid.toString())
                                            startActivity(intent)
                                            finish()
                                        }

                                        // 안내 팝업
                                        dialogLoginAlert = DialogLoginAlert(
                                            context,
                                            btnStep1,
                                            btnStep2
                                        )

                                        dialogLoginAlert.show()


                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                } else {
                    // 틀렸을 때
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    Log.d("####", task.exception?.message.toString())
                }
            }
    }
    // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
//    public override fun onStart() {
//        super.onStart()
//        moveMainPage(auth?.currentUser)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager!!.onActivityResult(requestCode, resultCode, data)

        Log.d("####-1", "11111")

        if (requestCode == GOOGLE_LOGIN_CODE) {
            var result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }!!
            // 구글API가 넘겨주는 값 받아옴

            Log.d("####-2", result.status.toString())

            if (result.isSuccess) {
                var accout = result.signInAccount
                firebaseAuthWithGoogle(accout)
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                Log.d("####-31", result.status.toString())
            } else {
                Log.d("####-32", result.status.toString())
                Toast.makeText(this, "로그인 실패!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePreferences(key : String, value: String) {
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    // 유저정보 넘겨주고 메인 액티비티 호출
    fun moveMainPage(user: FirebaseUser?){
        if(user!= null){
            mRootRef.child("User").child(user.uid)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val group: UserInfo? = dataSnapshot.getValue(UserInfo::class.java)

                            savePreferences("NICKNAME", group?.Nickname ?: "")
                            savePreferences("GENRE", group?.Genre ?: "")
                            savePreferences("UID", user.uid)

                            Toast.makeText(context, "환영한다 " + group?.Nickname, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(context, ActivityMain::class.java))
                            finish()
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    }
}