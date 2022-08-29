package com.example.moavara.Best

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.R
import com.example.moavara.Search.AnayzeData
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

            with(holder.binding) {

                if(position > 0){
                    tviewData.text = data.date

                    if(items[position].info.toFloat() - items[position - 1].info.toFloat() > 0){
                        tviewData1.text = "${data.info}(${(items[position].info.toFloat() - items[position - 1].info.toFloat()).toString().replace(".0", "")})"
                        tviewData1.setTextColor(Color.parseColor("#02BC77"))
                        iviewArrow.setImageResource(R.drawable.ic_arrow_drop_up_24px)
                    } else if(items[position].info.toFloat() - items[position - 1].info.toFloat() == 0F){
                        tviewData1.setTextColor(Color.parseColor("#ffffff"))
                        iviewArrow.visibility = View.GONE
                    } else {
                        tviewData1.text = "${data.info}(${(items[position].info.toFloat() - items[position - 1].info.toFloat()).toString().replace(".0", "")})"
                        tviewData1.setTextColor(Color.parseColor("#FF2366"))
                        iviewArrow.setImageResource(R.drawable.ic_arrow_drop_down_24px)
                    }
                } else {
                    tviewData.text = data.date
                    tviewData1.text = data.info
                    iviewArrow.visibility = View.GONE
                }
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