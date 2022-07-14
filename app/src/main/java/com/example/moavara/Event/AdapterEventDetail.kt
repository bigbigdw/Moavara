package com.example.moavara.Event

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moavara.Search.EventData
import com.example.moavara.Search.EventDetailData
import com.example.moavara.databinding.ItemEventBinding
import com.example.moavara.databinding.ItemEventDetailBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.ArrayList

class AdapterEventDetail(items: List<EventDetailData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<EventDetailData> = items as ArrayList<EventDetailData>

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemEventDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHoderEvent(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHoderEvent) {

            val item = this.holder[position]

            if(item.imgFile.isNotEmpty()){
                holder.binding.llayoutNull.visibility = View.GONE
                Glide.with(holder.itemView.context)
                    .load(item.imgFile)
                    .into(holder.binding.iView)
            } else {
                holder.binding.llayoutNull.visibility = View.VISIBLE
            }

            with(holder.binding){
                tviewTitle.text = item.title
                tviewSDate.text = item.sDate
                tviewEDate.text = item.wDate
                tviewCntRead.text = item.cntRead

                holder.binding.barChart.xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String? {
                        return item.chart?.dateList?.get(value.toInt())
                    }
                }

                val barDataSet = BarDataSet(item.chart?.entryList, item.title)

                barDataSet.color = ColorTemplate.rgb(item.chart?.color)
                barDataSet.valueTextColor = Color.parseColor("#ffffff")
                barDataSet.valueTextSize = 10F

                val barData = BarData(barDataSet)
                barData.barWidth = 0.5f

                val barChart = holder.binding.barChart
                barChart.data = barData

                barChart.apply {
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    isClickable = false

                    animateXY(0, 800)

                    description = null

                    legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

                    axisLeft.axisMinimum = 0f
                    axisRight.axisMinimum = 0f

                    axisRight.setDrawLabels(false)

                    xAxis.setDrawGridLines(false)

                    xAxis.position = XAxis.XAxisPosition.BOTTOM

                    xAxis.textColor = Color.parseColor("#ffffff")
                    axisLeft.textColor = Color.parseColor("#ffffff")
                    legend.textColor = Color.parseColor("#ffffff")
                }
//                if (item.entryList?.size!! < 7) {
//                    barChart.xAxis.labelCount = item.entryList?.size ?: 0
//                } else {
//                    barChart.xAxis.labelCount = 7
//                }
                barChart.xAxis.labelCount = item.chart?.entryList?.size ?: 0
                barChart.invalidate()
                barChart.setVisibleXRangeMinimum(7F)
                barChart.setVisibleXRangeMaximum(7F)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder.size
    }

    inner class ViewHoderEvent internal constructor(val binding: ItemEventDetailBinding) :
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

    fun getItem(position: Int): EventDetailData {
        return holder[position]
    }

}