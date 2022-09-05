package com.example.moavara.Event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.moavara.Best.AdapterType
import com.example.moavara.DataBase.DBUser
import com.example.moavara.DataBase.DataBaseUser
import com.example.moavara.R
import com.example.moavara.Search.BestType
import com.example.moavara.Util.BestRef
import com.example.moavara.databinding.FragmentEventBinding

class FragmentEvent: Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterType: AdapterType
    private val items = ArrayList<BestType>()
    private lateinit var mFragmentEventTab: FragmentEventTab

    var userDao: DBUser? = null
    var UserInfo = DataBaseUser()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        val view = binding.root

        userDao = Room.databaseBuilder(
            requireContext(),
            DBUser::class.java,
            "UserInfo"
        ).allowMainThreadQueries().build()

        if(userDao?.daoUser()?.get() != null){
            UserInfo = userDao?.daoUser()?.get() ?: DataBaseUser()
        }

        mFragmentEventTab = FragmentEventTab("Joara", UserInfo)
        childFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentEventTab)
        }

        adapterType = AdapterType(items)
        items.clear()

        with(binding){
            rviewType.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rviewType.adapter = adapterType
        }

        for(i in BestRef.typeListTitleEvent().indices){
            items.add(
                BestType(
                    BestRef.typeListTitleEvent()[i],
                    BestRef.typeListEvent()[i]
                )
            )
        }
        adapterType.notifyDataSetChanged()

        adapterType.setOnItemClickListener(object : AdapterType.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BestType = adapterType.getItem(position)
                adapterType.setSelectedBtn(position)
                adapterType.notifyDataSetChanged()

                mFragmentEventTab = FragmentEventTab(item.type ?: "", UserInfo)
                childFragmentManager.commit {
                    replace(R.id.llayoutWrap, mFragmentEventTab)
                }
            }
        })

        return view
    }
}
