package com.example.moavara.Best

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moavara.DataBase.BookListDataBest
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.R
import com.example.moavara.Search.BestChart
import com.example.moavara.Util.*
import com.example.moavara.databinding.FragmentBestDetailAnalyzeBinding
import com.example.moavara.databinding.ItemBestDetailAnalysisBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

class FragmentBestDetailAnalyze(private val platfrom: String, private val pos: Int) :
    Fragment() {

    private var adapterChart: AdapterChart? = null
    private val items = ArrayList<BestChart>()

    private var _binding: FragmentBestDetailAnalyzeBinding? = null
    private val binding get() = _binding!!

    var status = ""
    var cate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestDetailAnalyzeBinding.inflate(inflater, container, false)
        val view = binding.root

        cate = Genre.getGenre(requireContext()).toString()

        adapterChart = AdapterChart(items)
        binding.rViewChart.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rViewChart.adapter = adapterChart

        if (platfrom == "Joara" || platfrom == "Joara Nobless" || platfrom == "Joara Premium") {
            binding.rViewChartJoara.visibility = View.VISIBLE
            getAnalyzeJoara()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            getAnalyze()
        }, 3000) //1초 후 실행

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

        BestRef.getBestRefToday(platfrom, cate).child(pos.toString()).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val group: BookListDataBest? = dataSnapshot.getValue(BookListDataBest::class.java)
                val dateList = mutableListOf<String>()
                //BarEntry를 담는 리스트
                val entryList = mutableListOf<BarEntry>()
                val entryList2 = mutableListOf<BarEntry>()
                val entryList3 = mutableListOf<BarEntry>()
                val entryList4 = mutableListOf<Entry>()
                val entryList5 = mutableListOf<BarEntry>()
                var num = 0

                if (group?.data != null) {
                    getRankList(group.data!!)
                    requireActivity().runOnUiThread {
                        
                        for(item in group.data!!){
                            dateList.add(item.date)
                            //BarEntry로 값 추가 후 리스트에 담는다
                            if (platfrom == "Kakao Stage" || platfrom == "Ridi" || platfrom == "Toksoda") {
                                entryList.add(BarEntry(num.toFloat(), convertData(1, item.info1)))
                                entryList2.add(BarEntry(num.toFloat(), convertData(2, item.info2)))
                                entryList4.add(Entry(num.toFloat(), group.number.toFloat()))
                            } else {
                                entryList.add(BarEntry(num.toFloat(), convertData(1, item.info1)))
                                entryList2.add(BarEntry(num.toFloat(), convertData(2, item.info2)))
                                entryList3.add(BarEntry(num.toFloat(), convertData(3, item.info3)))
                                entryList4.add(Entry(num.toFloat(), group.number.toFloat()))
                            }
                            num += 1
                        }
                    }

                    if (platfrom == "Joara" || platfrom == "Joara Nobless" || platfrom == "Joara Premium") {
                        items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                        items.add(BestChart(dateList, entryList2, "선호작 수", "#4971EF"))
                        items.add(BestChart(dateList, entryList3, "추천 수", "#00d180"))
                    } else if (platfrom == "Naver Today" || platfrom == "Naver Challenge" || platfrom == "Naver") {
                        items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                        items.add(BestChart(dateList, entryList2, "관심 수", "#4971EF"))
                        items.add(BestChart(dateList, entryList3, "별점", "#00d180"))
                    } else if (platfrom == "Kakao") {
                        items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                        items.add(BestChart(dateList, entryList2, "추천 수", "#4971EF"))
                        items.add(BestChart(dateList, entryList3, "평점", "#00d180"))
                    } else if (platfrom == "Kakao Stage") {
                        items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                        items.add(BestChart(dateList, entryList2, "선호작 수", "#4971EF"))
                    } else if (platfrom == "Ridi") {
                        items.add(BestChart(dateList, entryList, "평점", "#ff7b22"))
                        items.add(BestChart(dateList, entryList2, "관심 수", "#4971EF"))
                    } else if (platfrom == "OneStore") {
                        items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                        items.add(BestChart(dateList, entryList2, "평점", "#4971EF"))
                        items.add(BestChart(dateList, entryList3, "댓글 수", "#00d180"))
                    } else if (platfrom == "Munpia") {
                        items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                        items.add(BestChart(dateList, entryList2, "평점", "#4971EF"))
                        items.add(BestChart(dateList, entryList3, "댓글 수", "#00d180"))
                        items.add(BestChart(dateList, entryList5, "베스트 시간", "#00d180"))
                    } else if (platfrom == "Toksoda") {
                        items.add(BestChart(dateList, entryList, "조회 수", "#ff7b22"))
                        items.add(BestChart(dateList, entryList2, "선호작 수", "#4971EF"))
                    }
                    adapterChart!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getRankList(data: ArrayList<BookListDataBestAnalyze>) {
        for (item in data) {
            val itemDate = DBDate.getDateData(item.date)

            with(binding.includeRank) {
                if (itemDate != null) {
                    if (itemDate.week == DBDate.Week().toInt()) {
                        when {
                            itemDate.date == 1 -> {
                                tviewRank1.visibility = View.VISIBLE

                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank1.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank1.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank1.text = (item.number + 1).toString()
                            }
                            itemDate.date == 2 -> {
                                tviewRank2.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank2.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank2.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank2.text = (item.number + 1).toString()
                                android.util.Log.d("@@@@", "HIHI")
                            }
                            itemDate.date == 3 -> {
                                tviewRank3.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank3.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank3.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank3.text = (item.number + 1).toString()
                            }
                            itemDate.date == 4 -> {
                                tviewRank4.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank4.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank4.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank4.text = (item.number + 1).toString()
                            }
                            itemDate.date == 5 -> {
                                tviewRank5.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank5.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank5.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank5.text = (item.number + 1).toString()
                            }
                            itemDate.date == 6 -> {
                                tviewRank6.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank6.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank6.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank6.text = (item.number + 1).toString()
                            }
                            itemDate.date == 7 -> {
                                tviewRank7.visibility = View.VISIBLE
                                if (itemDate.date == DBDate.DayInt()) {
                                    iviewRank7.setImageResource(R.drawable.ic_best_gn_24px)
                                } else {
                                    iviewRank7.setImageResource(R.drawable.ic_best_vt_24px)
                                }
                                tviewRank7.text = (item.number + 1).toString()
                            }
                        }
                    }
                }
            }
        }
    }

    fun convertData(num : Int, data : String) : Float{
        if(platfrom == "Joara" || platfrom == "Joara Nobless" || platfrom == "Joara Premium"){
            if(num == 1){
                return data.replace("조회 수 : ", "").toFloat()
            } else if(num == 2){
                return data.replace("선호작 수 : ", "").toFloat()
            } else {
                return data.replace("추천 수 : ", "").toFloat()
            }
        } else if (platfrom == "Naver Today" || platfrom == "Naver Challenge" || platfrom == "Naver") {
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
        } else if (platfrom == "Kakao Stage") {
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
                if (item.entryList?.size!! < 7) {
                    barChart.xAxis.labelCount = item.entryList?.size ?: 0
                } else {
                    barChart.xAxis.labelCount = 7
                }
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