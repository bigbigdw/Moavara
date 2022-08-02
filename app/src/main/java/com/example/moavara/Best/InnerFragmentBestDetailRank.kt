package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.DataBase.TrophyInfo
import com.example.moavara.R
import com.example.moavara.Search.BestRankListWeekend
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.FragmentBestDetailRankBinding
import java.util.*

class InnerFragmentBestDetailRank(
    private val item: ArrayList<BookListDataBestAnalyze>,
) : Fragment() {

    private lateinit var adapterMonth: AdapterBestRankList
    private val itemMonth = ArrayList<BestRankListWeekend>()
    val itemMonthList11 = BestRankListWeekend()
    val itemMonthList12 = BestRankListWeekend()
    val itemMonthList13 = BestRankListWeekend()
    val itemMonthList14 = BestRankListWeekend()
    val itemMonthList15 = BestRankListWeekend()
    val itemMonthList16 = BestRankListWeekend()

    val itemMonthList21 = BestRankListWeekend()
    val itemMonthList22 = BestRankListWeekend()
    val itemMonthList23 = BestRankListWeekend()
    val itemMonthList24 = BestRankListWeekend()
    val itemMonthList25 = BestRankListWeekend()
    val itemMonthList26 = BestRankListWeekend()

    val itemMonthList31 = BestRankListWeekend()
    val itemMonthList32 = BestRankListWeekend()
    val itemMonthList33 = BestRankListWeekend()
    val itemMonthList34 = BestRankListWeekend()
    val itemMonthList35 = BestRankListWeekend()
    val itemMonthList36 = BestRankListWeekend()

    var monthBeforeBefore1 = false
    var monthBeforeBefore2 = false
    var monthBeforeBefore3 = false
    var monthBeforeBefore4 = false
    var monthBeforeBefore5 = false
    var monthBeforeBefore6 = false

    var monthBefore1 = false
    var monthBefore2 = false
    var monthBefore3 = false
    var monthBefore4 = false
    var monthBefore5 = false
    var monthBefore6 = false

    var month1 = false
    var month2 = false
    var month3 = false
    var month4 = false
    var month5 = false
    var month6 = false
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

        getRankList(item)

        getAnalyze()

        return view
    }

    private fun getAnalyze() {

        val cmpAsc: Comparator<BookListDataBestAnalyze> =
            Comparator { o1, o2 -> o1.date.compareTo(o2.date) }
        Collections.sort(item, cmpAsc)

        for (itemData in item) {
            val itemDate = DBDate.getDateData(itemData.date)

            if (itemDate?.month == DBDate.Month().toInt()) {
                if (itemDate.week == 1) {
                    getItemMonthList(itemDate, itemData, itemMonthList11)
                    month1= true
                } else if (itemDate.week == 2) {
                    getItemMonthList(itemDate, itemData, itemMonthList12)
                    month2= true
                } else if (itemDate.week == 3) {
                    getItemMonthList(itemDate, itemData, itemMonthList13)
                    month3= true
                } else if (itemDate.week == 4) {
                    getItemMonthList(itemDate, itemData, itemMonthList14)
                    month4= true
                } else if (itemDate.week == 5) {
                    getItemMonthList(itemDate, itemData, itemMonthList15)
                    month5= true
                }  else if (itemDate.week == 5) {
                    getItemMonthList(itemDate, itemData, itemMonthList16)
                    month6= true
                }
            } else if (itemDate?.month == DBDate.Month().toInt() - 1) {
                if (itemDate.week == 1) {
                    getItemMonthList(itemDate, itemData, itemMonthList21)
                    monthBefore1 = true
                } else if (itemDate.week == 2) {
                    getItemMonthList(itemDate, itemData, itemMonthList22)
                    monthBefore2 = true
                } else if (itemDate.week == 3) {
                    getItemMonthList(itemDate, itemData, itemMonthList23)
                    monthBefore3 = true
                } else if (itemDate.week == 4) {
                    getItemMonthList(itemDate, itemData, itemMonthList24)
                    monthBefore4 = true
                } else if (itemDate.week == 5) {
                    getItemMonthList(itemDate, itemData, itemMonthList25)
                    monthBefore5 = true
                } else if (itemDate.week == 6) {
                    getItemMonthList(itemDate, itemData, itemMonthList26)
                    monthBefore6 = true
                }
            }  else if (itemDate?.month == DBDate.Month().toInt() - 2) {
                if (itemDate.week == 1) {
                    getItemMonthList(itemDate, itemData, itemMonthList31)
                    monthBeforeBefore1 = true
                } else if (itemDate.week == 2) {
                    getItemMonthList(itemDate, itemData, itemMonthList32)
                    monthBeforeBefore2 = true
                } else if (itemDate.week == 3) {
                    getItemMonthList(itemDate, itemData, itemMonthList33)
                    monthBeforeBefore3 = true
                } else if (itemDate.week == 4) {
                    getItemMonthList(itemDate, itemData, itemMonthList34)
                    monthBeforeBefore4 = true
                } else if (itemDate.week == 5) {
                    getItemMonthList(itemDate, itemData, itemMonthList35)
                    monthBeforeBefore5 = true
                } else if (itemDate.week == 6) {
                    getItemMonthList(itemDate, itemData, itemMonthList36)
                    monthBeforeBefore6 = true
                }
            }
        }

        if(monthBeforeBefore1){
            itemMonth.add(itemMonthList31)
        }
        if(monthBeforeBefore2){
            itemMonth.add(itemMonthList32)
        }
        if(monthBeforeBefore3){
            itemMonth.add(itemMonthList33)
        }
        if(monthBeforeBefore4){
            itemMonth.add(itemMonthList34)
        }
        if(monthBeforeBefore5){
            itemMonth.add(itemMonthList35)
        }
        if(monthBeforeBefore6){
            itemMonth.add(itemMonthList36)
        }

        if(monthBefore1){
            itemMonth.add(itemMonthList21)
        }
        if(monthBefore2){
            itemMonth.add(itemMonthList22)
        }
        if(monthBefore3){
            itemMonth.add(itemMonthList23)
        }
        if(monthBefore4){
            itemMonth.add(itemMonthList24)
        }
        if(monthBefore5){
            itemMonth.add(itemMonthList25)
        }
        if(monthBefore6){
            itemMonth.add(itemMonthList26)
        }

        if(month1){
            itemMonth.add(itemMonthList11)
        }
        if(month2){
            itemMonth.add(itemMonthList12)
        }
        if(month3){
            itemMonth.add(itemMonthList13)
        }
        if(month4){
            itemMonth.add(itemMonthList14)
        }
        if(month5){
            itemMonth.add(itemMonthList15)
        }
        if(month6){
            itemMonth.add(itemMonthList16)
        }

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

