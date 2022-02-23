package com.example.moavara.Main

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.R

class ActivityGenre : AppCompatActivity() {

    private var llayoutBtn1: LinearLayout? = null
    private var llayoutBtn2: LinearLayout? = null
    private var llayoutBtn3: LinearLayout? = null
    private var llayoutBtn4: LinearLayout? = null
    private var tviewMoavara: TextView? = null
    private var tviewGenre: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre)

        llayoutBtn1 = findViewById(R.id.llayout_btn1)
        llayoutBtn2 = findViewById(R.id.llayout_btn2)
        llayoutBtn3 = findViewById(R.id.llayout_btn3)
        llayoutBtn4 = findViewById(R.id.llayout_btn4)

        tviewMoavara = findViewById(R.id.tviewMoavara)
        tviewGenre = findViewById(R.id.tviewGenre)

        tviewMoavara!!.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        tviewGenre!!.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        llayoutBtn1!!.setOnClickListener {

            savePreferences("FANTASY")

            val novelIntent = Intent(this, ActivitySync::class.java)
            novelIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT; Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivityIfNeeded(novelIntent, 0)
        }

        llayoutBtn2!!.setOnClickListener {

            savePreferences("ROMANCE")

            val novelIntent = Intent(this, ActivitySync::class.java)
            novelIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT; Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivityIfNeeded(novelIntent, 0)
        }

        llayoutBtn3!!.setOnClickListener {

            savePreferences("ALL")

            val novelIntent = Intent(this, ActivitySync::class.java)
            novelIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT; Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivityIfNeeded(novelIntent, 0)
        }

        llayoutBtn4!!.setOnClickListener {

            savePreferences("BL")

            val novelIntent = Intent(this, ActivitySync::class.java)
            novelIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT; Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivityIfNeeded(novelIntent, 0)
        }

    }

    private fun savePreferences(genre: String) {
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("GENRE", genre)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()

        overridePendingTransition(0, 0)
    }
}