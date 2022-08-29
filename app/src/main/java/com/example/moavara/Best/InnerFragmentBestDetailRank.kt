package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.R
import com.example.moavara.Search.BestRankListWeekend
import com.example.moavara.Search.BookListDataBestAnalyze
import com.example.moavara.Search.TrophyInfo
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.FragmentBestDetailRankBinding
import java.util.*

class InnerFragmentBestDetailRank(
    private val item: ArrayList<BookListDataBestAnalyze>,
) : Fragment() {

    private lateinit var adapterMonth: AdapterBestRankList
    private val itemMonth = ArrayList<BestRankListWeekend>()
    var currentMonth = DBDate.Month().toInt() + 1
    private var monthCount = 0

    private var _binding: FragmentBestDetailRankBinding? = null
    private val binding get() = _binding!!
    private var year = 0
    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestDetailRankBinding.inflate(inflater, container, false)
        val view = binding.root

        adapterMonth = AdapterBestRankList(itemMonth)
        binding.rviewBestMonth.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rviewBestMonth.adapter = adapterMonth

        getRankList(item)

        getAnalyze(currentMonth - 1)

        with(binding){
            year = DBDate.Year().substring(2,4).toInt()
            llayoutAfter.visibility = View.INVISIBLE

            tviewMonth.text = "${year}년 ${currentMonth}월"

            llayoutBefore.setOnClickListener {
                monthCount += 1

                if(monthCount == 2){
                    llayoutBefore.visibility = View.INVISIBLE
                }  else {
                    llayoutAfter.visibility = View.VISIBLE
                }

                tviewMonth.text = "${year}년 ${currentMonth - monthCount}월"
                adapterMonth.setMonthDate(monthCount)
                getAnalyze((currentMonth -1) - monthCount)
            }

            llayoutAfter.setOnClickListener {
                monthCount -= 1

                if(monthCount == 0){
                    llayoutAfter.visibility = View.INVISIBLE
                } else {
                    llayoutBefore.visibility = View.VISIBLE
                }

                tviewMonth.text = "${year}년 ${currentMonth - monthCount}월"
                adapterMonth.setMonthDate(monthCount)
                getAnalyze((currentMonth -1) - monthCount)
            }
        }

        return view
    }

    private fun getAnalyze(month: Int) {

        val itemMonthList1 = BestRankListWeekend()
        val itemMonthList2 = BestRankListWeekend()
        val itemMonthList3 = BestRankListWeekend()
        val itemMonthList4 = BestRankListWeekend()
        val itemMonthList5 = BestRankListWeekend()
        val itemMonthList6 = BestRankListWeekend()

        itemMonth.clear()

        val cmpAsc: Comparator<BookListDataBestAnalyze> =
            Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
        Collections.sort(item, cmpAsc)

        for (itemData in item) {
            val itemDate = DBDate.getDateData(itemData.date)

            if (itemDate?.month == month) {
                when (itemDate.week) {
                    1 -> {
                        getItemMonthList(itemDate, itemData, itemMonthList1)
                    }
                    2 -> {
                        getItemMonthList(itemDate, itemData, itemMonthList2)
                    }
                    3 -> {
                        getItemMonthList(itemDate, itemData, itemMonthList3)
                    }
                    4 -> {
                        getItemMonthList(itemDate, itemData, itemMonthList4)
                    }
                    5 -> {
                        getItemMonthList(itemDate, itemData, itemMonthList5)
                    }
                    6 -> {
                        getItemMonthList(itemDate, itemData, itemMonthList6)
                    }
                }
            }
        }

        itemMonth.add(itemMonthList1)
        itemMonth.add(itemMonthList2)
        itemMonth.add(itemMonthList3)
        itemMonth.add(itemMonthList4)
        itemMonth.add(itemMonthList5)
        itemMonth.add(itemMonthList6)

        adapterMonth.notifyDataSetChanged()
    }

    fun getItemMonthList(
        trophyInfo: TrophyInfo,
        item: BookListDataBestAnalyze,
        itemMonthList: BestRankListWeekend
    ) {

        if (trophyInfo.date == 1) {
            itemMonthList.sun = item
        } else if (trophyInfo.date == 2) {
            itemMonthList.mon = item
        }  else if (trophyInfo.date == 3) {
            itemMonthList.tue = item
        }  else if (trophyInfo.date == 4) {
            itemMonthList.wed = item
        }  else if (trophyInfo.date == 5) {
            itemMonthList.thur = item
        }  else if (trophyInfo.date == 6) {
            itemMonthList.fri = item
        }  else if (trophyInfo.date == 7) {
            itemMonthList.sat = item
        }

    }

    private fun getRankList(data: ArrayList<BookListDataBestAnalyze>) {
        for (item in data) {
            val itemDate = DBDate.getDateData(item.date)

            with(binding.includeRank) {
                if (itemDate != null) {
                    if (itemDate.week == DBDate.Week().toInt()) {
                        when {
                            itemDate.date == 1 -> {
                                tviewRank1.visibility = View.VISIBLE

                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank1.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank1.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank1.text = "${(item.number + 1)}"
                            }
                            itemDate.date == 2 -> {
                                tviewRank2.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank2.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank2.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank2.text = "${(item.number + 1)}"
                            }
                            itemDate.date == 3 -> {
                                tviewRank3.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank3.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank3.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank3.text = "${(item.number + 1)}"
                            }
                            itemDate.date == 4 -> {
                                tviewRank4.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank4.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank4.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank4.text = "${(item.number + 1)}"
                            }
                            itemDate.date == 5 -> {
                                tviewRank5.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank5.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank5.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank5.text = "${(item.number + 1)}"
                            }
                            itemDate.date == 6 -> {
                                tviewRank6.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank6.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank6.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank6.text = "${(item.number + 1)}"
                            }
                            itemDate.date == 7 -> {
                                tviewRank7.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank7.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank7.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank7.text = "${(item.number + 1)}"
                            }
                        }
                    }
                }
            }
        }
    }
}

