package com.example.moavara.User

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import com.example.moavara.Best.BottomDialogMain
import com.example.moavara.Main.*
import com.example.moavara.Pick.ActivityPick
import com.example.moavara.R
import com.example.moavara.Search.UserInfo
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.ActivityUserBinding
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ActivityUser : AppCompatActivity() {
    var navController: NavController? = null
    private var auth: FirebaseAuth? = null
    private var googleSignInClient: GoogleSignInClient? = null
    private var GOOGLE_LOGIN_CODE = 9001
    private var callbackManager: CallbackManager? = null

    var userInfo = mRootRef.child("User")
    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val nickname = getSharedPreferences("pref", MODE_PRIVATE).getString("NICKNAME", "")
        val genre = getSharedPreferences("pref", MODE_PRIVATE).getString("GENRE", "")



        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)


        with(binding){
            tviewNickName.text = nickname + "님"
            tviewGenre.text = "선호장르 : $genre"

            tviewLogout.setOnClickListener {
                Log.d("####", auth?.currentUser.toString())
                auth?.signOut()
                googleSignInClient?.signOut()
                Toast.makeText(this@ActivityUser, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ActivityUser,ActivityLogin::class.java)
                intent.putExtra("MODE", "STOP")
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP )
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY )
                startActivity(intent)
            }

            tviewBtnGenre.setOnClickListener {
                val bottomDialogMain = BottomDialogMain()
                supportFragmentManager.let { bottomDialogMain.show(it, null) }
            }
        }
    }

    override fun onBackPressed() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when (item.itemId) {
            R.id.menu_option -> {
                val bottomDialogMain = BottomDialogMain()
                supportFragmentManager.let { bottomDialogMain.show(it, null) }
            }
            R.id.ActivityPick -> {
                val intent = Intent(this, ActivityPick::class.java)
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

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)

        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Log.d("####", "성공")
                } else {
                    Log.d("####", task.exception?.message.toString())
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)

        Log.d("####-1", "11111")

        if (requestCode == GOOGLE_LOGIN_CODE) {
            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }!!
            // 구글API가 넘겨주는 값 받아옴

            Log.d("####-2", result.status.toString())

            if (result.isSuccess) {
                val accout = result.signInAccount
                firebaseAuthWithGoogle(accout)
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                Log.d("####-31", result.status.toString())
            } else {
                Log.d("####-32", result.status.toString())
                Toast.makeText(this, "로그인 실패!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}