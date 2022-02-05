package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.moavara.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.FirebaseDatabase


class FragmentBestToday() : Fragment() {
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

        val mRootRef = FirebaseDatabase.getInstance().reference
        val bestRef = mRootRef.child("best")

        adapter.addFragment(FragmentBestTodayTab("Joara", bestRef), "조아라")
        adapter.addFragment(FragmentBestTodayTab("Ridi", bestRef), "리디북스")
        adapter.addFragment(FragmentBestTodayTab("Kakao", bestRef), "카카오페이지")
        adapter.addFragment(FragmentBestTodayTab("OneStore", bestRef), "원스토어")
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
