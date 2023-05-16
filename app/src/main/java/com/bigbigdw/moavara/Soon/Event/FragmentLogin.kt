package com.bigbigdw.moavara.Soon.Event

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bigbigdw.moavara.Retrofit.JoaraLoginResult
import com.bigbigdw.moavara.Retrofit.RetrofitDataListener
import com.bigbigdw.moavara.Retrofit.RetrofitJoara
import com.bigbigdw.moavara.Util.Param
import com.bigbigdw.moavara.databinding.FragmentLoginBinding

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

                val apiJoara = RetrofitJoara()
                val param = Param.getItemAPI(context)
//                param["member_id"] = idCheck
//                param["passwd"] = pwCheck
                param["member_id"] = "bigbigdw"
                param["passwd"] = "!ms47kdnt53"

                apiJoara.postLogin(
                    param,
                    object : RetrofitDataListener<JoaraLoginResult> {
                        override fun onSuccess(it: JoaraLoginResult) {

                            val token = it.user?.token
                            savePreferences("TOKEN", token!!)

                            val intent = Intent(context, ActivityEventDetail::class.java)
                            context?.startActivity(intent)
                        }
                    })
            }
        }
    }

    fun savePreferences(value: String, token: String) {
        val pref = context?.getSharedPreferences("LOGIN", AppCompatActivity.MODE_PRIVATE)
        val editor = pref?.edit()
        editor?.putString(value, token)
        editor?.apply()
    }


}
