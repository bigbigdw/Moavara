package com.example.moavara.Best

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.Search.BestChart
import com.example.moavara.Util.BestRef
import com.example.moavara.databinding.FragmentBestDetailAnalyzeBinding
import com.example.moavara.databinding.ItemBestDetailAnalysisBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class FragmentBestDetailAnalyze(
    private val platfrom: String,
    private val item: ArrayList<BookListDataBestAnalyze>?,
    ) : Fragment() {

    private var adapterChart: AdapterChart? = null
    private val items = ArrayList<BestChart>()

    private var _binding: FragmentBestDetailAnalyzeBinding? = null
    private val binding get() = _binding!!

    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestDetailAnalyzeBinding.inflate(inflater, container, false)
        val view = binding.root

        adapterChart = AdapterChart(items)
        binding.rViewChart.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rViewChart.adapter = adapterChart

        if (platfrom == "Joara" || platfrom == "Joara_Nobless" || platfrom == "Joara_Premium") {
            binding.rViewChartJoara.visibility = View.VISIBLE
            getAnalyzeJoara()
        }

        getAnalyze()

        return view
    }

    fun getAnalyzeJoara() {
        val adapterChartJoara: AdapterChart?
        val itemsJoara = ArrayList<BestChart>()

        adapterChartJoara = AdapterChart(itemsJoara)
        binding.rViewChartJoara.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rViewChartJoara.adapter = adapterChartJoara

        val chapter = (context as ActivityBestDetail).chapter
        val dateList = mutableListOf<String>()
        val entryList = mutableListOf<BarEntry>()
        val entryList2 = mutableListOf<BarEntry>()
        val entryList3 = mutableListOf<BarEntry>()
        var num = 0

        if (chapter != null) {
            for (i in chapter.indices) {
                dateList.add(chapter[i].sortno + "화")
                entryList.add(BarEntry(num.toFloat(), chapter[i].cnt_comment.toFloat()))
                entryList2.add(BarEntry(num.toFloat(), chapter[i].cnt_page_read.toFloat()))
                entryList3.add(BarEntry(num.toFloat(), chapter[i].cnt_recom.toFloat()))
                num += 1
            }
            itemsJoara.add(BestChart(dateList, entryList, "편당 댓글 수", "#6e2b93"))
            itemsJoara.add(BestChart(dateList, entryList2, "편당 조회 수", "#6e2b93"))
            itemsJoara.add(BestChart(dateList, entryList3, "편당 추천 수", "#6e2b93"))
            adapterChartJoara.notifyDataSetChanged()
        }
    }

    private fun getAnalyze() {

        val dateList = mutableListOf<String>()
        //BarEntry를 담는 리스트
        val entryList = mutableListOf<BarEntry>()
        val entryList2 = mutableListOf<BarEntry>()
        val entryList3 = mutableListOf<BarEntry>()
        var num = 0

        if (item != null) {

            for(item in item){
                dateList.add(item.date)
                //BarEntry로 값 추가 후 리스트에 담는다
                if (platfrom == "Kakao_Stage" || platfrom == "Ridi" || platfrom == "Toksoda") {
                    entryList.add(BarEntry(num.toFloat(), convertData(1, item.info1)))
                    entryList2.add(BarEntry(num.toFloat(), convertData(2, item.info2)))
                } else {
                    entryList.add(BarEntry(num.toFloat(), convertData(1, item.info1)))
                    entryList2.add(BarEntry(num.toFloat(), convertData(2, item.info2)))
                    entryList3.add(BarEntry(num.toFloat(), convertData(3, item.info3)))
                }
                num += 1
            }

            when (platfrom) {
                "Joara", "Joara_Nobless", "Joara_Premium" -> {
                    items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                    items.add(BestChart(dateList, entryList2, "선호작 수", "#4971EF"))
                    items.add(BestChart(dateList, entryList3, "추천 수", "#00d180"))
                }
                "Naver_Today", "Naver_Challenge", "Naver" -> {
                    items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                    items.add(BestChart(dateList, entryList2, "관심 수", "#4971EF"))
                    items.add(BestChart(dateList, entryList3, "별점", "#00d180"))
                }
                "Kakao" -> {
                    items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                    items.add(BestChart(dateList, entryList2, "추천 수", "#4971EF"))
                    items.add(BestChart(dateList, entryList3, "평점", "#00d180"))
                }
                "Kakao_Stage" -> {
                    items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                    items.add(BestChart(dateList, entryList2, "선호작 수", "#4971EF"))
                }
                "Ridi" -> {
                    items.add(BestChart(dateList, entryList, "평점", "#ff7b22"))
                    items.add(BestChart(dateList, entryList2, "관심 수", "#4971EF"))
                }
                "OneStore" -> {
                    items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                    items.add(BestChart(dateList, entryList2, "평점", "#4971EF"))
                    items.add(BestChart(dateList, entryList3, "댓글 수", "#00d180"))
                }
                "Munpia" -> {
                    items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                    items.add(BestChart(dateList, entryList2, "평점", "#4971EF"))
                    items.add(BestChart(dateList, entryList3, "댓글 수", "#00d180"))
                }
                "Toksoda" -> {
                    items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                    items.add(BestChart(dateList, entryList2, "선호작 수", "#4971EF"))
                }
            }
            adapterChart!!.notifyDataSetChanged()
        }
    }

    fun convertData(num : Int, data : String) : Float{
        if(platfrom == "Joara" || platfrom == "Joara_Nobless" || platfrom == "Joara_Premium"){
            if(num == 1){
                return data.replace("조회 수 : ", "").toFloat()
            } else if(num == 2){
                return data.replace("선호작 수 : ", "").toFloat()
            } else {
                return data.replace("추천 수 : ", "").toFloat()
            }
        } else if (platfrom == "Naver_Today" || platfrom == "Naver_Challenge" || platfrom == "Naver") {
            if(num == 1){
                return BestRef.StrToInt(data.replace("조회 ", "")).toFloat()
            } else if(num == 2){
                return BestRef.StrToInt(data.replace("관심 ", "")).toFloat()
            } else {
                return data.replace("별점", "").toFloat()
            }
        } else if (platfrom == "Kakao") {
            if(num == 1){
                return BestRef.StrToInt(data.replace("조회 수 : ", "")).toFloat()
            } else if(num == 2){
                return BestRef.StrToInt(data.replace("추천 수 : ", "")).toFloat()
            } else {
                return data.replace("평점 : ", "").toFloat()
            }
        } else if (platfrom == "Kakao_Stage") {
            if(num == 1){
                return BestRef.StrToInt(data.replace("조회 수 : ", "")).toFloat()
            } else if(num == 2){
                return  BestRef.StrToInt(data.replace("선호작 수 : ", "")).toFloat()
            }
        } else if (platfrom == "Ridi") {
            if(num == 1){
                return data.replace("평점 : ", "").replace("점", "").toFloat()
            } else if(num == 2){
                return BestRef.StrToInt(data.replace("추천 수 : ", "").replace("명", "")).toFloat()
            }
        } else if (platfrom == "OneStore") {
            if(num == 1){
                return data.replace("조회 수 : ", "").toFloat()
            } else if(num == 2){
                return data.replace("평점 : ", "").toFloat()
            } else {
                return data.replace("댓글 수 : ", "").toFloat()
            }
        }  else if (platfrom == "Munpia") {
            if(num == 1){
                return data.replace("조회 수 : ", "").toFloat()
            } else if(num == 2){
                return data.replace("방문 수 : ", "").toFloat()
            }else if(num == 3){
                return data.replace("선호작 수 : ", "").toFloat()
            } else {
                return data.replace("베스트 시간 : ", "").toFloat()
            }
        } else if (platfrom == "Toksoda") {
            if(num == 1){
                return data.replace("조회 수 : ", "").toFloat()
            } else if(num == 2){
                return data.replace("선호작 수 : ", "").toFloat()
            }
        } else {
            return 0F
        }
        return 0F
    }
}


class AdapterChart(items: List<BestChart?>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var holder: ArrayList<BestChart?>? = items as ArrayList<BestChart?>?

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            ItemBestDetailAnalysisBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            val item = this.holder!![position]

            if (item != null) {

                holder.binding.barChart.xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String? {
                        return item.dateList?.get(value.toInt())
                    }
                }

                val barDataSet = BarDataSet(item.entryList, item.title)

                barDataSet.color = ColorTemplate.rgb(item.color)
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
                barChart.xAxis.labelCount = item.entryList?.size ?: 0
                barChart.invalidate()
                barChart.setVisibleXRangeMinimum(7F)
                barChart.setVisibleXRangeMaximum(7F)
            }

        }
    }

    override fun getItemCount(): Int {
        return if (holder == null) 0 else holder!!.size
    }

    inner class ViewHolder internal constructor(val binding: ItemBestDetailAnalysisBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    fun getItem(position: Int): BestChart? {
        return holder!![position]
    }
}