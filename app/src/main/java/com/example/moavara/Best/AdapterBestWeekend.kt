package com.example.moavara.Best

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.databinding.ItemBooklistBestWeekendBinding

class AdapterBestWeekend(
    private var context : Context,
    private var items: ArrayList<ArrayList<BookListDataBest>?>,
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

                if (itemList != null) {
                    for (items in itemList) {
                        val itemBestWeekend = ItemBestWeekend(context)
                        itemBestWeekend.setItem(items.bookImg, items.title, items.writer, true)
                        itemBestWeekend.layoutParams = lp
                        llayoutView.addView(itemBestWeekend)
                    }
                } else {
                    for (num in 0..19) {
                        val itemBestWeekend = ItemBestWeekend(context)
                        itemBestWeekend.setItem("", "", "", false)
                        itemBestWeekend.layoutParams = lp
                        llayoutView.addView(itemBestWeekend)
                    }
                }

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

    fun getItem(position: Int): java.util.ArrayList<BookListDataBest>? {
        return items[position]
    }

}