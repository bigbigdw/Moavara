package com.example.takealook.Main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.takealook.R
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import java.util.*


class ActivityGuide : AppCompatActivity() {
    var carouselView: CarouselView? = null
    val sampleImages = intArrayOf(
        R.drawable.expic01,
        R.drawable.expic02,
        R.drawable.expic03,
        R.drawable.expic04,
    )

    var button_next: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        carouselView = findViewById(R.id.Carousel_MainBanner)
        carouselView!!.pageCount = sampleImages.size
        carouselView!!.setImageListener(imageListener)

        button_next = findViewById(R.id.button_next)

        setLayout()

    }

    var imageListener =
        ImageListener { position, imageView ->
            imageView.setImageResource(sampleImages[position]);
        }

    private fun setLayout() {


        button_next!!.setOnClickListener {
            val intent = Intent(applicationContext, ActivityMain::class.java)
            startActivity(intent)
        }

    }

}