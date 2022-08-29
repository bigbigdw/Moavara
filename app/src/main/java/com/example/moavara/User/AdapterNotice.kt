package com.example.moavara.User

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.Search.FCMAlert
import com.example.moavara.databinding.ItemNoticesBinding

class AdapterNotice(
    private var holder: List<FCMAlert>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemNoticesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = this.holder[position]

            val year = item.date.substring(0,4)
            val month = item.date.substring(4,6)
            val day = item.date.substring(6,8)

            with(holder.binding){
                tviewDate.text = "${year}년 ${month}월 ${day}일"
                tviewTitle.text = item.title
                tviewBody.text = item.body
            }

        }
    }

    override fun getItemCount(): Int {
        return holder.size
    }

    inner class MainBookViewHolder internal constructor(val binding: ItemNoticesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.llayoutWrap.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(v, pos)

                    if(binding.llayoutContents.visibility == View.VISIBLE){
                        binding.llayoutContents.visibility = View.GONE
                    } else if(binding.llayoutContents.visibility == View.GONE) {
                        binding.llayoutContents.visibility = View.VISIBLE
                    }
                }
            }

        }


    }

    fun getItem(position: Int): FCMAlert {
        return holder[position]
    }

}