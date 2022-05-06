package com.example.moavara.Main

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.R
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.ActivityBestDetailBinding
import com.example.moavara.databinding.ActivityGenreBinding
import com.example.moavara.databinding.ActivitySplashBinding

class ActivityGenre : AppCompatActivity() {

    private lateinit var binding: ActivityGenreBinding
    var context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            tviewMoavara.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tviewGenre.paintFlags = Paint.UNDERLINE_TEXT_FLAG

            llayoutBtn1.setOnClickListener {

                savePreferences("FANTASY")

                val novelIntent = Intent(context, ActivitySync::class.java)
                novelIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY; Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivityIfNeeded(novelIntent, 0)
                finish()
            }

            llayoutBtn2.setOnClickListener {

                savePreferences("ROMANCE")

                val novelIntent = Intent(context, ActivitySync::class.java)
                novelIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY; Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivityIfNeeded(novelIntent, 0)
                finish()
            }

            llayoutBtn3.setOnClickListener {

                savePreferences("ALL")

                val novelIntent = Intent(context, ActivitySync::class.java)
                novelIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY; Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivityIfNeeded(novelIntent, 0)
                finish()
            }

            llayoutBtn4.setOnClickListener {

                savePreferences("BL")

                val novelIntent = Intent(context, ActivitySync::class.java)
                novelIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY; Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivityIfNeeded(novelIntent, 0)
                finish()
            }
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