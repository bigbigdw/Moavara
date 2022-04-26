package com.example.moavara.Main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.work.WorkManager
import com.example.moavara.R
import com.example.moavara.Util.Genre
import com.google.android.material.bottomnavigation.BottomNavigationView


class ActivityMain : AppCompatActivity() {
    var navController: NavController? = null

    var cate = "ALL"
    var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cate = Genre.getGenre(this).toString()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.navHostFragmentMain)

        toolbar.setOnClickListener {
            WorkManager.getInstance().cancelAllWork()
            val miningRef = mRootRef.child("Mining")
            miningRef.setValue("HAHA")
            Toast.makeText(this, "WorkManager 해제됨", Toast.LENGTH_SHORT).show()
        }

        val navView = findViewById<BottomNavigationView>(R.id.nav_bottom)
        NavigationUI.setupWithNavController(navView, navController!!)
    }

}