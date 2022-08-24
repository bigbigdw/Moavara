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
    items: ArrayList<BestRankListWeekend>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item: ArrayList<BestRankListWeekend> = items
    var monthDate = "${DBDate.Year()}0${DBDate.Month().toInt() + 1}01"
    var monthNum = BookListDataBestMonthNum()
    var monthCount = 0

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

            when (monthCount) {
                0 -> {
                    monthDate = "${DBDate.Year()}0${DBDate.Month().toInt() + 1}01"
                }
                1 -> {
                    monthDate = "${DBDate.Year()}0${DBDate.Month().toInt()}01"
                }
                2 -> {
                    monthDate = "${DBDate.Year()}0${DBDate.Month().toInt() - 1}01"
                }
            }

            val date = DBDate.getDateData(monthDate)

            with(holder.binding){

                monthNum = DBDate.setMonthNum(date?.date ?: 0)

                val dateNum = DBDate.getMonthDates(date?.month ?: 0, monthNum, position)
                tviewDate1.text = dateNum.sun.toString()
                tviewDate2.text = dateNum.mon.toString()
                tviewDate3.text = dateNum.tue.toString()
                tviewDate4.text = dateNum.wed.toString()
                tviewDate5.text = dateNum.thur.toString()
                tviewDate6.text = dateNum.fri.toString()
                tviewDate7.text = dateNum.sat.toString()

                if (tviewDate1.text == "0") {
                    tviewDate1.visibility = View.INVISIBLE
                } else {
                    tviewDate1.visibility = View.VISIBLE
                }

                if (tviewDate2.text == "0") {
                    tviewDate2.visibility = View.INVISIBLE
                } else {
                    tviewDate2.visibility = View.VISIBLE
                }

                if (tviewDate3.text == "0") {
                    tviewDate3.visibility = View.INVISIBLE
                } else {
                    tviewDate3.visibility = View.VISIBLE
                }

                if (tviewDate4.text == "0") {
                    tviewDate4.visibility = View.INVISIBLE
                } else {
                    tviewDate4.visibility = View.VISIBLE
                }

                if (tviewDate5.text == "0") {
                    tviewDate5.visibility = View.INVISIBLE
                } else {
                    tviewDate5.visibility = View.VISIBLE
                }

                if (tviewDate6.text == "0") {
                    tviewDate6.visibility = View.INVISIBLE
                } else {
                    tviewDate6.visibility = View.VISIBLE
                }

                if (tviewDate7.text == "0") {
                    tviewDate7.visibility = View.INVISIBLE
                } else {
                    tviewDate7.visibility = View.VISIBLE
                }

                if (items.sun != null) {

                    tview1.visibility = View.VISIBLE

                    if(items.sun?.number == 999){
                        iviewBookImg1.setImageResource(R.mipmap.ic_launcher)

                        tview1.text = "PICK"
                    } else {
                        if (items.sun?.date == DBDate.DateMMDD()) {
                            iviewBookImg1.setImageResource(R.drawable.ic_best_gn_24px)
                        } else {
                            iviewBookImg1.setImageResource(R.drawable.ic_best_vt_24px)
                        }

                        tview1.text = "${(items.sun?.number ?: 0) + 1}등"
                    }

                } else {
                    iviewBookImg1.setImageResource(R.drawable.ic_launcher_gray)
                    tview1.visibility = View.GONE
                }

                if (items.mon != null) {

                    tview2.visibility = View.VISIBLE

                    if(items.mon?.number == 999){
                        iviewBookImg2.setImageResource(R.mipmap.ic_launcher)
                        tview2.text = "PICK"
                    } else {
                        if (items.mon?.date == DBDate.DateMMDD()) {
                            iviewBookImg2.setImageResource(R.drawable.ic_best_gn_24px)
                        } else {
                            iviewBookImg2.setImageResource(R.drawable.ic_best_vt_24px)
                        }

                        tview2.text = "${(items.mon?.number ?: 0) + 1}등"
                    }
                } else {
                    iviewBookImg2.setImageResource(R.drawable.ic_launcher_gray)
                    tview2.visibility = View.GONE
                }

                if (items.tue != null) {

                    tview3.visibility = View.VISIBLE

                    if(items.tue?.number == 999){
                        iviewBookImg3.setImageResource(R.mipmap.ic_launcher)
                        tview3.text = "PICK"
                    } else {
                        if (items.tue?.date == DBDate.DateMMDD()) {
                            iviewBookImg3.setImageResource(R.drawable.ic_best_gn_24px)
                        } else {
                            iviewBookImg3.setImageResource(R.drawable.ic_best_vt_24px)
                        }
                        tview3.text = "${(items.tue?.number ?: 0) + 1}등"
                    }
                } else {
                    iviewBookImg3.setImageResource(R.drawable.ic_launcher_gray)
                    tview3.visibility = View.GONE
                }

                if (items.wed != null) {

                    tview4.visibility = View.VISIBLE

                    if(items.wed?.number == 999){
                        iviewBookImg4.setImageResource(R.mipmap.ic_launcher)

                        tview4.text = "PICK"
                    } else {
                        if (items.wed?.date == DBDate.DateMMDD()) {
                            iviewBookImg4.setImageResource(R.drawable.ic_best_gn_24px)
                        } else {
                            iviewBookImg4.setImageResource(R.drawable.ic_best_vt_24px)
                        }

                        tview4.text = "${(items.wed?.number ?: 0) + 1}등"
                    }
                } else {
                    iviewBookImg4.setImageResource(R.drawable.ic_launcher_gray)
                    tview4.visibility = View.GONE
                }

                if (items.thur != null) {

                    tview5.visibility = View.VISIBLE

                    if(items.thur?.number == 999){
                        iviewBookImg5.setImageResource(R.mipmap.ic_launcher)

                        tview5.text = "PICK"
                    } else {
                        if (items.thur?.date == DBDate.DateMMDD()) {
                            iviewBookImg5.setImageResource(R.drawable.ic_best_gn_24px)
                        } else {
                            iviewBookImg5.setImageResource(R.drawable.ic_best_vt_24px)
                        }
                        tview5.text = "${(items.thur?.number ?: 0) + 1}등"
                    }
                } else {
                    iviewBookImg5.setImageResource(R.drawable.ic_launcher_gray)
                    tview5.visibility = View.GONE
                }

                if (items.fri != null) {

                    tview6.visibility = View.VISIBLE

                    if(items.fri?.number == 999){
                        iviewBookImg6.setImageResource(R.mipmap.ic_launcher)

                        tview6.text = "PICK"
                    } else {
                        if (items.fri?.date == DBDate.DateMMDD()) {
                            iviewBookImg6.setImageResource(R.drawable.ic_best_gn_24px)
                        } else {
                            iviewBookImg6.setImageResource(R.drawable.ic_best_vt_24px)
                        }

                        tview6.text = "${(items.fri?.number ?: 0) + 1}등"
                    }
                } else {
                    iviewBookImg6.setImageResource(R.drawable.ic_launcher_gray)
                    tview6.visibility = View.GONE
                }

                if (items.sat != null) {

                    tview7.visibility = View.VISIBLE

                    if(items.sat?.number == 999){
                        iviewBookImg7.setImageResource(R.mipmap.ic_launcher)

                        tview7.text = "PICK"
                    } else {
                        if (items.sat?.date == DBDate.DateMMDD()) {
                            iviewBookImg7.setImageResource(R.drawable.ic_best_gn_24px)
                        } else {
                            iviewBookImg7.setImageResource(R.drawable.ic_best_vt_24px)
                        }

                        tview7.text = "${(items.sat?.number ?: 0) + 1}등"
                    }
                } else {
                    iviewBookImg7.setImageResource(R.drawable.ic_launcher_gray)
                    tview7.visibility = View.GONE
                }
            }
        }
    }

    fun setMonthDate(count: Int) {
        monthCount = count
        notifyDataSetChanged()
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