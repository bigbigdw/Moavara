package com.example.moavara.Best

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class FragmentBestDetailAnalyze(
    private val platform: String,
    private val BookItem: ArrayList<BookListDataBestAnalyze>,
    private var genre: String,
    private var itemCount: Int,
) : Fragment() {

    private var _binding: FragmentBestDetailAnalyzeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mInnerFragmentBestDetailRank: InnerFragmentBestDetailRank
    private lateinit var mInnerFragmentBestDetailRadar: InnerFragmentBestDetailRadar
    private lateinit var mInnerFragmentBestDetailBar: InnerFragmentBestDetailBar

    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestDetailAnalyzeBinding.inflate(inflater, container, false)
        val view = binding.root

        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium") {
            binding.rViewChartJoara.visibility = View.VISIBLE
            getAnalyzeJoara()
        }

        mInnerFragmentBestDetailRank = InnerFragmentBestDetailRank(BookItem)
        childFragmentManager.commit {
            replace(R.id.InnerFragmentBestDetailRank, mInnerFragmentBestDetailRank)
        }

        mInnerFragmentBestDetailRadar = InnerFragmentBestDetailRadar(BookItem, itemCount, platform)
        childFragmentManager.commit {
            replace(R.id.InnerFragmentBestDetailRadar, mInnerFragmentBestDetailRadar)
        }

        getHorizontalBarChart()

        mInnerFragmentBestDetailBar = InnerFragmentBestDetailBar(BookItem, platform, genre)
        childFragmentManager.commit {
            replace(R.id.InnerFragmentBestDetailBar, mInnerFragmentBestDetailBar)
        }

        return view
    }

    private fun getAnalyzeJoara() {
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

            itemsJoara.add(BestChart(dateList, entryList, "편당 댓글 수", "#20459e"))
            itemsJoara.add(BestChart(dateList, entryList2, "편당 조회 수", "#20459e"))
            itemsJoara.add(BestChart(dateList, entryList3, "편당 추천 수", "#20459e"))
            adapterChartJoara.notifyDataSetChanged()
        }
    }

    private fun getHorizontalBarChart() {
        val Average =
            FirebaseDatabase.getInstance().reference.child("Best").child(platform)
                .child(genre).child("Average").child(DBDate.DateMMDD())

        Average.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val group: BestTodayAverage? = dataSnapshot.getValue(BestTodayAverage::class.java)

                Log.d("####", group.toString())

                with(binding) {
                    val entryList: ArrayList<BarEntry> = ArrayList()

                    val groupVal1 = ((group?.info1 ?: 0) / itemCount).toFloat()
                    val groupVal2 = ((group?.info2 ?: 0) / itemCount).toFloat()
                    val groupVal3 = ((group?.info3 ?: 0) / itemCount).toFloat()
                    var groupVal4 = ((group?.info4 ?: 0) / itemCount).toFloat()

                    val currentVal1 = BookItem[BookItem.size - 1].info1.toFloat()
                    val currentVal2 = BookItem[BookItem.size - 1].info2.toFloat()
                    val currentVal3 = BookItem[BookItem.size - 1].info3.toFloat()
                    var currentVal4 = BookItem[BookItem.size - 1].info4.toFloat()

                    entryList.add(BarEntry(5F, (currentVal1 / groupVal1) * 100))
                    entryList.add(BarEntry(4F, 100F))
                    entryList.add(BarEntry(3F, (currentVal2 / groupVal2) * 100))
                    entryList.add(BarEntry(2F, 100F))
                    entryList.add(BarEntry(1F, (currentVal3 / groupVal3) * 100))
                    entryList.add(BarEntry(0F, 100F))

                    val xAxisValues: List<String> = ArrayList(
                        listOf(
                            "평균 작품 값1",
                            "현재 작품 값1",
                            "평균 작품 값2",
                            "현재 작품 값2",
                            "평균 작품 값3",
                            "현재 작품 값3",
                        )
                    )

                    barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
                    barChart.legend.isEnabled = false
                    val barDataSet = BarDataSet(entryList, "HIHI")
                    barDataSet.isHighlightEnabled = false

                    barDataSet.setColors(
                        Color.parseColor("#621CEF"),
                        Color.parseColor("#EDE6FD"),
                        Color.parseColor("#621CEF"),
                        Color.parseColor("#EDE6FD"),
                        Color.parseColor("#621CEF"),
                        Color.parseColor("#EDE6FD")
                    )

                    val barData = BarData(barDataSet)

                    barChart.data = barData

                    barChart.apply {
                        setScaleEnabled(false)
                        setPinchZoom(false)
                        isClickable = false

                        animateXY(800, 800)
                        description = null

                        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

                        axisRight.setDrawLabels(false)
                        xAxis.setDrawGridLines(false)

                        xAxis.position = XAxis.XAxisPosition.BOTTOM

                        barDataSet.valueTextSize = 10f
                        barDataSet.valueTextColor = Color.parseColor("#EDE6FD")
                        xAxis.textColor = Color.parseColor("#EDE6FD")
                        axisLeft.textColor = Color.parseColor("#EDE6FD")
                        legend.textColor = Color.parseColor("#EDE6FD")
                    }

                    barChart.invalidate()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}