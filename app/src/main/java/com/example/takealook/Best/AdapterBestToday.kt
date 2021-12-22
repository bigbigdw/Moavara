package com.example.takealook.Best

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takealook.Search.BookListDataBestToday
import com.example.takealook.R
import java.util.ArrayList

class AdapterBestToday(private val mContext: Context, items: List<BookListDataBestToday?>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<BookListDataBestToday?>? = items as ArrayList<BookListDataBestToday?>?

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booklist_best_today, parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = this.holder!![position]

            Glide.with(holder.itemView.context)
                .load(item!!.bookImg)
                .circleCrop()
                .into(holder.image)

            holder.title.text = this.holder!![position]!!.title
            holder.number.text = this.holder!![position]!!.number.toString()

        }
    }

    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder!!.size
    }

    inner class MainBookViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.ivew_bookImg)
        var title: TextView = itemView.findViewById(R.id.tview_Title)
        var number: TextView = itemView.findViewById(R.id.tview_Num)
        var llayoutWrap: LinearLayout = itemView.findViewById(R.id.llayout_Wrap)

        init {

            llayoutWrap.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(v, pos)
                }
            }

        }


    }

    fun getItem(position: Int): BookListDataBestToday? {
        return holder!![position]
    }

}