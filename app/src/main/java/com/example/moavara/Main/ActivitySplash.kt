package com.example.moavara.Main


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.lifecycleScope
import com.example.moavara.Main.ViewModel.ViewModelSplash
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


val mRootRef = FirebaseDatabase.getInstance().reference
@AndroidEntryPoint
class ActivitySplash : ComponentActivity() {

    private val mainViewModel: ViewModelSplash by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.sideEffects
            .onEach { Toast.makeText(this@ActivitySplash, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        setContent {
            MaterialTheme {
                Surface {
                    SplashScreen()
                }
            }
        }

        mainViewModel.loadingSplash(this@ActivitySplash){ isFinish ->
            if(isFinish){
                mainViewModel.finishSplash(this@ActivitySplash)
            }
        }
    }
}
