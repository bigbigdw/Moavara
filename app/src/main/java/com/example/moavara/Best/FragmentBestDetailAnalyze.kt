package com.example.moavara.Best

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moavara.Search.BookListDataBestToday
import com.example.moavara.Util.BestRef
import com.example.moavara.Util.Genre
import com.example.moavara.databinding.FragmentBestDetailAnalyzeBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*


class FragmentBestDetailAnalyze(private val platfrom: String, private val bookCode: String) :
    Fragment() {

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

        if(platfrom == "Joara"){
            getAnalyzeJoara()
        }

        return view
    }

    private fun getAnalyzeJoara() {
        BestRef.setBestRefWeekList(platfrom, cate).get().addOnSuccessListener {
            val dateList = mutableListOf<String>()
            //BarEntry를 담는 리스트
            val entryList = mutableListOf<BarEntry>()
            val entryList2 = mutableListOf<BarEntry>()
            val entryList3 = mutableListOf<BarEntry>()
            val entryList4 = mutableListOf<Entry>()

            var num = 0

            for (i in it.children) {

                val group: BookListDataBestToday? = i.getValue(BookListDataBestToday::class.java)

                if (group!!.title == (context as ActivityBestDetail).bookTitle) {
                    dateList.add(group.date)

                    val cmpAsc: Comparator<String?> = Comparator { o1, o2 -> o1!!.compareTo(o2!!) }
                    Collections.sort(dateList, cmpAsc)

                    //BarEntry로 값 추가 후 리스트에 담는다
                    entryList.add(BarEntry(num.toFloat(),group.info3.replace("조회 수 : ", "").toFloat()))
                    entryList2.add(BarEntry(num.toFloat(),group.info4.replace("선호작 수 : ", "").toFloat()))
                    entryList3.add(BarEntry(num.toFloat(),group.info5.replace("추천 수 : ", "").toFloat()))
                    entryList4.add(Entry(num.toFloat(), group.number.toFloat()))

                    val cmpEntry: Comparator<BarEntry?> = Comparator { o1, o2 -> o1!!.x.compareTo(o2!!.x) }
                    Collections.sort(entryList, cmpEntry)

                    val cmpEntry2: Comparator<BarEntry?> = Comparator { o1, o2 -> o1!!.x.compareTo(o2!!.x) }
                    Collections.sort(entryList2, cmpEntry2)

                    val cmpEntry3: Comparator<BarEntry?> = Comparator { o1, o2 -> o1!!.x.compareTo(o2!!.x) }
                    Collections.sort(entryList3, cmpEntry3)

                    val cmpEntry4: Comparator<Entry?> = Comparator { o1, o2 -> o1!!.x.compareTo(o2!!.x) }
                    Collections.sort(entryList4, cmpEntry4)
                    num += 1
                }
            }

            binding.barChart.xAxis.valueFormatter = object: ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return dateList[value.toInt()]
                }
            }

            binding.barChart2.xAxis.valueFormatter = object: ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return dateList[value.toInt()]
                }
            }

            binding.barChart3.xAxis.valueFormatter = object: ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return dateList[value.toInt()]
                }
            }

            binding.lineChart.xAxis.valueFormatter = object: ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return dateList[value.toInt()]
                }
            }

            //위에서 만든 BarEntry 리스트를 인자로 준다
            val barDataSet = BarDataSet(entryList, "조회수")

            //example
            //다음과 같이 Bar 커스터마이징이 가능하다
            barDataSet.color = ColorTemplate.rgb("#ff7b22")
            barDataSet.valueTextColor = Color.parseColor("#ffffff")

            // , 구분으로 여러 BarDataSet을 줄 수 있습니다.
            val barData = BarData(barDataSet)

            //example
            //BarData에 추가된 모든 BarDataSet에 일괄 적용되는 값입니다.
            barData.barWidth = 0.5f

            //binding으로 접근하여 barData 전달
            val barChart = binding.barChart
            barChart.data = barData

            barChart.apply {
                //터치, Pinch 상호작용
                setScaleEnabled(false)
                setPinchZoom(false)

                //Chart가 그려질때 애니메이션
                animateXY(0,800)

                //Chart 밑에 description 표시 유무
                description=null

                //Legend는 차트의 범례를 의미합니다
                //범례가 표시될 위치를 설정
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

                //차트의 좌, 우측 최소/최대값을 설정합니다.
                //차트 제일 밑이 0부터 시작하고 싶은 경우 설정합니다.
                axisLeft.axisMinimum = 0f
                axisRight.axisMinimum = 0f

                //기본적으로 차트 우측 축에도 데이터가 표시됩니다
                //이를 활성화/비활성화 하기 위함
                axisRight.setDrawLabels(false)

                //xAxis, yAxis 둘다 존재하여 따로 설정이 가능합니다
                //차트 내부에 Grid 표시 유무
                xAxis.setDrawGridLines(false)

                //x축 데이터 표시 위치
                xAxis.position = XAxis.XAxisPosition.BOTTOM

                //x축 데이터 갯수 설정
                xAxis.labelCount = entryList.size

                xAxis.textColor = Color.parseColor("#ffffff")
                axisLeft.textColor = Color.parseColor("#ffffff")
                legend.textColor = Color.parseColor("#ffffff")
            }


            //barChart 갱신하여 데이터 표시
            barChart.invalidate()



            //위에서 만든 BarEntry 리스트를 인자로 준다
            val barDataSet2 = BarDataSet(entryList2, "선호작 수")

            //example
            //다음과 같이 Bar 커스터마이징이 가능하다
            barDataSet2.color = ColorTemplate.rgb("#4971EF")
            barDataSet2.valueTextColor = Color.parseColor("#ffffff")

            // , 구분으로 여러 BarDataSet을 줄 수 있습니다.
            val barData2 = BarData(barDataSet2)

            //example
            //BarData에 추가된 모든 BarDataSet에 일괄 적용되는 값입니다.
            barData2.barWidth = 0.5f

            //binding으로 접근하여 barData 전달
            val barChart2 = binding.barChart2
            barChart2.data = barData2

            barChart2.apply {
                //터치, Pinch 상호작용
                setScaleEnabled(false)
                setPinchZoom(false)

                //Chart가 그려질때 애니메이션
                animateXY(0,800)

                //Chart 밑에 description 표시 유무
                description=null

                //Legend는 차트의 범례를 의미합니다
                //범례가 표시될 위치를 설정
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

                //차트의 좌, 우측 최소/최대값을 설정합니다.
                //차트 제일 밑이 0부터 시작하고 싶은 경우 설정합니다.
                axisLeft.axisMinimum = 0f
                axisRight.axisMinimum = 0f

                //기본적으로 차트 우측 축에도 데이터가 표시됩니다
                //이를 활성화/비활성화 하기 위함
                axisRight.setDrawLabels(false)

                //xAxis, yAxis 둘다 존재하여 따로 설정이 가능합니다
                //차트 내부에 Grid 표시 유무
                xAxis.setDrawGridLines(false)

                //x축 데이터 표시 위치
                xAxis.position = XAxis.XAxisPosition.BOTTOM

                //x축 데이터 갯수 설정
                xAxis.labelCount = entryList.size

                xAxis.textColor = Color.parseColor("#ffffff")
                axisLeft.textColor = Color.parseColor("#ffffff")
                legend.textColor = Color.parseColor("#ffffff")
            }


            //barChart 갱신하여 데이터 표시
            barChart2.invalidate()

            //위에서 만든 BarEntry 리스트를 인자로 준다
            val barDataSet3 = BarDataSet(entryList3, "추천 수")

            //example
            //다음과 같이 Bar 커스터마이징이 가능하다
            barDataSet3.color = ColorTemplate.rgb("#00d180")
            barDataSet3.valueTextColor = Color.parseColor("#ffffff")

            // , 구분으로 여러 BarDataSet을 줄 수 있습니다.
            val barData3 = BarData(barDataSet3)

            //example
            //BarData에 추가된 모든 BarDataSet에 일괄 적용되는 값입니다.
            barData3.barWidth = 0.5f

            //binding으로 접근하여 barData 전달
            val barChart3 = binding.barChart3
            barChart3.data = barData3

            barChart3.apply {
                //터치, Pinch 상호작용
                setScaleEnabled(false)
                setPinchZoom(false)

                //Chart가 그려질때 애니메이션
                animateXY(0,800)

                //Chart 밑에 description 표시 유무
                description=null

                //Legend는 차트의 범례를 의미합니다
                //범례가 표시될 위치를 설정
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

                //차트의 좌, 우측 최소/최대값을 설정합니다.
                //차트 제일 밑이 0부터 시작하고 싶은 경우 설정합니다.
                axisLeft.axisMinimum = 0f
                axisRight.axisMinimum = 0f

                //기본적으로 차트 우측 축에도 데이터가 표시됩니다
                //이를 활성화/비활성화 하기 위함
                axisRight.setDrawLabels(false)

                //xAxis, yAxis 둘다 존재하여 따로 설정이 가능합니다
                //차트 내부에 Grid 표시 유무
                xAxis.setDrawGridLines(false)

                //x축 데이터 표시 위치
                xAxis.position = XAxis.XAxisPosition.BOTTOM

                //x축 데이터 갯수 설정
                xAxis.labelCount = entryList.size

                xAxis.textColor = Color.parseColor("#ffffff")
                axisLeft.textColor = Color.parseColor("#ffffff")
                legend.textColor = Color.parseColor("#ffffff")
            }


            //barChart 갱신하여 데이터 표시
            barChart3.invalidate()


            val set1 = LineDataSet(entryList4, "순위")

            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // black lines and points
            set1.color = Color.parseColor("#4971EF")

            set1.setCircleColor(Color.BLACK)
            set1.setDrawCircles(false)
            set1.setDrawValues(false)
            set1.lineWidth = 5F

            // set data
            binding.lineChart.data = data

            val lineChart = binding.lineChart
            barChart3.data = barData3

            lineChart.apply {
                //터치, Pinch 상호작용
                setScaleEnabled(false)
                setPinchZoom(false)

                //Chart가 그려질때 애니메이션
                animateXY(0,800)

                //Chart 밑에 description 표시 유무
                description=null

                //Legend는 차트의 범례를 의미합니다
                //범례가 표시될 위치를 설정
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

                //차트의 좌, 우측 최소/최대값을 설정합니다.
                //차트 제일 밑이 0부터 시작하고 싶은 경우 설정합니다.
                axisLeft.axisMinimum = -20f
                axisRight.axisMinimum = -20f

                axisLeft.axisMaximum = 20f
                axisRight.axisMaximum = 20f

                axisLeft.isInverted = true
                axisRight.isInverted = true

                //기본적으로 차트 우측 축에도 데이터가 표시됩니다
                //이를 활성화/비활성화 하기 위함
                axisRight.setDrawLabels(false)

                //xAxis, yAxis 둘다 존재하여 따로 설정이 가능합니다
                //차트 내부에 Grid 표시 유무
                xAxis.setDrawGridLines(false)

                //x축 데이터 표시 위치
                xAxis.position = XAxis.XAxisPosition.BOTTOM

                //x축 데이터 갯수 설정
                xAxis.labelCount = entryList.size

                xAxis.textColor = Color.parseColor("#ffffff")
                axisLeft.textColor = Color.parseColor("#ffffff")
                legend.textColor = Color.parseColor("#ffffff")
            }



        }.addOnFailureListener {}
    }


}