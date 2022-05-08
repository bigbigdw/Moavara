package com.example.moavara.Best

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.BookListDataBestWeekend
import com.example.moavara.databinding.ItemBooklistBestMonthBinding
import com.example.moavara.databinding.ItemBooklistBestWeekendBinding
import java.util.ArrayList

class AdapterBestMonth(
    items: ArrayList<BookListDataBestWeekend>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item: ArrayList<BookListDataBestWeekend> = items
    var selected: String? = ""

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

            with(holder.binding){
                if (items.sun != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.sun!!.bookImg)
                        .into(iviewBookImg1)

                    tviewDate1.text = items.sun!!.date.substring(3)
                    isSelectBook(items.sun, llayoutCover1)
                    llayoutNull1.visibility = View.GONE
                } else {
                    llayoutNull1.visibility = View.VISIBLE
                }

                if (items.mon != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.mon!!.bookImg)
                        .into(iviewBookImg2)

                    tviewDate2.text = items.mon!!.date.substring(3)
                    isSelectBook(items.mon, llayoutCover2)
                    llayoutNull2.visibility = View.GONE
                } else {
                    llayoutNull2.visibility = View.VISIBLE
                }

                if (items.tue != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.tue!!.bookImg)
                        .into(iviewBookImg3)

                    tviewDate3.text = items.tue!!.date.substring(3)
                    isSelectBook(items.tue, llayoutCover3)
                    llayoutNull3.visibility = View.GONE
                } else {
                    llayoutNull3.visibility = View.VISIBLE
                }

                if (items.wed != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.wed!!.bookImg)
                        .into(iviewBookImg4)

                    tviewDate4.text = items.wed!!.date.substring(3)
                    isSelectBook(items.wed, llayoutCover4)
                    llayoutNull4.visibility = View.GONE
                } else {
                    llayoutNull4.visibility = View.VISIBLE
                }

                if (items.thur != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.thur!!.bookImg)
                        .into(iviewBookImg5)

                    tviewDate5.text = items.thur!!.date.substring(3)
                    isSelectBook(items.thur, llayoutCover5)
                    llayoutNull5.visibility = View.GONE
                } else {
                    llayoutNull5.visibility = View.VISIBLE
                }

                if (items.fri != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.fri!!.bookImg)
                        .into(iviewBookImg6)

                    tviewDate6.text = items.fri!!.date.substring(3)
                    isSelectBook(items.fri, llayoutCover6)
                    llayoutNull6.visibility = View.GONE
                } else {
                    llayoutNull6.visibility = View.VISIBLE
                }

                if (items.sat != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.sat!!.bookImg)
                        .into(iviewBookImg7)

                    tviewDate7.text = items.sat!!.date.substring(3)
                    isSelectBook(items.sat, llayoutCover7)
                    llayoutNull7.visibility = View.GONE
                } else {
                    llayoutNull7.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun isSelectBook(items : BookListDataBestToday?, llayout : LinearLayout){
        if (selected != "" && getSelectedBook() != items!!.bookCode) {
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
                        listener!!.onItemClick(v, pos, "sun")
                    }
                }

                cviewWrap2.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(v, pos, "mon")
                    }
                }

                cviewWrap3.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(v, pos, "tue")
                    }
                }

                cviewWrap4.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(v, pos, "wed")
                    }
                }

                cviewWrap5.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(v, pos, "thur")
                    }
                }

                cviewWrap6.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(v, pos, "fri")
                    }
                }

                cviewWrap7.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(v, pos, "sat")
                    }
                }
            }
        }

    }

    fun getItem(position: Int): BookListDataBestWeekend? {
        return item[position]
    }


    fun setSelectedBook(bookcode: String) {
        selected = bookcode
    }


    fun getSelectedBook(): String? {
        return selected
    }

}