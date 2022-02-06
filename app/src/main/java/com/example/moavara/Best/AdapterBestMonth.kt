package com.example.moavara.Best

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestWeekend
import java.util.ArrayList

class AdapterBestMonth(
    private val mContext: Context,
    items:
    ArrayList<BookListDataBestWeekend?>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item: ArrayList<BookListDataBestWeekend?> = items
    var selected: String? = ""

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, value: String?)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booklist_best_month, parent, false)
        return HolderBestWeekend(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HolderBestWeekend) {

            val items = item[position]

            if (items!!.sun != null) {
                Glide.with(holder.itemView.context)
                    .load(items.sun!!.bookImg)
                    .into(holder.iviewBookImg1)

                if (selected != "" && getSelectedBook() != items.sun!!.bookCode) {
                    holder.llayoutCover1.visibility = View.VISIBLE
                } else {
                    holder.llayoutCover1.visibility = View.GONE
                }
            }

            if (items.mon != null) {
                Glide.with(holder.itemView.context)
                    .load(items.mon!!.bookImg)
                    .into(holder.iviewBookImg2)

                if (selected != "" && getSelectedBook() != items.mon!!.bookCode) {
                    holder.llayoutCover2.visibility = View.VISIBLE
                } else {
                    holder.llayoutCover2.visibility = View.GONE
                }

            }

            if (items.tue != null) {
                Glide.with(holder.itemView.context)
                    .load(items.tue!!.bookImg)
                    .into(holder.iviewBookImg3)

                if (selected != "" && getSelectedBook() != items.tue!!.bookCode) {
                    holder.llayoutCover3.visibility = View.VISIBLE
                } else {
                    holder.llayoutCover3.visibility = View.GONE
                }
            }

            if (items.wed != null) {
                Glide.with(holder.itemView.context)
                    .load(items.wed!!.bookImg)
                    .into(holder.iviewBookImg4)

                if (selected != "" && getSelectedBook() != items.wed!!.bookCode) {
                    holder.llayoutCover4.visibility = View.VISIBLE
                } else {
                    holder.llayoutCover4.visibility = View.GONE
                }
            }

            if (items.thur != null) {
                Glide.with(holder.itemView.context)
                    .load(items.thur!!.bookImg)
                    .into(holder.iviewBookImg5)

                if (selected != "" && getSelectedBook() != items.thur!!.bookCode) {
                    holder.llayoutCover5.visibility = View.VISIBLE
                } else {
                    holder.llayoutCover5.visibility = View.GONE
                }
            }

            if (items.fri != null) {
                Glide.with(holder.itemView.context)
                    .load(items.fri!!.bookImg)
                    .into(holder.iviewBookImg6)

                if (selected != "" && getSelectedBook() != items.fri!!.bookCode) {
                    holder.llayoutCover6.visibility = View.VISIBLE
                } else {
                    holder.llayoutCover6.visibility = View.GONE
                }
            }

            if (items.sat != null) {
                Glide.with(holder.itemView.context)
                    .load(items.sat!!.bookImg)
                    .into(holder.iviewBookImg7)

                if (selected != "" && getSelectedBook() != items.sat!!.bookCode) {
                    holder.llayoutCover7.visibility = View.VISIBLE
                } else {
                    holder.llayoutCover7.visibility = View.GONE
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class HolderBestWeekend internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var cvieWrap1: CardView = itemView.findViewById(R.id.cview_Wrap1)
        var iviewBookImg1: ImageView = itemView.findViewById(R.id.iview_BookImg1)
        var llayoutCover1: LinearLayout = itemView.findViewById(R.id.llayout_Cover1)

        var cvieWrap2: CardView = itemView.findViewById(R.id.cview_Wrap2)
        var iviewBookImg2: ImageView = itemView.findViewById(R.id.iview_BookImg2)
        var llayoutCover2: LinearLayout = itemView.findViewById(R.id.llayout_Cover2)

        var cvieWrap3: CardView = itemView.findViewById(R.id.cview_Wrap3)
        var iviewBookImg3: ImageView = itemView.findViewById(R.id.iview_BookImg3)
        var llayoutCover3: LinearLayout = itemView.findViewById(R.id.llayout_Cover3)

        var cvieWrap4: CardView = itemView.findViewById(R.id.cview_Wrap4)
        var iviewBookImg4: ImageView = itemView.findViewById(R.id.iview_BookImg4)
        var llayoutCover4: LinearLayout = itemView.findViewById(R.id.llayout_Cover4)

        var cvieWrap5: CardView = itemView.findViewById(R.id.cview_Wrap5)
        var iviewBookImg5: ImageView = itemView.findViewById(R.id.iview_BookImg5)
        var llayoutCover5: LinearLayout = itemView.findViewById(R.id.llayout_Cover5)

        var cvieWrap6: CardView = itemView.findViewById(R.id.cview_Wrap6)
        var iviewBookImg6: ImageView = itemView.findViewById(R.id.iview_BookImg6)
        var llayoutCover6: LinearLayout = itemView.findViewById(R.id.llayout_Cover6)

        var cvieWrap7: CardView = itemView.findViewById(R.id.cview_Wrap7)
        var iviewBookImg7: ImageView = itemView.findViewById(R.id.iview_BookImg7)
        var llayoutCover7: LinearLayout = itemView.findViewById(R.id.llayout_Cover7)

        init {

            cvieWrap1.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(v, pos, "sun")
                }
            }

            cvieWrap2.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(v, pos, "mon")
                }
            }

            cvieWrap3.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(v, pos, "tue")
                }
            }

            cvieWrap4.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(v, pos, "wed")
                }
            }

            cvieWrap5.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(v, pos, "thur")
                }
            }

            cvieWrap6.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(v, pos, "fri")
                }
            }

            cvieWrap7.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(v, pos, "sat")
                }
            }

        }

    }

    fun getItem(position: Int): BookListDataBestWeekend? {
        return item!![position]
    }


    fun setSelectedBook(bookcode: String) {
        selected = bookcode
    }


    fun getSelectedBook(): String? {
        return selected
    }

}