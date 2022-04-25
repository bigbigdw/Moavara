package com.example.moavara.Main

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import androidx.work.WorkManager
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.DataBase.DataBestDay
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.Genre
import com.example.moavara.Util.Mining
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import kotlin.collections.HashMap
import kotlin.system.exitProcess


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
    }

}