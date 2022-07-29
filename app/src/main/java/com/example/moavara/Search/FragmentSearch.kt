package com.example.moavara.Search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.Best.AdapterType
import com.example.moavara.R
import com.example.moavara.Soon.Search.FragmentSearchMoavara
import com.example.moavara.Util.BestRef
import com.example.moavara.databinding.FragmentSearchBinding
import com.google.android.material.tabs.TabLayout


class FragmentSearch : Fragment() {

    private lateinit var mFragmentSearchBookcode: FragmentSearchBookcode
    private lateinit var mSearchMoavara: FragmentSearchMoavara
    private lateinit var adapterType: AdapterType
    private val typeItems = ArrayList<BestType>()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        mFragmentSearchBookcode = FragmentSearchBookcode("Joara")
        childFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentSearchBookcode)
        }

        val fragmentBestTab = binding.tabs
        fragmentBestTab.addTab(fragmentBestTab.newTab().setText("북코드"))
        fragmentBestTab.addTab(fragmentBestTab.newTab().setText("투데이"))

        fragmentBestTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0->{
                        mFragmentSearchBookcode = FragmentSearchBookcode("Joara")
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentSearchBookcode)
                        }
                        binding.rviewType.visibility = View.VISIBLE
                    }
                    1->{
                        mSearchMoavara = FragmentSearchMoavara()
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, mSearchMoavara)
                        }
                        binding.rviewType.visibility = View.GONE
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        getType()

        return view
    }

    private fun getType() {

        adapterType = AdapterType(typeItems)
        typeItems.clear()

        with(binding){
            rviewType.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rviewType.adapter = adapterType
        }

        for(i in BestRef.typeListTitleBookCode().indices){
            typeItems.add(
                BestType(
                    BestRef.typeListTitleBookCode()[i],
                    BestRef.typeListBookcode()[i]
                )
            )
        }
        adapterType.notifyDataSetChanged()

        adapterType.setOnItemClickListener(object : AdapterType.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BestType = adapterType.getItem(position)
                adapterType.setSelectedBtn(position)
                adapterType.notifyDataSetChanged()

                mFragmentSearchBookcode = FragmentSearchBookcode(item.type ?: "")
                childFragmentManager.commit {
                    replace(R.id.llayoutWrap, mFragmentSearchBookcode)
                }
            }
        })
    }
}
