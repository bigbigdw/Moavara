package com.example.moavara.Main

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.User.ActivityGuide
import com.example.moavara.Util.Genre
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.ActivityGenreBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ActivityRegister : ComponentActivity() {

    var UID = ""
    var Email = ""

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val viewModelRegister: ViewModelRegister by viewModels()
    private lateinit var binding: ActivityGenreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = Firebase.analytics

        viewModelRegister.sideEffects
            .onEach { Toast.makeText(this@ActivityRegister, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        UID = intent.getStringExtra("UID") ?: ""
        Email = intent.getStringExtra("EMAIL") ?: ""

        setContent {

            val baseUser = DataBaseUser()
            baseUser.UID = UID
            baseUser.Email = Email

            val (getter, setter) = remember { mutableStateOf(baseUser) }

            MaterialTheme {
                Surface {
                    RegsisterScreen(
                        state = viewModelRegister.state.collectAsState().value,
                        onStep1Finish = { viewModelRegister.fetchStep1(this@ActivityRegister, getter)  },
                        onStep1Error = { viewModelRegister.fetchStep1Error() },
                        onStep2Finish = { viewModelRegister.fetchStep2(this@ActivityRegister, getter) },
                        onStep2Error = { viewModelRegister.fetchStep2Error()},
                        getter = getter,
                        setter = setter
                    )
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        setContent {
            val baseUser = DataBaseUser()
            baseUser.UID = UID
            baseUser.Email = Email

            val (getter, _) = remember { mutableStateOf(baseUser) }

            viewModelRegister.fetchonBackPressed(this@ActivityRegister, getter)
        }
    }

    override fun onPause() {
        super.onPause()

        overridePendingTransition(0, 0)
    }
}