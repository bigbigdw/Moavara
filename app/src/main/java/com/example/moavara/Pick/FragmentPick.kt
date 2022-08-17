package com.example.moavara.Pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.commit
import androidx.viewpager.widget.ViewPager
import com.example.moavara.Best.FragmentBestTabMonth
import com.example.moavara.Best.FragmentBestTabToday
import com.example.moavara.Best.FragmentBestTabWeekend
import com.example.moavara.R
import com.example.moavara.databinding.ActivityPickBinding
import com.google.android.material.tabs.TabLayout

class FragmentPick : Fragment() {

    private var _binding: ActivityPickBinding? = null
    private val binding get() = _binding!!
    var cate = "ALL"
    var status = ""
    private lateinit var mFragmentPickTabEvent: FragmentPickTabEvent
    private lateinit var mFragmentPickTabNovel: FragmentPickTabNovel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ActivityPickBinding.inflate(inflater, container, false)
        val view = binding.root

        with(binding) {
            tabs.addTab(tabs.newTab().setText("소설"))
            tabs.addTab(tabs.newTab().setText("이벤트"))

            mFragmentPickTabNovel = FragmentPickTabNovel()
            childFragmentManager.commit {
                replace(R.id.view_pager, mFragmentPickTabNovel)
            }

            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when(tab.position){
                        0->{
                            mFragmentPickTabNovel = FragmentPickTabNovel()
                            childFragmentManager.commit {
                                replace(R.id.view_pager, mFragmentPickTabNovel)
                            }
                        }
                        1->{
                            mFragmentPickTabEvent = FragmentPickTabEvent()
                            childFragmentManager.commit {
                                replace(R.id.view_pager, mFragmentPickTabEvent)
                            }
                        }
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }

        return view
    }
}