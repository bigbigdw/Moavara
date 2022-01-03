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
import com.example.takealook.Search.BookListDataBestWeekend
import java.util.ArrayList

class AdapterBestWeekend(
    private val mContext: Context, items:
    ArrayList<BookListDataBestWeekend?>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item: ArrayList<BookListDataBestWeekend?> = items

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

    fun getItem(position: Int): BookListDataBestWeekend? {
        return item[position]
    }

}