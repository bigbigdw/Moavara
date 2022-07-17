package com.example.moavara.Search

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.Best.AdapterType
import com.example.moavara.R
import com.example.moavara.Util.BestRef
import com.example.moavara.databinding.ActivitySearchBinding

class ActivitySearch : AppCompatActivity() {

    private lateinit var mFragmentSearch: FragmentSearch
    private lateinit var adapterType: AdapterType
    private val typeItems = ArrayList<BestType>()
    var pos = 0
    private lateinit var binding: ActivitySearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFragmentSearch = FragmentSearch()
        supportFragmentManager.commit {
            replace(R.id.llayoutWrap, mFragmentSearch)
        }

        getType()
    }

    private fun getType() {

        adapterType = AdapterType(typeItems)
        typeItems.clear()

        with(binding){
            rviewType.layoutManager =
                LinearLayoutManager(this@ActivitySearch, LinearLayoutManager.HORIZONTAL, false)
            rviewType.adapter = adapterType
        }

        for(i in BestRef.typeList().indices){
            typeItems.add(
                BestType(
                    BestRef.typeListTitleSearch()[i],
                    BestRef.typeListSearch()[i]
                )
            )
        }
        adapterType.notifyDataSetChanged()

        adapterType.setOnItemClickListener(object : AdapterType.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BestType? = adapterType.getItem(position)
                adapterType.setSelectedBtn(position)
                pos = position
                adapterType.notifyDataSetChanged()
            }
        })
    }

}