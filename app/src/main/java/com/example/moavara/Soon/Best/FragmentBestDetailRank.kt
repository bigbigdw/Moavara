package com.example.moavara.Soon.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.Best.AdapterBestRankList
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.DataBase.TrophyInfo
import com.example.moavara.R
import com.example.moavara.Search.BestRankListWeekend
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.FragmentBestDetailRankBinding

class FragmentBestDetailRank(
    private val platfrom: String,
    private val item: ArrayList<BookListDataBestAnalyze>?,
) : Fragment() {

    private lateinit var adapterMonth: AdapterBestRankList
    private val itemMonth = ArrayList<BestRankListWeekend>()
    private var itemMonthList1 = BestRankListWeekend()
    private var itemMonthList2 = BestRankListWeekend()
    private var itemMonthList3 = BestRankListWeekend()
    private var itemMonthList4 = BestRankListWeekend()
    private var itemMonthList5 = BestRankListWeekend()
    private var _binding: FragmentBestDetailRankBinding? = null
    private val binding get() = _binding!!

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

        getRankList(item!!)

        getAnalyze()

        return view
    }

    private fun getAnalyze() {

        if (item != null) {

            for(itemData in item){
                val itemDate = DBDate.getDateData(itemData.date)
                if(itemDate?.month == DBDate.Month().toInt()){
                    if (itemDate.week == 1) {
                        getItemMonthList(itemDate, itemData, itemMonthList1)
                    } else if (itemDate.week == 2) {
                        getItemMonthList(itemDate, itemData, itemMonthList2)
                    }  else if (itemDate.week == 3) {
                        getItemMonthList(itemDate, itemData, itemMonthList3)
                    }  else if (itemDate.week == 4) {
                        getItemMonthList(itemDate, itemData, itemMonthList4)
                    }  else if (itemDate.week == 5) {
                        getItemMonthList(itemDate, itemData, itemMonthList5)
                    }
                }
            }

            itemMonthList1.let { itemMonth.add(it) }
            itemMonthList2.let { itemMonth.add(it) }
            itemMonthList3.let { itemMonth.add(it) }
            itemMonthList4.let { itemMonth.add(it) }
            itemMonthList5.let { itemMonth.add(it) }
            adapterMonth.notifyDataSetChanged()


        }
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
                                tviewRank1.text = "${(item.number + 1)}등"
                            }
                            itemDate.date == 2 -> {
                                tviewRank2.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank2.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank2.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank2.text = "${(item.number + 1)}등"
                            }
                            itemDate.date == 3 -> {
                                tviewRank3.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank3.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank3.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank3.text = "${(item.number + 1)}등"
                            }
                            itemDate.date == 4 -> {
                                tviewRank4.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank4.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank4.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank4.text = "${(item.number + 1)}등"
                            }
                            itemDate.date == 5 -> {
                                tviewRank5.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank5.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank5.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank5.text = "${(item.number + 1)}등"
                            }
                            itemDate.date == 6 -> {
                                tviewRank6.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank6.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank6.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank6.text = "${(item.number + 1)}등"
                            }
                            itemDate.date == 7 -> {
                                tviewRank7.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank7.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank7.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank7.text = "${(item.number + 1)}등"
                            }
                        }
                    }
                }
            }
        }
    }

}

