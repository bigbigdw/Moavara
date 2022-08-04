package com.example.moavara.Best

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moavara.DataBase.BestTodayAverage
import com.example.moavara.DataBase.BookListDataBestAnalyze
import com.example.moavara.Search.BestChart
import com.example.moavara.Util.DBDate
import com.example.moavara.databinding.FragmentBestDetailChartBinding
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class InnerFragmentBestDetailBar(
    private var BookItem: ArrayList<BookListDataBestAnalyze>,
    private val platform: String,
    private var genre: String,
) : Fragment() {
    val dateList = mutableListOf<String>()
    val entryList: ArrayList<BarEntry> = ArrayList()
    val entryList2 = mutableListOf<BarEntry>()
    val entryList3 = mutableListOf<BarEntry>()
    val entryList4 = mutableListOf<BarEntry>()

    private var adapterChart: AdapterChart? = null
    private val items = ArrayList<BestChart>()

    private var _binding: FragmentBestDetailChartBinding? = null
    private val binding get() = _binding!!

    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestDetailChartBinding.inflate(inflater, container, false)
        val view = binding.root

        adapterChart = AdapterChart(items)
        binding.rViewChart.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rViewChart.adapter = adapterChart

        if (platform == "Joara" || platform == "Joara_Nobless" || platform == "Joara_Premium") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("선호작 수 : ", "").toFloat()))
                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("추천 수 : ", "").toFloat()))
            }

            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#20459e"))
            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#20459e"))
            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#20459e"))
            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Naver_Today" || platform == "Naver_Challenge" || platform == "Naver") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("선호작 수 : ", "").toFloat()))
                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("추천 수 : ", "").toFloat()))
            }

            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#00dc64"))
            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#00dc64"))
            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#00dc64"))
            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Kakao") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("추천 수 : ", "").toFloat()))
                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("평점 : ", "").toFloat()))
//                entryList4.add(BarEntry(i.toFloat(), BookItem[i].info4.replace("조회 수 : ", "").toFloat()))
            }

            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#ffd200"))
            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#ffd200"))
            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#ffd200"))
//            items.add(BestChart(dateList, entryList4, "편당 추천 수", "#EDE6FD"))
            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Kakao_Stage") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("추천 수 : ", "").toFloat()))
                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("평점 : ", "").toFloat()))
                entryList4.add(BarEntry(i.toFloat(), BookItem[i].info4.replace("조회 수 : ", "").toFloat()))
            }

            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#ffd200"))
            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#ffd200"))
            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#ffd200"))
            items.add(BestChart(dateList, entryList4, "편당 추천 수", "#ffd200"))
            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Ridi") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace(",", "").replace("추천 수 : ", "").replace("명", "").toFloat()))
                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("평점 : ", "").replace("점", "").toFloat()))
            }

            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#1f8ce6"))
            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#1f8ce6"))
        } else if (platform == "OneStore") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.replace("조회 수 : ", "").toFloat()))
                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.replace("평점 : ", "").toFloat()))
                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.replace("댓글 수 : ", "").toFloat()))
            }

            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#fc6b05"))
            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#fc6b05"))
            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#fc6b05"))
            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Munpia") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.toFloat()))
                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.toFloat()))
                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.toFloat()))
                entryList4.add(BarEntry(i.toFloat(), BookItem[i].info4.toFloat()))
            }

            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#5f9bd1"))
            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#5f9bd1"))
            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#5f9bd1"))
            items.add(BestChart(dateList, entryList4, "편당 추천 수", "#5f9bd1"))
            adapterChart?.notifyDataSetChanged()
        } else if (platform == "Toksoda") {
            for (i in BookItem.indices) {
                dateList.add(BookItem[i].date.substring(4))
                entryList.add(BarEntry(i.toFloat(), BookItem[i].info1.toFloat()))
                entryList2.add(BarEntry(i.toFloat(), BookItem[i].info2.toFloat()))
                entryList3.add(BarEntry(i.toFloat(), BookItem[i].info3.toFloat()))
//                entryList4.add(BarEntry(i.toFloat(), BookItem[i].info4.toFloat()))
            }

            items.add(BestChart(dateList, entryList, "편당 댓글 수", "#ff442c"))
            items.add(BestChart(dateList, entryList2, "편당 조회 수", "#ff442c"))
            items.add(BestChart(dateList, entryList3, "편당 추천 수", "#ff442c"))
//            items.add(BestChart(dateList, entryList4, "편당 추천 수", "#ff442c"))
            adapterChart?.notifyDataSetChanged()
        }

        return view
    }
}