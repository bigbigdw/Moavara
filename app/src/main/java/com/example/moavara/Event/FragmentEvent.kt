package com.example.moavara.Event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.moavara.Best.FragmentBestTodayTab
import com.example.moavara.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.FirebaseDatabase

class FragmentEvent: Fragment() {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val root: View = inflater.inflate(R.layout.fragment_best_today, container, false)
        viewPager = root.findViewById(R.id.view_pager)
        setupViewPager(viewPager)
        tabLayout = root.findViewById(R.id.post_tab)
        tabLayout!!.setupWithViewPager(viewPager)

        return root
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(
            childFragmentManager
        )

        adapter.addFragment(FragmentEventTab("Joara"), "조아라")
        adapter.addFragment(FragmentEventTab("Naver"), "네이버")
        adapter.addFragment(FragmentEventTab("Kakao"), "카카오")
        adapter.addFragment(FragmentEventTab("Ridi"), "리디북스")
        adapter.addFragment(FragmentEventTab("OneStore"), "원스토어")
        adapter.addFragment(FragmentEventTab("MrBlue"), "미스터블루")
        viewPager!!.adapter = adapter
    }

    class ViewPagerAdapter(manager: FragmentManager?) :
        FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }


}
