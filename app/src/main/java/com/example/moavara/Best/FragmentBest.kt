package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.R
import com.example.moavara.Search.BestType
import com.example.moavara.Util.BestRef
import com.example.moavara.databinding.FragmentBestBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class FragmentBest : Fragment() {

    private lateinit var mFragmentBestTabToday: FragmentBestTabToday
    private lateinit var mFragmentBestTabMonth: FragmentBestTabMonth
    private lateinit var mFragmentBestTabWeekend: FragmentBestTabWeekend
    private lateinit var adapterType: AdapterType
    private val typeItems = ArrayList<BestType>()

    var userDao: DBUser? = null
    var UserInfo = DataBaseUser()

    private var _binding: FragmentBestBinding? = null
    private val binding get() = _binding!!
    var pos = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBestBinding.inflate(inflater, container, false)
        val view = binding.root
        val fragmentBestTab = binding.tabs

        userDao = Room.databaseBuilder(
            requireContext(),
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        if(userDao?.daoUser()?.get() != null){
            UserInfo = userDao?.daoUser()?.get() ?: DataBaseUser()
        }

        mFragmentBestTabToday = FragmentBestTabToday("Joara", UserInfo)
        childFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentBestTabToday)
        }

        getType("Today")

        fragmentBestTab.addTab(fragmentBestTab.newTab().setText(R.string.Best_Tab1))
        fragmentBestTab.addTab(fragmentBestTab.newTab().setText(R.string.Best_Tab2))
        fragmentBestTab.addTab(fragmentBestTab.newTab().setText(R.string.Best_Tab3))

        fragmentBestTab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0->{
                        mFragmentBestTabToday = FragmentBestTabToday("Joara", UserInfo)
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestTabToday)
                        }
                        getType("Today")
                    }
                    1->{
                        mFragmentBestTabWeekend = FragmentBestTabWeekend("Joara", UserInfo)
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestTabWeekend)
                        }
                        getType("Weekend")
                    }
                    2->{
                        mFragmentBestTabMonth = FragmentBestTabMonth("Joara", UserInfo)
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestTabMonth)
                        }
                        getType("Month")
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return view
    }

    private fun getType(type : String) {

        adapterType = AdapterType(typeItems)
        typeItems.clear()

        with(binding){
            rviewType.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rviewType.adapter = adapterType
        }

        for(i in BestRef.typeList().indices){
            typeItems.add(
                BestType(
                    BestRef.typeListTitle()[i],
                    BestRef.typeList()[i]
                )
            )
        }
        adapterType.notifyDataSetChanged()

        adapterType.setOnItemClickListener(object : AdapterType.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BestType = adapterType.getItem(position)
                adapterType.setSelectedBtn(position)
                pos = position
                adapterType.notifyDataSetChanged()

                when (type) {
                    "Today" -> {
                        mFragmentBestTabToday = FragmentBestTabToday(item.type?: "", UserInfo)
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestTabToday)
                        }
                    }
                    "Weekend" -> {
                        mFragmentBestTabWeekend = FragmentBestTabWeekend(item.type?: "", UserInfo)
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestTabWeekend)
                        }
                    }
                    "Month" -> {
                        mFragmentBestTabMonth = FragmentBestTabMonth(item.type?: "", UserInfo)
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestTabMonth)
                        }
                    }
                }
            }
        })
    }
}