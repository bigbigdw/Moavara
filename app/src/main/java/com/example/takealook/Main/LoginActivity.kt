package com.example.takealook.Main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.takealook.Joara.LoginResult
import com.example.takealook.Joara.RetrofitJoara
import com.example.takealook.R
import com.example.takealook.Util.DialogText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import java.util.*

class ActivityLogin : AppCompatActivity() {
    var idtext: TextInputLayout? = null
    var pwtext: TextInputLayout? = null
    var loginMainFindID: TextView? = null
    var loginMainFIndPW: TextView? = null
    var logo: ImageView? = null
    var loginBtn: Button? = null
    var registerBtn: Button? = null
    var token: String? = null
    var loginFailMsg = "로그인에 실패하였습니다"
    private var dialogText: DialogText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        idtext = findViewById(R.id.IDtext)
        pwtext = findViewById(R.id.PWtext)
        loginBtn = findViewById(R.id.LoginBtn)
        logo = findViewById(R.id.LOGO)
        loginMainFindID = findViewById(R.id.LoginMain_FindID)
        loginMainFIndPW = findViewById(R.id.LoginMain_FIndPW)
        registerBtn = findViewById(R.id.RegisterBtn)
        setLayout()
    }

    private fun setLayout() {
        Objects.requireNonNull(idtext!!.editText)?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                Log.d("idtext", "beforeTextChanged")
            }

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("idtext", "onTextChanged")
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isEmpty()) {
                    idtext!!.error = getString(R.string.Login_EmptyID)
                    idtext!!.isErrorEnabled = true
                } else {
                    idtext!!.isErrorEnabled = false
                }
            }
        })
        Objects.requireNonNull(pwtext!!.editText)?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                Log.d("pwtext", "beforeTextChanged")
            }

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("pwtext", "onTextChanged")
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isEmpty()) {
                    pwtext!!.error = getString(R.string.Login_EmptyPW)
                    pwtext!!.isErrorEnabled = true
                } else {
                    pwtext!!.isErrorEnabled = false
                }
            }
        })
        loginBtn!!.setOnClickListener { v: View? ->
            val idCheck = Objects.requireNonNull(idtext!!.editText)?.text.toString()
            val pwCheck = Objects.requireNonNull(pwtext!!.editText)?.text.toString()

            RetrofitJoara.postLogin(idCheck, pwCheck, this)!!.enqueue(object :
                Callback<LoginResult?> {
                override fun onResponse(
                    call: Call<LoginResult?>,
                    response: retrofit2.Response<LoginResult?>
                ) {
                    if (response.isSuccessful) {

                        response.body()?.let { it ->
                            val status = it.status
                            val message = it.message
                            val nickname = it.user?.nickname
                            val token = it.user?.token
                            val mana = it.user?.mana
                            val expireCash = it.user?.expireCash
                            val cash = it.user?.cash
                            val manuscriptCoupon = it.user?.manuscriptCoupon
                            val supportCoupon = it.user?.supportCoupon
                            val memberId = it.user?.memberId
                            val profile = it.user?.profile
                            val grade = it.user?.grade

                            if (status.equals("1")) {
                                Toast.makeText(
                                    applicationContext,
                                    "환영합니다!" + " " + nickname + "님!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                savePreferences("TOKEN", token!!)
                                savePreferences("NICKNAME", nickname!!)
                                savePreferences("MANA", mana!!)
                                savePreferences("EXPIRECASH", expireCash!!)
                                savePreferences("CASH", cash!!)
                                savePreferences("MANUSCRIPTCOUPON", manuscriptCoupon!!)
                                savePreferences("SUPPORTCOUPON", supportCoupon!!)
                                savePreferences("MEMBERID", memberId!!)
                                savePreferences("STATUS", status!!)
                                savePreferences("PROFILEIMG", profile!!)
                                savePreferences("GRADE", grade!!)

                                val intent = Intent(applicationContext, ActivityGuide::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                startActivityIfNeeded(intent, 0)
                                finish()

                            } else {
                                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, loginFailMsg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResult?>, t: Throwable) {
                    Toast.makeText(applicationContext, loginFailMsg, Toast.LENGTH_SHORT).show()
                }
            })

        }

        loginMainFindID!!.setOnClickListener {
            Toast.makeText(applicationContext, "아이디 찾기로 이동합니다.", Toast.LENGTH_SHORT).show()
        }

        loginMainFIndPW!!.setOnClickListener {
            Toast.makeText(applicationContext, "비밀번호 찾기로 이동합니다.", Toast.LENGTH_SHORT).show()
        }

        registerBtn!!.setOnClickListener {
            savePreferences("TOKEN", "")
            val btnLeftListener = View.OnClickListener {
                Toast.makeText(applicationContext, "비회원으로 입장합니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, ActivityBookSetting::class.java)
                startActivity(intent)
                dialogText!!.dismiss()

            }
            val btnRightListener = View.OnClickListener { dialogText!!.dismiss() }

            dialogText = DialogText(
                this,
                btnLeftListener,
                btnRightListener,
                "비회원을 선택하셨습니다.\n" +
                        "정보는 저장되지 않으며\n" +
                        "일부 서비스가 제한될 수 있습니다.\n" +
                        "(19금 열람 제한)"
            )
            dialogText!!.show()
        }

    }

    override fun onBackPressed() {
        finish()
    }

    fun savePreferences(value: String, token: String) {
        val pref = getSharedPreferences("LOGIN", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(value, token)
        editor.apply()
    }
}