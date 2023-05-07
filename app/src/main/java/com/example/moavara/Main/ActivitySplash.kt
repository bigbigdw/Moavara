package com.example.moavara.Main


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import com.example.moavara.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


val mRootRef = FirebaseDatabase.getInstance().reference
//@AndroidEntryPoint
class ActivitySplash : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)

        mainViewModel.sideEffects
            .onEach { Toast.makeText(this@ActivitySplash, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        setContent {
            MaterialTheme {
                Surface {
                    MainScreen(
                        state = mainViewModel.state.collectAsState().value,
                        onFetchClick = { mainViewModel.fetchSplash(this@ActivitySplash) }
                    )
                }
            }
        }
    }
}
