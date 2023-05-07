package com.example.moavara.Best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.moavara.Search.BestLineChart
import com.example.moavara.Search.BookListDataBestAnalyze
import com.example.moavara.databinding.FragmentBestDetailLineBinding
//import com.github.mikephil.charting.data.BarEntry
//import com.github.mikephil.charting.data.Entry

class InnerFragmentBestDetailLine(
    private val platform: String,
    private val BookItem: ArrayList<BookListDataBestAnalyze>,
) : Fragment() {
    val dateList = mutableListOf<String>()
//    val entryList: ArrayList<Entry> = ArrayList()
//    val entryList2 = mutableListOf<Entry>()
//    val entryList3 = mutableListOf<Entry>()
//    val entryList4 = mutableListOf<Entry>()

    val dataList1 = ArrayList<String>()
    val dataList2 = ArrayList<String>()
    val dataList3 = ArrayList<String>()
    val dataList4 = ArrayList<String>()

//    private var adapterChart: AdapterLine? = null
//    private val items = ArrayList<BestLineChart>()

    private var _binding: FragmentBestDetailLineBinding? = null
    private val binding get() = _binding!!

    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestDetailLineBinding.inflate(inflater, container, false)
        val view = binding.root

//        adapterChart = AdapterLine(requireContext(), items)
        binding.rViewChart.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        binding.rViewChart.adapter = adapterChart

        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("선호작 수 : ", "").toFloat()))
//                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("추천 수 : ", "").toFloat()))
                //TODO:
//                entryList4.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("댓글 수 : ", "").toFloat()))

                dataList1.add(BookItem[i].info1.replace("조회 수 : ", ""))
                dataList2.add(BookItem[i].info2.replace("선호작 수 : ", ""))
                dataList3.add(BookItem[i].info3.replace("추천 수 : ", ""))
            }

//            items.add(BestLineChart(dateList, entryList, "조회 수", "#621CEF", dataList1))
//            items.add(BestLineChart(dateList, entryList2, "선호작 수", "#621CEF", dataList2))
//            items.add(BestLineChart(dateList, entryList3, "추천 수", "#621CEF", dataList3))
            //TODO:
//            items.add(BestLineChart(dateList, entryList4, "댓글 수", "#20459e"))
//            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("만","0000").replace(",","").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("만","0000").replace(",","").toFloat()))

                if(BookItem[i].info3.isNotEmpty()){
//                    entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("만","0000").replace(",","").toFloat()))
                    dataList3.add(BookItem[i].info3.replace("만","0000").replace(",",""))
                }

                dataList1.add(BookItem[i].info1.replace("만","0000").replace(",",""))
                dataList2.add(BookItem[i].info2.replace("만","0000").replace(",",""))

            }

//            items.add(BestLineChart(dateList, entryList, "별점 수", "#621CEF", dataList1))
//            items.add(BestLineChart(dateList, entryList2, "조회 수", "#621CEF", dataList2))
            if(BookItem[0].info3.isNotEmpty()){
//                items.add(BestLineChart(dateList, entryList3, "관심 수", "#621CEF", dataList3))
            }

//            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Kakao") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("추천 수 : ", "").toFloat()))
//                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("평점 : ", "").toFloat()))
                //TODO:
//                entryList4.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("댓글 수 : ", "").toFloat()))

                dataList1.add(BookItem[i].info1.replace("조회 수 : ", ""))
                dataList2.add(BookItem[i].info2.replace("추천 수 : ", ""))
                dataList3.add(BookItem[i].info3.replace("평점 : ", ""))
            }

//            items.add(BestLineChart(dateList, entryList, "조회 수", "#621CEF", dataList1))
//            items.add(BestLineChart(dateList, entryList2, "추천 수", "#621CEF", dataList2))
//            items.add(BestLineChart(dateList, entryList3, "평점 수", "#621CEF", dataList3))
            //TODO:
//            items.add(BestLineChart(dateList, entryList4, "댓글 수", "#20459e"))
//            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Kakao_Stage") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("추천 수 : ", "").toFloat()))
//                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("평점 : ", "").toFloat()))
//                entryList4.add(BarEntry(i.toFloat(), BookItem[i].info4.replace("댓글 수 : ", "").toFloat()))

                dataList1.add(BookItem[i].info1.replace("조회 수 : ", ""))
                dataList2.add(BookItem[i].info2.replace("추천 수 : ", ""))
                dataList3.add(BookItem[i].info3.replace("평점 : ", ""))
                dataList4.add(BookItem[i].info4.replace("댓글 수 : ", ""))
            }

//            items.add(BestLineChart(dateList, entryList, "조회 수", "#621CEF", dataList1))
//            items.add(BestLineChart(dateList, entryList2, "추천 수", "#621CEF", dataList2))
//            items.add(BestLineChart(dateList, entryList3, "평점 수", "#621CEF", dataList3))
//            items.add(BestLineChart(dateList, entryList4, "댓글 수", "#621CEF", dataList4))
//            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Ridi") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace(",", "").replace("추천 수 : ", "").replace("명", "").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("평점 : ", "").replace("점", "").toFloat()))

                dataList1.add(BookItem[i].info1.replace(",", "").replace("추천 수 : ", "").replace("명", ""))
                dataList2.add(BookItem[i].info2.replace("평점 : ", "").replace("점", ""))
            }

//            items.add(BestLineChart(dateList, entryList, "추천 수", "#1f8ce6", dataList1))
//            items.add(BestLineChart(dateList, entryList2, "평점 수", "#1f8ce6", dataList2))
        } else if (platform == "OneStore") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("평점 : ", "").toFloat()))
//                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("댓글 수 : ", "").toFloat()))

                dataList1.add(BookItem[i].info1.replace("조회 수 : ", ""))
                dataList2.add(BookItem[i].info2.replace("평점 : ", ""))
                dataList3.add(BookItem[i].info3.replace("댓글 수 : ", ""))
            }

//            items.add(BestLineChart(dateList, entryList, "편당 댓글 수", "#621CEF", dataList1))
//            items.add(BestLineChart(dateList, entryList2, "편당 조회 수", "#621CEF", dataList2))
//            items.add(BestLineChart(dateList, entryList3, "편당 추천 수", "#621CEF", dataList3))
//            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Munpia") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace(",","").toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace(",","").toFloat()))

                if(BookItem[i].info3.isNotEmpty()){
//                    entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace(",","").toFloat()))
                    dataList3.add(BookItem[i].info3.replace(",",""))
                }

                if(BookItem[i].info4.isNotEmpty()){
//                    entryList4.add(BarEntry(i.toFloat(), BookItem[i].info4.replace(",","").toFloat()))
                    dataList4.add(BookItem[i].info4.replace(",",""))
                }

                dataList1.add(BookItem[i].info1.replace(",",""))
                dataList2.add(BookItem[i].info2.replace(",",""))
            }

//            items.add(BestLineChart(dateList, entryList, "히트 수", "#621CEF", dataList1))
//            items.add(BestLineChart(dateList, entryList2, "조회 수", "#621CEF", dataList2))

            if(BookItem[0].info3.isNotEmpty()){
//                items.add(BestLineChart(dateList, entryList3, "선호 수", "#621CEF", dataList3))
            }

            if(BookItem[0].info4.isNotEmpty()){
//                items.add(BestLineChart(dateList, entryList4, "베스트 시간", "#621CEF", dataList4))
            }

//            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Toksoda") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
//                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.toFloat()))
//                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.toFloat()))
//                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.toFloat()))

                dataList1.add(BookItem[i].info1)
                dataList2.add(BookItem[i].info2)
                dataList3.add(BookItem[i].info3)
            }

//            items.add(BestLineChart(dateList, entryList, "찜 수", "#621CEF", dataList1))
//            items.add(BestLineChart(dateList, entryList2, "조회 수", "#621CEF", dataList2))
//            items.add(BestLineChart(dateList, entryList3, "선호작 수", "#621CEF", dataList3))
//            adapterChart?.notifyDataSetChanged()
        }

        return view
    }
}