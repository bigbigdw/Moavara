package com.example.moavara.Best

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.databinding.FragmentBestdetailRadarBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import java.util.*

class InnerFragmentBestDetailRadar(
    BookItem: ArrayList<BookListDataBestAnalyze>,
    private var itemCount: Int,
    private val platform: String,
) : Fragment() {

    val entryAverage = ArrayList<RadarEntry>()
    val entriesCurrent = ArrayList<RadarEntry>()
    var labels = arrayOf("조회 수", "선호작 수", "추천 수", "트로피" ,"댓글 수")
    val valCur1 = itemCount - BookItem[BookItem.size-1].numInfo1
    val valCur2 = itemCount - BookItem[BookItem.size-1].numInfo2
    val valCur3 = itemCount - BookItem[BookItem.size-1].numInfo3
    val valCur4 = itemCount - BookItem[BookItem.size-1].numInfo4
    val numberAvg = itemCount - BookItem[BookItem.size-1].number


    private var _binding: FragmentBestdetailRadarBinding? = null
    private val binding get() = _binding!!

    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestdetailRadarBinding.inflate(inflater, container, false)
        val view = binding.root

        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium") {
            labels = arrayOf("조회 수", "선호작 수", "추천 수", "트로피")
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))

            entriesCurrent.add(RadarEntry(valCur1.toFloat()))
            entriesCurrent.add(RadarEntry(valCur2.toFloat()))
            entriesCurrent.add(RadarEntry(valCur3.toFloat()))
            entriesCurrent.add(RadarEntry(numberAvg.toFloat()))
        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver") {
            labels = arrayOf("조회 수", "선호작 수", "추천 수", "트로피")
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))

            entriesCurrent.add(RadarEntry(valCur1.toFloat()))
            entriesCurrent.add(RadarEntry(valCur2.toFloat()))
            entriesCurrent.add(RadarEntry(valCur3.toFloat()))
            entriesCurrent.add(RadarEntry(numberAvg.toFloat()))
        } else if (platform == "Kakao") {
            labels = arrayOf("조회 수", "선호작 수", "추천 수", "트로피" ,"댓글 수")
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))

            entriesCurrent.add(RadarEntry(valCur1.toFloat()))
            entriesCurrent.add(RadarEntry(valCur2.toFloat()))
            entriesCurrent.add(RadarEntry(valCur3.toFloat()))
            entriesCurrent.add(RadarEntry(valCur4.toFloat()))
            entriesCurrent.add(RadarEntry(numberAvg.toFloat()))
        } else if (platform == "Kakao_Stage") {
            labels = arrayOf("조회 수", "선호작 수", "추천 수", "트로피" ,"댓글 수")
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))

            entriesCurrent.add(RadarEntry(valCur1.toFloat()))
            entriesCurrent.add(RadarEntry(valCur2.toFloat()))
            entriesCurrent.add(RadarEntry(valCur3.toFloat()))
            entriesCurrent.add(RadarEntry(valCur4.toFloat()))
            entriesCurrent.add(RadarEntry(numberAvg.toFloat()))
        } else if (platform == "Ridi") {
            labels = arrayOf("조회 수", "선호작 수", "추천 수", "트로피")
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))

            entriesCurrent.add(RadarEntry(valCur1.toFloat()))
            entriesCurrent.add(RadarEntry(valCur2.toFloat()))
            entriesCurrent.add(RadarEntry(numberAvg.toFloat()))
        } else if (platform == "OneStore") {
            labels = arrayOf("조회 수", "선호작 수", "추천 수", "트로피")
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))

            entriesCurrent.add(RadarEntry(valCur1.toFloat()))
            entriesCurrent.add(RadarEntry(valCur2.toFloat()))
            entriesCurrent.add(RadarEntry(valCur3.toFloat()))
            entriesCurrent.add(RadarEntry(numberAvg.toFloat()))
        } else if (platform == "Munpia") {
            labels = arrayOf("조회 수", "선호작 수", "추천 수", "트로피" ,"댓글 수")
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))

            entriesCurrent.add(RadarEntry(valCur1.toFloat()))
            entriesCurrent.add(RadarEntry(valCur2.toFloat()))
            entriesCurrent.add(RadarEntry(valCur3.toFloat()))
            entriesCurrent.add(RadarEntry(valCur4.toFloat()))
            entriesCurrent.add(RadarEntry(numberAvg.toFloat()))
        } else if (platform == "Toksoda") {
            labels = arrayOf("조회 수", "선호작 수", "추천 수", "트로피" ,"댓글 수")
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))
            entryAverage.add(RadarEntry((itemCount / 2).toFloat()))

            entriesCurrent.add(RadarEntry(valCur1.toFloat()))
            entriesCurrent.add(RadarEntry(valCur2.toFloat()))
            entriesCurrent.add(RadarEntry(valCur3.toFloat()))
            entriesCurrent.add(RadarEntry(valCur4.toFloat()))
            entriesCurrent.add(RadarEntry(numberAvg.toFloat()))
        }

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

            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.textColor = Color.WHITE

            val yAxis: YAxis = radarChart.yAxis
            yAxis.setLabelCount(5, false)
            yAxis.textSize = 9f
            yAxis.axisMinimum = 0f
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

        return view
    }
}