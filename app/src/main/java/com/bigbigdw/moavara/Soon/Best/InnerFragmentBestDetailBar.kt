package com.bigbigdw.moavara.Soon.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.moavara.Search.BestChart
import com.bigbigdw.moavara.Search.BestTodayAverage
import com.bigbigdw.moavara.Search.BestListAnalyze
import com.bigbigdw.moavara.Util.DBDate
import com.bigbigdw.moavara.databinding.FragmentBestDetailBarBinding
//import com.github.mikephil.charting.components.Legend
//import com.github.mikephil.charting.components.XAxis
//import com.github.mikephil.charting.data.BarData
//import com.github.mikephil.charting.data.BarDataSet
//import com.github.mikephil.charting.data.BarEntry
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InnerFragmentBestDetailBar(
    private val platform: String,
    private val BookItem: ArrayList<BestListAnalyze>,
    private var genre: String,
    private var itemCount: Int,
) : Fragment() {
//    val dateList = mutableListOf<String>()
//    val entryList: ArrayList<BarEntry> = ArrayList()
//    val entryList2 = mutableListOf<BarEntry>()
//    val entryList3 = mutableListOf<BarEntry>()
//    val entryList4 = mutableListOf<BarEntry>()

//    private var adapterChart: AdapterChart? = null
//    private val items = ArrayList<BestChart>()

    private var _binding: FragmentBestDetailBarBinding? = null
    private val binding get() = _binding!!

    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestDetailBarBinding.inflate(inflater, container, false)
        val view = binding.root

//        adapterChart = AdapterChart(items)
        binding.rViewChart.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        binding.rViewChart.adapter = adapterChart

        getHorizontalBarChart()

        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium") {
            for (i in BookItem.indices) {
//                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("선호작 수 : ", "").toFloat()))
//                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("추천 수 : ", "").toFloat()))
            }

//            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#20459e"))
//            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#20459e"))
//            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#20459e"))
//            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver") {
            for (i in BookItem.indices) {
//                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("선호작 수 : ", "").toFloat()))
//                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("추천 수 : ", "").toFloat()))
            }

//            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#00dc64"))
//            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#00dc64"))
//            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#00dc64"))
//            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Kakao") {
            for (i in BookItem.indices) {
//                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("추천 수 : ", "").toFloat()))
//                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("평점 : ", "").toFloat()))
//                entryList4.add(BarEntry(i.toFloat(), BookItem[i].info4.replace("조회 수 : ", "").toFloat()))
            }

//            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#ffd200"))
//            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#ffd200"))
//            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#ffd200"))
//            items.add(BestChart(dateList, entryList4, "편당 추천 수", "#EDE6FD"))
//            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Kakao_Stage") {
            for (i in BookItem.indices) {
//                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("추천 수 : ", "").toFloat()))
//                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("평점 : ", "").toFloat()))
//                entryList4.add(BarEntry(i.toFloat(), BookItem[i].info4.replace("조회 수 : ", "").toFloat()))
            }

//            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#ffd200"))
//            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#ffd200"))
//            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#ffd200"))
//            items.add(BestChart(dateList, entryList4, "편당 추천 수", "#ffd200"))
//            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Ridi") {
            for (i in BookItem.indices) {
//                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace(",", "").replace("추천 수 : ", "").replace("명", "").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("평점 : ", "").replace("점", "").toFloat()))
            }

//            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#1f8ce6"))
//            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#1f8ce6"))
        } else if (platform == "OneStore") {
            for (i in BookItem.indices) {
//                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("평점 : ", "").toFloat()))
//                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("댓글 수 : ", "").toFloat()))
            }

//            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#fc6b05"))
//            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#fc6b05"))
//            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#fc6b05"))
//            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Munpia") {
            for (i in BookItem.indices) {
//                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.toFloat()))
//                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.toFloat()))
//                entryList4.add(BarEntry(i.toFloat(), BookItem[i].info4.toFloat()))
            }

//            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#5f9bd1"))
//            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#5f9bd1"))
//            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#5f9bd1"))
//            items.add(BestChart(dateList, entryList4, "편당 추천 수", "#5f9bd1"))
//            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Toksoda") {
            for (i in BookItem.indices) {
//                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.toFloat()))
//                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.toFloat()))
//                entryList4.add(BarEntry(i.toFloat(), BookItem[i].info4.toFloat()))
            }

//            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#ff442c"))
//            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#ff442c"))
//            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#ff442c"))
//            items.add(BestChart(dateList, entryList4, "편당 추천 수", "#ff442c"))
//            adapterChart?.notifyDataSetChanged()
        }

        return view
    }

    private fun getHorizontalBarChart() {
        val Average =
            FirebaseDatabase.getInstance().reference.child("").child(platform)
                .child(genre).child("Average").child(DBDate.DateMMDD())

        Average.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val group: BestTodayAverage? = dataSnapshot.getValue(BestTodayAverage::class.java)

                with(binding) {
//                    val entryList: ArrayList<BarEntry> = ArrayList()

                    val groupVal1 = ((group?.info1?.toInt() ?: 0) / itemCount).toFloat()
                    val groupVal2 = ((group?.info2?.toInt() ?: 0) / itemCount).toFloat()
                    val groupVal3 = ((group?.info3?.toInt() ?: 0) / itemCount).toFloat()
//                    var groupVal4 = ((group?.info4 ?: 0) / itemCount).toFloat()

                    val currentVal1 = BookItem[BookItem.size - 1].info1.toFloat()
                    val currentVal2 = BookItem[BookItem.size - 1].info2.toFloat()
                    val currentVal3 = BookItem[BookItem.size - 1].info3.toFloat()
//                    var currentVal4 = BookItem[BookItem.size - 1].info4.toFloat()

//                    entryList.add(BarEntry(5F, (currentVal1 / groupVal1) * 100))
//                    entryList.add(BarEntry(4F, 100F))
//                    entryList.add(BarEntry(3F, (currentVal2 / groupVal2) * 100))
//                    entryList.add(BarEntry(2F, 100F))
//                    entryList.add(BarEntry(1F, (currentVal3 / groupVal3) * 100))
//                    entryList.add(BarEntry(0F, 100F))

                    val xAxisValues: List<String> = ArrayList(
                        listOf(
                            "조회수 평균",
                            "현재 작품 조회수",
                            "선호작 평균",
                            "현재 작품 선호작",
                            "추천수 평균",
                            "현재 추천 수",
                        )
                    )

//                    barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
//                    barChart.legend.isEnabled = false
//                    val barDataSet = BarDataSet(entryList, "HIHI")
//                    barDataSet.isHighlightEnabled = false

//                    barDataSet.setColors(
//                        Color.parseColor("#621CEF"),
//                        Color.parseColor("#EDE6FD"),
//                        Color.parseColor("#621CEF"),
//                        Color.parseColor("#EDE6FD"),
//                        Color.parseColor("#621CEF"),
//                        Color.parseColor("#EDE6FD")
//                    )

//                    val barData = BarData(barDataSet)
//
//                    barChart.data = barData
//
//                    barChart.apply {
//                        setScaleEnabled(false)
//                        setPinchZoom(false)
//                        isClickable = false
//
//                        animateXY(800, 800)
//                        description = null
//
//                        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//                        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
//
//                        axisRight.setDrawLabels(false)
//                        xAxis.setDrawGridLines(false)
//
//                        xAxis.position = XAxis.XAxisPosition.BOTTOM
//
//                        barDataSet.valueTextSize = 10f
//                        barDataSet.valueTextColor = Color.parseColor("#EDE6FD")
//                        xAxis.textColor = Color.parseColor("#EDE6FD")
//                        axisLeft.textColor = Color.parseColor("#EDE6FD")
//                        legend.textColor = Color.parseColor("#EDE6FD")
//                    }
//
//                    barChart.invalidate()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}