package com.bigbigdw.moavara.Best

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bigbigdw.moavara.databinding.DialogBestcoverBinding
import java.util.*

class DialogBestBookCover(
    context: Context,
    private val imgUrl: String,
) : Dialog(context) {

    private lateinit var binding: DialogBestcoverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.8f
        Objects.requireNonNull(window)?.attributes = layoutParams
        binding = DialogBestcoverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(context)
            .load(imgUrl)
            .into(binding.imgCover)

        binding.imgCover.setOnClickListener {
            dismiss()
        }
    }

}