package com.example.takealook.Best

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takealook.Main.ActivityMain
import com.example.takealook.Search.BookListDataBestToday
import com.example.takealook.R
import java.util.ArrayList

class AdapterBestWeekend(
    private val mContext: Context, items:
    ArrayList<ArrayList<BookListDataBestToday?>?>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item: ArrayList<ArrayList<BookListDataBestToday?>?> = items

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booklist_best_weekend, parent, false)
        return HolderBestWeekend(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HolderBestWeekend) {

            val items = item!![position]
            val adapterWeek: AdapterBestWeekendSub?

            val weekitems = ArrayList<BookListDataBestToday?>()

            adapterWeek = AdapterBestWeekendSub(mContext, weekitems)

            when (position) {
                0 -> {
                    holder.week.text = "월"
                }
                1 -> {
                    holder.week.text = "화"
                }
                2 -> {
                    holder.week.text = "수"
                }
                3 -> {
                    holder.week.text = "목"
                }
                4 -> {
                    holder.week.text = "금"
                }
                5 -> {
                    holder.week.text = "토"
                }
                6 -> {
                    holder.week.text = "일"
                }
            }

            val linearLayoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)

            for (i in items!!.indices) {
                weekitems.add(
                    BookListDataBestToday(
                        items[i]!!.writer,
                        items[i]!!.title,
                        items[i]!!.bookImg,
                        items[i]!!.intro,
                        items[i]!!.bookCode,
                        items[i]!!.cntChapter,
                        items[i]!!.cntPageRead,
                        items[i]!!.cntFavorite,
                        items[i]!!.cntRecom,
                        i + 1,
                        false
                    )
                )
                holder.iview_Cover.visibility = View.GONE
            }

            holder.rview_BestWeek.layoutManager = linearLayoutManager
            holder.rview_BestWeek.adapter = adapterWeek
            adapterWeek!!.notifyDataSetChanged()

            adapterWeek!!.setOnItemClickListener(object : AdapterBestWeekendSub.OnItemClickListener {
                override fun onItemClick(v: View?, position: Int) {
                    val item: BookListDataBestToday? = adapterWeek!!.getItem(position)

                    item!!.isVisible = true

                    adapterWeek!!.notifyDataSetChanged()
                }
            })

        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class HolderBestWeekend internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var week: TextView = itemView.findViewById(R.id.tview_week)
        var rview_BestWeek: RecyclerView = itemView.findViewById(R.id.rview_BestWeek)
        var iview_Cover: ImageView = itemView.findViewById(R.id.iview_Cover)

    }

    fun getItem(position: Int): ArrayList<BookListDataBestToday?>? {
        return item[position]
    }

}