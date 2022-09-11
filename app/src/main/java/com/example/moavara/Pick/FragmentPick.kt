package com.example.moavara.Pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.room.Room
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.R
import com.example.moavara.databinding.ActivityPickBinding
import com.google.android.material.tabs.TabLayout

class FragmentPick: Fragment() {

    private var _binding: ActivityPickBinding? = null
    private val binding get() = _binding!!
    private lateinit var mFragmentPickTabEvent: FragmentPickTabEvent
    private lateinit var mFragmentPickTabNovel: FragmentPickTabNovel

    var userDao: DBUser? = null
    var UserInfo = DataBaseUser()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ActivityPickBinding.inflate(inflater, container, false)
        val view = binding.root

        userDao = Room.databaseBuilder(
            requireContext(),
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        if(userDao?.daoUser()?.get() != null){
            UserInfo = userDao?.daoUser()?.get() ?: DataBaseUser()
        }

        with(binding) {
            tabs.addTab(tabs.newTab().setText("작품"))
            tabs.addTab(tabs.newTab().setText("이벤트"))

            mFragmentPickTabNovel = FragmentPickTabNovel()
            mFragmentPickTabEvent = FragmentPickTabEvent()

            childFragmentManager.commit {
                replace(R.id.view_pager, mFragmentPickTabNovel)
            }

            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when(tab.position){
                        0->{
                            childFragmentManager.commit {
                                replace(R.id.view_pager, mFragmentPickTabNovel)
                            }
                        }
                        1->{
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