package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.R

class ActivityIntro : AppCompatActivity() {

    private var llayoutBtn1: LinearLayout? = null
    private var llayoutBtn2: LinearLayout? = null
    private var llayoutBtn3: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        llayoutBtn1 = findViewById(R.id.llayout_btn1)
        llayoutBtn2 = findViewById(R.id.llayout_btn2)
        llayoutBtn3 = findViewById(R.id.llayout_btn3)

        llayoutBtn1!!.setOnClickListener {

            Toast.makeText(
                this,
                "상남자 특) 위에꺼 누름",
                Toast.LENGTH_SHORT
            ).show()

            val novelIntent = Intent(this, ActivityMain::class.java)
            novelIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivityIfNeeded(novelIntent, 0)
        }

        llayoutBtn2!!.setOnClickListener {

            Toast.makeText(
                this,
                "로맨스를 선택하셨습니다.",
                Toast.LENGTH_SHORT
            ).show()

            val novelIntent = Intent(this, ActivityMain::class.java)
            novelIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivityIfNeeded(novelIntent, 0)
        }

        llayoutBtn3!!.setOnClickListener {

            Toast.makeText(
                this,
                "장르 무관을 선택하셨습니다.",
                Toast.LENGTH_SHORT
            ).show()

            val novelIntent = Intent(this, ActivityMain::class.java)
            novelIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivityIfNeeded(novelIntent, 0)
        }

    }
}