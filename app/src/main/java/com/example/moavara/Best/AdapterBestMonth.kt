package com.example.moavara.Best

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.Search.BookListDataBestMonthNum
import com.example.moavara.Search.BookListDataBestWeekend
import com.example.moavara.Util.DBDate
import com.example.moavara.Util.DBDate.getMonthDates
import com.example.moavara.databinding.ItemBooklistBestMonthBinding

class AdapterBestMonth(
    items: ArrayList<BookListDataBestWeekend>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item: ArrayList<BookListDataBestWeekend> = items
    var selected: String? = ""
    var monthDate = "${DBDate.Year()}0${DBDate.Month().toInt() + 1}01"

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, value: String?)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemBooklistBestMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderBestWeekend(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HolderBestWeekend) {

            val items = item[position]

            val date = DBDate.getDateData(monthDate)

            with(holder.binding){

                val monthNum = date?.let { DBDate.setMonthNum(it.date) }
                if (monthNum != null) {
                    val dateNum = getMonthDates(monthNum, position)
                    tviewDate1.text = dateNum.sun.toString()
                    tviewDate2.text = dateNum.mon.toString()
                    tviewDate3.text = dateNum.tue.toString()
                    tviewDate4.text = dateNum.wed.toString()
                    tviewDate5.text = dateNum.thur.toString()
                    tviewDate6.text = dateNum.fri.toString()
                    tviewDate7.text = dateNum.sat.toString()
                }
                if(tviewDate1.text == "0"){
                    tviewDate1.visibility = View.INVISIBLE
                } else {
                    tviewDate1.visibility = View.VISIBLE
                }

                if(tviewDate2.text == "0"){
                    tviewDate2.visibility = View.INVISIBLE
                } else {
                    tviewDate2.visibility = View.VISIBLE
                }

                if(tviewDate3.text == "0"){
                    tviewDate3.visibility = View.INVISIBLE
                } else {
                    tviewDate3.visibility = View.VISIBLE
                }

                if(tviewDate4.text == "0"){
                    tviewDate4.visibility = View.INVISIBLE
                } else {
                    tviewDate4.visibility = View.VISIBLE
                }

                if(tviewDate5.text == "0"){
                    tviewDate5.visibility = View.INVISIBLE
                } else {
                    tviewDate5.visibility = View.VISIBLE
                }

                if(tviewDate6.text == "0"){
                    tviewDate6.visibility = View.INVISIBLE
                } else {
                    tviewDate6.visibility = View.VISIBLE
                }

                if(tviewDate7.text == "0"){
                    tviewDate7.visibility = View.INVISIBLE
                } else {
                    tviewDate7.visibility = View.VISIBLE
                }

                if (items.sun != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.sun?.bookImg)
                        .into(iviewBookImg1)
                    isSelectBook(items.sun, llayoutCover1)
                    iviewBookImg1.visibility = View.VISIBLE

                } else {
                    iviewBookImg1.visibility = View.GONE
                }

                if (items.mon != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.mon?.bookImg)
                        .into(iviewBookImg2)

                    isSelectBook(items.mon, llayoutCover2)
                    iviewBookImg2.visibility = View.VISIBLE
                } else {
                    iviewBookImg2.visibility = View.GONE
                }

                if (items.tue != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.tue?.bookImg)
                        .into(iviewBookImg3)

                    isSelectBook(items.tue, llayoutCover3)
                    iviewBookImg3.visibility = View.VISIBLE
                } else {
                    iviewBookImg3.visibility = View.GONE
                }

                if (items.wed != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.wed?.bookImg)
                        .into(iviewBookImg4)

                    isSelectBook(items.wed, llayoutCover4)
                    iviewBookImg4.visibility = View.VISIBLE
                } else {
                    iviewBookImg4.visibility = View.GONE
                }

                if (items.thur != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.thur?.bookImg)
                        .into(iviewBookImg5)

                    isSelectBook(items.thur, llayoutCover5)
                    iviewBookImg5.visibility = View.VISIBLE
                } else {
                    iviewBookImg5.visibility = View.GONE
                }

                if (items.fri != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.fri?.bookImg)
                        .into(iviewBookImg6)

                    isSelectBook(items.fri, llayoutCover6)
                    iviewBookImg6.visibility = View.VISIBLE
                } else {
                    iviewBookImg6.visibility = View.GONE
                }

                if (items.sat != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.sat?.bookImg)
                        .into(iviewBookImg7)

                    isSelectBook(items.sat, llayoutCover7)
                    iviewBookImg7.visibility = View.VISIBLE
                } else {
                    iviewBookImg7.visibility = View.GONE
                }
            }
        }
    }

    private fun isSelectBook(items : BookListDataBest?, llayout : LinearLayout){
        if (selected != "" && getSelectedBook() != items?.bookCode) {
            llayout.visibility = View.VISIBLE
        } else {
            llayout.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class HolderBestWeekend internal constructor(val binding: ItemBooklistBestMonthBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            with(binding){
                cviewWrap1.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v, pos, "1")
                    }
                }

                cviewWrap2.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v, pos, "2")
                    }
                }

                cviewWrap3.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v, pos, "3")
                    }
                }

                cviewWrap4.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v, pos, "4")
                    }
                }

                cviewWrap5.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v, pos, "5")
                    }
                }

                cviewWrap6.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v, pos, "6")
                    }
                }

                cviewWrap7.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v, pos, "7")
                    }
                }
            }
        }

    }

    fun getItem(position: Int): BookListDataBestWeekend? {
        return item[position]
    }


    fun getSelectedBook(): String? {
        return selected
    }

}