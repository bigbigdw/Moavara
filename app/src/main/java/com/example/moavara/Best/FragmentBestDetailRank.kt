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
    private var itemMonthList1 : BestRankListWeekend? = null
    private var itemMonthList2 : BestRankListWeekend? = null
    private var itemMonthList3 : BestRankListWeekend? = null
    private var itemMonthList4 : BestRankListWeekend? = null
    private var itemMonthList5 : BestRankListWeekend? = null
    private val itemTrophy = ArrayList<TrophyInfo>()
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
                if (itemDate?.week == 1) {
                    itemMonthList1 = getItemMonthList(itemDate, itemData)
                } else if (itemDate?.week == 2) {
                    itemMonthList2 = getItemMonthList(itemDate, itemData)
                }  else if (itemDate?.week == 3) {
                    itemMonthList3 = getItemMonthList(itemDate, itemData)
                }  else if (itemDate?.week == 4) {
                    itemMonthList4 = getItemMonthList(itemDate, itemData)
                }  else if (itemDate?.week == 5) {
                    itemMonthList5 = getItemMonthList(itemDate, itemData)
                }
            }

            Log.d("####", itemMonthList2.toString())

            itemMonthList1?.let { itemMonth.add(it) }
            itemMonthList2?.let { itemMonth.add(it) }
            itemMonthList3?.let { itemMonth.add(it) }
            itemMonthList4?.let { itemMonth.add(it) }
            itemMonthList5?.let { itemMonth.add(it) }
            adapterMonth.notifyDataSetChanged()


        }
    }

    fun getItemMonthList(
        trophyInfo: TrophyInfo,
        item: BookListDataBestAnalyze
    ): BestRankListWeekend? {

        Log.d("####-3", trophyInfo.toString())
        Log.d("####-4", item.toString())
        val bestRankListWeekend : BestRankListWeekend? = null

        if (trophyInfo.date == 1) {
            bestRankListWeekend?.sun = item
        } else if (trophyInfo.date == 2) {
            bestRankListWeekend?.mon = item
        }  else if (trophyInfo.date == 3) {
            bestRankListWeekend?.tue = item
        }  else if (trophyInfo.date == 4) {
            bestRankListWeekend?.wed = item
        }  else if (trophyInfo.date == 5) {
            bestRankListWeekend?.thur = item
        }  else if (trophyInfo.date == 6) {
            bestRankListWeekend?.fri = item
        }  else if (trophyInfo.date == 7) {
            bestRankListWeekend?.sat = item
        }
        Log.d("####-2", bestRankListWeekend.toString())
        return bestRankListWeekend
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
                    tviewDate1.text = items.sun!!.date.substring(3)
                    iviewBookImg1.visibility = View.VISIBLE
                    iviewBookImg1.setImageResource(R.drawable.ic_best_vt_24px)
                    tview1.visibility = View.VISIBLE
                    tview1.text = "${items.sun!!.number}등\n(${items.sun!!.numberDiff})"
                } else {
                    iviewBookImg1.visibility = View.GONE
                    tview1.visibility = View.GONE
                }

                if (items.mon != null) {
                    tviewDate2.text = items.mon!!.date.substring(3)
                    iviewBookImg2.visibility = View.VISIBLE
                    iviewBookImg2.setImageResource(R.drawable.ic_best_vt_24px)
                    tview2.visibility = View.VISIBLE
                    tview2.text = "${items.mon!!.number}등\n(${items.mon!!.numberDiff})"
                } else {
                    iviewBookImg2.visibility = View.GONE
                    tview2.visibility = View.GONE
                }

                if (items.tue != null) {
                    tviewDate3.text = items.tue!!.date.substring(3)
                    iviewBookImg3.visibility = View.VISIBLE
                    iviewBookImg3.setImageResource(R.drawable.ic_best_vt_24px)
                    tview3.visibility = View.VISIBLE
                    tview3.text = "${items.tue!!.number}등\n(${items.tue!!.numberDiff})"
                } else {
                    iviewBookImg3.visibility = View.GONE
                    tview3.visibility = View.GONE
                }

                if (items.wed != null) {
                    tviewDate4.text = items.wed!!.date.substring(3)
                    iviewBookImg4.visibility = View.VISIBLE
                    iviewBookImg4.setImageResource(R.drawable.ic_best_vt_24px)
                    tview3.visibility = View.VISIBLE
                    tview3.text = "${items.wed!!.number}등\n(${items.wed!!.numberDiff})"
                } else {
                    iviewBookImg4.visibility = View.GONE
                    tview3.visibility = View.GONE
                }

                if (items.thur != null) {
                    tviewDate5.text = items.thur!!.date.substring(3)
                    iviewBookImg5.visibility = View.VISIBLE
                    iviewBookImg5.setImageResource(R.drawable.ic_best_vt_24px)
                    tview3.visibility = View.VISIBLE
                    tview3.text = "${items.thur!!.number}등\n(${items.thur!!.numberDiff})"
                } else {
                    iviewBookImg5.visibility = View.GONE
                    tview3.visibility = View.GONE
                }

                if (items.fri != null) {
                    tviewDate6.text = items.fri!!.date.substring(3)
                    iviewBookImg6.visibility = View.VISIBLE
                    iviewBookImg6.setImageResource(R.drawable.ic_best_vt_24px)
                    tview3.visibility = View.VISIBLE
                    tview3.text = "${items.fri!!.number}등\n(${items.fri!!.numberDiff})"
                } else {
                    iviewBookImg6.visibility = View.GONE
                    tview3.visibility = View.GONE
                }

                if (items.sat != null) {
                    tviewDate7.text = items.sat!!.date.substring(3)
                    iviewBookImg7.visibility = View.VISIBLE
                    iviewBookImg7.setImageResource(R.drawable.ic_best_vt_24px)
                    tview3.visibility = View.VISIBLE
                    tview3.text = "${items.sat!!.number}등\n(${items.sat!!.numberDiff})"
                } else {
                    iviewBookImg7.visibility = View.GONE
                    tview3.visibility = View.GONE
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

