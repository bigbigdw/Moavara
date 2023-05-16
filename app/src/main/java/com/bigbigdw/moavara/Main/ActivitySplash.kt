package com.bigbigdw.moavara.Main


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.bigbigdw.moavara.DataBase.DBUser
import com.bigbigdw.moavara.DataBase.DataBaseUser
import com.bigbigdw.moavara.Main.ViewModel.ViewModelLogin
import com.bigbigdw.moavara.Main.ViewModel.ViewModelSplash
import com.bigbigdw.moavara.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn

val mRootRef = FirebaseDatabase.getInstance().reference
@AndroidEntryPoint
class ActivitySplash : ComponentActivity() {
    private var auth: FirebaseAuth? = null
    private var googleSignInClient: GoogleSignInClient? = null

    private val viewModelSplash: ViewModelSplash by viewModels()
    private val viewModelLogin: ViewModelLogin by viewModels()
    var task: Task<GoogleSignInAccount>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        viewModelSplash.sideEffects.launchIn(lifecycleScope)
        viewModelLogin.sideEffects.launchIn(lifecycleScope)

        setContent {
            MaterialTheme {
                Surface {
                    SplashScreen()
                }
            }
        }

        val userDao: DBUser?
        val UserInfo: DataBaseUser?

        userDao = Room.databaseBuilder(
            this@ActivitySplash,
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        UserInfo = userDao.daoUser().get()

        if(UserInfo != null){
            viewModelLogin.googleLogin(googleSignInClient, this@ActivitySplash)
        } else {
            viewModelSplash.loadingSplash(this@ActivitySplash){ isFinish ->
                if(isFinish){
                    viewModelSplash.finishSplash(this@ActivitySplash)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                viewModelSplash.loadingSplash(this@ActivitySplash){ isFinish ->
                    if(isFinish){
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task?.getResult(ApiException::class.java)!!
                        viewModelLogin.firebaseAuthWithGoogle(auth, account.idToken!!, this@ActivitySplash)
                    }
                }
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
