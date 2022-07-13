package com.example.moavara.Event

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.moavara.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import java.util.*

class FragmentLogin: Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        setLayout()

        return view
    }

    fun setLayout() {
        with(binding){
            IDtext.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                    Log.d("idtext", "beforeTextChanged")
                }

                override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                    Log.d("idtext", "onTextChanged")
                }

                override fun afterTextChanged(s: Editable) {
                    if (s.toString().isEmpty()) {
                        IDtext.error = "아이디를 입력해주세요"
                        IDtext.isErrorEnabled = true
                    } else {
                        IDtext.isErrorEnabled = false
                    }
                }
            })
            PWtext.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                    Log.d("pwtext", "beforeTextChanged")
                }

                override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                    Log.d("pwtext", "onTextChanged")
                }

                override fun afterTextChanged(s: Editable) {
                    if (s.toString().isEmpty()) {
                        PWtext.error = "패스워드를 입력해주세요"
                        PWtext.isErrorEnabled = true
                    } else {
                        PWtext.isErrorEnabled = false
                    }
                }
            })

            LoginBtn.setOnClickListener { v: View? ->
                val idCheck = IDtext.editText?.text.toString()
                val pwCheck = PWtext.editText?.text.toString()

//                RetrofitObject.postLogin(idCheck,pwCheck, this)!!.enqueue(object :
//                    Callback<LoginResult?> {
//                    override fun onResponse(
//                        call: Call<LoginResult?>,
//                        response: retrofit2.Response<LoginResult?>
//                    ) {
//                        if (response.isSuccessful) {
//
//                            response.body()?.let { it ->
//                                val status = it.status
//                                val message = it.message
//                                val nickname = it.user?.nickname
//                                val token = it.user?.token
//                                val mana = it.user?.mana
//                                val expireCash = it.user?.expireCash
//                                val cash = it.user?.cash
//                                val manuscriptCoupon = it.user?.manuscriptCoupon
//                                val supportCoupon = it.user?.supportCoupon
//                                val memberId = it.user?.memberId
//                                val profile = it.user?.profile
//                                val grade = it.user?.grade
//
//                                if(status.equals("1")){
//
//                                    savePreferences("TOKEN", token!!)
//                                    savePreferences("NICKNAME", nickname!!)
//                                    savePreferences("MANA", mana!!)
//                                    savePreferences("EXPIRECASH", expireCash!!)
//                                    savePreferences("CASH", cash!!)
//                                    savePreferences("MANUSCRIPTCOUPON", manuscriptCoupon!!)
//                                    savePreferences("SUPPORTCOUPON", supportCoupon!!)
//                                    savePreferences("MEMBERID", memberId!!)
//                                    savePreferences("STATUS", status!!)
//                                    savePreferences("PROFILEIMG", profile!!)
//                                    savePreferences("GRADE", grade!!)
//
//                                    val intent = Intent(context, ActivityMain::class.java)
//                                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//                                    startActivityIfNeeded(intent, 0)
//                                    finish()
//
//                                } else {
//                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                        } else {
//                            Toast.makeText(context, "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<LoginResult?>, t: Throwable) {
//                        Toast.makeText(applicationContext, loginFailMsg, Toast.LENGTH_SHORT).show()
//                    }
//                })

            }
        }
    }

    fun savePreferences(value: String, token: String) {
//        val pref = getSharedPreferences("LOGIN", AppCompatActivity.MODE_PRIVATE)
//        val editor = pref.edit()
//        editor.putString(value, token)
//        editor.apply()
    }


}
