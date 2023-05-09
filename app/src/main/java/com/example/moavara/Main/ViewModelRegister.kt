package com.example.moavara.Main

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.User.ActivityGuide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class ViewModelRegister @Inject constructor() : ViewModel() {

    private val events = Channel<RegierEvent>()

    val state: StateFlow<RegiserState> = events.receiveAsFlow()
        .runningFold(RegiserState(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, RegiserState())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: RegiserState, event:RegierEvent):RegiserState{
        return when(event){
            RegierEvent.BeginRegister -> {
                current.copy(BeginRegister = true, Step1Finish = false, Step2Finish = false)
            }
            RegierEvent.Step1Finish -> {
                current.copy(BeginRegister = false, Step1Finish = true, Step2Finish = false)
            }
            is RegierEvent.Step2Finish -> {
                current.copy(BeginRegister = false, Step1Finish = false, Step2Finish = true)
            }
            else -> {
                current.copy(Error = "에러!")
            }
        }
    }

    private fun savePreferences(activity: ComponentActivity, key : String, value: String) {
        val pref = activity.getSharedPreferences("pref", ComponentActivity.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun fetchStep1(activity: ComponentActivity, getter: DataBaseUser) {
        nicknameConfirm(activity, getter = getter)
    }

    fun fetchonBackPressed(activity: ComponentActivity, getter: DataBaseUser) {
        viewModelScope.launch {
            if(state.value.Step2Finish){
                events.send(RegierEvent.Step1Finish)
            } else if(state.value.Step1Finish){
                RegisterOnbackPressed(activity,getter)
                events.send(RegierEvent.BeginRegister)
            }
        }
    }

    fun fetchStep1Error() {
        viewModelScope.launch {
            events.send(RegierEvent.Step1Error)
            _sideEffects.send("닉네임을 설정해 주세요")
        }
    }

    fun fetchStep2Error() {
        viewModelScope.launch {
            events.send(RegierEvent.Step1Error)
            _sideEffects.send("장르를 선택해 주세요")
        }
    }

    fun fetchStep2(activity: ComponentActivity, getter: DataBaseUser) {
        viewModelScope.launch {

            val firebaseAnalytics = Firebase.analytics

            savePreferences(activity, "GENRE",getter.Genre)
            mRootRef.child("User").child(getter.UID).child("Genre").setValue("ROMANCE")

            val bundle = Bundle()
            bundle.putString("USER_GENRE", "FANTASY")
            firebaseAnalytics.logEvent("USER_ActivityRegister", bundle)
            events.send(RegierEvent.Step2Finish)
            genreConfirm(activity, getter = getter)
        }
    }


    private fun nicknameConfirm(activity: ComponentActivity, getter: DataBaseUser){
        var dialogLogin: DialogConfirm? = null

        val leftListener = View.OnClickListener {
            dialogLogin?.dismiss()
        }

        val rightListener = View.OnClickListener {

            dialogLogin?.dismiss()

            viewModelScope.launch {
                events.send(RegierEvent.Step1Finish)
            }
        }

        // 안내 팝업
        dialogLogin = DialogConfirm(
            activity,
            "",
            "닉네임 : ${getter.Nickname}",
            leftListener,
            rightListener,
            "취소",
            "확인"
        )

        dialogLogin.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT))
        dialogLogin.show()
    }

    private fun genreConfirm(activity: ComponentActivity, getter: DataBaseUser){

        var genre = ""

        val userDao = Room.databaseBuilder(
            activity,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        mRootRef.child("User").child(getter.UID).child("Nickname").setValue(getter.Nickname)
        mRootRef.child("User").child(getter.UID).child("Email").setValue(getter.Email)
        mRootRef.child("User").child(getter.UID).child("isInit").setValue(true)

        userDao.daoUser().insert(
            DataBaseUser(
                getter.Nickname,
                getter.Genre,
                getter.UID
            )
        )

        when (getter.Genre) {
            "ALL" -> {
                genre = "장르 무관"
            }
            "FANTASY" -> {
                genre = "판타지"
            }
            "ROMANCE" -> {
                genre = "로맨스"
            }
            "BL" -> {
                genre = "BL"
            }
        }

        var dialogLogin: DialogConfirm? = null

        val leftListener = View.OnClickListener {
            dialogLogin?.dismiss()
        }

        val rightListener = View.OnClickListener {

            savePreferences(activity,"NICKNAME", getter.Nickname)
            val intent = Intent(activity, ActivityGuide::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        }

        // 안내 팝업
        dialogLogin = DialogConfirm(
            activity,
            "",
            "닉네임 : " + getter.Nickname + "\n선호장르 : $genre",
            leftListener,
            rightListener,
            "",
            ""
        )

        dialogLogin.window?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT)
        )
        dialogLogin.show()
    }

    fun RegisterOnbackPressed(activity: ComponentActivity, getter: DataBaseUser){
        var dialogLogin: DialogConfirm? = null

        val leftListener = View.OnClickListener { v: View? ->
            dialogLogin?.dismiss()
        }

        val rightListener = View.OnClickListener {

            mRootRef.child("User").child(getter.UID).removeValue()

            val intent = Intent(activity, ActivityLogin::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)

            val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val client = GoogleSignIn.getClient(activity, opt)
            client.revokeAccess()
        }

        // 안내 팝업
        dialogLogin = DialogConfirm(
            activity,
            "안내",
            "가입을 그만두고 로그인 화면으로 돌아가시겠습니까?",
            leftListener,
            rightListener,
            "취소",
            "확인"
        )

        dialogLogin.window?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT)
        )
        dialogLogin.show()
    }
}
