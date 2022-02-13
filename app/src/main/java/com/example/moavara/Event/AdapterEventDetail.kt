package com.example.moavara.Event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.R
import com.example.moavara.Search.EventData
import java.util.ArrayList

class AdapterEventDetail(items: List<String?>?) :
RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<String?>? = items as ArrayList<String?>?

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return ViewHoderEvent(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHoderEvent) {

            val item = this.holder!![position]

            Glide.with(holder.itemView.context)
                .load(item!!)
                .into(holder.image)
        }
    }

    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder!!.size
    }

    inner class ViewHoderEvent internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.ivew_eventImg)
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

    fun getItem(position: Int): String? {
        return holder!![position]
    }

}