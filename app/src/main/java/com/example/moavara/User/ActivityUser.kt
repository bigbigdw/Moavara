package com.example.moavara.User

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.example.moavara.Best.BottomDialogMain
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Firebase.*
import com.example.moavara.Main.ActivityLogin
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.ActivityUserBinding
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ActivityUser : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private var googleSignInClient: GoogleSignInClient? = null
    private var GOOGLE_LOGIN_CODE = 9001
    private var callbackManager: CallbackManager? = null

    private lateinit var binding: ActivityUserBinding
    private var pushTitle = ""
    private var pushBody = ""

    var userDao: DBUser? = null
    var UserInfo : DataBaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userDao = Room.databaseBuilder(
            this,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()
        
        if(userDao?.daoUser() != null){
            UserInfo = userDao?.daoUser()?.get()
        }

        if(UserInfo?.UID == "BW2mZVCzMxeUDN5z65MX6ZZ2tgD3"){
            binding.etviewTitle.visibility = View.VISIBLE
            binding.etviewBody.visibility = View.VISIBLE
            binding.llayoutPush.visibility = View.VISIBLE
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = googleSignInClient?.signInIntent
        if (signInIntent != null) {
            startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
        }

        with(binding){

            loading.root.visibility = View.VISIBLE
            window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            val btnBG = GradientDrawable().apply {
                setColor(Color.parseColor("#0D0E10"))
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 100f.dpToPx()
                setStroke(2f.dpToPx().toInt(), Color.parseColor("#773E424B"))
            }

            llayoutPush.background = btnBG
            llayoutNotice.background = btnBG
            tviewBtnGenre.background = btnBG
            llayoutGuide.background = btnBG
            llayoutCall.background = btnBG

            llayoutUpper.background = GradientDrawable().apply {
                setColor(Color.parseColor("#0D0E10"))
                shape = GradientDrawable.RECTANGLE
                cornerRadii = floatArrayOf(0f,0f,0f,0f, 50f.dpToPx(), 50f.dpToPx(), 50f.dpToPx(), 50f.dpToPx())
            }

            llayoutUser.background = GradientDrawable().apply {
                setColor(Color.parseColor("#3E424B"))
                shape = GradientDrawable.OVAL
            }

            tviewNickName.text = UserInfo?.Nickname

            when (UserInfo?.Genre) {
                "ALL" -> {
                    tviewGenre.text = "선택장르 : 장르 무관"
                }
                "FANTASY" -> {
                    tviewGenre.text = "선택장르 : 판타지"
                }
                "ROMANCE" -> {
                    tviewGenre.text = "선택장르 : 로맨스"
                }
                "BL" -> {
                    tviewGenre.text = "선택장르 : BL"
                }
            }

            iviewBtnEdit.setOnClickListener {

                if(tviewNickName.visibility == View.VISIBLE){
                    etviewNickname.visibility = View.VISIBLE
                    tviewNickName.visibility = View.GONE
                    etviewNickname.setText(tviewNickName.text, TextView.BufferType.EDITABLE);
                } else {
                    etviewNickname.visibility = View.GONE
                    tviewNickName.visibility = View.VISIBLE
                    tviewNickName.text = etviewNickname.text.toString()
                    mRootRef.child("User").child(UserInfo?.UID ?: "").child("Nickname").setValue(etviewNickname.text.toString())
                    Toast.makeText(this@ActivityUser, "닉네임이 변경되었습니다", Toast.LENGTH_SHORT).show()
                    savePreferences("NICKNAME", etviewNickname.text.toString() ?: "")
                }
            }

            llayoutNotice.setOnClickListener {
                val intent = Intent(this@ActivityUser, ActivityNotice::class.java)
                startActivity(intent)
            }

            llayoutGuide.setOnClickListener {
                val intent = Intent(this@ActivityUser, ActivityGuide::class.java)
                startActivity(intent)
            }

            llayoutCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01033049425"))
                startActivity(intent)
            }

            llayoutLogout.setOnClickListener {
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
                val bottomDialogMain = BottomDialogMain(UserInfo ?: DataBaseUser())
                supportFragmentManager.let { bottomDialogMain.show(it, null) }
            }

            etviewTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    text: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    Log.d("idtext", "beforeTextChanged")
                }

                override fun onTextChanged(
                    text: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    Log.d("idtext", "onTextChanged")
                }

                override fun afterTextChanged(s: Editable) {
                    pushTitle = s.toString()
                }
            })

            etviewBody.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    text: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    Log.d("idtext", "beforeTextChanged")
                }

                override fun onTextChanged(
                    text: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    Log.d("idtext", "onTextChanged")
                }

                override fun afterTextChanged(s: Editable) {
                    pushBody = s.toString()
                }
            })

            llayoutPush.setOnClickListener {
                val fcm = Intent(applicationContext, FCM::class.java)
                startService(fcm)

                val fcmBody = DataFCMBody(
                    "/topics/all",
                    "high",
                    DataFCMBodyData(pushTitle, pushBody),
                    DataFCMBodyNotification(pushTitle, pushBody, "default", "ic_stat_ic_notification"),
                )

                val call = Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com")
                    .addConverterFactory(GsonConverterFactory.create()).build()
                    .create(FirebaseService::class.java)
                    .postRetrofit(
                        fcmBody
                    )

                call.enqueue(object : Callback<FWorkManagerResult?> {
                    override fun onResponse(
                        call: Call<FWorkManagerResult?>,
                        response: retrofit2.Response<FWorkManagerResult?>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                pushTitle = ""
                                pushBody = ""
                                etviewTitle.text = SpannableStringBuilder("제목")
                                etviewBody.text = SpannableStringBuilder("내용")
                            }
                        } else {
                            pushTitle = ""
                            pushBody = ""
                            etviewTitle.text = SpannableStringBuilder("제목")
                            etviewBody.text = SpannableStringBuilder("내용")
                        }
                    }

                    override fun onFailure(call: Call<FWorkManagerResult?>, t: Throwable) {
                        pushTitle = ""
                        pushBody = ""
                        etviewTitle.text = SpannableStringBuilder("제목")
                        etviewBody.text = SpannableStringBuilder("내용")
                    }
                })
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)

        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Log.d("####LOGIN####", "성공")
                } else {
                    Log.d("####LOGIN####", task.exception?.message.toString())
                }
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)

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

    private fun savePreferences(key : String, value: String) {
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}