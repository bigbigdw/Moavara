package com.example.moavara.Main

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.moavara.DataBase.DBBest
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.Firebase.FCM
import com.example.moavara.Search.UserInfo
import com.example.moavara.Util.BestRef
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

    private val events = Channel<SpalshEvent>()
    private val TAG = "GoogleActivity"
    private val RC_SIGN_IN = 9001

    val state: StateFlow<SplashState> = events.receiveAsFlow()
        .runningFold(SplashState(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, SplashState())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: SplashState, event:SpalshEvent):SplashState{
        return when(event){
            SpalshEvent.Loading -> {
                current.copy(loading = true)
            }
            is SpalshEvent.Loaded -> {
                current.copy(loading = false, init = true)
            }
        }
    }

    fun fetchSplash(activity : ComponentActivity) {
        viewModelScope.launch {
            events.send(SpalshEvent.Loading)
            loadingSplash(activity) { isFinish ->
                if(isFinish){
//                    events.send(MainEvent.Loaded)
                }
            }
            events.send(SpalshEvent.Loaded)
            _sideEffects.send("로딩 완료")
            finishSplash(activity)
        }
    }

    fun loadingSplash(activity: ComponentActivity, callback : (Boolean) -> Unit){

        val userDao: DBUser?
        val UserInfo: DataBaseUser?

        val fcm = Intent(activity, FCM::class.java)
        activity.startService(fcm)

        userDao = Room.databaseBuilder(
            activity,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        UserInfo = userDao.daoUser().get()

        mRootRef.child("User").child(UserInfo.UID ?: "").child("isInit").get().addOnSuccessListener {
            if (it.value == true && !it.exists()) {
                Toast.makeText(activity, "데이터를 새로 받아오고 있습니다.", Toast.LENGTH_SHORT)
                    .show()

                mRootRef.child("User").child(UserInfo.UID ?: "").child("isInit").setValue(false)

                for(platform in BestRef.typeList()){
                    val bestDaoToday = Room.databaseBuilder(
                        activity,
                        DBBest::class.java,
                        "Today_${platform}_${UserInfo.Genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoWeek = Room.databaseBuilder(
                        activity,
                        DBBest::class.java,
                        "Week_${platform}_${UserInfo.Genre}"
                    ).allowMainThreadQueries().build()

                    val bestDaoMonth = Room.databaseBuilder(
                        activity,
                        DBBest::class.java,
                        "Month_${platform}_${UserInfo.Genre}"
                    ).allowMainThreadQueries().build()

                    bestDaoToday.bestDao().initAll()
                    bestDaoWeek.bestDao().initAll()
                    bestDaoMonth.bestDao().initAll()
                    callback.invoke(true)
                }
            }
            callback.invoke(true)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }

    private fun finishSplash(activity: ComponentActivity){
        Looper.myLooper()?.let {
            Handler(it).postDelayed(
                {
                    val intent = Intent(activity, ActivityLogin::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    activity.startActivityIfNeeded(intent, 0)
                    activity.finish()
                },
                2000
            )
        }
    }

    fun firebaseAuthWithGoogle(auth: FirebaseAuth?, idToken: String, activity: ComponentActivity) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(activity) { task ->
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
    fun googleLogin(googleSignInClient: GoogleSignInClient?, activity: ComponentActivity) {
        val signInIntent = googleSignInClient?.signInIntent
        if (signInIntent != null) {
            activity.startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    fun loginSuccess(activity: ComponentActivity, task: Task<AuthResult>){
        val user =  mRootRef.child("User").child(task.result?.user?.uid.toString())

        var userDao = Room.databaseBuilder(
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

                        userDao = Room.databaseBuilder(
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

                } else {

                    val dialogLogin: DialogLogin?

                    val doLogin = View.OnClickListener {

                        userDao.daoUser().init()

                        userDao.daoUser().insert(
                            DataBaseUser(
                                group?.Nickname ?: "",
                                group?.Genre ?: "",
                                task.result?.user?.uid ?: "",
                                task.result?.user?.email ?: ""
                            )
                        )

                        val intent = Intent(activity, ActivityGenre::class.java)
                        intent.putExtra("MODE", "NEWBIE")
                        intent.putExtra("UID", task.result?.user?.uid.toString())
                        intent.putExtra("EMAIL", task.result?.user?.email.toString())
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        activity.startActivity(intent)
                        activity.finish()
                    }

                    // 안내 팝업
                    dialogLogin = DialogLogin(
                        activity,
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

                            activity.startActivity(Intent(activity, ActivityMain::class.java))
                            activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            activity.finish()
                        } else {
                            val intent = Intent(activity, ActivityGenre::class.java)
                            intent.putExtra("MODE", "NEWBIE")
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