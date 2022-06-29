package com.example.moavara.User

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.moavara.Main.ActivityMain
import com.example.moavara.R
import com.example.moavara.databinding.ActivityGuideBinding
import com.synnapps.carouselview.ViewListener
import kotlinx.android.synthetic.main.item_guide_carousel.*


class ActivityGuide : AppCompatActivity() {
    val imgList = intArrayOf(
        R.drawable.coach_mark_img_01,
        R.drawable.coach_mark_img_02,
        R.drawable.coach_mark_img_03,
        R.drawable.coach_mark_img_04,
        R.drawable.coach_mark_img_05,
        R.drawable.coach_mark_img_06,
    )

    val textList = arrayListOf(
        "각 플랫폼 별로 매일 업데이트 되는 이벤트를 확인할 수 있습니다.",
        "각 플랫폼 별로 매일의 작품 순위를 확인할 수 있습니다.",
        "각 플랫폼 별로 매일 업데이트 되는 이벤트를 확인할 수 있습니다.",
        "베스트/이벤트 탭에서 마이픽!한 작품, 이벤트를 확인할 수 있습니다.",
        "경쟁사의 다양한 소식들을 한번에! 다양한 뉴스를 확인해 보세요.",
        "각 플랫폼 별로 업데이트 되는 <조아라> 게시글을 확인해 보세요.",
    )


    private lateinit var binding: ActivityGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            cview.pageCount = imgList.size
            cview.setViewListener(viewListenerNew)

            cview.stopCarousel()
            cview.indicatorMarginHorizontal
        }

        setLayout()
    }

    private val viewListenerNew =
        ViewListener { position ->
            val itemView: View = layoutInflater.inflate(R.layout.item_guide_carousel, null)

            val tview: TextView = itemView.findViewById(R.id.tview)
            val iview: ImageView = itemView.findViewById(R.id.iview)

            tview.text = textList[position]
            Glide.with(applicationContext).load(imgList[position])
                .into(iview)

            binding.cview.indicatorGravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
            itemView
        }

    private fun setLayout() {
        binding.buttonNext.setOnClickListener {
            finish()
        }
    }
}