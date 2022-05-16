package com.example.moavara.Main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.work.WorkManager
import com.example.moavara.Best.BottomDialogMain
import com.example.moavara.R
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.system.exitProcess


class ActivityMain : AppCompatActivity() {
    var navController: NavController? = null

    var cate = "ALL"
    var status = ""
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cate = Genre.getGenre(this).toString()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.navHostFragmentMain)

        toolbar.setOnClickListener {
            WorkManager.getInstance().cancelAllWork()
            val miningRef = mRootRef.child("Mining")
            miningRef.setValue("NULL")
            Toast.makeText(this, "WorkManager 해제됨", Toast.LENGTH_SHORT).show()
        }

        val navView = findViewById<BottomNavigationView>(R.id.nav_bottom)
        NavigationUI.setupWithNavController(navView, navController!!)
    }

    override fun onBackPressed() {

        val myAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(this@ActivityMain)
        myAlertBuilder.setTitle("모아바라 종료")
        myAlertBuilder.setMessage("모아바라를 종료하시겠습니까?")
        myAlertBuilder.setPositiveButton(
            "예"
        ) { _, _ ->

            finishAffinity();
            System.runFinalization();
            exitProcess(0);
        }
        myAlertBuilder.setNegativeButton(
            "아니요"
        ) { _, _ ->
        }
        // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
        myAlertBuilder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when (item.itemId) {
            R.id.menu_option -> {
                val bottomDialogMain = BottomDialogMain()
                supportFragmentManager.let { bottomDialogMain.show(it, null) }
            }
            R.id.Fragment_Pick -> {
                val intent = Intent(this, ActivityLogin::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}