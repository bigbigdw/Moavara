package com.example.moavara.Main.ViewModel

import android.content.Intent
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Main.*
import com.example.moavara.Main.Model.LoginState
import com.example.moavara.Search.UserInfo
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class ViewModelLogin @Inject constructor() : ViewModel() {

    private val events = Channel<LoginEvent>()
    private val RC_SIGN_IN = 9001
    private var taskValue: Task<AuthResult>? = null

    val state: StateFlow<LoginState> = events.receiveAsFlow()
        .runningFold(LoginState(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, LoginState())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: LoginState, event: LoginEvent): LoginState {
        return when(event){
            LoginEvent.Loaded -> {
                current.copy(Loaded = true)
            }
            is LoginEvent.Register -> {
                current.copy(Loaded = false, Register = true, MoveMain = true)
            }
            is LoginEvent.MoveMain -> {
                current.copy(Loaded = false, Register = false, MoveMain = true)
            }
        }
    }

    fun fetchLogin(googleSignInClient: GoogleSignInClient?, activity: ComponentActivity) {
        viewModelScope.launch {
            googleLogin(googleSignInClient, activity)
            events.send(LoginEvent.Loaded)
            _sideEffects.send("로딩 완료")
        }
    }


    fun firebaseAuthWithGoogle(auth: FirebaseAuth?, idToken: String, activity: ComponentActivity) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(activity) { task ->

                taskValue = task

                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    loginSuccess(activity = activity, task = task)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(activity, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    // 구글 로그인 함수
    private fun googleLogin(googleSignInClient: GoogleSignInClient?, activity: ComponentActivity) {
        val signInIntent = googleSignInClient?.signInIntent
        if (signInIntent != null) {
            activity.startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun loginSuccess(activity: ComponentActivity, task: Task<AuthResult>){
        val user =  mRootRef.child("User").child(task.result?.user?.uid.toString())

        val userDao = Room.databaseBuilder(
            activity,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        user.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val group: UserInfo? = dataSnapshot.getValue(UserInfo::class.java)

                if (dataSnapshot.exists()) {
                    if(dataSnapshot.getValue(UserInfo::class.java) != null){

                        viewModelScope.launch {
                            saveUserDataMovePage(activity = activity, group = group, task = task)
                            events.send(LoginEvent.MoveMain)
                            _sideEffects.send("LoginEvent.MoveMain")
                        }
                    }

                } else {
                    viewModelScope.launch {
                        events.send(LoginEvent.Register)
                        _sideEffects.send("LoginEvent.Register")
                    }

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun saveUserDataMovePage(
        activity: ComponentActivity,
        group: UserInfo?,
        task: Task<AuthResult>
    ){

        val userDao = Room.databaseBuilder(
            activity,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        userDao.daoUser().init()

        userDao.daoUser().insert(
            DataBaseUser(
                group?.Nickname ?: "",
                group?.Genre ?: "",
                task.result?.user?.uid ?: "",
                task.result?.user?.email ?: ""
            )
        )

        activity.startActivity(Intent(activity, ActivityMain::class.java))
        activity.finish()
    }

    fun moveRegiseterPage(activity: ComponentActivity){

        val userDao = Room.databaseBuilder(
            activity,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        userDao.daoUser().init()

        val intent = Intent(activity, ActivityRegister::class.java)
        intent.putExtra("UID", taskValue?.result?.user?.uid.toString())
        intent.putExtra("EMAIL", taskValue?.result?.user?.email.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
        activity.finish()
    }

    // 유저정보 넘겨주고 메인 액티비티 호출
    fun moveMainPage(user: FirebaseUser?, activity: ComponentActivity){

        if(user!= null){

            mRootRef.child("User").child(user.uid)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val group: UserInfo? = dataSnapshot.getValue(UserInfo::class.java)

                            val userDao = Room.databaseBuilder(
                                activity,
                                DBUser::class.java,
                                "UserInfo"
                            ).allowMainThreadQueries().build()

                            userDao.daoUser().init()

                            userDao.daoUser().insert(
                                DataBaseUser(
                                    group?.Nickname ?: "",
                                    group?.Genre ?: "",
                                    user.uid,
                                    user.email ?: ""
                                )
                            )

                            val intent = Intent(activity, ActivityMain::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            activity.startActivity(intent)
                            activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            activity.finish()
                        } else {
                            val intent = Intent(activity, ActivityRegister::class.java)
                            intent.putExtra("UID", user.uid)
                            intent.putExtra("EMAIL", user.email)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            activity.startActivity(intent)
                            activity.finish()
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    }
}