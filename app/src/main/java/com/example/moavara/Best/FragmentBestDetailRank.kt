package com.example.moavara.Best

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.DataBase.TrophyInfo
import com.example.moavara.R
import com.example.moavara.Search.BestRankListWeekend
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.FragmentBestDetailRankBinding
import com.example.moavara.databinding.ItemBestRanklistBinding

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

            Log.d("####", itemMonthList1.sat.toString())

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

class AdapterBestRankList(
    items: java.util.ArrayList<BestRankListWeekend>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item: java.util.ArrayList<BestRankListWeekend> = items
    var selected: String? = ""

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, value: String?)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemBestRanklistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderBestWeekend(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HolderBestWeekend) {

            val items = item[position]

            with(holder.binding){
                if (items.sun != null) {
                    tviewDate1.text = items.sun?.date?.substring(3)
                    if (items.sun?.date == DBDate.DateMMDD()) {
                        iviewBookImg1.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg1.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview1.visibility = View.VISIBLE
                    tview1.text = "${items.sun?.number}등"
                } else {
                    iviewBookImg1.setImageResource(R.drawable.ic_best_gr_24px)
                    tview1.visibility = View.GONE
                }

                if (items.mon != null) {
                    tviewDate2.text = items.mon?.date?.substring(3)
                    if (items.mon?.date == DBDate.DateMMDD()) {
                        iviewBookImg2.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg2.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview2.visibility = View.VISIBLE
                    tview2.text = "${items.mon?.number}등"
                } else {
                    iviewBookImg2.setImageResource(R.drawable.ic_best_gr_24px)
                    tview2.visibility = View.GONE
                }

                if (items.tue != null) {
                    tviewDate3.text = items.tue?.date?.substring(3)
                    if (items.tue?.date == DBDate.DateMMDD()) {
                        iviewBookImg3.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg3.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview3.visibility = View.VISIBLE
                    tview3.text = "${items.tue?.number}등"
                } else {
                    iviewBookImg3.setImageResource(R.drawable.ic_best_gr_24px)
                    tview3.visibility = View.GONE
                }

                if (items.wed != null) {
                    tviewDate4.text = items.wed?.date?.substring(3)
                    if (items.wed?.date == DBDate.DateMMDD()) {
                        iviewBookImg4.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg4.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview4.visibility = View.VISIBLE
                    tview4.text = "${items.wed?.number}등"
                } else {
                    iviewBookImg4.setImageResource(R.drawable.ic_best_gr_24px)
                    tview4.visibility = View.GONE
                }

                if (items.thur != null) {
                    tviewDate5.text = items.thur?.date?.substring(3)
                    if (items.thur?.date == DBDate.DateMMDD()) {
                        iviewBookImg5.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg5.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview5.visibility = View.VISIBLE
                    tview5.text = "${items.thur?.number}등"
                } else {
                    iviewBookImg5.setImageResource(R.drawable.ic_best_gr_24px)
                    tview5.visibility = View.GONE
                }

                if (items.fri != null) {
                    tviewDate6.text = items.fri?.date?.substring(3)
                    if (items.fri?.date == DBDate.DateMMDD()) {
                        iviewBookImg6.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg6.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview6.visibility = View.VISIBLE
                    tview6.text = "${items.fri?.number}등"
                } else {
                    iviewBookImg6.setImageResource(R.drawable.ic_best_gr_24px)
                    tview6.visibility = View.GONE
                }

                if (items.sat != null) {
                    tviewDate7.text = items.sat?.date?.substring(3)
                    if (items.sat?.date == DBDate.DateMMDD()) {
                        iviewBookImg7.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg7.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview7.visibility = View.VISIBLE
                    tview7.text = "${items.sat?.number}등"
                } else {
                    iviewBookImg7.setImageResource(R.drawable.ic_best_gr_24px)
                    tview7.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class HolderBestWeekend internal constructor(val binding: ItemBestRanklistBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    fun getItem(position: Int): BestRankListWeekend {
        return item[position]
    }


    fun getSelectedBook(): String? {
        return selected
    }

}

