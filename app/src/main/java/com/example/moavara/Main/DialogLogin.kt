package com.example.moavara.Main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.moavara.databinding.DialogLoginAlertBinding

class DialogLogin(
    context: Context,
    private val mBtnRightListener: View.OnClickListener,
) : Dialog(context) {

    private lateinit var binding: DialogLoginAlertBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.8f
        window!!.attributes = layoutParams
        binding = DialogLoginAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cbox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.btnEnable.visibility = View.VISIBLE
            } else {
                binding.btnEnable.visibility = View.GONE
            }
        }

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        binding.btnEnable.setOnClickListener(mBtnRightListener)

    }

}