package com.example.moavara.Main

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.moavara.R
import com.example.moavara.Util.DialogText
import com.google.android.material.navigation.NavigationView


class ActivityMain : AppCompatActivity() {
    var navController: NavController? = null
    private var mContext: Context? = null
    private var dialogText: DialogText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContext = this

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.navHostFragmentMain)

        setLayout()
    }

    fun setLayout() {

        if (getSharedPreferences("LOGIN", MODE_PRIVATE).getString("TOKEN", "") != "") {

            val btnLeftListener = View.OnClickListener {
                dialogText!!.dismiss()
            }
            val btnRightListener = View.OnClickListener { dialogText!!.dismiss() }

            dialogText = DialogText(
                this,
                btnLeftListener,
                btnRightListener,
                "안녕하세요 회원님!\n" +
                        "회원님의 원활한 앱 사용을 위해\n" +
                        "최초 로그인 시 안내를 드리고 있습니다.\n" +
                        "해당 안내는 왼쪽 햄버거 버튼을 누르신 후\n" +
                        "메뉴에 있는 앱 가이드 버튼을 누르시면\n" +
                        "다시 만날 수 있습니다!"
            )
            dialogText!!.show()

        }

        val navView = findViewById<BottomNavigationView>(R.id.nav_bottom)
        NavigationUI.setupWithNavController(navView, navController!!)
    }

}