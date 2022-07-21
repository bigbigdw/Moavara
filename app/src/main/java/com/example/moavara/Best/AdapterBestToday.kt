package com.example.moavara.Best

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.databinding.ItemBooklistBestTodayBinding

class AdapterBestToday(
    private var holder: List<BookListDataBest>,
    private var bookCodeItems: ArrayList<BookListDataBestAnalyze>
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
        val view = ItemBooklistBestTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainBookViewHolder) {

            val item = this.holder[position]

            with(holder.binding){
                Glide.with(holder.itemView.context)
                    .load(item.bookImg)
                    .circleCrop()
                    .into(ivewBookImg)

                tviewIndex.text = (position + 1).toString()
                tviewTitle.text = item.title
                when (item.status) {
                    "UP" -> {
            //                        tviewNum.text =  "+ ${item.numberDiff.times(-1)} (${item.trophyCount})"
                        tviewNum.setTextColor(Color.parseColor("#02A247"));
                    }
                    "DOWN" -> {
            //                        tviewNum.text = "- ${item.numberDiff} (${item.trophyCount})"
                        tviewNum.setTextColor(Color.parseColor("#FF2C00"));
                    }
                    "-" -> {
            //                        tviewNum.text =  "(${item.trophyCount})"
                        tviewNum.setTextColor(Color.parseColor("#eeeeee"));
                    }
                    else -> {
                        tviewNum.text = "NEW"
                    }
                }


            }
        }
    }

    override fun getItemCount(): Int {
        return holder.size
    }

    inner class MainBookViewHolder internal constructor(val binding: ItemBooklistBestTodayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.llayoutWrap.setOnClickListener { v: View? ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(v, pos)
                }
            }

        }


    }

    fun getItem(position: Int): BookListDataBest {
        return holder[position]
    }

}