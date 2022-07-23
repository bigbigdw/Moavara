package com.example.moavara.Best

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.R
import com.example.moavara.Search.BestType

class AdapterType(private var holder: List<BestType>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selected = 0

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_best_type, parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = this.holder[position]

            holder.tveiwTitle.text = item.title

            if(getSelectedBtn() == position){
                holder.tveiwTitle.setTextColor(Color.parseColor("#0D0E10"));
                holder.llayoutWrap.setBackgroundResource(R.drawable.selector_item_type_on);
            } else {
                holder.tveiwTitle.setTextColor(Color.parseColor("#EDE6FD"));
                holder.llayoutWrap.setBackgroundResource(R.drawable.selector_item_type_off);
            }

        }
    }

    override fun getItemCount(): Int {
        return holder.size
    }

    inner class MainBookViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tveiwTitle: TextView = itemView.findViewById(R.id.tveiwTitle)
        var llayoutWrap: LinearLayout = itemView.findViewById(R.id.llayout_Wrap)

        init {

            llayoutWrap.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(v, pos)
                }
            }

        }


    }

    fun getItem(position: Int): BestType {
        return holder[position]
    }

    fun setSelectedBtn(select: Int) {
        selected = select
    }

    fun getSelectedBtn(): Int {
        return selected
    }

}