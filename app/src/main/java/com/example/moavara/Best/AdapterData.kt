package com.example.moavara.Best

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.DataBase.AnayzeData
import com.example.moavara.databinding.ItemBestDetailDataBinding

class AdapterBestData(
    private var items: ArrayList<AnayzeData>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, value: String?)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemBestDetailDataBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {

            val data = items[position]

            Log.d("####-2", "${data}")

            with(holder.binding) {
                tviewData.text = data.date
                tviewData1.text = data.info.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Holder internal constructor(val binding: ItemBestDetailDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {}

    }

    fun getItem(position: Int): AnayzeData {
        return items[position]
    }

}