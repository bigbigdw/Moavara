package com.example.takealook.Util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.takealook.R
import java.util.*


class DialogText(
    context: Context,
    private val mBtnLeftListener: View.OnClickListener,
    private val mBtnRightListener: View.OnClickListener,
    textString: String
) : Dialog(context) {


    private var textString: String = ""
    var tview_DialogText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.8f
        Objects.requireNonNull(window)!!.attributes = layoutParams
        setContentView(R.layout.dialog_text)


        tview_DialogText = findViewById(R.id.tview_DialogText)

        tview_DialogText!!.text = textString

        //셋팅
        val btnLeft = findViewById<Button>(R.id.BtnLeft)
        val btnRight = findViewById<Button>(R.id.BtnRight)

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        btnLeft.setOnClickListener(mBtnLeftListener)
        btnRight.setOnClickListener(mBtnRightListener)
    }

    //생성자 생성
    init {
        this.textString = textString
    }

}