package com.example.moavara.Event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.moavara.R
import com.example.moavara.databinding.FragmentEventBinding
import com.example.moavara.databinding.FragmentEventTabBinding
import com.google.android.material.tabs.TabLayout

class FragmentEvent: Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        val view = binding.root

        with(binding){
            setupViewPager(viewPager)
            tabs.setupWithViewPager(viewPager)
        }

        return view
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(
            childFragmentManager
        )

        adapter.addFragment(FragmentEventTab("Joara"), "조아라")
//        adapter.addFragment(FragmentEventTab("Naver"), "네이버")
        adapter.addFragment(FragmentEventTab("Kakao"), "카카오")
        adapter.addFragment(FragmentEventTab("Ridi"), "리디북스")
//        adapter.addFragment(FragmentEventTab("OneStore"), "원스토어")
        adapter.addFragment(FragmentEventTab("Munpia"), "문피아")
        adapter.addFragment(FragmentEventTab("Toksoda"), "톡소다")
        adapter.addFragment(FragmentEventTab("MrBlue"), "미스터블루")
        viewPager!!.adapter = adapter
    }

    class ViewPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager) {
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

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }


}
