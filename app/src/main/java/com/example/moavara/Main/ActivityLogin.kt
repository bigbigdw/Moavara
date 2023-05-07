package com.example.moavara.Main

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.R
import com.example.moavara.Search.UserInfo
import com.example.moavara.Util.dpToPx
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
import com.google.firebase.database.ValueEventListener

class ActivityLogin : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private var googleSignInClient: GoogleSignInClient? = null
    private var GOOGLE_LOGIN_CODE = 9001
    private var callbackManager: CallbackManager? = null
    private lateinit var binding: ActivityLoginBinding
    var context = this
    var userDao: DBUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            loading.root.visibility = View.GONE
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            llayoutUpper.background = GradientDrawable().apply {
                setColor(Color.parseColor("#121212"))
                shape = GradientDrawable.RECTANGLE
                cornerRadii = floatArrayOf(0f,0f,0f,0f, 50f.dpToPx(), 50f.dpToPx(), 50f.dpToPx(), 50f.dpToPx())
            }

            btnLogin.background = GradientDrawable().apply {
                setColor(Color.parseColor("#844DF3"))
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 100f.dpToPx()
            }

            // 구글 로그인 버튼
            btnLogin.setOnClickListener { googleLogin() }

            // 로그인 버튼
            btnRegister.setOnClickListener {
                auth?.signOut()
                googleSignInClient?.signOut()
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    // 구글 로그인 함수
    private fun googleLogin() {
        val signInIntent = googleSignInClient?.signInIntent
        if (signInIntent != null) {
            startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)

        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->

                if (task.isSuccessful) {
                   val user =  mRootRef.child("User").child(task.result?.user?.uid.toString())

                    user.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val group: UserInfo? = dataSnapshot.getValue(UserInfo::class.java)

                                    if (dataSnapshot.exists()) {
                                        if(dataSnapshot.getValue(UserInfo::class.java) != null){

                                            userDao = Room.databaseBuilder(
                                                context,
                                                DBUser::class.java,
                                                "UserInfo"
                                            ).allowMainThreadQueries().build()

                                            userDao?.daoUser()?.init()

                                            userDao?.daoUser()?.insert(
                                                DataBaseUser(
                                                    group?.Nickname ?: "",
                                                    group?.Genre ?: "",
                                                    task.result?.user?.uid ?: "",
                                                    task.result?.user?.email ?: ""
                                                )
                                            )

                                            startActivity(Intent(context, ActivityMain::class.java))
                                            finish()
                                        }

                                    } else {

                                        val dialogLogin: DialogLogin?

                                        val doLogin = View.OnClickListener {

                                            userDao?.daoUser()?.init()

                                            userDao?.daoUser()?.insert(
                                                DataBaseUser(
                                                    group?.Nickname ?: "",
                                                    group?.Genre ?: "",
                                                    task.result?.user?.uid ?: "",
                                                    task.result?.user?.email ?: ""
                                                )
                                            )

                                            val intent = Intent(context, ActivityGenre::class.java)
                                            intent.putExtra("MODE", "NEWBIE")
                                            intent.putExtra("UID", task.result?.user?.uid.toString())
                                            intent.putExtra("EMAIL", task.result?.user?.email.toString())
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
                                            finish()
                                        }

                                        // 안내 팝업
                                        dialogLogin = DialogLogin(
                                            context,
                                            doLogin
                                        )

                                        dialogLogin.window?.setBackgroundDrawable(
                                            ColorDrawable(
                                                Color.TRANSPARENT)
                                        )
                                        dialogLogin.show()


                                    }
                                }
                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                } else {
                    // 틀렸을 때
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
    public override fun onStart() {
        super.onStart()
        if((intent.getStringExtra("MODE") ?: "GO") == "GO"){
            moveMainPage(auth?.currentUser)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager!!.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_LOGIN_CODE) {
            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }!!
            // 구글API가 넘겨주는 값 받아옴

            if (result.isSuccess) {
                val accout = result.signInAccount
                firebaseAuthWithGoogle(accout)
                binding.loading.root.visibility = View.GONE
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            } else {
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 유저정보 넘겨주고 메인 액티비티 호출
    private fun moveMainPage(user: FirebaseUser?){
        if(user!= null){

            mRootRef.child("User").child(user.uid)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val group: UserInfo? = dataSnapshot.getValue(UserInfo::class.java)

                            userDao = Room.databaseBuilder(
                                context,
                                DBUser::class.java,
                                "UserInfo"
                            ).allowMainThreadQueries().build()

                            userDao?.daoUser()?.init()

                            userDao?.daoUser()?.insert(
                                DataBaseUser(
                                    group?.Nickname ?: "",
                                    group?.Genre ?: "",
                                    user.uid,
                                    user.email ?: ""
                                )
                            )

                            startActivity(Intent(context, ActivityMain::class.java))
                            binding.loading.root.visibility = View.GONE
                            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            finish()
                        } else {
                            val intent = Intent(context, ActivityGenre::class.java)
                            intent.putExtra("MODE", "NEWBIE")
                            intent.putExtra("UID", user.uid)
                            intent.putExtra("EMAIL", user.email)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    }
}