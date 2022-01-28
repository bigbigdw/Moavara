package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.moavara.R
import java.util.*


class ActivityBookSetting : AppCompatActivity() {
    var llayout_step1: LinearLayout? = null

    var llayout_step2: LinearLayout? = null

    var llayout_step3: LinearLayout? = null

    var button_next: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_setting)

        llayout_step1 = findViewById(R.id.llayout_step1)
        llayout_step2 = findViewById(R.id.llayout_step2)
        llayout_step3 = findViewById(R.id.llayout_step3)


        button_next = findViewById(R.id.button_next)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)

        setLayout()

    }

    private fun setLayout() {


        button_next!!.setOnClickListener {
            if (llayout_step1!!.visibility == View.VISIBLE) {
                llayout_step2!!.visibility = View.VISIBLE
                llayout_step1!!.visibility = View.GONE
                button_next!!.text = "다음으로"
            } else if (llayout_step2!!.visibility == View.VISIBLE) {
                llayout_step3!!.visibility = View.VISIBLE
                llayout_step2!!.visibility = View.GONE
                button_next!!.text = "완료"
            } else if (llayout_step3!!.visibility == View.VISIBLE) {
                val intent = Intent(applicationContext, ActivityGuide::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}