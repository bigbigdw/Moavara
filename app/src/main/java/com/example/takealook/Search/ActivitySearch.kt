package com.example.takealook.Search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.takealook.R
import com.example.takealook.databinding.ActivitySearchBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class ActivitySearch : AppCompatActivity() {

    var tabNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(activitySearchBinding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(
            this, supportFragmentManager
        )

        val viewPager = activitySearchBinding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val fragmentNewTab: TabLayout = activitySearchBinding.postTab
        fragmentNewTab.setupWithViewPager(viewPager)

        val intent = intent

        if (intent != null) {
            tabNum = intent.getIntExtra("TabNum",0)
            viewPager.currentItem = tabNum
        }
    }

    class SectionsPagerAdapter(private val mContext: Context?, fm: FragmentManager?) :
        FragmentPagerAdapter(
            fm!!
        ) {
        private val tabTitles = intArrayOf(
            R.string.search_tab1,
            R.string.search_tab2,
            R.string.app_name,
        )

        override fun getItem(position: Int): Fragment {
            return FragmentSearchTab.newInstance(position + 1)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mContext!!.resources.getString(tabTitles[position])
        }

        override fun getCount(): Int {
            return tabTitles.size
        }
    }

}