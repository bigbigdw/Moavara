package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.moavara.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class FragmentBestMonth : Fragment() {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
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

        adapter.addFragment(FragmentBestMonthTab("Joara"), "조아라")
        adapter.addFragment(FragmentBestMonthTab("Joara Nobless"), "조아라 노블레스")
        adapter.addFragment(FragmentBestMonthTab("Joara Premium"), "조아라 프리미엄")
        adapter.addFragment(FragmentBestMonthTab("Naver Today"), "네이버 오늘의 웹소설")
        adapter.addFragment(FragmentBestMonthTab("Naver Challenge"), "네이버 챌린지리그")
        adapter.addFragment(FragmentBestMonthTab("Naver"), "네이버 베스트리그")
        adapter.addFragment(FragmentBestMonthTab("Kakao"), "카카오 페이지")
        adapter.addFragment(FragmentBestMonthTab("Kakao Stage"), "카카오 스테이지")
        adapter.addFragment(FragmentBestMonthTab("Ridi"), "리디북스")
        adapter.addFragment(FragmentBestMonthTab("OneStore"), "원스토어")
        adapter.addFragment(FragmentBestMonthTab("MrBlue"), "미스터블루")
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