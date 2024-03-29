package com.example.moavara.Main

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.moavara.Util.dpToPx
import com.example.moavara.databinding.DialogLoginConfirmBinding

class DialogConfirm(
    context: Context,
    private val textHead: String,
    private val textBody: String,
    private val LeftListner: View.OnClickListener,
    private val RightListener: View.OnClickListener,
    private val leftBtnText: String,
    private val rightBtnText: String,
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

        val body = GradientDrawable().apply {
            setColor(Color.parseColor("#3E424B"))
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 15f.dpToPx()
        }

        if(textHead == ""){
            binding.tviewUnderline.visibility = View.VISIBLE
        } else {
            binding.tviewUnderline.visibility = View.GONE
            binding.tviewHeadWht.text = textHead
        }

        if(textBody == ""){
            binding.tviewBody.visibility = View.GONE
        } else {
            binding.tviewBody.visibility = View.VISIBLE
            binding.tviewBody.text = textBody
        }

        if(leftBtnText != ""){
            binding.btnLeft.text = leftBtnText
        }

        if(rightBtnText != ""){
            binding.btnRight.text = rightBtnText
        }

        binding.llayoutBodyInner.background = body

        val btnLeftBG = GradientDrawable().apply {
            setColor(Color.parseColor("#6E7686"))
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 15f.dpToPx(), 15f.dpToPx())
        }

        binding.btnLeft.background = btnLeftBG

        val btnRightBG = GradientDrawable().apply {
            setColor(Color.parseColor("#844DF3"))
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 15f.dpToPx(), 15f.dpToPx(), 0f, 0f)
        }

        binding.btnRight.background = btnRightBG

        binding.tviewUnderline.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        binding.btnLeft.setOnClickListener(LeftListner)
        binding.btnRight.setOnClickListener(RightListener)

    }

}