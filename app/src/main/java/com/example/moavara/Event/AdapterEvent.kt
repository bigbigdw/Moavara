package com.example.moavara.Event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.Search.EventData
import com.example.moavara.databinding.ItemEventBinding
import java.util.ArrayList

class AdapterEvent(items: List<EventData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<EventData> = items as ArrayList<EventData>

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHoderEvent(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHoderEvent) {

            val item = this.holder[position]

            Glide.with(holder.itemView.context)
                .load(item.imgfile)
                .into(holder.binding.iview)
        }
    }

    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder.size
    }

    inner class ViewHoderEvent internal constructor(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding){
                llayoutWrap.setOnClickListener { v: View? ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(v, pos)
                    }
                }
            }
        }


    }

    fun getItem(position: Int): EventData {
        return holder[position]
    }

}