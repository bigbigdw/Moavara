package com.example.moavara.Pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.moavara.Event.FragmentEventTab
import com.example.moavara.R
import com.google.android.material.tabs.TabLayout

class FragmentPick : Fragment() {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val root: View = inflater.inflate(R.layout.fragment_pick, container, false)
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

        adapter.addFragment(FragmentEventTab("Joara"), "소설")
        adapter.addFragment(FragmentPickTabEvent(), "이벤트")
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