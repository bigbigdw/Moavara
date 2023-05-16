package com.bigbigdw.moavara.Soon.Best

//import com.example.moavara.Search.BestChart
//import com.github.mikephil.charting.components.Legend
//import com.github.mikephil.charting.components.XAxis
//import com.github.mikephil.charting.data.BarData
//import com.github.mikephil.charting.data.BarDataSet
//import com.github.mikephil.charting.formatter.ValueFormatter
//import com.github.mikephil.charting.utils.ColorTemplate

//class AdapterChart(private var holder: List<BestChart>) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    interface OnItemClickListener {
//        fun onItemClick(v: View?, position: Int)
//    }
//
//    private var listener: OnItemClickListener? = null
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        this.listener = listener
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val view =
//            ItemBestDetailAnalysisBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (holder is ViewHolder) {
//
//            val item = this.holder[position]
//
//            holder.binding.barChart.xAxis.valueFormatter = object : ValueFormatter() {
//                override fun getFormattedValue(value: Float): String? {
//                    return item.dateList?.get(value.toInt())
//                }
//            }
//
//            val barDataSet = BarDataSet(item.entryList, item.title)
//
//            barDataSet.color = ColorTemplate.rgb(item.color)
//            barDataSet.highLightColor  = ColorTemplate.rgb(item.color)
//            barDataSet.isHighlightEnabled = false
//            barDataSet.valueTextSize = 0F
//
//            val barData = BarData(barDataSet)
//
//            val barChart = holder.binding.barChart
//            barChart.data = barData
//
//            barChart.apply {
//                setScaleEnabled(false)
//                setPinchZoom(false)
//                isClickable = false
//
//                animateXY(800, 800)
//                description = null
//
//                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
//
//                axisRight.setDrawLabels(false)
//                xAxis.setDrawGridLines(false)
//
//                xAxis.position = XAxis.XAxisPosition.BOTTOM
//
//                xAxis.textColor = Color.parseColor("#EDE6FD")
//                axisLeft.textColor = Color.parseColor("#EDE6FD")
//                legend.textColor = Color.parseColor("#EDE6FD")
//            }
//
//            barChart.invalidate()
//
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return holder.size
//    }
//
//    inner class ViewHolder internal constructor(val binding: ItemBestDetailAnalysisBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//    }
//
//    fun getItem(position: Int): BestChart {
//        return holder[position]
//    }
//}