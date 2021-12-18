package com.example.takealook.Best

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takealook.Joara.BookListDataBestToday
import com.example.takealook.R
import java.util.ArrayList


class AdapterBestWeekendSub(private val mContext: Context, items: List<BookListDataBestToday?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<BookListDataBestToday?>? = items as ArrayList<BookListDataBestToday?>?

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booklist_best_weekend_sub, parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = this.holder!![position]

            Glide.with(holder.itemView.context)
                .load(item!!.bookImg)
                .into(holder.image)

        }
    }

    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder!!.size
    }

    inner class MainBookViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.iview_BookImg)
        var cviewWrap: CardView = itemView.findViewById(R.id.cview_Wrap)

        init {

            cviewWrap.setOnClickListener { v: View? ->
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