package com.example.takealook.Best

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takealook.Joara.BookListDataBestToday
import com.example.takealook.Joara.BookListDataBestWeekend
import com.example.takealook.R
import java.util.ArrayList

class AdapterBestWeekend(
    private val mContext: Context, items:
    ArrayList<ArrayList<BookListDataBestToday?>?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<ArrayList<BookListDataBestToday?>?> = items

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
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = this.holder!![position]

        }
    }

    override fun getItemCount(): Int {
        return holder!!.size
    }

    inner class MainBookViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var week: TextView = itemView.findViewById(R.id.tview_week)
        var rview_BestWeek: RecyclerView = itemView.findViewById(R.id.rview_BestWeek)

    }

    fun getItem(position: Int): ArrayList<BookListDataBestToday?>? {
        return holder!![position]
    }

}