package com.example.moavara.Best

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Search.BookListDataBestWeekend
import java.util.ArrayList

class AdapterBestMonth(
    items: ArrayList<BookListDataBestWeekend>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item: ArrayList<BookListDataBestWeekend> = items
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

            if (items.sun != null) {
                Glide.with(holder.itemView.context)
                    .load(items.sun!!.bookImg)
                    .into(holder.iviewBookImg1)

                holder.tviewDate1.text = items.sun!!.date.substring(3)
                isSelectBook(items.sun, holder.llayoutCover1)
                holder.llayoutNull1.visibility = View.GONE
            } else {
                holder.llayoutNull1.visibility = View.VISIBLE
            }

            if (items.mon != null) {
                Glide.with(holder.itemView.context)
                    .load(items.mon!!.bookImg)
                    .into(holder.iviewBookImg2)

                holder.tviewDate2.text = items.mon!!.date.substring(3)
                isSelectBook(items.mon, holder.llayoutCover2)
                holder.llayoutNull2.visibility = View.GONE
            } else {
                holder.llayoutNull2.visibility = View.VISIBLE
            }

            if (items.tue != null) {
                Glide.with(holder.itemView.context)
                    .load(items.tue!!.bookImg)
                    .into(holder.iviewBookImg3)

                holder.tviewDate3.text = items.tue!!.date.substring(3)
                isSelectBook(items.tue, holder.llayoutCover3)
                holder.llayoutNull3.visibility = View.GONE
            } else {
                holder.llayoutNull3.visibility = View.VISIBLE
            }

            if (items.wed != null) {
                Glide.with(holder.itemView.context)
                    .load(items.wed!!.bookImg)
                    .into(holder.iviewBookImg4)

                holder.tviewDate4.text = items.wed!!.date.substring(3)
                isSelectBook(items.wed, holder.llayoutCover4)
                holder.llayoutNull4.visibility = View.GONE
            } else {
                holder.llayoutNull4.visibility = View.VISIBLE
            }

            if (items.thur != null) {
                Glide.with(holder.itemView.context)
                    .load(items.thur!!.bookImg)
                    .into(holder.iviewBookImg5)

                holder.tviewDate5.text = items.thur!!.date.substring(3)
                isSelectBook(items.thur, holder.llayoutCover5)
                holder.llayoutNull5.visibility = View.GONE
            } else {
                holder.llayoutNull5.visibility = View.VISIBLE
            }

            if (items.fri != null) {
                Glide.with(holder.itemView.context)
                    .load(items.fri!!.bookImg)
                    .into(holder.iviewBookImg6)

                holder.tviewDate6.text = items.fri!!.date.substring(3)
                isSelectBook(items.fri, holder.llayoutCover6)
                holder.llayoutNull6.visibility = View.GONE
            } else {
                holder.llayoutNull6.visibility = View.VISIBLE
            }

            if (items.sat != null) {
                Glide.with(holder.itemView.context)
                    .load(items.sat!!.bookImg)
                    .into(holder.iviewBookImg7)

                holder.tviewDate7.text = items.sat!!.date.substring(3)
                isSelectBook(items.sat, holder.llayoutCover7)
                holder.llayoutNull7.visibility = View.GONE
            } else {
                holder.llayoutNull7.visibility = View.VISIBLE
            }

        }
    }

    private fun isSelectBook(items : BookListDataBestToday?, llayout : LinearLayout){
        if (selected != "" && getSelectedBook() != items!!.bookCode) {
            llayout.visibility = View.VISIBLE
        } else {
            llayout.visibility = View.GONE
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
        var tviewDate1: TextView = itemView.findViewById(R.id.tviewDate1)
        var llayoutNull1 : LinearLayout = itemView.findViewById(R.id.llayoutNull1)

        var cvieWrap2: CardView = itemView.findViewById(R.id.cview_Wrap2)
        var iviewBookImg2: ImageView = itemView.findViewById(R.id.iview_BookImg2)
        var llayoutCover2: LinearLayout = itemView.findViewById(R.id.llayout_Cover2)
        var tviewDate2: TextView = itemView.findViewById(R.id.tviewDate2)
        var llayoutNull2 : LinearLayout = itemView.findViewById(R.id.llayoutNull2)

        var cvieWrap3: CardView = itemView.findViewById(R.id.cview_Wrap3)
        var iviewBookImg3: ImageView = itemView.findViewById(R.id.iview_BookImg3)
        var llayoutCover3: LinearLayout = itemView.findViewById(R.id.llayout_Cover3)
        var tviewDate3: TextView = itemView.findViewById(R.id.tviewDate3)
        var llayoutNull3 : LinearLayout = itemView.findViewById(R.id.llayoutNull3)

        var cvieWrap4: CardView = itemView.findViewById(R.id.cview_Wrap4)
        var iviewBookImg4: ImageView = itemView.findViewById(R.id.iview_BookImg4)
        var llayoutCover4: LinearLayout = itemView.findViewById(R.id.llayout_Cover4)
        var tviewDate4: TextView = itemView.findViewById(R.id.tviewDate4)
        var llayoutNull4 : LinearLayout = itemView.findViewById(R.id.llayoutNull4)

        var cvieWrap5: CardView = itemView.findViewById(R.id.cview_Wrap5)
        var iviewBookImg5: ImageView = itemView.findViewById(R.id.iview_BookImg5)
        var llayoutCover5: LinearLayout = itemView.findViewById(R.id.llayout_Cover5)
        var tviewDate5: TextView = itemView.findViewById(R.id.tviewDate5)
        var llayoutNull5 : LinearLayout = itemView.findViewById(R.id.llayoutNull5)

        var cvieWrap6: CardView = itemView.findViewById(R.id.cview_Wrap6)
        var iviewBookImg6: ImageView = itemView.findViewById(R.id.iview_BookImg6)
        var llayoutCover6: LinearLayout = itemView.findViewById(R.id.llayout_Cover6)
        var tviewDate6: TextView = itemView.findViewById(R.id.tviewDate6)
        var llayoutNull6 : LinearLayout = itemView.findViewById(R.id.llayoutNull6)

        var cvieWrap7: CardView = itemView.findViewById(R.id.cview_Wrap7)
        var iviewBookImg7: ImageView = itemView.findViewById(R.id.iview_BookImg7)
        var llayoutCover7: LinearLayout = itemView.findViewById(R.id.llayout_Cover7)
        var tviewDate7: TextView = itemView.findViewById(R.id.tviewDate7)
        var llayoutNull7 : LinearLayout = itemView.findViewById(R.id.llayoutNull7)

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
        return item[position]
    }


    fun setSelectedBook(bookcode: String) {
        selected = bookcode
    }


    fun getSelectedBook(): String? {
        return selected
    }

}