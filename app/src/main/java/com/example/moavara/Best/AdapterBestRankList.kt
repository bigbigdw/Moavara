package com.example.moavara.Best

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.R
import com.example.moavara.Search.BestRankListWeekend
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.ItemBestRanklistBinding

class AdapterBestRankList(
    items: ArrayList<BestRankListWeekend>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item: ArrayList<BestRankListWeekend> = items
    var itemMode = 1

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
                    tviewDate1.text = items.sun?.date?.substring(4)
                    if (items.sun?.date == DBDate.DateMMDD()) {
                        iviewBookImg1.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg1.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview1.visibility = View.VISIBLE
                    tview1.text = "${items.sun?.number!!.toInt() + 1}등"
                } else {
                    iviewBookImg1.setImageResource(R.drawable.ic_best_gr_24px)
                    tview1.visibility = View.GONE
                }

                if (items.mon != null) {
                    tviewDate2.text = items.mon?.date?.substring(4)
                    if (items.mon?.date == DBDate.DateMMDD()) {
                        iviewBookImg2.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg2.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview2.visibility = View.VISIBLE
                    tview2.text = "${items.mon?.number!!.toInt() + 1}등"
                } else {
                    iviewBookImg2.setImageResource(R.drawable.ic_best_gr_24px)
                    tview2.visibility = View.GONE
                }

                if (items.tue != null) {
                    tviewDate3.text = items.tue?.date?.substring(4)
                    if (items.tue?.date == DBDate.DateMMDD()) {
                        iviewBookImg3.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg3.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview3.visibility = View.VISIBLE
                    tview3.text = "${(items.tue?.number?.toInt() ?: 0) + 1}등"
                } else {
                    iviewBookImg3.setImageResource(R.drawable.ic_best_gr_24px)
                    tview3.visibility = View.GONE
                }

                if (items.wed != null) {
                    tviewDate4.text = items.wed?.date?.substring(4)
                    if (items.wed?.date == DBDate.DateMMDD()) {
                        iviewBookImg4.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg4.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview4.visibility = View.VISIBLE
                    tview4.text = "${(items.wed?.number?.toInt() ?: 0) + 1}등"
                } else {
                    iviewBookImg4.setImageResource(R.drawable.ic_best_gr_24px)
                    tview4.visibility = View.GONE
                }

                if (items.thur != null) {
                    tviewDate5.text = items.thur?.date?.substring(4)
                    if (items.thur?.date == DBDate.DateMMDD()) {
                        iviewBookImg5.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg5.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview5.visibility = View.VISIBLE
                    tview5.text = "${(items.thur?.number?.toInt() ?: 0) + 1}등"
                } else {
                    iviewBookImg5.setImageResource(R.drawable.ic_best_gr_24px)
                    tview5.visibility = View.GONE
                }

                if (items.fri != null) {
                    tviewDate6.text = items.fri?.date?.substring(4)
                    if (items.fri?.date == DBDate.DateMMDD()) {
                        iviewBookImg6.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg6.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview6.visibility = View.VISIBLE
                    tview6.text = "${(items.fri?.number?.toInt() ?: 0) + 1}등"
                } else {
                    iviewBookImg6.setImageResource(R.drawable.ic_best_gr_24px)
                    tview6.visibility = View.GONE
                }

                if (items.sat != null) {
                    tviewDate7.text = items.sat?.date?.substring(4)
                    if (items.sat?.date == DBDate.DateMMDD()) {
                        iviewBookImg7.setImageResource(R.drawable.ic_best_gn_24px)
                    } else {
                        iviewBookImg7.setImageResource(R.drawable.ic_best_vt_24px)
                    }
                    tview7.visibility = View.VISIBLE
                    tview7.text = "${(items.sat?.number?.toInt() ?: 0) + 1}등"
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


}