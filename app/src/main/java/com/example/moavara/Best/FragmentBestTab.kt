package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.R
import com.example.moavara.Search.BestType
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.example.moavara.Util.TabViewModel
import kotlin.collections.ArrayList

class FragmentBestTab : Fragment() {

    private var tabViewModel: TabViewModel? = null
    var index = 0

    private var adapterType: AdapterType? = null
    var rviewType: RecyclerView? = null
    private val typeItems = ArrayList<BestType?>()

    private var mFragmentBestTabToday: FragmentBestTabToday? = null
    private var mFragmentBestTabMonth: FragmentBestTabMonth? = null
    private var mFragmentBestTabWeekend: FragmentBestTabWeekend? = null
    var llayoutWrap: LinearLayout? = null

    var pos = 0
    var cate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabViewModel = ViewModelProvider(this)[TabViewModel::class.java]
        index = 1
        if (arguments != null) {
            index = requireArguments().getInt(ARG_SECTION_NUMBER)
        }
        tabViewModel!!.setIndex(index)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        cate = Genre.getGenre(requireContext()).toString()
        val root = inflater.inflate(R.layout.fragment_best_tab, container, false)

        rviewType = root.findViewById(R.id.rviewType)
        llayoutWrap = root.findViewById(R.id.llayoutWrap)
        adapterType = AdapterType(typeItems)

        setLayout()

        return root
    }

    private fun setLayout(){

        var type = ""

        tabViewModel!!.text.observe(viewLifecycleOwner) { tabNum: String? ->
            type = when (tabNum) {
                "TAB1" -> {
                    "Today"
                }
                "TAB2" -> {
                    "Weekend"
                }
                else -> {
                    "Month"
                }
            }

            if(type == "Today"){
                mFragmentBestTabToday = FragmentBestTabToday("Joara")
                childFragmentManager.commit {
                    replace(R.id.llayoutWrap, mFragmentBestTabToday!!)
                }
            } else if(type == "Weekend"){
                mFragmentBestTabWeekend = FragmentBestTabWeekend("Joara")
                childFragmentManager.commit {
                    replace(R.id.llayoutWrap, mFragmentBestTabWeekend!!)
                }
            } else if(type == "Month"){
                mFragmentBestTabMonth = FragmentBestTabMonth("Joara")
                childFragmentManager.commit {
                    replace(R.id.llayoutWrap, mFragmentBestTabMonth!!)
                }
            }

            getType(type)
        }
    }

    private fun getType(type : String) {

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
                pos = position
                adapterType!!.notifyDataSetChanged()

                when (type) {
                    "Today" -> {
                        mFragmentBestTabToday = FragmentBestTabToday(item!!.type!!)
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestTabToday!!)
                        }
                    }
                    "Weekend" -> {
                        mFragmentBestTabWeekend = FragmentBestTabWeekend(item!!.type!!)
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestTabWeekend!!)
                        }
                    }
                    "Month" -> {
                        mFragmentBestTabMonth = FragmentBestTabMonth(item!!.type!!)
                        childFragmentManager.commit {
                            replace(R.id.llayoutWrap, mFragmentBestTabMonth!!)
                        }
                    }
                }
            }
        })
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        fun newInstance(index: Int): FragmentBestTab {
            val fragment = FragmentBestTab()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }
}