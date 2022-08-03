package com.example.moavara.Best

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.DataBase.BestTodayAverage
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.R
import com.example.moavara.Search.BestChart
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.FragmentBestDetailAnalyzeBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentBestDetailAnalyze(
    private val platfrom: String,
    private val item: ArrayList<BookListDataBestAnalyze>,
    private var genre: String,
    private var itemCount: Int,
) : Fragment() {

    private var adapterChart: AdapterChart? = null
    private val items = ArrayList<BestChart>()

    private var _binding: FragmentBestDetailAnalyzeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mInnerFragmentBestDetailRank: InnerFragmentBestDetailRank

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

        mInnerFragmentBestDetailRank = InnerFragmentBestDetailRank(item)
        childFragmentManager.commit {
            replace(R.id.InnerFragmentBestDetailRank, mInnerFragmentBestDetailRank)
        }

        getRadarChart()
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
        val entryList2 = mutableListOf<BarEntry>()
        val entryList3 = mutableListOf<BarEntry>()
        val entryList: ArrayList<BarEntry> = ArrayList()
        var num = 0

        if (chapter != null) {
            for (i in chapter.indices) {
                dateList.add(chapter[i].sortno + "화")
                entryList.add(BarEntry(num.toFloat(), chapter[i].cnt_comment.toFloat()))
                entryList2.add(BarEntry(num.toFloat(), chapter[i].cnt_page_read.toFloat()))
                entryList3.add(BarEntry(num.toFloat(), chapter[i].cnt_recom.toFloat()))
                num += 1
            }
            itemsJoara.add(BestChart(dateList, entryList, "편당 댓글 수", "#ff7b22"))
            itemsJoara.add(BestChart(dateList, entryList2, "편당 조회 수", "#4971EF"))
            itemsJoara.add(BestChart(dateList, entryList3, "편당 추천 수", "#00d180"))
            adapterChartJoara.notifyDataSetChanged()
        }
    }

    private fun getAnalyze() {

    }

    private fun getRadarChart() {

        val Average =
            FirebaseDatabase.getInstance().reference.child("Best").child(platfrom)
                .child(genre).child("Average").child(DBDate.DateMMDD())

        val entryAverage = ArrayList<RadarEntry>()
        val entriesCurrent = ArrayList<RadarEntry>()

        Average.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val group: BestTodayAverage? = dataSnapshot.getValue(BestTodayAverage::class.java)

                val valCur1 = itemCount - item[item.size-1].numInfo1
                val valCur2 = itemCount - item[item.size-1].numInfo2
                val valCur3 = itemCount - item[item.size-1].numInfo3
                var valCur4 = itemCount - item[item.size-1].numInfo4
                val numberAvg = itemCount - item[item.size-1].number

                entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
                entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
                entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
                entryAverage.add(RadarEntry((itemCount / 2).toFloat()))

                entriesCurrent.add(RadarEntry(valCur1.toFloat()))
                entriesCurrent.add(RadarEntry(valCur2.toFloat()))
                entriesCurrent.add(RadarEntry(valCur3.toFloat()))
                entriesCurrent.add(RadarEntry(numberAvg.toFloat()))

                with(binding) {

                    radarChart.description.isEnabled = false
                    radarChart.webLineWidth = 1f
                    radarChart.webColor = Color.parseColor("#EDE6FD")
                    radarChart.webLineWidthInner = 1f
                    radarChart.webColorInner = Color.parseColor("#EDE6FD")
                    radarChart.webAlpha = 100
                    radarChart.setExtraOffsets(-5f, -20f, -5f, -5f)

                    val set1 = RadarDataSet(entryAverage, "평균")
                    set1.color = Color.parseColor("#6E7686")
                    set1.fillColor = Color.parseColor("#6E7686")
                    set1.setDrawFilled(true)
                    set1.fillAlpha = 180
                    set1.lineWidth = 2f
                    set1.isDrawHighlightCircleEnabled = true
                    set1.setDrawHighlightIndicators(false)

                    val set2 = RadarDataSet(entriesCurrent, "현재 작품")
                    set2.color = Color.parseColor("#621CEF")
                    set2.fillColor = Color.parseColor("#621CEF")
                    set2.setDrawFilled(true)
                    set2.fillAlpha = 180
                    set2.lineWidth = 2f
                    set2.isDrawHighlightCircleEnabled = true
                    set2.setDrawHighlightIndicators(false)

                    val sets = ArrayList<IRadarDataSet>()
                    sets.add(set1)
                    sets.add(set2)

                    val data = RadarData(sets)
                    data.setValueTextSize(8f)
                    data.setDrawValues(false)
                    data.setValueTextColor(Color.WHITE)

                    radarChart.animateXY(1400, 1400, Easing.EaseInOutQuad)

                    val xAxis: XAxis = radarChart.xAxis
                    xAxis.textSize = 9f
                    xAxis.yOffset = 0f
                    xAxis.xOffset = 0f
                    xAxis.axisMinimum = 0f
                    xAxis.axisMaximum = 100f
                    val labels = arrayOf("조회 수", "선호작 수", "추천 수", "트로피")
                    xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                    xAxis.textColor = Color.WHITE

                    val yAxis: YAxis = radarChart.yAxis
                    yAxis.setLabelCount(5, false)
                    yAxis.textSize = 9f
                    yAxis.axisMinimum = 0f
                    yAxis.axisMaximum = 80f
                    yAxis.setDrawLabels(false)

                    val l: Legend = radarChart.legend
                    l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                    l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    l.orientation = Legend.LegendOrientation.HORIZONTAL
                    l.setDrawInside(false)
                    l.xEntrySpace = 7f
                    l.yEntrySpace = 5f
                    l.textColor = Color.WHITE

                    radarChart.data = data
                    radarChart.invalidate()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getMax(value : Float) : Float {
        return if(value > 100F){
            100F
        } else {
            value
        }
    }
}