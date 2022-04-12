package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.moavara.DataBase.DataBaseBestDay
import com.example.moavara.R
import com.example.moavara.Search.BestType
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.example.moavara.Util.TabViewModel
import com.example.moavara.databinding.FragmentBestTabBinding
import kotlin.collections.ArrayList

class FragmentBestTab : Fragment() {

    private var tabViewModel: TabViewModel? = null
    var index = 0

    private lateinit var adapterType: AdapterType
    private val typeItems = ArrayList<BestType>()

    private var _binding: FragmentBestTabBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFragmentBestTabToday: FragmentBestTabToday
    private lateinit var mFragmentBestTabMonth: FragmentBestTabMonth
    private lateinit var mFragmentBestTabWeekend: FragmentBestTabWeekend

    lateinit var dbYesterday: DataBaseBestDay
    var status = ""
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
        _binding = FragmentBestTabBinding.inflate(inflater, container, false)
        val view = binding.root

        adapterType = AdapterType(typeItems)

        dbYesterday =
            Room.databaseBuilder(requireContext(), DataBaseBestDay::class.java, "best-yesterday")
                .allowMainThreadQueries().build()

        setLayout()

        BestToday("JOARA")

        return view
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

            when (type) {
                "Today" -> {
                    BestToday("JOARA")
//                    mFragmentBestTabToday = FragmentBestTabToday("Joara")
//                    childFragmentManager.commit {
//                        replace(R.id.llayoutWrap, mFragmentBestTabToday)
//                    }
                }
                "Weekend" -> {
//                    mFragmentBestTabWeekend = FragmentBestTabWeekend("Joara")
//                    childFragmentManager.commit {
//                        replace(R.id.llayoutWrap, mFragmentBestTabWeekend)
//                    }
                }
                "Month" -> {
//                    mFragmentBestTabMonth = FragmentBestTabMonth("Joara")
//                    childFragmentManager.commit {
//                        replace(R.id.llayoutWrap, mFragmentBestTabMonth)
//                    }
                }
            }

            getType(type)
        }
    }

    private fun getType(type : String) {

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
            adapterType!!.notifyDataSetChanged()
        }

        adapterType.setOnItemClickListener(object : AdapterType.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BestType? = adapterType.getItem(position)
                adapterType.setSelectedBtn(position)
                pos = position
                adapterType.notifyDataSetChanged()

                when (type) {
                    "Today" -> {
//                        mFragmentBestTabToday = FragmentBestTabToday(item!!.type!!)
//                        childFragmentManager.commit {
//                            replace(R.id.llayoutWrap, mFragmentBestTabToday)
//                        }
                    }
                    "Weekend" -> {
//                        mFragmentBestTabWeekend = FragmentBestTabWeekend(item!!.type!!)
//                        childFragmentManager.commit {
//                            replace(R.id.llayoutWrap, mFragmentBestTabWeekend)
//                        }
                    }
                    "Month" -> {
//                        mFragmentBestTabMonth = FragmentBestTabMonth(item!!.type!!)
//                        childFragmentManager.commit {
//                            replace(R.id.llayoutWrap, mFragmentBestTabMonth)
//                        }
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

    fun BestToday(tabType : String){
        var adapterToday: AdapterBestToday?

        val items = ArrayList<BookListDataBestToday?>()
        var status = ""
        var cate = ""

        cate = Genre.getGenre(requireContext()).toString()

        adapterToday = AdapterBestToday(items)

        binding.rviewBest.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBest.adapter = adapterToday

        BestRef.getBestRefToday(tabType, cate).get().addOnSuccessListener {

            for (i in it.children) {
                val group: BookListDataBestToday? = i.getValue(BookListDataBestToday::class.java)
                items.add(
                    BookListDataBestToday(
                        group!!.writer,
                        group.title,
                        group.bookImg,
                        group.bookCode,
                        group.info1,
                        group.info2,
                        group.info3,
                        group.info4,
                        group.info5,
                        group.number,
                        calculateNum(tabType, group.number, group.title),
                        group.date,
                        status
                    )
                )
                adapterToday!!.notifyDataSetChanged()
            }

        }.addOnFailureListener {}

        adapterToday.setOnItemClickListener(object : AdapterBestToday.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                val item: BookListDataBestToday? = adapterToday!!.getItem(position)

                val mBottomDialogBest = BottomDialogBest(requireContext(), item!!, tabType, cate)
                fragmentManager?.let { mBottomDialogBest.show(it, null) }
            }
        })
    }

    private fun calculateNum(tabType: String, num: Int?, title: String?): Int {

        val yesterdayNum = dbYesterday.bestDao().findName(tabType, title!!)

        if (yesterdayNum == 0) {
            status = "NEW"
            return 0
        } else {
            return when {
                yesterdayNum < num!! -> {
                    status = "DOWN"
                    num - yesterdayNum
                }
                yesterdayNum > num -> {
                    status = "UP"
                    num - yesterdayNum
                }
                yesterdayNum == num -> {
                    status = "SAME"
                    0
                }
                else -> {
                    status = "SAME"
                    0
                }
            }
        }
    }
}