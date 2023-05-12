package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.GuideComponent
import com.example.moavara.Main.ActivityMain
import com.example.moavara.Main.ViewModel.ViewModelSplash
import com.example.moavara.R
import com.example.moavara.databinding.ActivityGuideBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class ActivityGuide : ComponentActivity() {

    var guideComponent = ArrayList<GuideComponent>()

    val imgList = intArrayOf(
        R.drawable.coach_mark_img_011,
        R.drawable.coach_mark_img_02,
        R.drawable.coach_mark_img_03,
        R.drawable.coach_mark_img_04,
        R.drawable.coach_mark_img_051,
        R.drawable.coach_mark_img_06,
    )

    val textList = arrayListOf(
        "각 플랫폼 별로 매일 업데이트 되는 이벤트를 확인할 수 있습니다.",
        "각 플랫폼 별로 매일의 작품 순위를 확인할 수 있습니다.",
        "각 플랫폼 별로 매일 업데이트 되는 이벤트를 확인할 수 있습니다.",
        "베스트/이벤트 탭에서 마이픽!한 작품, 이벤트를 확인할 수 있습니다.",
        "플랫폼과 작품코드를 알면 원하는 작품을 빠르게 찾을 수 있어요.",
        "각 플랫폼 별로 업데이트 되는 <조아라> 게시글을 확인해 보세요.",
    )

    private val viewModelSplash: ViewModelSplash by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelSplash.sideEffects
            .onEach { Toast.makeText(this@ActivityGuide, it, Toast.LENGTH_SHORT).show() }
            .launchIn(lifecycleScope)

        for (i in 0..5) {
            guideComponent.add(GuideComponent(
                Img = imgList[i],
                Comment = textList[i],
            ))
        }

        setContent {
            MaterialTheme {
                Surface {
                    QuesViewPagerDiagnosticResult(guideComponent,{viewModelSplash.fetchGuide(this@ActivityGuide)})
                }
            }
        }

    }
}