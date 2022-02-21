package com.example.moavara.Best

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.moavara.R
import com.example.moavara.databinding.FragmentBestBinding
import com.google.android.material.tabs.TabLayout

class FragmentBest : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBestBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sectionsPagerAdapter = SectionsPagerAdapter(
            context, childFragmentManager
        )

        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val fragmentBestTab = binding.tabs
        fragmentBestTab.setupWithViewPager(viewPager)

        return root
    }

    class SectionsPagerAdapter(private val mContext: Context?, fm: FragmentManager?) :
        FragmentPagerAdapter(
            fm!!
        ) {
        private val tabTitles = intArrayOf(
            R.string.Best_Tab1,
            R.string.Best_Tab2,
            R.string.Best_Tab3
        )

        override fun getItem(position: Int): Fragment {
            return FragmentBestTab.newInstance(position + 1)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mContext!!.resources.getString(tabTitles[position])
        }

        override fun getCount(): Int {
            return tabTitles.size
        }
    }
}