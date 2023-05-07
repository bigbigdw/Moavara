package com.example.moavara.Main

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.R
import com.example.moavara.Search.UserInfo
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ActivityLogin : ComponentActivity() {
    private var auth: FirebaseAuth? = null
    private var googleSignInClient: GoogleSignInClient? = null

    private val viewModelLogin: ViewModelLogin by viewModels()

    private lateinit var binding: ActivityLoginBinding
    var context = this
    var userDao: DBUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        viewModelLogin.sideEffects
            .onEach { Toast.makeText(this@ActivityLogin, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

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

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    // 구글 로그인 함수
    private fun googleLogin() {
        val signInIntent = googleSignInClient?.signInIntent
        if (signInIntent != null) {
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth?.currentUser
                    viewModelLogin.loginSuccess(activity = this@ActivityLogin, task = task)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
    public override fun onStart() {
        super.onStart()
        if((intent.getStringExtra("MODE") ?: "GO") == "GO"){
            viewModelLogin.moveMainPage(auth?.currentUser, this@ActivityLogin)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}