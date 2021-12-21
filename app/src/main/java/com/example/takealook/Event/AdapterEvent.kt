package com.example.takealook.Event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takealook.R
import com.example.takealook.Search.EventData
import java.util.ArrayList

class AdapterEvent(items: List<EventData?>?) :
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
            .inflate(R.layout.item_event, parent, false)
        return ViewHoderEvent(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHoderEvent) {

            val item = this.holder!![position]

            Glide.with(holder.itemView.context)
                .load(item!!.imgfile)
                .circleCrop()
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

    fun getItem(position: Int): EventData? {
        return holder!![position]
    }

}