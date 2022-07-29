package com.example.moavara.User

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import com.example.moavara.R
import com.example.moavara.Search.ActivitySearch
import com.example.moavara.databinding.ActivityNoticeBinding
import com.google.android.material.tabs.TabLayout

class ActivityNotice : AppCompatActivity() {

    var cate = "ALL"
    var status = ""
    private lateinit var binding: ActivityNoticeBinding
    private lateinit var mFragmentNotice : FragmentNotice
    private lateinit var mFragmentAlert : FragmentAlert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mFragmentNotice = FragmentNotice()
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentNotice)
        }

        val fragmentBestTab = binding.tabs

        fragmentBestTab.addTab(fragmentBestTab.newTab().setText("공지"))
        fragmentBestTab.addTab(fragmentBestTab.newTab().setText("알림"))

        fragmentBestTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0->{
                        mFragmentNotice = FragmentNotice()
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentNotice)
                        }
                    }
                    1->{
                        mFragmentAlert = FragmentAlert()
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentAlert)
                        }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 클릭된 메뉴 아이템의 아이디 마다 when 구절로 클릭시 동작을 설정한다.
        when (item.itemId) {
            R.id.ActivitySearch -> {
                val intent = Intent(this, ActivitySearch::class.java)
                startActivity(intent)
            }
            R.id.ActivityUser -> {
                val intent = Intent(this, ActivityUser::class.java)
                startActivity(intent)
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}