package com.example.moavara.Best

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.R
import com.example.moavara.databinding.ItemBooklistBestWeekendBinding
import com.google.android.material.chip.Chip

class AdapterBestWeekend(
    private var context : Context,
    private var items: ArrayList<ArrayList<BookListDataBest>>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val lp = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )


    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, value: String?)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemBooklistBestWeekendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HolderBestWeekend(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HolderBestWeekend) {

            val itemList = items[position]

            with(holder.binding) {

                for (items in itemList) {
                    val itemBestWeekend = ItemBestWeekend(context)
                    itemBestWeekend.setItem(items.bookImg, items.title, items.writer, true)
                    itemBestWeekend.layoutParams = lp
                    llayoutView.addView(itemBestWeekend)
                }


//                val item0 = items[0]
//                val item1 = items[1]
//                val item2 = items[2]
//                val item3 = items[3]
//                val item4 = items[4]
//                val item5 = items[5]
//                val item6 = items[6]
//                val item7 = items[7]
//                val item8 = items[8]
//
//                if (item0 != null) {
//                    llayoutNull1.visibility = View.GONE
//
//                    Glide.with(holder.itemView.context)
//                        .load(item0.bookImg)
//                        .into(iviewImg1)
//
//                    tviewTitle1.text = item0.title
//                    tviewWriter1.text = item0.writer
//                } else {
//                    llayoutNull1.visibility = View.VISIBLE
//                }
//
//                if (item1 != null) {
//                    llayoutNull2.visibility = View.GONE
//
//                    Glide.with(holder.itemView.context)
//                        .load(item1.bookImg)
//                        .into(iviewImg2)
//
//                    tviewTitle2.text = item1.title
//                    tviewWriter2.text = item1.writer
//                } else {
//                    llayoutNull2.visibility = View.VISIBLE
//                }
//
//                if (item2 != null) {
//                    llayoutNull3.visibility = View.GONE
//
//                    Glide.with(holder.itemView.context)
//                        .load(item2.bookImg)
//                        .into(iviewImg3)
//
//                    tviewTitle3.text = item2.title
//                    tviewWriter3.text = item2.writer
//                } else {
//                    llayoutNull3.visibility = View.VISIBLE
//                }
//
//                if (item3 != null) {
//                    llayoutNull4.visibility = View.GONE
//
//                    Glide.with(holder.itemView.context)
//                        .load(item3.bookImg)
//                        .into(iviewImg4)
//
//                    tviewTitle4.text = item3.title
//                    tviewWriter4.text = item3.writer
//                } else {
//                    llayoutNull4.visibility = View.VISIBLE
//                }
//
//                if (item4 != null) {
//                    llayoutNull5.visibility = View.GONE
//
//                    Glide.with(holder.itemView.context)
//                        .load(item4.bookImg)
//                        .into(iviewImg5)
//
//                    tviewTitle5.text = item4.title
//                    tviewWriter5.text = item4.writer
//                } else {
//                    llayoutNull5.visibility = View.VISIBLE
//                }
//
//                if (item5 != null) {
//                    llayoutNull6.visibility = View.GONE
//
//                    Glide.with(holder.itemView.context)
//                        .load(item5.bookImg)
//                        .into(iviewImg6)
//
//                    tviewTitle6.text = item5.title
//                    tviewWriter6.text = item5.writer
//                } else {
//                    llayoutNull7.visibility = View.VISIBLE
//                }
//
//                if (item6 != null) {
//                    llayoutNull7.visibility = View.GONE
//
//                    Glide.with(holder.itemView.context)
//                        .load(item6.bookImg)
//                        .into(iviewImg7)
//
//                    tviewTitle7.text = item6.title
//                    tviewWriter7.text = item6.writer
//                } else {
//                    llayoutNull7.visibility = View.VISIBLE
//                }
//
//                if (item7 != null) {
//                    llayoutNull8.visibility = View.GONE
//
//                    Glide.with(holder.itemView.context)
//                        .load(item7.bookImg)
//                        .into(iviewImg8)
//
//                    tviewTitle8.text = item7.title
//                    tviewWriter8.text = item7.writer
//                } else {
//                    llayoutNull8.visibility = View.VISIBLE
//                }
//
//                if (item8 != null) {
//                    llayoutNull9.visibility = View.GONE
//
//                    Glide.with(holder.itemView.context)
//                        .load(item8.bookImg)
//                        .into(iviewImg9)
//
//                    tviewTitle9.text = item8.title
//                    tviewWriter9.text = item8.writer
//                } else {
//                    llayoutNull9.visibility = View.VISIBLE
//                }

                if (position == 0) {
                    tviewBestTop.text = "일요일 주간 베스트"
                } else if (position == 1) {
                    tviewBestTop.text = "월요일 주간 베스트"
                } else if (position == 2) {
                    tviewBestTop.text = "화요일 주간 베스트"
                } else if (position == 3) {
                    tviewBestTop.text = "수요일 주간 베스트"
                } else if (position == 4) {
                    tviewBestTop.text = "목요일 주간 베스트"
                } else if (position == 5) {
                    tviewBestTop.text = "금요일 주간 베스트"
                } else if (position == 6) {
                    tviewBestTop.text = "토요일 주간 베스트"
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

            with(binding) {
                root.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v, pos, "sat")
                    }
                }
            }
        }

    }

    fun getItem(position: Int): ArrayList<BookListDataBest> {
        return items[position]
    }

}