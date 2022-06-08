package com.example.moavara.Main

import android.app.Dialog
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.example.moavara.databinding.DialogLoginConfirmBinding

class DialogConfirmLogin(
    context: Context,
    private val textBody: String,
    private val LeftListner: View.OnClickListener,
    private val RightListener: View.OnClickListener,
) : Dialog(context) {

    private lateinit var binding: DialogLoginConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        //다이얼로그 밖의 화면은 흐리게 만들어줌
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.8f
        window!!.attributes = layoutParams
        binding = DialogLoginConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tviewBody.text = textBody

        binding.tviewUnderline.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        binding.btnLeft.setOnClickListener(LeftListner)
        binding.btnRight.setOnClickListener(RightListener)

    }

}