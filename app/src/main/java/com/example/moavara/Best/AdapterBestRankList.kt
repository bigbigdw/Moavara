package com.example.moavara.Best

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.R
import com.example.moavara.Search.BestRankListWeekend
import com.example.moavara.Search.BookListDataBestMonthNum
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.ItemBestRanklistBinding

class AdapterBestRankList(
    items: java.util.ArrayList<BestRankListWeekend>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item: java.util.ArrayList<BestRankListWeekend> = items
    var selected: String? = ""
    var monthDate = "${DBDate.Year()}0${DBDate.Month().toInt() + 1}01"
    var monthNum = BookListDataBestMonthNum()

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
            val date = DBDate.getDateData(monthDate)

            monthNum = DBDate.setMonthNum(date?.date ?: 0)
            val dateNum = DBDate.getMonthDates(date?.month ?: 0,monthNum, position)


            with(holder.binding){
                tviewDate1.text = dateNum.sun.toString()
                tviewDate2.text = dateNum.mon.toString()
                tviewDate3.text = dateNum.tue.toString()
                tviewDate4.text = dateNum.wed.toString()
                tviewDate5.text = dateNum.thur.toString()
                tviewDate6.text = dateNum.fri.toString()
                tviewDate7.text = dateNum.sat.toString()

                if (items.sun != null) {
                    if (items.sun?.date == DBDate.DateMMDD()) {
                        iviewBookImg1.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg1.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview1.visibility = View.VISIBLE
                    tview1.text = "${items.sun?.number?.toInt() ?: 0 + 1}등"
                } else {
                    iviewBookImg1.setImageResource(R.drawable.ic_best_gr_24px)
                    tview1.visibility = View.GONE
                }

                if (items.mon != null) {
                    if (items.mon?.date == DBDate.DateMMDD()) {
                        iviewBookImg2.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg2.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview2.visibility = View.VISIBLE
                    tview2.text = "${items.mon?.number?.toInt() ?: 0 + 1}등"
                } else {
                    iviewBookImg2.setImageResource(R.drawable.ic_best_gr_24px)
                    tview2.visibility = View.GONE
                }

                if (items.tue != null) {
                    if (items.tue?.date == DBDate.DateMMDD()) {
                        iviewBookImg3.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg3.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview3.visibility = View.VISIBLE
                    tview3.text = "${items.tue?.number?.toInt() ?: 0 + 1}등"
                } else {
                    iviewBookImg3.setImageResource(R.drawable.ic_best_gr_24px)
                    tview3.visibility = View.GONE
                }

                if (items.wed != null) {
                    if (items.wed?.date == DBDate.DateMMDD()) {
                        iviewBookImg4.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg4.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview4.visibility = View.VISIBLE
                    tview4.text = "${items.wed?.number?.toInt() ?: 0 + 1}등"
                } else {
                    iviewBookImg4.setImageResource(R.drawable.ic_best_gr_24px)
                    tview4.visibility = View.GONE
                }

                if (items.thur != null) {
                    if (items.thur?.date == DBDate.DateMMDD()) {
                        iviewBookImg5.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg5.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview5.visibility = View.VISIBLE
                    tview5.text = "${items.thur?.number?.toInt() ?: 0 + 1}등"
                } else {
                    iviewBookImg5.setImageResource(R.drawable.ic_best_gr_24px)
                    tview5.visibility = View.GONE
                }

                if (items.fri != null) {
                    if (items.fri?.date == DBDate.DateMMDD()) {
                        iviewBookImg6.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg6.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview6.visibility = View.VISIBLE
                    tview6.text = "${items.fri?.number?.toInt() ?: 0 + 1}등"
                } else {
                    iviewBookImg6.setImageResource(R.drawable.ic_best_gr_24px)
                    tview6.visibility = View.GONE
                }

                if (items.sat != null) {
                    if (items.sat?.date == DBDate.DateMMDD()) {
                        iviewBookImg7.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg7.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview7.visibility = View.VISIBLE
                    tview7.text = "${items.sat?.number?.toInt() ?: 0 + 1}등"
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