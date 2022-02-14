package com.example.moavara.Pick

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Search.EventData

class AdapterPickEvent(items: List<EventData?>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<EventData?>? = items as ArrayList<EventData?>?

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pick_event, parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = this.holder!![position]

            Glide.with(holder.itemView.context)
                .load(item!!.imgfile)
                .into(holder.iView)

            if(item.startDate != null){
                holder.date.visibility = View.VISIBLE
                holder.date.text = item.startDate + " ~ " + item.endDate
            } else {
                holder.date.visibility = View.GONE
            }

            holder.title.text = item.title
            holder.platform.text = item.type
        }
    }

    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder!!.size
    }

    inner class MainBookViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var iView: ImageView = itemView.findViewById(R.id.iView)
        var title: TextView = itemView.findViewById(R.id.tviewTitle)
        var date: TextView = itemView.findViewById(R.id.tviewDate)
        var platform: TextView = itemView.findViewById(R.id.tviewPlatform)
        var llayoutWrap: LinearLayout = itemView.findViewById(R.id.llayoutWrap)

        init {

            llayoutWrap.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(v, pos)
                }
            }

        }


    }

    fun getItem(position: Int): EventData? {
        return holder!![position]
    }

}