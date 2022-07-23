package com.example.moavara.Soon.Event

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import com.example.moavara.Event.FragmentEventDetail
import com.example.moavara.R
import com.example.moavara.Search.ActivitySearch
import com.example.moavara.User.ActivityUser
import com.example.moavara.databinding.ActivityEventDetailBinding
import com.google.android.material.tabs.TabLayout

class ActivityEventDetail : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding
    private lateinit var mFragmentEventDetail: FragmentEventDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mFragmentEventDetail = FragmentEventDetail("")
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentEventDetail)
        }
        binding.tabs.addTab(binding.tabs.newTab().setText(R.string.Best_Tab1))
        binding.tabs.addTab(binding.tabs.newTab().setText(R.string.Best_Tab2))

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0->{
                        mFragmentEventDetail = FragmentEventDetail("")
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentEventDetail)
                        }
                    }
                    1->{
                        mFragmentEventDetail = FragmentEventDetail("Notice")
                        supportFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentEventDetail)
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