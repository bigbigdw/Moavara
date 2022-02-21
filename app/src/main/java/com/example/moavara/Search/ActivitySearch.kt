package com.example.moavara.Search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moavara.R

class ActivitySearch : AppCompatActivity() {

    var bookCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val intent = intent
        bookCode = intent.getStringExtra("BookCode")!!

    }

}