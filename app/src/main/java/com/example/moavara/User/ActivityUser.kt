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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.work.WorkManager
import com.example.moavara.Best.BottomDialogMain
import com.example.moavara.Firebase.*
import com.example.moavara.Main.ActivityLogin
import com.example.moavara.Main.mRootRef
import com.example.moavara.R
import com.example.moavara.Search.ActivitySearch
import com.example.moavara.Util.Mining
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

    var userInfo = mRootRef.child("User")
    var UID = ""
    private lateinit var binding: ActivityUserBinding
    private var pushTitle = ""
    private var pushBody = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val nickname = getSharedPreferences("pref", MODE_PRIVATE).getString("NICKNAME", "")
        val genre = getSharedPreferences("pref", MODE_PRIVATE).getString("GENRE", "")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)

        UID = getSharedPreferences("pref", MODE_PRIVATE)
            ?.getString("UID", "").toString()




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
            llayoutPickFCM.background = btnBG
            llayoutPickFCMStop.background = btnBG

            llayoutUpper.background = GradientDrawable().apply {
                setColor(Color.parseColor("#0D0E10"))
                shape = GradientDrawable.RECTANGLE
                cornerRadii = floatArrayOf(0f,0f,0f,0f, 50f.dpToPx(), 50f.dpToPx(), 50f.dpToPx(), 50f.dpToPx())
            }

            llayoutUser.background = GradientDrawable().apply {
                setColor(Color.parseColor("#3E424B"))
                shape = GradientDrawable.OVAL
            }

            tviewNickName.text = nickname

            when (genre) {
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
                    userInfo.child(UID).child("Nickname").setValue(etviewNickname.text.toString())
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
                val bottomDialogMain = BottomDialogMain()
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

            llayoutPickFCM.setOnClickListener {
                val fcm = Intent(applicationContext, FCM::class.java)
                startService(fcm)

                val User = getSharedPreferences("pref", MODE_PRIVATE)
                    ?.getString("NICKNAME", "").toString()
                val UID = getSharedPreferences("pref", MODE_PRIVATE)
                    ?.getString("UID", "").toString()

                val fcmBody = DataFCMBody(
                    "/topics/${UID}",
                    "high",
                    DataFCMBodyData("모아바라 PICK 최신화", "${User}님의 마이픽 리스트가 최신화 되었습니다."),
                    DataFCMBodyNotification("모아바라 PICK 최신화", "${User}님의 마이픽 리스트가 최신화 되었습니다.", "default", "ic_stat_ic_notification"),
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
                            Mining.getMyPickMining(applicationContext)
                        }
                    }

                    override fun onFailure(call: Call<FWorkManagerResult?>, t: Throwable) {}
                })
            }

            llayoutPickFCMStop.setOnClickListener {
                val workManager = WorkManager.getInstance(applicationContext)
                workManager.cancelAllWorkByTag("MoavaraPick")
                Toast.makeText(applicationContext, "선호작 최신화 해제됨", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when (item.itemId) {
            R.id.ActivitySearch -> {
                val intent = Intent(this, ActivitySearch::class.java)
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
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            } else {
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

}