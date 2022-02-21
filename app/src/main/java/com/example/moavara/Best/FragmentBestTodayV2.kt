package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.R
import com.example.moavara.Search.BestType
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import kotlin.collections.ArrayList

class FragmentBestTodayV2 : Fragment() {

    private var adapterType: AdapterType? = null
    var rviewType: RecyclerView? = null
    private val typeItems = ArrayList<BestType?>()

    private var mFragmentBestTodayTab: FragmentBestTodayTab? = null
    var llayoutWrap: LinearLayout? = null

    var pos = 0
    var cate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        cate = Genre.getGenre(requireContext()).toString()
        val root = inflater.inflate(R.layout.fragment_best_today_v2, container, false)

        rviewType = root.findViewById(R.id.rviewType)
        llayoutWrap = root.findViewById(R.id.llayoutWrap)
        adapterType = AdapterType(typeItems)

        mFragmentBestTodayTab = FragmentBestTodayTab("Joara")
        parentFragmentManager.beginTransaction()
            .replace(R.id.llayoutWrap, mFragmentBestTodayTab!!)
            .commit()

        setLayout()

        return root
    }

    private fun setLayout(){
        getType()

    }

    private fun getType() {

        rviewType!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rviewType!!.adapter = adapterType

        for(i in BestRef.typeList().indices){
            typeItems.add(
                BestType(
                    BestRef.typeListTitle()[i],
                    BestRef.typeList()[i]
                )
            )
            adapterType!!.notifyDataSetChanged()
        }

        adapterType!!.setOnItemClickListener(object : AdapterType.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BestType? = adapterType!!.getItem(position)
                adapterType!!.setSelectedBtn(position)
                Log.d("!!!!", position.toString())
                pos = position
                adapterType!!.notifyDataSetChanged()

                mFragmentBestTodayTab = FragmentBestTodayTab(item!!.type!!)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.llayoutWrap, mFragmentBestTodayTab!!)
                    .commit()
            }
        })
    }
}