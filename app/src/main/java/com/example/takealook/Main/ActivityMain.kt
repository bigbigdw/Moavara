package com.example.takealook.Main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.takealook.Joara.CheckTokenResult
import com.example.takealook.Joara.LogoutResult
import com.example.takealook.Joara.RetrofitJoara
import com.example.takealook.R
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityMain : AppCompatActivity() {
    private var appBarConfiguration: AppBarConfiguration? = null
    var drawerLogout: LinearLayout? = null
    var drawerLogin: LinearLayout? = null
    var viewMana: TextView? = null
    var coupon: TextView? = null
    var viewCash: TextView? = null
    var viewManuscriptCoupon: TextView? = null
    var viewSupportCoupon: TextView? = null
    var userName: TextView? = null
    var iviewBadge: ImageView? = null
    var btnLogout: ImageView? = null
    var navController: NavController? = null
    var navigationView: NavigationView? = null
    var navHeaderView: View? = null
    var drawer: DrawerLayout? = null
    private var mContext: Context? = null

    val MenuList: MutableList<String> = ArrayList()
    val MenuListPosition: MutableList<String> = ArrayList()

    override fun onResume() {
        super.onResume()
        loginCheck(
            getSharedPreferences("LOGIN", MODE_PRIVATE).getString("TOKEN", ""),
            drawerLogin,
            drawerLogout,
            navigationView
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContext = this

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_main)

        navHeaderView = navigationView!!.getHeaderView(0)
        drawerLogout = navHeaderView!!.findViewById(R.id.Drawer_LogOut)
        drawerLogin = navHeaderView!!.findViewById(R.id.Drawer_LogIn)
        viewMana = navHeaderView!!.findViewById(R.id.Mana)
        coupon = navHeaderView!!.findViewById(R.id.Coupon)
        viewCash = navHeaderView!!.findViewById(R.id.Cash)
        viewManuscriptCoupon = navHeaderView!!.findViewById(R.id.Manuscript_Coupon)
        viewSupportCoupon = navHeaderView!!.findViewById(R.id.Support_Coupon)
        userName = navHeaderView!!.findViewById(R.id.UserName)
        iviewBadge = navHeaderView!!.findViewById(R.id.iview_badge)
        btnLogout = navHeaderView!!.findViewById(R.id.Btn_Logout)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_novel)

        setLayout()
    }

    fun setLayout() {

        drawerLogout!!.setOnClickListener { v: View? ->
            Toast.makeText(applicationContext, "로그인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, ActivityLogin::class.java)
            startActivity(intent)
        }

        btnLogout!!.setOnClickListener {
            onClickLogout()
        }

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.Fragment_Best
        ).setOpenableLayout(drawer).build()

        NavigationUI.setupActionBarWithNavController(this, navController!!, appBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView!!, navController!!)
        val navView = findViewById<BottomNavigationView>(R.id.nav_bottom)
        NavigationUI.setupWithNavController(navView, navController!!)

    }


    private fun onClickLogout() {

        val token = getSharedPreferences("LOGIN", MODE_PRIVATE).getString("TOKEN", "").toString()

        RetrofitJoara.onClickLogout(token, this)!!.enqueue(object : Callback<LogoutResult?> {
            override fun onResponse(call: Call<LogoutResult?>, response: Response<LogoutResult?>) {
                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val status = it.status

                        hideItem(navigationView, status.equals("1"))

                        if (status.equals("1")) {
                            deleteSignedInfo()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LogoutResult?>, t: Throwable) {
                Log.d("Splash: onResponse", "실패")
            }
        })
    }

    //로그인 체크
    private fun loginCheck(
        usertoken: String?,
        drawerLogIn: LinearLayout?,
        drawerLogOut: LinearLayout?,
        navigationView: NavigationView?
    ) {

        RetrofitJoara.loginCheck(usertoken, this)!!.enqueue(object : Callback<CheckTokenResult?> {
            override fun onResponse(
                call: Call<CheckTokenResult?>,
                response: Response<CheckTokenResult?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        val status = it.status
                        hideItem(navigationView, status == 1)

                        if (status == 1) {
                            drawerLogOut!!.visibility = View.GONE
                            drawerLogIn!!.visibility = View.VISIBLE
                            viewMana!!.text =
                                getSharedPreferences("LOGIN", MODE_PRIVATE).getString("MANA", "마나")
                            coupon!!.text = getSharedPreferences("LOGIN", MODE_PRIVATE).getString(
                                "EXPIRECASH",
                                "이용권"
                            )
                            viewCash!!.text =
                                getSharedPreferences("LOGIN", MODE_PRIVATE).getString("CASH", "딱지")
                            userName!!.text = getSharedPreferences("LOGIN", MODE_PRIVATE).getString(
                                "NICKNAME",
                                "NICKNAME"
                            )

                            val grade = getSharedPreferences("LOGIN", MODE_PRIVATE).getString(
                                "GRADE",
                                "blue"
                            )

                            when {
                                grade.equals("blue") -> {
                                    iviewBadge!!.setImageResource(R.drawable.icon_user_blue)
                                }
                                grade.equals("silver") -> {
                                    iviewBadge!!.setImageResource(R.drawable.icon_user_silver)
                                }
                                grade.equals("gold") -> {
                                    iviewBadge!!.setImageResource(R.drawable.icon_user_gold)
                                }
                                grade.equals("vip") -> {
                                    iviewBadge!!.setImageResource(R.drawable.icon_user_vip)
                                }
                            }

                            viewManuscriptCoupon!!.text = getSharedPreferences(
                                "LOGIN",
                                MODE_PRIVATE
                            ).getString("MANUSCRIPTCOUPON", "후원쿠폰")
                            viewSupportCoupon!!.text = getSharedPreferences(
                                "LOGIN",
                                MODE_PRIVATE
                            ).getString("SUPPORTCOUPON", "원고료쿠폰")
                        } else {
                            drawerLogOut!!.visibility = View.VISIBLE
                            drawerLogIn!!.visibility = View.GONE

                        }
                    }
                }
            }

            override fun onFailure(call: Call<CheckTokenResult?>, t: Throwable) {
                Log.d("Novel: onResponse", "실패")
            }
        })
    }

    private fun hideItem(navigationView: NavigationView?, check: Boolean) {
        val navMenu = navigationView!!.menu
        navMenu.findItem(R.id.Menu_Logined).isVisible = check
    }

    override fun onSupportNavigateUp(): Boolean {
        val navControllerUp = Navigation.findNavController(this, R.id.nav_host_fragment_novel)
        return (NavigationUI.navigateUp(navControllerUp, appBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    //로그아웃
    fun deleteSignedInfo() {
        val alBuilder = AlertDialog.Builder(this)
        alBuilder.setMessage("로그아웃하시겠습니까?")
        alBuilder.setPositiveButton("예") { _: DialogInterface?, _: Int ->
            val editor = getSharedPreferences("LOGIN", MODE_PRIVATE).edit()
            editor.clear()
            editor.apply()

            Toast.makeText(applicationContext, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, ActivityMain::class.java)
            startActivity(intent)
            finish()
        }
        alBuilder.setNegativeButton("아니오") { _: DialogInterface?, _: Int -> }
        alBuilder.show()
    }

}