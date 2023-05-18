package com.bigbigdw.moavara.Best

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bigbigdw.moavara.Search.BestItemData
import com.bigbigdw.moavara.Search.BookListDataBestMonthNum
import com.bigbigdw.moavara.Search.BookListDataBestWeekend
import com.bigbigdw.moavara.Util.DBDate
import com.bigbigdw.moavara.Util.DBDate.getMonthDates
import com.bigbigdw.moavara.Util.dpToPx
import com.bigbigdw.moavara.databinding.ItemBooklistBestMonthBinding

class AdapterBestMonth(
    items: ArrayList<BookListDataBestWeekend>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item: ArrayList<BookListDataBestWeekend> = items
    var selected: String? = ""
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
        val view =
            ItemBooklistBestMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

            val today = DBDate.getDateData(DBDate.DateMMDD())

            with(holder.binding) {

                monthNum = DBDate.setMonthNum(date?.date ?: 0)

                val dateNum = getMonthDates(date?.month ?: 0, monthNum, position)
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
                    Glide.with(holder.itemView.context)
                        .load(items.sun?.bookImg?.replace("http://","https://"))
                        .into(iviewBookImg1)

                    isSelectBook(items.sun, llayoutCover1)
                    iviewBookImg1.visibility = View.VISIBLE

                    if (((today?.week ?: 0) - 1) == position && today?.date == 1 && monthCount == 0) {
                        llayoutWrap1.background = todayMark()
                    } else {
                        llayoutWrap1.background = null
                    }

                } else {
                    iviewBookImg1.visibility = View.GONE
                }

                if (items.mon != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.mon?.bookImg?.replace("http://","https://"))
                        .into(iviewBookImg2)

                    isSelectBook(items.mon, llayoutCover2)
                    iviewBookImg2.visibility = View.VISIBLE

                    if (((today?.week?: 0) - 1) == position && today?.date == 2 && monthCount == 0) {
                        llayoutWrap2.background = todayMark()
                    } else {
                        llayoutWrap2.background = null
                    }
                } else {
                    iviewBookImg2.visibility = View.GONE
                }

                if (items.tue != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.tue?.bookImg?.replace("http://","https://"))
                        .into(iviewBookImg3)

                    isSelectBook(items.tue, llayoutCover3)
                    iviewBookImg3.visibility = View.VISIBLE

                    if (((today?.week?: 0) - 1) == position && today?.date == 3 && monthCount == 0) {
                        llayoutWrap3.background = todayMark()
                    } else {
                        llayoutWrap3.background = null
                    }
                } else {
                    iviewBookImg3.visibility = View.GONE
                }

                if (items.wed != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.wed?.bookImg?.replace("http://","https://"))
                        .into(iviewBookImg4)

                    isSelectBook(items.wed, llayoutCover4)
                    iviewBookImg4.visibility = View.VISIBLE

                    if (((today?.week?: 0) - 1) == position && today?.date == 4 && monthCount == 0) {
                        llayoutWrap4.background = todayMark()
                    } else {
                        llayoutWrap4.background = null
                    }
                } else {
                    iviewBookImg4.visibility = View.GONE
                }

                if (items.thur != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.thur?.bookImg?.replace("http://","https://"))
                        .into(iviewBookImg5)

                    isSelectBook(items.thur, llayoutCover5)
                    iviewBookImg5.visibility = View.VISIBLE

                    if (((today?.week?: 0) - 1) == position && today?.date == 5 && monthCount == 0) {
                        llayoutWrap5.background = todayMark()
                    } else {
                        llayoutWrap5.background = null
                    }
                } else {
                    iviewBookImg5.visibility = View.GONE
                }

                if (items.fri != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.fri?.bookImg?.replace("http://","https://"))
                        .into(iviewBookImg6)

                    isSelectBook(items.fri, llayoutCover6)
                    iviewBookImg6.visibility = View.VISIBLE

                    if (((today?.week?: 0) - 1) == position && today?.date == 6 && monthCount == 0) {
                        llayoutWrap6.background = todayMark()
                    } else {
                        llayoutWrap6.background = null
                    }
                } else {
                    iviewBookImg6.visibility = View.GONE
                }

                if (items.sat != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.sat?.bookImg?.replace("http://","https://"))
                        .into(iviewBookImg7)

                    isSelectBook(items.sat, llayoutCover7)
                    iviewBookImg7.visibility = View.VISIBLE

                    if (((today?.week?: 0) - 1) == position && today?.date == 7 && monthCount == 0) {
                        llayoutWrap7.background = todayMark()
                    } else {
                        llayoutWrap7.background = null
                    }
                } else {
                    iviewBookImg7.visibility = View.GONE
                }
            }
        }
    }

    private fun todayMark(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor("#0D0E10"))
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 4f.dpToPx()
            setStroke(2f.dpToPx().toInt(), Color.parseColor("#844DF3"))
        }
    }

    fun setMonthDate(count: Int) {
        monthCount = count
        notifyDataSetChanged()
    }

    private fun isSelectBook(items: BestItemData?, llayout: LinearLayout) {
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

            with(binding) {
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