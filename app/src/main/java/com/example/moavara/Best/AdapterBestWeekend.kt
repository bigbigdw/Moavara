package com.example.moavara.Best

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.Search.BookListDataBestWeekend
import com.example.moavara.databinding.ItemBooklistBestWeekendBinding

class AdapterBestWeekend(
    private var items: ArrayList<BookListDataBestWeekend>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selected: String? = ""
    var num: Int? = 0

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, value: String?)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemBooklistBestWeekendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderBestWeekend(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HolderBestWeekend) {

            val items = items[position]

            with(holder.binding){
                if (items.sun != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.sun!!.bookImg)
                        .into(iviewBookImg1)

                    if (selected != "" && getSelectedBook() != items.sun!!.title) {
                        llayoutCover1.visibility = View.VISIBLE
                    } else {
                        llayoutCover1.visibility = View.GONE
                    }
                    llayoutNull1.visibility = View.GONE
                } else {
                    llayoutNull1.visibility = View.VISIBLE
                }

                if (items.mon != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.mon!!.bookImg)
                        .into(iviewBookImg2)

                    if (selected != "" && getSelectedBook() != items.mon!!.title) {
                        llayoutCover2.visibility = View.VISIBLE
                    } else {
                        llayoutCover2.visibility = View.GONE
                    }
                    llayoutNull2.visibility = View.GONE
                } else {
                    llayoutNull2.visibility = View.VISIBLE
                }

                if (items.tue != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.tue!!.bookImg)
                        .into(iviewBookImg3)

                    if (selected != "" && getSelectedBook() != items.tue!!.title) {
                        llayoutCover3.visibility = View.VISIBLE
                    } else {
                        llayoutCover3.visibility = View.GONE
                    }
                    llayoutNull3.visibility = View.GONE
                } else {
                    llayoutNull3.visibility = View.VISIBLE
                }

                if (items.wed != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.wed!!.bookImg)
                        .into(iviewBookImg4)

                    if (selected != "" && getSelectedBook() != items.wed!!.title) {
                        llayoutCover4.visibility = View.VISIBLE
                    } else {
                        llayoutCover4.visibility = View.GONE
                    }
                    llayoutNull4.visibility = View.GONE
                } else {
                    llayoutNull4.visibility = View.VISIBLE
                }

                if (items.thur != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.thur!!.bookImg)
                        .into(iviewBookImg5)

                    if (selected != "" && getSelectedBook() != items.thur!!.title) {
                        llayoutCover5.visibility = View.VISIBLE
                    } else {
                        llayoutCover5.visibility = View.GONE
                    }
                    llayoutNull5.visibility = View.GONE
                } else {
                    llayoutNull5.visibility = View.VISIBLE
                }

                if (items.fri != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.fri!!.bookImg)
                        .into(iviewBookImg6)

                    if (selected != "" && getSelectedBook() != items.fri!!.title) {
                        llayoutCover6.visibility = View.VISIBLE
                    } else {
                        llayoutCover6.visibility = View.GONE
                    }
                    llayoutNull6.visibility = View.GONE
                } else {
                    llayoutNull6.visibility = View.VISIBLE
                }

                if (items.sat != null) {
                    Glide.with(holder.itemView.context)
                        .load(items.sat!!.bookImg)
                        .into(iviewBookImg7)

                    if (selected != "" && getSelectedBook() != items.sat!!.title) {
                        llayoutCover7.visibility = View.VISIBLE
                    } else {
                        llayoutCover7.visibility = View.GONE
                    }
                    llayoutNull7.visibility = View.GONE
                } else {
                    llayoutNull7.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class HolderBestWeekend internal constructor(val binding: ItemBooklistBestWeekendBinding) :
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

    fun getItem(position: Int): BookListDataBestWeekend {
        return items[position]
    }

    fun setSelectedBook(title: String) {
        selected = title
    }

    fun getSelectedBook(): String? {
        return selected
    }

}