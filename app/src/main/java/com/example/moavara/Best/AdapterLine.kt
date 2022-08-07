package com.example.moavara.Best

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.DataBase.AnayzeData
import com.example.moavara.Search.BestLineChart
import com.example.moavara.databinding.ItemBestDetailLineBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class AdapterLine(private var context : Context, private var holder: List<BestLineChart>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            ItemBestDetailLineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            val item = this.holder[position]

            val dataItem = ArrayList<AnayzeData>()

            for(i in item.data.indices){
                dataItem.add(
                    AnayzeData(
                        (item.dateList as ArrayList<String>)[i],
                        item.data[i],
                    )
                )
            }

            val adapter = AdapterBestData(dataItem)

            holder.binding.rview.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            holder.binding.rview.adapter = adapter

            holder.binding.tviewData.text = item.title
            holder.binding.barChart.xAxis.isEnabled = false
            val barDataSet = LineDataSet(item.entryList, item.title)
            val colors = ArrayList<Int>()
            colors.add(Color.parseColor("#621CEF"))

            barDataSet.color = ColorTemplate.rgb(item.color)
            barDataSet.highLightColor  = ColorTemplate.rgb(item.color)
            barDataSet.isHighlightEnabled = false
            barDataSet.valueTextSize = 0F
            barDataSet.color = Color.parseColor("#621CEF")
            barDataSet.lineWidth = 3f
            barDataSet.circleColors = colors

            val barData = LineData(barDataSet)

            val barChart = holder.binding.barChart
            barChart.data = barData

            barChart.apply {
                setScaleEnabled(false)
                setPinchZoom(false)
                isClickable = false

                animateXY(800, 800)
                description = null

                barChart.setExtraOffsets(5f,5f,5f,15f)
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

                axisRight.setDrawLabels(false)
                xAxis.setDrawGridLines(false)

                xAxis.position = XAxis.XAxisPosition.BOTTOM

                xAxis.textColor = Color.parseColor("#EDE6FD")
                axisLeft.textColor = Color.parseColor("#EDE6FD")
                legend.textColor = Color.parseColor("#EDE6FD")
            }

            barChart.invalidate()

        }
    }

    override fun getItemCount(): Int {
        return holder.size
    }

    inner class ViewHolder internal constructor(val binding: ItemBestDetailLineBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    fun getItem(position: Int): BestLineChart {
        return holder[position]
    }
}