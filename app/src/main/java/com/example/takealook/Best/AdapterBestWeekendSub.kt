package com.example.takealook.Best

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takealook.Search.BookListDataBestToday
import com.example.takealook.R
import java.util.ArrayList


class AdapterBestWeekendSub(private val mContext: Context, items: ArrayList<BookListDataBestToday?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var item: ArrayList<BookListDataBestToday?>? = items

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booklist_best_weekend_sub, parent, false)
        return ViewHolderBestWeekend(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderBestWeekend) {

            val item = this.item!![position]

            Glide.with(holder.itemView.context)
                .load(item!!.bookImg)
                .into(holder.image)

            if(item.isVisible!!){
                holder.llayoutCover.visibility = View.GONE
            } else {

            }

        }
    }

    override fun getItemCount(): Int {
        return item!!.size
    }

    inner class ViewHolderBestWeekend internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.iview_BookImg)
        var cviewWrap: CardView = itemView.findViewById(R.id.cview_Wrap)
        var llayoutCover: LinearLayout = itemView.findViewById(R.id.llayout_Cover)

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
        return item!![position]
    }

}